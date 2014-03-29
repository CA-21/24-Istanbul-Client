package killerapp.istanbul24;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends Activity
{
	ProgressBar progressBar;
	TextView textView;
	Activity activity;
	Handler handler;
	boolean unzipped = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		progressBar = (ProgressBar) findViewById(R.id.splashProgress);
		textView = (TextView) findViewById(R.id.splashText);

		textView.setText("24Istanbul is initializing.");
		progressBar.setIndeterminate(true);

		activity = this;

		handler = new Handler();
		handler.postDelayed(new taskController(), 1000);
	}

	private class taskController extends Thread
	{
		int level = 0;
		boolean first;
		Long downloadID;
		Long lastUpdate;
		boolean internet;
		SharedPreferences init;
		SharedPreferences.Editor editor;

		@Override
		public void run()
		{
			if (level == 0)
			{
				init();
				first = true;
			}
			

			if (!internet && level < 3)
			{
				new AlertDialog.Builder(activity)
						.setTitle("Warning")
						.setMessage("24Istanbul requires Internet connection for downloading map in first run.")
						.setNeutralButton("OK", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								System.exit(0);
							}
						}).show();
				
				return ;
			}
			
			if(level == 4)
			{
				if(!internet ||System.currentTimeMillis()/1000L - lastUpdate < 86400)	// 24 hours
					level++;
			}

			switch (level)
			{
			case 1:
				textView.setText("The map is downloading.");
				init = getSharedPreferences("init", 0);
				Long downloadID = Download.downloadFile(activity, "http://sw2.obcdn.net/api/map", "map.zip", "24Istanbul", "The map is downloading.");
				editor = init.edit();
				editor.putLong("downloadID", downloadID);
				editor.commit();
				level++;
				first = true;
				break;
			case 2:
				if (first)
				{
					textView.setText("The map is downloading.");
					first = false;
				}

				if (downloadComplated())
				{
					level++;
					first = true;
				}
				break;
			case 3:
				if (first)
				{
					textView.setText("The map is extracting.");
					first = false;
					new unzipThread().start();
				}

				if (unzipped)
				{
					level++;
					first = true;
				}
				break;
			case 4:
				if (first)
				{
					textView.setText("Database is updating.");
					first = false;
				}
				// TODO: update database
				
				init = getSharedPreferences("init", 0);
				editor = init.edit();
				editor.putLong("lastUpdate", System.currentTimeMillis()/1000L);
				editor.commit();
				
				first = true;
				level++;
				break;
			case 5:
				textView.setText("Completed.");
				level++;
				break;
			}

			if (level < 6)
				handler.postDelayed(this, 1000);
			else
			{
				Intent myIntent = new Intent(activity, MainActivity.class);
				startActivityForResult(myIntent, 0);
				finish();
			}
		}

		private void init()
		{
			internet = isInternetAvailable();
			
			SharedPreferences init = getSharedPreferences("init", 0);
			downloadID = init.getLong("downloadID", 0);
			lastUpdate = init.getLong("lastUpdate", 0);

			if (!new File(Environment.getExternalStorageDirectory() + "/24Istanbul/istanbul-gh").exists())
			// Map was not extracted or downloaded.
			{
				if (!new File(Environment.getExternalStorageDirectory() + "/24Istanbul/map.zip").exists())
				// Map was not downloaded.
				{
					level = 1;
				}
				else
				{
					DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
					DownloadManager.Query query = new DownloadManager.Query();
					Cursor c = manager.query(query);

					boolean downloading = false;
					int status;

					if (c.moveToFirst())
					{
						do
						{
							if (c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)) == downloadID)
							{
								status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

								if (status == DownloadManager.STATUS_RUNNING
										|| status == DownloadManager.STATUS_PAUSED
										|| status == DownloadManager.STATUS_PENDING)
								{
									downloading = true;
									break;
								}
							}
						}
						while (c.moveToNext());
					}

					if (downloading)
						level = 2;
					else
						level = 3;
				}
			}
			else
			{
				level = 4;
			}

		}

		private boolean downloadComplated()
		{

			if (downloadID == 0)
			{
				SharedPreferences init = getSharedPreferences("init", 0);
				downloadID = init.getLong("downloadID", 0);
			}

			DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
			DownloadManager.Query query = new DownloadManager.Query();
			Cursor c = manager.query(query);
			int status;

			if (c.moveToFirst())
			{
				do
				{
					if (c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)) == downloadID)
					{
						status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

						if (status == DownloadManager.STATUS_SUCCESSFUL)
						{
							return true;
						}
					}
				}
				while (c.moveToNext());
			}

			return false;
		}

		private class unzipThread extends Thread
		{
			@Override
			public void run()
			{
				Download.unzip();
				Download.deleteZip();
				unzipped = true;
			}
		}
	}

	private boolean isInternetAvailable()
	{
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo)
		{
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

}

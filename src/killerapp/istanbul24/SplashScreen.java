package killerapp.istanbul24;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

import killerapp.istanbul24.db.DatabaseHelper;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends Activity
{
	ProgressBar progressBar;
	TextView textView;
	Activity activity;
	Handler handler;
	boolean unzipped = false;
	Location location = null;
	Intent myIntent;
	DatabaseHelper db;

	double longitude = 0;
	double latitude = 0;
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

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

	private class taskController extends Thread implements LocationListener
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

				return;
			}

			if (level == 4)
			{
				// TODO: check this
				if (!internet || System.currentTimeMillis() / 1000L - lastUpdate < 86400) // 24hours
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

				init = getSharedPreferences("init", 0);
				editor = init.edit();
				editor.putLong("lastUpdate", System.currentTimeMillis() / 1000L);
				editor.commit();

				first = true;
				level++;
				break;
			case 5:
				if (first)
				{
					textView.setText("Downloading POIs...");
					first = false;
				}

				if (db == null)
					db = new DatabaseHelper(activity);
				
				String poiJsonResult = null;
				String questionJsonResult = null;
				String categoryJsonResult = null;
				
				try
				{

					poiJsonResult = new HttpAsyncTask().execute(
								"http://sw2.obcdn.net/api/poi/all.json").get();
						questionJsonResult = new HttpAsyncTask().execute(
								"http://sw2.obcdn.net/api/question/all.json")
								.get();
						categoryJsonResult = new HttpAsyncTask().execute(
								"http://sw2.obcdn.net/api/category/all.json")
								.get();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}

				JSONParser.parsePois(poiJsonResult, db);
				JSONParser.parseQuestions(questionJsonResult, db);
				JSONParser.parseCategories(categoryJsonResult, db);

				db.exportDB();
				
				first = true;
				level++;
				break;
			case 6:
				if (first)
				{
					textView.setText("Location data is retrieving.");
					first = false;

					myIntent = new Intent(activity, MainActivity.class);

					LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
					boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
					boolean isNetworkEnabled = locationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

					if (!isGPSEnabled && !isNetworkEnabled)
					{
						new AlertDialog.Builder(activity)
								.setTitle("Warning")
								.setMessage("24Istanbul requires GPS service or any network connection to determine your location.")
								.setNeutralButton("OK", new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int which)
									{
										System.exit(0);
									}
								}).show();
					}
					else
					{
						if (isNetworkEnabled)
						{
							locationManager.requestLocationUpdates(
									LocationManager.NETWORK_PROVIDER,
									MIN_TIME_BW_UPDATES,
									MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
							Log.d("Network", "Network Enabled");
							if (locationManager != null)
							{
								location = locationManager
										.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
								if (location != null)
								{
									latitude = location.getLatitude();
									longitude = location.getLongitude();
								}
							}
						}

						if (isGPSEnabled)
						{
							if (location == null)
							{
								locationManager.requestLocationUpdates(
										LocationManager.GPS_PROVIDER,
										MIN_TIME_BW_UPDATES,
										MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
								Log.d("GPS", "GPS Enabled");
								if (locationManager != null)
								{
									location = locationManager
											.getLastKnownLocation(LocationManager.GPS_PROVIDER);
									if (location != null)
									{
										latitude = location.getLatitude();
										longitude = location.getLongitude();
									}
								}
							}
						}
					}

				}

				if (location != null)
				{
					myIntent.putExtra("long", longitude);
					myIntent.putExtra("lat", latitude);
					level++;
				}
				break;
			}

			if (level < 7)
				handler.postDelayed(this, 1000);
			else
			{
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

		@Override
		public void onLocationChanged(Location arg0)
		{
		}

		@Override
		public void onProviderDisabled(String arg0)
		{
		}

		@Override
		public void onProviderEnabled(String arg0)
		{
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2)
		{
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

	private class HttpAsyncTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... urls)
		{

			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result)
		{
//			Log.d("JSON", result);
		}
	}

	public static String GET(String url)
	{
		InputStream inputStream = null;
		String result = "";
		try
		{
			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(url);
			
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Accept-Encoding", "gzip");
			
			// make GET request which will accept gzip encoding to the given URL
			HttpResponse httpResponse = httpclient.execute(httpGet);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			Header contentEncoding = httpResponse
					.getFirstHeader("Content-Encoding");

			if (contentEncoding != null
					&& contentEncoding.getValue().equalsIgnoreCase("gzip"))
			{
				
				GZIPInputStream gis = new GZIPInputStream(inputStream);

				// convert inputStream to string
				if (inputStream != null)
					result = convertInputStreamToString(gis);
				else
					result = "Did not work!";
			}

		}
		catch (Exception e)
		{
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException
	{
		
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

}

package killerapp.istanbul24;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

@SuppressLint("NewApi")
public class Download
{
	public static void downloadFile(Activity currentActivity, String url, String fileName)
	{
		downloadFile(currentActivity, url, fileName, fileName, "File is downloading.");
	}
	
	public static void downloadFile(Activity currentActivity, String url, String fileName, String title, String description)
	{
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		request.setDescription(description);
		request.setTitle(title);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)	// After finish of download, notification won't be removed (Android 3+)
		{
			request.allowScanningByMediaScanner();
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}

		File dir = new File(Environment.getExternalStorageDirectory() + "/24Istanbul");	
		if (!dir.exists())	// If directory doesn't exist, create it.
			dir.mkdir();

		request.setDestinationInExternalPublicDir("/24Istanbul", fileName);

		DownloadManager manager = (DownloadManager) currentActivity.getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request);
	}
}

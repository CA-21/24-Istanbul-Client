package killerapp.istanbul24;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	private static File dir;
	
	private static void initDir()
	{
		if (dir == null)
			dir = new File(Environment.getExternalStorageDirectory() + "/24Istanbul");
	}
	
	public static File getDir()
	{
		return dir;
	}
	
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

		initDir();
		if (!dir.exists())	// If directory doesn't exist, create it.
			dir.mkdir();

		request.setDestinationInExternalPublicDir("/24Istanbul", fileName);

		DownloadManager manager = (DownloadManager) currentActivity.getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request);
	}
	
	public static boolean deleteZip() {
		File zipFile = new File(dir, "map.zip");
		
		if (zipFile.exists() && zipFile.isFile())
		{
			zipFile.delete();
			
			return true;
		}
		return false;
	}
	
	public static boolean unzip()
	{       
	     InputStream is;
	     ZipInputStream zis;
	     
	     initDir();
	     String path = dir + "/";
	     String zipname = "map.zip";
	     
	     try 
	     {
	         String filename;
	         is = new FileInputStream(path + zipname);
	         zis = new ZipInputStream(new BufferedInputStream(is));          
	         ZipEntry zipEntry;
	         byte[] buffer = new byte[1024];
	         int count;

	         while ((zipEntry = zis.getNextEntry()) != null) 
	         {
	             filename = zipEntry.getName();

	             // Need to create directories if not exists, or
	             // it will generate an Exception...
	             if (zipEntry.isDirectory()) {
	                File fmd = new File(path + filename);
	                fmd.mkdirs();
	                continue;
	             }

	             FileOutputStream fout = new FileOutputStream(path + filename);

	             while ((count = zis.read(buffer)) != -1) 
	             {
	                 fout.write(buffer, 0, count);             
	             }

	             fout.close();               
	             zis.closeEntry();
	         }

	         zis.close();
	     } 
	     catch(IOException e)
	     {
	         e.printStackTrace();
	         return false;
	     }
	     
	     return true;
	}
}

package killerapp.istanbul24;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class CurrentLocation implements LocationListener
{
	public static double longitude = 0;
	public static double latitude = 0;
	
	@Override
	public void onLocationChanged(Location location)
	{
		Log.d("Location","Location is retrieved from "+location.getProvider()+".");
		
		longitude = location.getLongitude();
		latitude = location.getLatitude();
	}

	@Override
	public void onProviderDisabled(String provider)
	{

	}

	@Override
	public void onProviderEnabled(String provider)
	{

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{

	}

}

package killerapp.istanbul24;

import killerapp.istanbul24.db.DatabaseHelper;

import org.mapsforge.core.model.GeoPoint;

import data.DataSource;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class RangeActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_range);

		DataSource.getInstance().nothingFound=false;

		//selected = intent.getIntegerArrayListExtra("selected");
	
		
		final TextView rangeView = (TextView) findViewById(R.id.rangeView);
		
		SeekBar seekbar = (SeekBar) findViewById(R.id.rangeSeek);
		seekbar.setMax(13);
		seekbar.setProgress(5);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{	
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				rangeView.setText("Range: "+ String.format("%.01f", (progress+1)/2.0)+" km");
			}
		});
	}
	
	public void rangeOK(View view)
	{
		SeekBar seekbar = (SeekBar) findViewById(R.id.rangeSeek);
		DatabaseHelper.radius = (float) dist2deg(CurrentLocation.latitude, CurrentLocation.longitude,(seekbar.getProgress()+1)*0.5)/1.41421356237f;
		
		Log.d("radius", ""+DatabaseHelper.radius);
		
		Intent intent = new Intent(getBaseContext(), QuestionActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent); // Activity is created.
		finish();
	}
	

	
	/**
	 * Returns the approximate distance in degrees. Starting point must be provided.
	 * @param lat 
	 * @param lon
	 * @param dist
	 */
	private double dist2deg(double lat, double lon, double dist)
	{
		double distInDegrees = 0;

		// number of kms in 1 degrees of longitude
		double avgDist = 111.32 * (Math.cos(deg2rad(lat)) + Math
				.cos(deg2rad(lat + 1))) / 2;

		double avgDistInDeg = dist / avgDist;
		
		// 1 degrees of latitude is approximately (average) 111.133 km
		distInDegrees = Math.sqrt(avgDistInDeg * avgDistInDeg + 1);
		
		//return distInDegrees;
		return avgDistInDeg;
	}
	
	/**
	 * Returns the approximate distance in degrees. Starting point must be provided.
	 * @param p
	 * @param dist
	 */
	private double dist2deg(GeoPoint p, double dist)
	{
		return dist2deg(p.latitude, p.longitude, dist);
	}
	
	private double deg2rad(double deg)
	{
		// multiply with (PI / 180.0)
		return (deg * 0.017453292519943278);
	}

}

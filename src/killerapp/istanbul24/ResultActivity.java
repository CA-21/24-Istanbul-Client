package killerapp.istanbul24;

import java.io.Serializable;
import java.util.ArrayList;

import killerapp.istanbul24.adapters.ResultListAdapter;
import killerapp.istanbul24.db.DatabaseHelper;
import killerapp.istanbul24.db.Venue;

import org.mapsforge.core.model.GeoPoint;

import data.DataSource;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Displays the places of interest that are filtered according to the user's
 * answers.
 * 
 */
public class ResultActivity extends Activity
{
	DataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		dataSource = DataSource.getInstance();
		dataSource.setVenues(sortVenues(dataSource.getVenues()));

		if (dataSource.nothingFound)
		{
			TextView warningText = (TextView)findViewById(R.id.warning_text);
			warningText.setVisibility(View.VISIBLE);
			DatabaseHelper db = new DatabaseHelper(this);
			if (CurrentLocation.longitude == 0)
			{
				// Fake location for VM or broken GPS
				CurrentLocation.longitude = 28.986435;
				CurrentLocation.latitude = 41.036762;
			}
			dataSource.setVenues(sortVenues(db.getVenues(CurrentLocation.longitude, CurrentLocation.latitude)));
		}


		for (Venue venue : dataSource.getVenues() )
		{
			GeoPoint geo1 = new GeoPoint(venue.getLatitude(), venue.getLongitude());
			GeoPoint geo2 = new GeoPoint(CurrentLocation.latitude, CurrentLocation.longitude);
			int calculatedDistance = (int) (calculateDistance(geo1, geo2) * 1000);
			venue.setCalculatedDistance(calculatedDistance);
		}

		ListView listView = (ListView) this.findViewById(R.id.listView_items);

		ResultListAdapter itemAdapter = new ResultListAdapter(this, dataSource.getVenues() );
		listView.setAdapter(itemAdapter);

		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3)
			{
				GeoPoint start = new GeoPoint(CurrentLocation.latitude,
						CurrentLocation.longitude);

				GeoPoint end = new GeoPoint(dataSource.getVenues().get(position).getLatitude(),
						dataSource.getVenues().get(position).getLongitude());

				Intent intent = new Intent(ResultActivity.this, RouteActivity.class);
				intent.putExtra("start", start);
				intent.putExtra("end", end);
				intent.putExtra("venue", (Serializable) dataSource.getVenues().get(position));
				startActivity(intent);
			}
		});

	}

	private ArrayList<Venue> sortVenues(ArrayList<Venue> oldVenues)
	{
		ArrayList<Venue> newVenues = new ArrayList<Venue>();
		int venueCount = oldVenues.size();
		Venue smallest;

		for (int i = 0; i < venueCount; i++)
		{
			smallest = oldVenues.get(0);

			for (Venue venue : oldVenues)
			{
				if (smallest.getFakeDistance() > venue.getFakeDistance())
				{
					smallest = venue;
				}
			}

			newVenues.add(smallest);
			oldVenues.remove(smallest);
		}

		return newVenues;
	}

	private double calculateDistance(double lat1, double lon1, double lat2,
			double lon2)
	{
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 111.18957696;

		return (dist);
	}

	private double deg2rad(double deg)
	{
		// multiply with (PI / 180.0)
		return (deg * 0.017453292519943278);
	}

	private double rad2deg(double rad)
	{
		// multiply with (180.0 / PI)
		return (rad * 57.29577951308238);
	}

	private double calculateDistance(GeoPoint p1, GeoPoint p2)
	{
		return calculateDistance(p1.latitude, p1.longitude, p2.latitude,
				p2.longitude);
	}

}

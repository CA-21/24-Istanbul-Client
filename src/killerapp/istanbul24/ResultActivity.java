package killerapp.istanbul24;

import java.io.Serializable;
import java.util.ArrayList;

import killerapp.istanbul24.db.Venue;

import org.mapsforge.core.model.GeoPoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Displays the places of interest that are filtered according to the user's
 * answers.
 * 
 */
public class ResultActivity extends Activity
{
	private ArrayList<Venue> venues;
	private ResultActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		instance = this;
		Intent intent = getIntent();
		venues = intent.getParcelableArrayListExtra("venues");
		venues = sortVenues(venues);
		ArrayList<String> nameList = new ArrayList<String>();

		for (Venue venue : venues)
		{
			nameList.add(venue.getName());
		}

		ListView listView = (ListView) this.findViewById(R.id.listView_items);

		ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList);
		listView.setAdapter(itemAdapter);

		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3)
			{
				GeoPoint start = new GeoPoint(CurrentLocation.latitude,
						CurrentLocation.longitude);

				GeoPoint end = new GeoPoint(venues.get(position).getLatitude(),
						venues.get(position).getLongitude());

				Intent intent = new Intent(instance, RouteActivity.class);
				intent.putExtra("start", start);
				intent.putExtra("end", end);
				intent.putExtra("venue", (Serializable) venues.get(position));
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
				if(smallest.getFakeDistance() > venue.getFakeDistance())
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

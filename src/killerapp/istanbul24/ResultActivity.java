package killerapp.istanbul24;

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
				// TODO: set intent and create RouteActivity

				GeoPoint start = new GeoPoint(CurrentLocation.latitude,
						CurrentLocation.longitude);

				GeoPoint end = new GeoPoint(venues.get(position).getLatitude(),
						venues.get(position).getLongitude());

				Intent intent = new Intent(instance, RouteActivity.class);
				intent.putExtra("start", start);
				intent.putExtra("end", end);
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

}

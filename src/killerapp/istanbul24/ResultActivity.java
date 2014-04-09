package killerapp.istanbul24;

import java.util.ArrayList;

import killerapp.istanbul24.db.Venue;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ResultActivity extends Activity
{
	private ArrayList<Venue> venues;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		Intent intent = getIntent();
		venues = intent.getParcelableArrayListExtra("venues");
		ArrayList<String> nameList = new ArrayList<String>();
		
		for(Venue venue:venues)
			nameList.add(venue.getName());
		
		ListView listView = (ListView)this.findViewById(R.id.listView_items);
		
		ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nameList);
        listView.setAdapter(itemAdapter);
        
        listView.setOnItemClickListener(new OnItemClickListener()
		{
        	@Override 
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            { 
                // TODO: set intent and create RouteActivity
            }
		});
	}


}

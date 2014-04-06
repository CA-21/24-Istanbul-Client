package killerapp.istanbul24;

import java.util.ArrayList;

import org.mapsforge.core.model.GeoPoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startDemo(View view)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		// TODO: Tags of nearest POIs will retrieved from database.
		
		ArrayList<Integer> selected = new ArrayList<Integer>(); // It is empty. Selected tags will add into this.
		ArrayList<Place> places = new ArrayList<Place>();	// It is empty. Places will add into this.
		
		Intent intent = new Intent(this, QuestionActivity.class);	// Intent is created.
		intent.putExtra("list", list);
		intent.putExtra("selected", selected);
		intent.putExtra("question", 0);
		intent.putParcelableArrayListExtra("places", places);
		startActivity(intent);	// Activity is created.
	}

	public void routeDemo(View view)
	{
		// If can't unzip and can't find the map directory, stop
		if (!Download.unzip() && !Download.getDir().exists())
		{
			Toast.makeText(this, "Please download the map.", Toast.LENGTH_LONG)
					.show();
			return;
		}
		else
			Download.deleteZip();
		/*
		GeoPoint start = new GeoPoint(41.02546548103654, 28.968420743942254);
		GeoPoint end = new GeoPoint(41.011898495994, 28.97524428367614);
		*/
		
		GeoPoint start = new GeoPoint(41.0119,28.925972);
		GeoPoint end = new GeoPoint(41.01177,29.013519);
		
		Intent intent = new Intent(this, RouteActivity.class);
		intent.putExtra("start", start);
		intent.putExtra("end", end);
		startActivity(intent);
		
	}
	
	public void shareDemo(View view)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "I found this awesome place with 24Istanbul.");
		
		//startActivity(Intent.createChooser(fbIntent(), "Share with"));
		startActivity(intent);
	}

}

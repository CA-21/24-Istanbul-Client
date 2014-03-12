package killerapp.istanbul24;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

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

}

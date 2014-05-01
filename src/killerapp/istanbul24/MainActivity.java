package killerapp.istanbul24;

import java.util.ArrayList;
import java.util.Calendar;

import killerapp.istanbul24.db.DatabaseHelper;
import killerapp.istanbul24.db.Venue;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * The main activity. Categories are selected.
 * 
 */
public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageView imageView = (ImageView) findViewById(R.id.mainImage);
		
		
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		
		if (hour >= 6 && hour < 12)
		{
			imageView.setBackgroundResource(R.drawable.flag_start);
		}
		else if (hour >= 12 && hour < 18)
		{
			imageView.setBackgroundResource(R.drawable.flag_start);
		}
		else if (hour >= 18 && hour < 23)
		{
			imageView.setBackgroundResource(R.drawable.flag_end);
		}
		else
		{
			imageView.setBackgroundResource(R.drawable.flag_end);
		}
	}

	public void startQuestionActivity(View view)
	{
		int catID = 0;
	
		switch (view.getId())
		{
		case R.id.eatButton:
			catID = 1;
			break;
		case R.id.attractionsButton:
			catID = 2;
			break;
		case R.id.goButton:
			catID = 3;
			break;
		}
		
		ArrayList<Integer> questions = new DatabaseHelper(this).getQuestions(catID);

//		ArrayList<Integer> selected = new ArrayList<Integer>(); // It is empty.
																// Selected tags
																// will add into
																// this.
		ArrayList<Venue> venues = new ArrayList<Venue>(); // It is empty. Places
															// will add into
															// this.

		Intent intent = new Intent(getBaseContext(), RangeActivity.class); // Intent is
																	// created.
//		intent.putExtra("selected", selected);
		intent.putExtra("question", 0);
		intent.putExtra("questions", questions);
		intent.putParcelableArrayListExtra("venues", venues);
		intent.putIntegerArrayListExtra("questions", questions);
		startActivity(intent); // Activity is created.
	}



}

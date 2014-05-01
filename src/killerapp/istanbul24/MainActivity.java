package killerapp.istanbul24;

import java.util.ArrayList;
import java.util.Calendar;

import data.DataSource;
import killerapp.istanbul24.db.DatabaseHelper;
import killerapp.istanbul24.db.Venue;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The main activity. Categories are selected.
 * 
 */
public class MainActivity extends Activity
{

    private TextView greetingView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        greetingView = (TextView)findViewById(R.id.greetingTextView);
        greetingView.setText(TimeHelper.getGreeting());

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
		DataSource.getInstance().setQuestions(questions);

		Intent intent = new Intent(getBaseContext(), RangeActivity.class); // Intent is created.
		startActivity(intent); // Activity is created.
	}



}

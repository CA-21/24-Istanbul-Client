package killerapp.istanbul24;

import java.util.ArrayList;
import java.util.Random;

import killerapp.istanbul24.db.DatabaseHelper;
import killerapp.istanbul24.db.Option;
import killerapp.istanbul24.db.Question;
import killerapp.istanbul24.db.Venue;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Displays questions related to the chosen categories, and filters nearby
 * places of interest according to the answers given.
 * 
 */
public class QuestionActivity extends Activity implements OnClickListener
{
	private ArrayList<Integer> selected;
	private Question question;
	private int questionCount;
	private ArrayList<Venue> venues;
	private ArrayList<Integer> questions;
	ArrayList<Option> options;
	private DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity);

		Log.d("long",CurrentLocation.longitude+"");

		db = new DatabaseHelper(this);
		
		TextView questionView = (TextView) findViewById(R.id.questionView);

		Intent intent = getIntent();

		//selected = intent.getIntegerArrayListExtra("selected");
		questionCount = intent.getIntExtra("question", 0);
		venues = intent.getParcelableArrayListExtra("venues");
		questions = intent.getIntegerArrayListExtra("questions");
		
		int questionIndex = new Random().nextInt(questions.size());
		int questionID = questions.get(questionIndex);

        
		question = db.getQuestion(questionID);//new Question(questionID, db);
		questionView.setText(question.getQuestion());

		options = db.getOptions(questionID);
		
		// TODO: Buttons will be created dynamicly (Button count may vary)
		// TODO: There will be a "Skip this question" button.
		Button button1 = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		Button skip = (Button) findViewById(R.id.skip);

		button1.setText(options.get(0).getName());
		button2.setText(options.get(1).getName());
		skip.setText("Skip this question");

		// TODO: onClick listeners will be added for each button
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		skip.setOnClickListener(this);
		
		questions.remove(new Integer(questionID));

	}

	@Override
	public void onClick(View button)
	{
		int option = -1;

		// TODO: Button count may vary. Handle this.
		switch (button.getId())
		{
		case R.id.button1:
			option = 0;
			break;
		case R.id.button2:
			option = 1;
			break;
		}

		if (option != -1)
		{
			int newTag = options.get(option).getTagId();
		
			if(newTag != -1)
			{
//				if(selected == null) {
//					selected = new ArrayList<Integer>();
//				}
//				selected.add(newTag);

				ArrayList<Venue> venuesToAdd = db.getVenues(newTag,
						CurrentLocation.longitude, CurrentLocation.latitude);
				
				boolean isFound = true;
				for (Venue venueToAdd : venuesToAdd)
				{
					int len = venues.size();
					if (len > 0)
					{
						isFound = false;
						for (Venue venue : venues)
						{
							if (venue.equals(venueToAdd.getId()))
							{
								isFound = true;
								break;
							}
						}
					}
					else
						venues.add(venueToAdd);
					
					if (!isFound)
						venues.add(venueToAdd);
				}
			}

			questionCount++;

		}
		
		if (questionCount < 5 && venues.size() < 5 && questions.size()>0)
		{
			Intent intent = new Intent(getBaseContext(), QuestionActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.putIntegerArrayListExtra("selected", selected);
			intent.putIntegerArrayListExtra("questions", questions);
			intent.putExtra("question", questionCount);
			intent.putParcelableArrayListExtra("venues", venues);
			startActivity(intent); // Activity is created.

		}
		else
		{
			Intent intent = new Intent(this, ResultActivity.class);
			intent.putParcelableArrayListExtra("venues", venues);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent); // Activity is created.
		}


	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (db != null)
			db.close();
	}

}

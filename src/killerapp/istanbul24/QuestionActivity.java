package killerapp.istanbul24;

import java.util.ArrayList;
import java.util.Random;

import data.DataSource;
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
import android.widget.Toast;

/**
 * Displays questions related to the chosen categories, and filters nearby
 * places of interest according to the answers given.
 * 
 */
public class QuestionActivity extends Activity implements OnClickListener
{
	private Question question;
	private int questionCount;
	ArrayList<Option> options;
	private DatabaseHelper db;
    private TextView greetingView;
    private DataSource dataSource;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity);

		Log.d("long",CurrentLocation.longitude+"");

		db = new DatabaseHelper(this);
		
		TextView questionView = (TextView) findViewById(R.id.questionView);

		dataSource = DataSource.getInstance();
		questionCount = dataSource.getQuestionCount();
		
		int questionIndex = new Random().nextInt(dataSource.getQuestions().size());
		int questionID = dataSource.getQuestions().get(questionIndex);

        greetingView = (TextView)findViewById(R.id.greetingView);
        greetingView.setText(TimeHelper.getGreeting());

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
		
		dataSource.getQuestions().remove(Integer.valueOf(questionID));

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
					int len = dataSource.getVenues().size();
					if (len > 0)
					{
						isFound = false;
						for (Venue venue : dataSource.getVenues())
						{
							if (venue.equals(venueToAdd))
							{
								isFound = true;
								break;
							}
						}
					}
					else
						dataSource.getVenues().add(venueToAdd);
					
					if (!isFound)
						dataSource.getVenues().add(venueToAdd);
				}
			}

			questionCount++;

		}
		if(dataSource.getVenues().size()==0){
			DataSource.getInstance().nothingFound=true;
			goToResults();
		}
		if (questionCount < 5 && dataSource.getVenues().size() < 5 && dataSource.getVenues().size()>0)
		{
			Intent intent = new Intent(getBaseContext(), QuestionActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent); // Activity is created.

		}
		else
		{
			goToResults();
		}

		finish();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (db != null)
			db.close();
	}

	public void goToResults(){
		Intent intent = new Intent(this, ResultActivity.class);
		startActivity(intent); // Activity is created.
	}
}

package killerapp.istanbul24;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class QuestionActivity extends Activity implements OnClickListener
{
	private ArrayList<Integer> list;
	private ArrayList<Integer> selected;
	private Question question;
	private int questionCount;
	private ArrayList<Place> places;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity);

		TextView questionView = (TextView) findViewById(R.id.questionView);

		Intent intent = getIntent();

		list = intent.getIntegerArrayListExtra("list");
		selected = intent.getIntegerArrayListExtra("selected");
		questionCount = intent.getIntExtra("question", 0);
		places = intent.getParcelableArrayListExtra("places");

		// TODO: A question will be choosen for a random tag in list.
		int questionID = 42;
		question = new Question(questionID);
		questionView.setText(question.toString());

		// TODO: Buttons will be created dynamicly (Button count may vary)
		// TODO: There will be a "Skip this question" button.
		Button button1 = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		Button button3 = (Button) findViewById(R.id.button3);
		Button skip = (Button) findViewById(R.id.skip);

		button1.setText(question.getOption(0));
		button2.setText(question.getOption(1));
		button3.setText(question.getOption(2));
		skip.setText("Skip this question");

		// TODO: onClick listeners will be added for each button
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		skip.setOnClickListener(this);

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
		case R.id.button3:
			option = 2;
			break;
		}

		if (option != -1)
		{
			selected.add(question.getTagID(option));
			questionCount++;

			// TODO: New places will be added into places arraylist.
		}

		if (questionCount < 5 && places.size() < 5)
		{
			Intent intent = new Intent(this, QuestionActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("list", list);
			intent.putExtra("selected", selected);
			intent.putExtra("question", questionCount);
			intent.putParcelableArrayListExtra("places", places);
			startActivity(intent); // Activity is created.
		}
		else
		{
			Intent intent = new Intent(this, ResultActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent); // Activity is created.
		}

	}

}

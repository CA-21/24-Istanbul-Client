package killerapp.istanbul24;

import java.util.ArrayList;

import killerapp.istanbul24.db.DatabaseHelper;
import killerapp.istanbul24.db.Venue;

import org.mapsforge.core.model.GeoPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.settings_range_button:
				final EditText input = new EditText(this);
				input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

				SharedPreferences init = getSharedPreferences("init", 0);
				input.setText(init.getFloat("range", 1) + "");

				new AlertDialog.Builder(this)
						.setView(input)
						.setTitle(R.string.settings_range)
						.setNeutralButton("Ok",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,
											int whichButton)
									{
										try
										{
											float range = Float
													.parseFloat(input.getText()
															.toString());
											
											DatabaseHelper.radius = range;
											Editor editor = getSharedPreferences(
													"init", 0).edit();
											editor.putFloat("range", range);
											editor.commit();
										}
										catch (NumberFormatException nfe)
										{

										}

									}
								}).show();
				break;
		}
		return true;
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

		Intent intent = new Intent(getBaseContext(), QuestionActivity.class); // Intent is
																	// created.
//		intent.putExtra("selected", selected);
		intent.putExtra("question", 0);
		intent.putExtra("questions", questions);
		intent.putParcelableArrayListExtra("venues", venues);
		startActivity(intent); // Activity is created.
	}



}

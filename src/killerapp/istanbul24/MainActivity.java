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
		startActivity(intent); // Activity is created.
	}



}

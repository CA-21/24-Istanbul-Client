package killerapp.istanbul24.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "istanbul24";

	// Table Names
	private static final String TABLE_VENUE = "venues";
	private static final String TABLE_QUESTION = "questions";
	private static final String TABLE_CATEGORY = "categories";
	private static final String TABLE_TAG = "tags";
	private static final String TABLE_OPTION = "options";
	private static final String TABLE_VENUE_META = "venue_metas";

	// Common columns (not necessarily in every table!)
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_TAG_ID = "tagId";
	private static final String KEY_CATEGORY_ID = "categoryId";
	private static final String KEY_LAST_UPDATE_DATE = "lastUpdateDate";

	// VENUES Table - columns
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_RATING = "rating";

	// QUESTIONS Table - columns
	private static final String KEY_QUESTION = "question";

	// CATEGORIES Table - columns
	// all defined in common columns

	// OPTIONS Table - columns
	private static final String KEY_QUESTION_ID = "questionId";

	// TAGS Table - columns
	// all defined in common columns

	// VENUE_METAS Table - columns
	// all defined in common columns

	// types
	private static final String TYPE_INTEGER = "INTEGER";
	private static final String TYPE_DATETIME = "DATETIME";
	private static final String TYPE_TEXT = "TEXT";
	private static final String TYPE_REAL = "REAL";
	
	// modifiers
	private static final String MOD_PK = "PRIMARY KEY";
	private static final String MOD_NOT_NULL = "NOT NULL";
	
	// attributes - TODO write clearer code, may combine keys with attributes
	private static final Attribute ATTR_ID = new Attribute(KEY_ID, TYPE_INTEGER, MOD_PK, MOD_NOT_NULL);
	private static final Attribute ATTR_NAME = new Attribute(KEY_NAME, TYPE_TEXT);
	private static final Attribute ATTR_TAG_ID = new Attribute(KEY_TAG_ID, TYPE_INTEGER, MOD_NOT_NULL);
	private static final Attribute ATTR_CATEGORY_ID = new Attribute(KEY_CATEGORY_ID, TYPE_INTEGER, MOD_NOT_NULL);
	private static final Attribute ATTR_LAST_UPDATE_DATE = new Attribute(KEY_LAST_UPDATE_DATE, TYPE_DATETIME);
	private static final Attribute ATTR_ADDRESS = new Attribute(KEY_ADDRESS, TYPE_TEXT);
	private static final Attribute ATTR_LONGITUDE = new Attribute(KEY_LONGITUDE, TYPE_REAL);
	private static final Attribute ATTR_LATITUDE = new Attribute(KEY_LATITUDE, TYPE_REAL);
	private static final Attribute ATTR_RATING = new Attribute(KEY_RATING, TYPE_REAL);
	private static final Attribute ATTR_QUESTION = new Attribute(KEY_QUESTION, TYPE_TEXT);
	private static final Attribute ATTR_QUESTION_ID = new Attribute(KEY_QUESTION_ID, TYPE_INTEGER, MOD_NOT_NULL);
	
	public DatabaseHelper(Context context, CursorFactory factory)
	{
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 *  
	 * @param tableName
	 * @param args
	 * @return SQL command for creating the table with given parameters.
	 */
	public String createTable(String tableName, Attribute... args)
	{
		String attrs = "";
		int i = 0, len = args.length - 1;
		
		// don't add a comma if it's the last attribute
		for (Attribute attr : args) 
			attrs += (i++ != len) ? attr + ", " : attr;
		
		return "CREATE TABLE " + tableName + "(" + attrs + ");";
	}
	
	public String updateTable(String tableName)
	{
		return "NOT YET IMPLEMENED!";
	}
	
	private void log(String msg)
	{
		Log.d("DB-Istanbul24", msg);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		//db.execSQL(createTable(TABLE_TAG, ATTR_ID, ATTR_CATEGORY_ID, ATTR_NAME));
		//db.execSQL(createTable(TABLE_, ATTR_ID, ATTR_));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
	}

}

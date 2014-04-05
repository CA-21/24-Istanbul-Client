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

//	// Table Names
//	private static final String TABLE_VENUE = "venues";
//	private static final String TABLE_QUESTION = "questions";
//	private static final String TABLE_CATEGORY = "categories";
//	private static final String TABLE_TAG = "tags";
//	private static final String TABLE_OPTION = "options";
//	private static final String TABLE_VENUE_META = "venue_metas";
//
//	// Common columns (not necessarily in every table!)
//	private static final String KEY_ID = "id";
//	private static final String KEY_NAME = "name";
//	private static final String KEY_TAG_ID = "tagId";
//	private static final String KEY_VENUE_ID = "venueId";
//	private static final String KEY_CATEGORY_ID = "categoryId";
//	private static final String KEY_LAST_UPDATE_DATE = "lastUpdateDate";
//
//	// VENUES Table - columns
//	private static final String KEY_ADDRESS = "address";
//	private static final String KEY_LONGITUDE = "longitude";
//	private static final String KEY_LATITUDE = "latitude";
//	private static final String KEY_RATING = "rating";
//
//	// QUESTIONS Table - columns
//	private static final String KEY_QUESTION = "question";
//
//	// CATEGORIES Table - columns
//	// all defined in common columns
//
//	// OPTIONS Table - columns
//	private static final String KEY_QUESTION_ID = "questionId";
//
//	// TAGS Table - columns
//	// all defined in common columns
//
//	// VENUE_METAS Table - columns
//	// all defined in common columns
//
//	// types
//	private static final String TYPE_INT = "INT(10)";
//	private static final String TYPE_TINYINT = "TINYINT(3)";
//	private static final String TYPE_DATETIME = "DATETIME";
//	private static final String TYPE_DATE = "DATE";
//	private static final String TYPE_VARCHAR36 = "VARCHAR(36)";
//	private static final String TYPE_REAL = "REAL";
//
//	// modifiers
//	private static final String MOD_PK = "PRIMARY KEY";
//	private static final String MOD_FK = "FOREIGN KEY";
//	private static final String MOD_NOT_NULL = "NOT NULL";
//	private static final String MOD_AUTOINCREMENT = "AUTOINCREMENT";
//	
//	// attributes - TODO write clearer code, may combine keys with attributes
//	private static final Attribute ATTR_VENUE_ID = new Attribute(KEY_VENUE_ID, "VARCHAR(36)", MOD_PK, MOD_NOT_NULL);
//	private static final Attribute ATTR_NAME = new Attribute(KEY_NAME, "VARCHAR(32)");
//	private static final Attribute ATTR_TAG_ID = new Attribute(KEY_TAG_ID, "TINYINT(4)", MOD_NOT_NULL, MOD_AUTOINCREMENT);
//	private static final Attribute ATTR_CATEGORY_ID = new Attribute(KEY_CATEGORY_ID, "TINYINT(4)", MOD_NOT_NULL, MOD_AUTOINCREMENT);
//	private static final Attribute ATTR_LAST_UPDATE_DATE = new Attribute(KEY_LAST_UPDATE_DATE, "DATE");
//	private static final Attribute ATTR_ADDRESS = new Attribute(KEY_ADDRESS, "VARCHAR(255)");
//	private static final Attribute ATTR_LONGITUDE = new Attribute(KEY_LONGITUDE, "FLOAT(10,6)");
//	private static final Attribute ATTR_LATITUDE = new Attribute(KEY_LATITUDE, "FLOAT(10,6)");
//	private static final Attribute ATTR_RATING = new Attribute(KEY_RATING, "FLOAT(4,2)");
//	private static final Attribute ATTR_QUESTION = new Attribute(KEY_QUESTION, "VARCHAR(255)");
//	private static final Attribute ATTR_QUESTION_ID = new Attribute(KEY_QUESTION_ID, "TINYINT(4)", MOD_NOT_NULL, MOD_AUTOINCREMENT);

	private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE IF NOT EXISTS categories ("
			+ "id TINYINT(4) PRIMARY KEY NOT NULL AUTO_INCREMENT,"
			+ "name VARCHAR(32) NOT NULL,"
			+ "lastUpdateDate DATE NOT NULL"
			+ ");";

	private static final String CREATE_TABLE_OPTIOINS = "CREATE TABLE IF NOT EXISTS options ("
			+ "id TINYINT(4) PRIMARY KEY NOT NULL AUTO_INCREMENT,"
			+ "questionId TINYINT(4) unsigned NOT NULL,"
			+ "name VARCHAR(64) NOT NULL,"
			+ "tagId TINYINT(4) NOT NULL,"
			+ "FOREIGN KEY(tagId) REFERENCES tags(id),"
			+ "FOREIGN KEY(questionId) REFERENCES questions(id)," + ");";

	private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE IF NOT EXISTS questions ("
			+ "id TINYINT(4) PRIMARY KEY NOT NULL AUTO_INCREMENT,"
			+ "question VARCHAR(255) NOT NULL,"
			+ "lastUpdateDate DATE NOT NULL," + ");";

	private static final String CREATE_TABLE_VENUE_META = "CREATE TABLE IF NOT EXISTS venue_meta ("
			+ "id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,"
			+ "venueId VARCHAR(36) NOT NULL,"
			+ "tagId TINYINT(4) NOT NULL,"
			+ "FOREIGN KEY(venueId) REFERENCES venues(id),"
			+ "FOREIGN KEY(tagId) REFERENCES tags(id)" + ");";

	private static final String CREATE_TABLE_VENUES = "CREATE TABLE IF NOT EXISTS venues ("
			+ "id VARCHAR(36) PRIMARY KEY NOT NULL AUTO_INCREMENT,"
			+ "name VARCHAR(255) NOT NULL,"
			+ "latitude FLOAT(10,6) NOT NULL,"
			+ "longitude FLOAT(10,6) NOT NULL,"
			+ "lastUpdateDate DATE NOT NULL,"
			+ "rating FLOAT(5,2) DEFAULT NULL,"
			+ "address VARCHAR(255) DEFAULT NULL" + ");";

	public DatabaseHelper(Context context, CursorFactory factory)
	{
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
//	/**
//	 *  
//	 * @param tableName
//	 * @param args
//	 * @return SQL command for creating the table with given parameters.
//	 */
//	public String createTable(String tableName, Attribute... args)
//	{
//		String attrs = "";
//		int i = 0, len = args.length - 1;
//		
//		// don't add a comma if it's the last attribute
//		for (Attribute attr : args) 
//			attrs += (i++ != len) ? attr + ", " : attr;
//		
//		return "CREATE TABLE " + tableName + "(" + attrs + ");";
//	}
//	
//	public String updateTable(String tableName)
//	{
//		return "NOT YET IMPLEMENED!";
//	}
	
	private void log(String msg)
	{
		Log.d("DB-Istanbul24", msg);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		//db.execSQL(CREATE_TABLE_TAGS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
	}

}

package killerapp.istanbul24;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import killerapp.istanbul24.db.Category;
import killerapp.istanbul24.db.DatabaseHelper;
import killerapp.istanbul24.db.Option;
import killerapp.istanbul24.db.Question;
import killerapp.istanbul24.db.Tag;
import killerapp.istanbul24.db.Venue;
import killerapp.istanbul24.db.VenueMeta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

public class JSONParser
{
	private final static DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-mm-dd");

	public static void parsePois(String str, DatabaseHelper dbHelper)
	{
	//	assert (dbHelper != null);

		try
		{
			JSONObject jsonObject = new JSONObject(str);

			int count = jsonObject.getInt("count");
			Log.d("JSON", count + " pois received.");

			JSONArray pois = jsonObject.getJSONArray("pois");

			int i = 0, j;
			for (; i < count; ++i)
			{
				JSONObject poi = pois.getJSONObject(i);
				String venueId = poi.getString("id");
				String updateDate = poi.getString("update_date");

				Venue exVenue = null;
				
				try 
				{
					exVenue = dbHelper.getVenue(venueId);
				}
				catch (CursorIndexOutOfBoundsException e)
				{
					Log.d("24Istanbul-DB", e.getMessage());
				}
				
				if (exVenue == null)
				{
					dbHelper.createVenue(new Venue(venueId, poi
							.getString("address"), poi.getString("name"), poi
							.getDouble("lng"), poi.getDouble("lat"), updateDate));
				}
				else
				{
					Date dateNew = null, dateOld = null;
					
					try
					{
						dateNew = DATE_FORMAT.parse(updateDate);
						dateOld = DATE_FORMAT.parse(exVenue.getLastUpdateDate());
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					}
					
					if (dateNew.after(dateOld))
					{
						dbHelper.updateVenue(new Venue(venueId, poi
								.getString("address"), poi.getString("name"), poi
								.getDouble("lng"), poi.getDouble("lat"), updateDate));
					}
				}
				
				JSONArray tags = poi.getJSONArray("tags");

				for (j = 0; j < tags.length(); ++j)
				{
					int tagId = Integer.parseInt(tags.getString(j));

					VenueMeta exVenueMeta = null;

					try 
					{
						exVenueMeta = dbHelper.getVenueMeta(tagId,
								venueId);
					}
					catch (CursorIndexOutOfBoundsException e)
					{
						Log.d("24Istanbul-DB", e.getMessage());
					}
					
					if (exVenueMeta == null)
						dbHelper.createVenueMeta(new VenueMeta(tagId, venueId));
					else
						Log.d("24Istanbul-DB", "VenueMeta already exists.");
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

	}

	public static void parseQuestions(String str, DatabaseHelper dbHelper)
	{
		//assert (dbHelper != null);
		
		try
		{
			JSONObject jsonObject = new JSONObject(str);
			
			int count = jsonObject.getInt("count");
			Log.d("JSON", count + " questions received.");

			JSONArray questions = jsonObject.getJSONArray("question");
			
			int i = 0, j;
			for (; i < count; ++i)
			{
				JSONObject question = questions.getJSONObject(i);
				int questionId = question.getInt("id");
				String updateDate = question.getString("update_date");

				Question exQuestion = null;
				
				try 
				{
					exQuestion = dbHelper.getQuestion(questionId);
				}
				catch (CursorIndexOutOfBoundsException e)
				{
					Log.d("24Istanbul-DB", e.getMessage());
				}
				
				if (exQuestion == null)
				{
					dbHelper.createQuestion(new Question(question.getString("question"), 
							question.getString("update_date")));
				}
				else
				{
					Date dateNew = null, dateOld = null;
					
					try
					{
						dateNew = DATE_FORMAT.parse(updateDate);
						dateOld = DATE_FORMAT.parse(exQuestion.getLastUpdateDate());
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					}
					
					if (dateNew.after(dateOld))
					{
						dbHelper.updateQuestion(new Question(question.getString("question"), 
								question.getString("update_date")));
					}
				}
				
				JSONArray options = question.getJSONArray("options");

				for (j = 0; j < options.length(); ++j)
				{
					
					JSONObject option = options.getJSONObject(j);
					Option exOption = null;

					try 
					{
						exOption = dbHelper.getOption(option.getInt("id"));
					}
					catch (CursorIndexOutOfBoundsException e)
					{
						Log.d("24Istanbul-DB", e.getMessage());
					}
					
					int tagId;
					try
					{
						tagId = option.getInt("tag");
					}
					catch (Exception e)
					{
						tagId = -1;
					}
					
					if (exOption == null)
						dbHelper.createOption(new Option(question.getInt("id"), tagId, option.getString("text")));
					else
						Log.d("24Istanbul-DB", "Option already exists.");
				}
			}
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
	}

	public static void parseCategories(String str, DatabaseHelper dbHelper)
	{
		//assert (dbHelper != null);
		
				try
				{
					JSONObject jsonObject = new JSONObject(str);
					
					int count = jsonObject.getInt("count");
					Log.d("JSON", count + " categories received.");

					JSONArray questions = jsonObject.getJSONArray("category");
					
					int i = 0, j;
					for (; i < count; ++i)
					{
						JSONObject category = questions.getJSONObject(i);
						int categoryId = category.getInt("id");
						String updateDate = category.getString("update_date");

						Category exCategory = null;
						
						try 
						{
							exCategory = dbHelper.getCategory(categoryId);
						}
						catch (CursorIndexOutOfBoundsException e)
						{
							Log.d("24Istanbul-DB", e.getMessage());
						}
						
						if (exCategory == null)
						{
							dbHelper.createCategory(new Category(category.getString("name"), 
									category.getString("update_date")));
						}
						else
						{
							Date dateNew = null, dateOld = null;
							
							try
							{
								dateNew = DATE_FORMAT.parse(updateDate);
								dateOld = DATE_FORMAT.parse(exCategory.getLastUpdateDate());
							}
							catch (ParseException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							if (dateNew.after(dateOld))
							{
								dbHelper.updateCategory(new Category(category.getString("name"), 
										category.getString("update_date")));
							}
						}
						
						JSONArray tags = category.getJSONArray("tags");

						for (j = 0; j < tags.length(); ++j)
						{
							
							JSONObject tag = tags.getJSONObject(j);
							Tag exTag = null;

							try 
							{
								exTag = dbHelper.getTag(tag.getInt("id"));
							}
							catch (CursorIndexOutOfBoundsException e)
							{
								Log.d("24Istanbul-DB", e.getMessage());
							}
							
							int tagId;
							try
							{
								tagId = tag.getInt("tag");
							}
							catch (Exception e)
							{
								tagId = -1;
							}
							/*
							if (exTag == null)
								dbHelper.createTag(new Tag(question.getInt("id"), tagId, option.getString("text")));
							else
								Log.d("24Istanbul-DB", "Option already exists.");
							*/
						}
					}
					
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

}

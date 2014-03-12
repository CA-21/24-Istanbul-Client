package killerapp.istanbul24;

import java.util.ArrayList;

public class Question
{
	private String question;
	private ArrayList<Option> options;
	
	public Question(int id)
	{
		options = new ArrayList<Option>();
		
		
		// TODO: Question and options are retrieved from database.
		question = "Example question";
		int tagID1 = 42;
		int tagID2 = 43;
		int tagID3 = 44;
		options.add(new Option("Example option 1", tagID1));
		options.add(new Option("Example option 1", tagID2));
		options.add(new Option("Example option 1", tagID3));
		
	}
	
	@Override
	public String toString()
	{
		return question;
	}
	
	
	public String getOption(int index)
	{
		return options.get(index).toString();
	}
	
	
	public int getTagID(int index)
	{
		return options.get(index).getTagID();
	}
}

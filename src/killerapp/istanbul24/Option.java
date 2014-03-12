package killerapp.istanbul24;

public class Option
{
	private String text;
	private int tagID;
	
	public Option(String text, int tagID)
	{
		this.text = text;
		this.tagID = tagID;
	}
	
	
	@Override
	public String toString()
	{
		return text;
	}
	
	public int getTagID()
	{
		return tagID;
	}
}

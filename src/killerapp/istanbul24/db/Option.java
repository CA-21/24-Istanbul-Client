package killerapp.istanbul24.db;

/**
 * Model class for the option entity.
 * 
 */
public final class Option
{
	private int id;
	private int questionId;
	private int tagId;
	private String name;

	public Option(int id, int questionId, int tagId, String name)
	{
		this.id = id;
		this.questionId = questionId;
		this.tagId = tagId;
		this.name = name;
	}
	
	public Option(int questionId, int tagId, String name)
	{
		this(-1, questionId, tagId, name);
	}
	public Option(int questionId, String name)
	{
		this(-1, questionId, -1, name);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getQuestionId()
	{
		return questionId;
	}

	public void setQuestionId(int questionId)
	{
		this.questionId = questionId;
	}

	public int getTagId()
	{
		return tagId;
	}

	public void setTagId(int tagId)
	{
		this.tagId = tagId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}

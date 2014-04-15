package killerapp.istanbul24.db;

public final class Question
{
	private int id;
	private int categoryId;
	private String question;
	private String lastUpdateDate;
	
	public Question(int id, int categoryId, String question, String lastUpdateDate)
	{
		this.id = id;
		this.categoryId = categoryId;
		this.question = question;
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public Question(int categoryId, String question, String lastUpdatedate)
	{
		this(-1, categoryId, question, lastUpdatedate);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(int categoryId)
	{
		this.categoryId = categoryId;
	}

	public String getQuestion()
	{
		return question;
	}

	public void setQuestion(String question)
	{
		this.question = question;
	}

	public String getLastUpdateDate()
	{
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate)
	{
		this.lastUpdateDate = lastUpdateDate;
	}
}

package killerapp.istanbul24.db;

public final class Question
{
	private int id;
	private String question;
	private String lastUpdateDate;
	
	public Question(int id, String question, String lastUpdateDate)
	{
		this.id = id;
		this.question = question;
		this.lastUpdateDate = lastUpdateDate;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
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

package killerapp.istanbul24.db;

public final class Category
{
	private int id;
	private String name;
	private String lastUpdateDate;

	public Category(int id, String name, String lastUpdateDate)
	{
		this.id = id;
		this.name = name;
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

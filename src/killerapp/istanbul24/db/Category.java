package killerapp.istanbul24.db;

/**
 * Model class for the category entity.
 * 
 */
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
	
	public Category(String name, String lastUpdateDate)
	{
		this(-1, name, lastUpdateDate);
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

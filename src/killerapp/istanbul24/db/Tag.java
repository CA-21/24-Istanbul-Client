package killerapp.istanbul24.db;

public final class Tag
{
	private int id;
	private int categoryId;
	private String name;

	public Tag(int id, int categoryId, String name)
	{
		this.id = id;
		this.categoryId = categoryId;
		this.name = name;
	}
	
	public Tag(int categoryId, String name)
	{
		this(-1, categoryId, name);
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}

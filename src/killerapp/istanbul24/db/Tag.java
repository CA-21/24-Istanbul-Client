package killerapp.istanbul24.db;

/**
 * Model class for the tag entity.
 * 
 */
public final class Tag
{
	private int id;
	private int categoryId;

	public Tag(int id, int categoryId)
	{
		this.id = id;
		this.categoryId = categoryId;
	}
	
	public Tag(int categoryId)
	{
		this(-1, categoryId);
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
}

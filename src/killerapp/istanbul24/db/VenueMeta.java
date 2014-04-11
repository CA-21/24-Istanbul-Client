package killerapp.istanbul24.db;

public final class VenueMeta
{
	private int id;
	private int tagId;
	private String venueId;

	public VenueMeta(int id, int tagId, String venueId)
	{
		this.id = id;
		this.tagId = tagId;
		this.venueId = venueId;
	}

	public VenueMeta(int tagId, String venueId)
	{
		this(-1, tagId, venueId);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getTagId()
	{
		return tagId;
	}

	public void setTagId(int tagId)
	{
		this.tagId = tagId;
	}

	public String getVenueId()
	{
		return venueId;
	}

	public void setVenueId(String venueId)
	{
		this.venueId = venueId;
	}
}
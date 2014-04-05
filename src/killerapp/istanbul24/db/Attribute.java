package killerapp.istanbul24.db;

public final class Attribute
{
	private String type;
	private String name;

	public Attribute(String name, String type, String... mods)
	{
		String s = "";

		for (String modifier : mods)
			s += " " + modifier;

		this.name = name;
		this.type = type + s;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name + " " + type;
	}
}
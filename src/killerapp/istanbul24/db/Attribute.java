package killerapp.istanbul24.db;

public final class Attribute
{
	private final String type;
	private final String name;

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

	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return name + " " + type;
	}
}

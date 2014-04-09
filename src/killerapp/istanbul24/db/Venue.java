package killerapp.istanbul24.db;

import android.os.Parcel;
import android.os.Parcelable;

public final class Venue implements Parcelable
{
	private String id;
	private String address;
	private String name;
	private double longitude;
	private double latitude;
	private String lastUpdateDate;
	
	public Venue(String id, String address, String name, double longitude,
			double latitude, String lastUpdateDate)
	{
		this.id = id;
		this.address = address;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public String getLastUpdateDate()
	{
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate)
	{
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// TODO Auto-generated method stub
		
	}
}

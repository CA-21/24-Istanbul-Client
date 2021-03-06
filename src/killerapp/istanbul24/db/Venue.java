package killerapp.istanbul24.db;

import java.io.Serializable;

import killerapp.istanbul24.CurrentLocation;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Model class for the venue entity.
 * 
 */
public final class Venue implements Parcelable, Serializable
{
	private String id;
	private String address;
	private String name;
	private double longitude;
	private double latitude;
	private String lastUpdateDate;
	private double fakeDistance = -1;
	private double calculatedDistance;
	
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

	public Venue(Parcel in)
	{
		String[] data = new String[6];

		in.readStringArray(data);
		this.id = data[0];
		this.address = data[1];
		this.name = data[2];
		this.longitude = Double.parseDouble(data[3]);
		this.latitude = Double.parseDouble(data[4]);
		this.lastUpdateDate = data[5];
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
		dest.writeStringArray(new String[] { this.id, this.address, this.name,
				Double.toString(this.longitude),
				Double.toString(this.latitude), this.lastUpdateDate });
	}

	public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>()
	{
		public Venue createFromParcel(Parcel in)
		{
			return new Venue(in);
		}

		public Venue[] newArray(int size)
		{
			return new Venue[size];
		}
	};
	
	public double getFakeDistance()
	{
		if(fakeDistance == -1)
		{
			fakeDistance = Math.sqrt(
				(longitude - CurrentLocation.longitude) * (longitude - CurrentLocation.longitude)
				+ (latitude - CurrentLocation.latitude) * (latitude - CurrentLocation.latitude));
		}
		calculatedDistance = fakeDistance;
		return fakeDistance;
	}
	
	/**
	 * Compares venues based on their id strings. Checks if these are duplicates
	 * with different coordinates.
	 */
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Venue))
			return false;

		Venue other = (Venue) o;

		if (other.id.equals(this.id))
		{
			if (Double.compare(other.latitude, this.latitude) == 0)
				return true;
			else
			{
				Log.d("24Istanbul-Error",
						"Duplicate venue with different coordinates.");
				return false;
			}
		}
		
		return false;
	}

	public int getCalculatedDistance() {
		return (int)calculatedDistance;
	}

	public void setCalculatedDistance(double calculatedDistance) {
		this.calculatedDistance = calculatedDistance;
	}
	
	
}

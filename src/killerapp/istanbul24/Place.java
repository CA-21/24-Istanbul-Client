package killerapp.istanbul24;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable
{
	private int id;
	private String name;
	private int matchedTag;
	
	public Place(int id, String name)
	{
		this.id = id;
		this.name = name;
		matchedTag = 1;
	}
	
	public void match()
	{
		matchedTag++;
	}
	

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1)
	{
		
	}

}

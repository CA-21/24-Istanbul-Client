package killerapp.istanbul24.adapters;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import killerapp.istanbul24.R;
import killerapp.istanbul24.db.Venue;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultListAdapter extends ArrayAdapter<Venue> {
	
	private Context context;
	private int layoutResourceId;
	private ArrayList<Venue> objects;
	
	public ResultListAdapter(Context context, ArrayList<Venue> objects) {
		super(context, R.layout.list_item_results, objects);
		this.context = context;
		this.objects = objects;
		layoutResourceId = R.layout.list_item_results;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
		
		TextView nameView = (TextView) convertView.findViewById(R.id.name);
		nameView.setText(objects.get(position).getName());
		
		TextView distanceView = (TextView) convertView.findViewById(R.id.distance);
		distanceView.setText(objects.get(position).getDistance()+"m");
		
        return convertView;
	}
	
	

}

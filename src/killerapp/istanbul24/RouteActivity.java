package killerapp.istanbul24;

/* * * * * * 
 * RouteActivity: The activity that handles the routing. 
 * v0.2
 * -----
 * TODO Prevent showing out of bounds.
 * * * * * *
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import killerapp.istanbul24.db.Venue;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.GraphHopperAPI;
import com.graphhopper.android.GHAsyncTask;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.Constants;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;

/**
 * Handles routing and map rendering. Start and end points are passed as extra
 * data inside the intent.
 * 
 */
public class RouteActivity extends MapActivity
{

	enum ZoomLevelDistance
	{
		L12(4000), L13(2000), L14(1000), L15(500), L16(250), L17(125), L18(65)

		;

		private final int distance;

		private ZoomLevelDistance(int a)
		{
			distance = a;
		}

		public int getDistance()
		{
			return distance;
		}
	}

	private MapView mapView;
	private BoundingBox bbox;
	private GraphHopperAPI hopper;
	private GeoPoint start, end, midPoint;
	private static final String CURRENT_AREA = "istanbul";
	private Venue venue;

	private ListOverlay pathOverlay = new ListOverlay();

	// private volatile boolean shortestPathRunning = false;
	private volatile boolean prepareInProgress = false;
	private static final boolean DEBUG = false;

	private File mapFolder;
	private String mapFile;

	private SimpleOnGestureListener listener = new SimpleOnGestureListener()
	{
		@Override
		public boolean onSingleTapConfirmed(MotionEvent motionEvent)
		{
			log("Scroll coords: " + mapView.getScrollX() + ", "
					+ mapView.getScrollY());
			mapView.getMapMover().checkAccess();
			BoundingBox bb = mapView.getMapViewPosition().getBoundingBox();
			MapPosition mp = mapView.getMapViewPosition().getMapPosition();
			if (mapView.getDebugSettings().highlightWaterTiles == true)
				;
			// DebugSettings ds = new DebugSettings()
			// mapView.setDebugSettings(debugSettings);
			// motionEvent.getl
			return true;
		}
	};

	private GestureDetector gestureDetector;

	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		// bbox = new BoundingBox(40.9480, 28.9126, 41.0928, 29.1666);
		mapView = new MapView(this)
		{
			@Override
			public boolean onTouchEvent(MotionEvent event)
			{
				// BoundingBox bb = this.getMapViewPosition().getBoundingBox();
				// boolean moveCenter = false;
				// double x = 0, y = 0;

				// log(bb.minLatitude + "," + bb.minLongitude + ","
				// + bb.maxLatitude + "," + bb.maxLongitude);
				// log(bbox.minLatitude + "," + bbox.minLongitude + ","
				// + bbox.maxLatitude + "," + bbox.maxLongitude);
				// log("");

				if (gestureDetector.onTouchEvent(event))
				{
					return true;
				}

				// if (bbox.minLatitude > bb.minLatitude)
				// {
				// moveCenter = true;
				// y += bb.getLatitudeSpan() / 2;
				// }
				// else if (bbox.maxLatitude < bb.maxLatitude)
				// {
				// moveCenter = true;
				// y -= bb.getLatitudeSpan() / 2;
				// //log("aaaa, " + bb.maxLatitude);
				// }
				//
				// if (bbox.minLongitude > bb.minLongitude)
				// {
				// moveCenter = true;
				// x += bb.getLongitudeSpan() / 2;
				// }
				// else if (bbox.maxLongitude < bb.maxLongitude)
				// {
				// moveCenter = true;
				// x -= bb.getLongitudeSpan() / 2;
				// }
				//
				// log(x + ", " + y);
				// if (moveCenter)
				// {
				// if (y < 0)
				// y += bbox.maxLatitude;
				// else if (y > 0)
				// y += bbox.minLatitude;
				//
				// if (x < 0)
				// x += bbox.maxLongitude;
				// else if (x > 0)
				// x += bbox.minLongitude;
				//
				//
				// GeoPoint newCenter = new GeoPoint(y, x);
				//
				// this.getMapViewPosition().setCenter(newCenter);
				//
				// return false;
				// }

				return super.onTouchEvent(event);

				// return super.onTouchEvent(event);

				// return true;
			}

			// 28.9126,40.9480,29.1666,41.0928

		};

		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);

		mapView.getMapScaleBar().setImperialUnits(false);
		mapView.getMapScaleBar().setShowMapScaleBar(true);

		// mapView.getMapViewPosition().gz

		log("Zoom levels: " + mapView.getMapZoomControls().getZoomLevelMin()
				+ ", " + mapView.getMapZoomControls().getZoomLevelMax() + ", "
				+ mapView.getMapZoomControls().getZoomControlsGravity() + ", "
				+ mapView.getMapViewPosition().getZoomLevel());

		if (mapView.getMapZoomControls().isShowMapZoomControls())
		{
			mapView.getMapZoomControls().setZoomLevelMin((byte) 12);
			mapView.getMapZoomControls().setZoomLevelMax((byte) 18);

			mapView.getMapViewPosition().setZoomLevel((byte) 13);
		}

		log("Zoom levels: " + mapView.getMapZoomControls().getZoomLevelMin()
				+ ", " + mapView.getMapZoomControls().getZoomLevelMax() + ", "
				+ mapView.getMapZoomControls().getZoomControlsGravity() + ", "
				+ mapView.getMapViewPosition().getZoomLevel());

		gestureDetector = new GestureDetector(this, listener);

		// Load the map
		mapFolder = new File(Environment.getExternalStorageDirectory(),
				"/24Istanbul/istanbul-gh");

		// boolean greaterOrEqKitkat = Build.VERSION.SDK_INT >= 19;
		// if (greaterOrEqKitkat) {
		// if (Environment.getExternalStorageState() !=
		// Environment.MEDIA_MOUNTED)
		// throw new IllegalStateException(
		// "media is not mounted or not readable");
		// mapFolder = new File(
		// Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
		// "/24Istanbul/istanbul-gh");
		// } else
		// mapFolder = new File(Environment.getExternalStorageDirectory(),
		// "/24Istanbul/istanbul-gh");

		if (!mapFolder.exists())
			mapFolder.mkdirs();

		initFiles();

		// Get start and end points

		Intent intent = getIntent();
		start = (GeoPoint) intent.getSerializableExtra("start");
		end = (GeoPoint) intent.getSerializableExtra("end");
		venue = (Venue) intent.getSerializableExtra("venue");

		// Draw the route

		// shortestPathRunning = true;
		Marker markerStart = createMarker(start, R.drawable.marker_icon_green);
		Marker markerEnd = createMarker(end, R.drawable.marker_icon_red);
		if (markerStart != null && markerEnd != null)
		{
			pathOverlay.getOverlayItems().add(markerStart);
			pathOverlay.getOverlayItems().add(markerEnd);

			mapView.redraw();

			calcPath(start.latitude, start.longitude, end.latitude,
					end.longitude);
		}
		


	}

	private Marker createMarker(GeoPoint p, int resource)
	{
		Drawable drawable = getResources().getDrawable(resource);
		return new Marker(p, Marker.boundCenterBottom(drawable));
	}

	public void calcPath(final double fromLat, final double fromLon,
			final double toLat, final double toLon)
	{

		log("calculating path ...");
		new AsyncTask<Void, Void, GHResponse>()
		{
			float time;

			protected GHResponse doInBackground(Void... v)
			{
				// Wait for the preparation of the map and/or graph
				while (prepareInProgress);

				StopWatch sw = new StopWatch().start();
				GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon)
						.setAlgorithm("dijkstrabi")
						.putHint("instructions", false)
						.putHint("douglas.minprecision", 1)
						.setVehicle("foot");

				GraphHopper _gh = ((GraphHopper) hopper);

				/*
				 * String _loc = _gh.getGraphHopperLocation(), _w = _gh
				 * .getCHWeighting(), _osm = _gh.getOSMFile();
				 */
				Graph _graph = _gh.getGraph();

				GHResponse resp = hopper.route(req);
				time = sw.stop().getSeconds();
				return resp;
			}

			protected void onPostExecute(GHResponse resp)
			{
				if (!resp.hasErrors())
				{
					double distance = resp.getDistance();
					


					log("from:" + fromLat + "," + fromLon + " to:" + toLat
							+ "," + toLon + " found path with distance:"
							+ distance / 1000f + ", nodes:"
							+ resp.getPoints().getSize() + ", time:" + time
							+ " " + resp.getDebugInfo());
					String toastText = "The route is " + (int) (distance / 100)
							/ 10f + " km long. Time: " + resp.getMillis()
							/ 60000f + " min";

					// Append the elapsed time if debugging.
					toastText += (DEBUG) ? ", debug:" + time : "";

					logUser(toastText);
					
					

					// center the start point
					// mapView.getMapViewPosition().setCenter(start);

					// center the middle point
					midPoint = new GeoPoint(
							(start.latitude + end.latitude) / 2,
							(start.longitude + end.longitude) / 2);
					mapView.getMapViewPosition().setCenter(midPoint);

					byte zoomLevel = 12;

					if (distance < ZoomLevelDistance.L17.getDistance())
						zoomLevel = 18;
					else if (distance < ZoomLevelDistance.L16.getDistance())
						zoomLevel = 17;
					else if (distance < ZoomLevelDistance.L15.getDistance())
						zoomLevel = 16;
					else if (distance < ZoomLevelDistance.L14.getDistance())
						zoomLevel = 15;
					else if (distance < ZoomLevelDistance.L13.getDistance())
						zoomLevel = 14;
					else if (distance < ZoomLevelDistance.L12.getDistance())
						zoomLevel = 13;

					mapView.getMapViewPosition().setZoomLevel(
							(byte) (zoomLevel));

					pathOverlay.getOverlayItems().add(createPolyline(resp));
					mapView.redraw();
					
					TextView textView = (TextView) findViewById(R.id.infoText);
					textView.setText(venue.getName()+"\n(Distance: "+String.format("%.02f", distance)+"m)");
					
					if(!venue.getAddress().equals("null"))
						textView.append("\n"+venue.getAddress());
				}
				else
				{
					logUser("Error:" + resp.getErrors());
				}
				// shortestPathRunning = false;
			}
		}.execute();
	}

	private boolean initFiles()
	{
		// only return true if already loaded
		if (hopper != null)
			return true;

		if (prepareInProgress)
		{
			logUser("Preparation still in progress");
			return false;
		}
		prepareInProgress = true;
		loadMap();
		return false;
	}

	private Polyline createPolyline(GHResponse response)
	{
		int points = response.getPoints().getSize();
		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>(points);
		PointList tmp = response.getPoints();
		for (int i = 0; i < response.getPoints().getSize(); i++)
			geoPoints
					.add(new GeoPoint(tmp.getLatitude(i), tmp.getLongitude(i)));

		PolygonalChain polygonalChain = new PolygonalChain(geoPoints);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.CYAN);
		paint.setAlpha(192);
		paint.setStrokeWidth(8);
		paint.setPathEffect(new DashPathEffect(new float[] { 25, 15 }, 0));

		return new Polyline(polygonalChain, paint);
	}

	private void logUser(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	void loadMap()
	{
		logUser("loading map");
		mapFile = mapFolder + "/" + CURRENT_AREA + ".map";
		FileOpenResult fileOpenResult = mapView.setMapFile(new File(mapFile));
		if (!fileOpenResult.isSuccess())
		{
			logUser(fileOpenResult.getErrorMessage());
			finishPrepare();
			return;
		}
		setContentView(mapView);
		addContentView(getLayoutInflater().inflate(R.layout.activity_route, null),
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.FILL_PARENT));

		mapView.getOverlays().clear();
		mapView.getOverlays().add(pathOverlay);
		loadGraphStorage();
	}

	void loadGraphStorage()
	{
		logUser("loading graph (" + Constants.VERSION + ") ... ");
		new GHAsyncTask<Void, Void, Path>()
		{
			@Override
			protected Path saveDoInBackground(Void... v) throws Exception
			{
				// CmdArgs args = new CmdArgs();

				// args.put("osmreader.acceptWay", "FOOT");

				GraphHopper tmpHopp = new GraphHopper().forMobile();// .init(args);
				tmpHopp.setCHShortcuts("fastest");
				// tmpHopp.disableCHShortcuts();
				tmpHopp.load(mapFolder.toString());
				log("found graph " + tmpHopp.getGraph().toString() + ", nodes:"
						+ tmpHopp.getGraph().getNodes());
				hopper = tmpHopp;
				return null;
			}

			@Override
			protected void onPostExecute(Path o)
			{
				if (hasError())
				{
					log("An error happend while creating graph:"
							+ getErrorMessage());
				}
				else
				{
					log("Finished loading graph.");
				}

				finishPrepare();
			}
		}.execute();
	}

	private void log(String str)
	{
		Log.i("GH", str);
	}

	private void finishPrepare()
	{
		prepareInProgress = false;
	}

	public void infoButton(View view)
	{
		TextView textView = (TextView) findViewById(R.id.infoText);
		Button button = (Button) findViewById(R.id.infoButton);

		if (textView.getVisibility() == textView.VISIBLE)
		{
			textView.setVisibility(textView.INVISIBLE);
		}
		else
		{
			textView.setVisibility(textView.VISIBLE);
		}

	}
	
	public void shareButton(View view)
	{
		TextView textView = (TextView) findViewById(R.id.infoText);
		
		String googleMapsUrl = "http://maps.google.com/maps?z="
				+ mapView.getMapViewPosition().getZoomLevel() + "&t=m&q=loc:"
				+ end.latitude + "+" + end.longitude;

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "I found this awesome place with 24Istanbul: " + venue.getName() +"\n" + googleMapsUrl);
		
		
		//intent.putExtra(Intent.EXTRA_TEXT, googleMapsUrl);

		// startActivity(Intent.createChooser(fbIntent(), "Share with"));
		startActivity(intent);
	}

}

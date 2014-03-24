package killerapp.istanbul24;

/* * * * * * 
 * RouteActivity: The activity that handles the routing.
 * v0.1
 * -----
 * TODO Ask the user to download the map if it doesn't exist.
 * TODO Decide if shortestPathRunning is necessary; will there be a re-routing feature?
 * TODO Decide if the map files will be loaded the during the "questions" phase. 
 * TODO Test on other Android versions.
 * * * * * *
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.GeoPoint;
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
import android.view.Window;
import android.widget.Toast;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopperAPI;
import com.graphhopper.android.GHAsyncTask;
import com.graphhopper.GraphHopper;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.Constants;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;

public class RouteActivity extends MapActivity
{

	private MapView					mapView;
	private volatile GraphHopperAPI	hopper;
	private volatile GeoPoint		start;
	private volatile GeoPoint		end;
	private static final String		CURRENT_AREA		= "istanbul";

	private ListOverlay				pathOverlay			= new ListOverlay();

	// private volatile boolean shortestPathRunning = false;
	private volatile boolean		prepareInProgress	= false;
	private static final boolean	DEBUG				= false;

	private File					mapFolder;
	private String					mapFile;

	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		mapView = new MapView(this);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);

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

		// Draw the route

		// shortestPathRunning = true;
		Marker markerStart = createMarker(start, R.drawable.flag_start);
		Marker markerEnd = createMarker(end, R.drawable.flag_end);
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
			float	time;

			protected GHResponse doInBackground(Void... v)
			{
				// Wait for the preparation of the map and/or graph
				while (prepareInProgress)
					;

				StopWatch sw = new StopWatch().start();
				GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon)
						.setAlgorithm("dijkstrabi")
						.putHint("instructions", false)
						.putHint("douglas.minprecision", 1);

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
					log("from:" + fromLat + "," + fromLon + " to:" + toLat
							+ "," + toLon + " found path with distance:"
							+ resp.getDistance() / 1000f + ", nodes:"
							+ resp.getPoints().getSize() + ", time:" + time
							+ " " + resp.getDebugInfo());
					String toastText = "The route is "
							+ (int) (resp.getDistance() / 100) / 10f
							+ " km long. Time: " + resp.getMillis() / 60000f
							+ " min";

					// Append the elapsed time if debugging.
					toastText += (DEBUG) ? ", debug:" + time : "";

					logUser(toastText);

					pathOverlay.getOverlayItems().add(createPolyline(resp));
					mapView.redraw();
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
		paint.setColor(Color.BLUE);
		paint.setAlpha(128);
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
		// TODO sometimes the center is wrong
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
				GraphHopper tmpHopp = new GraphHopper().forMobile();
				tmpHopp.setCHShortcuts("fastest");
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

}

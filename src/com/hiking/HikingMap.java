package com.hiking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class HikingMap extends MapActivity {
	/** Called when the activity is first created. */

	List<Overlay> mapOverlays;
	Drawable drawable;
	mapoverlay itemizedOverlay;
	ImgOverlay img_layer;
	// PathOverlay pathOverlay;
	Location loc;
	LocationManager locman;
	MapView mapView;
	// TextView tv, tv2, tv3, tv4;
	int pontok;
	// GeoPoint pont1, pont2;
	long time1, time2;
	static long startTime;
	static long pauseTime;
	// static GeoPoint[] points = null;
	static List<GeoPoint> points = new ArrayList<GeoPoint>();
	static List<Double> elevation;
	static boolean rec = false;
	boolean pause;
	static boolean is_stop = false;
	private Projection projection;
	boolean isrec = false;
	static Time start;
	static long pause_time;
	static double curEl;
	static float velocity;
	Intent i;

	String GEO_LAT = "latitude";
	String GEO_LONG = "longitude";
	GeoPoint first, current;
	OverlayItem current_overlay;
	OverlayItem start_overlay;
	long currentTrackId;
	DatabaseHandler db;
	List<TableTrackList> loadlist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		/*
		 * GPSTracker gps = new GPSTracker(this); if (gps.canGetLocation()) {
		 * 
		 * double latitude = gps.getLatitude(); double longitude =
		 * gps.getLongitude();
		 * 
		 * // \n is for new line Toast.makeText( getApplicationContext(),
		 * "Your Location is - \nLat: " + latitude + "\nLong: " + longitude,
		 * Toast.LENGTH_LONG).show(); } else { // can't get location // GPS or
		 * Network is not enabled // Ask user to enable GPS/network in settings
		 * gps.showSettingsAlert(); }
		 */
		elevation = new ArrayList<Double>();
		pause = false;
		Time start = new Time();
		start.setToNow();
		velocity = 0;

		// locman = (LocationManager) getSystemService(LOCATION_SERVICE);
		// pontok = 0;

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setBackgroundColor(Color.BLACK);

		// for the current location
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.mymarker2);
		itemizedOverlay = new mapoverlay(drawable, this);

		// for the image location
		// drawable = this.getResources().getDrawable(R.drawable.mymarker);
		// String p = "/sdcard/HikingApp/1359475673943.jpg/";
		// img_layer = new ImgOverlay(drawable, this, p);

		// for the testing with fix points
		/*
		 * GeoPoint point = new GeoPoint(48308403, 14295080); GeoPoint point2 =
		 * new GeoPoint(48307461, 14293728); GeoPoint point3 = new
		 * GeoPoint(48306734, 14294050);
		 * 
		 * points.add(point); points.add(point2); points.add(point3);
		 */
		// for the texting to show how the markers are working
		/*
		 * OverlayItem overlayitem = new OverlayItem(points.get(2), "" +
		 * points.get(0).getLatitudeE6(), "" + points.get(0).getLongitudeE6());
		 * itemizedOverlay.addOverlay(overlayitem);
		 * mapOverlays.add(itemizedOverlay);
		 * 
		 * OverlayItem overlayitem2 = new OverlayItem(points.get(0), "" +
		 * points.get(0).getLatitudeE6(), "" + points.get(0).getLongitudeE6());
		 * img_layer.addOverlay(overlayitem2); mapOverlays.add(img_layer);
		 * 
		 * // for the testing - draw the path projection =
		 * mapView.getProjection(); mapOverlays.add(new PathOverlay());
		 */
		IntentFilter filter = new IntentFilter();
		filter.addAction(GEO_LONG);
		filter.addAction(GEO_LAT);
		registerReceiver(locationRec, filter);

		Calendar c = Calendar.getInstance();
		String currentDate = "" + c.get(Calendar.YEAR) + "-"
				+ (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH);

		i = new Intent(this, LocService.class);
		startService(i);
		mapView.setSatellite(true);
		db = new DatabaseHandler(this);
		TableTrackList currenTrack = new TableTrackList(currentDate,
				System.currentTimeMillis());
		currentTrackId = db.addTrack(currenTrack);
		List<TableTrackList> list = new ArrayList<TableTrackList>();
		list = db.getAllTracks();

		db.close();

		for (TableTrackList l : list) {
			Log.d("foscsi",
					"" + l.getDate() + " " + l.getStartTime() + " " + l.getId());
		}

		// currentTrackId = db.addTrack(currenTrack);
		List<TablePosition> list1 = new ArrayList<TablePosition>();
		list1 = db.getPositionsByTrackId(1);

		for (TablePosition l : list1) {
			Log.d("foscsi",
					"" + l.getId() + " " + l.getLatitude() + " "
							+ l.getLongitude());
		}

		db.close();
	}

	private BroadcastReceiver locationRec = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			double geoLng = intent.getExtras().getDouble(GEO_LONG);
			double geoLat = intent.getExtras().getDouble(GEO_LAT);
			Toast.makeText(HikingMap.this,
					"Got broadcast " + geoLng + " " + geoLat, Toast.LENGTH_LONG)
					.show();
			drawOnMap(geoLng, geoLat);
			if (points.size() == 1) {
				first = new GeoPoint((int) (geoLat), (int) (geoLng));
				start_overlay = new OverlayItem(first, GEO_LAT, GEO_LAT);
				itemizedOverlay.addOverlay(start_overlay);
				mapOverlays.add(itemizedOverlay);

			}
			mapView.invalidate();
		}
	};

	public void drawOnMap(double geoLong, double geoLat) {
		// for the current location
		mapOverlays = mapView.getOverlays();
		mapOverlays.clear();
		drawable = this.getResources().getDrawable(R.drawable.mymarker2);
		itemizedOverlay = new mapoverlay(drawable, this);

		// for the image location
		// drawable = this.getResources().getDrawable(R.drawable.mymarker);
		// String p = "/sdcard/HikingApp/1359475673943.jpg/";
		// img_layer = new ImgOverlay(drawable, this, p);

		// for the testing with fix points

		GeoPoint point = new GeoPoint((int) (geoLat), (int) (geoLong));

		Log.d("loca", "" + point.getLatitudeE6() + " " + point.getLongitudeE6());
		/*
		 * GeoPoint point2 = new GeoPoint(48307461, 14293728); GeoPoint point3 =
		 * new GeoPoint(48306734, 14294050);
		 */
		points.add(point);

		DatabaseHandler db = new DatabaseHandler(this);
		TablePosition currentPos = new TablePosition((int) currentTrackId,
				(int) geoLong, (int) geoLat, 0d, 0d);
		db.addPosition(currentPos);

		/*
		 * start_overlay = new OverlayItem(first, GEO_LAT, GEO_LAT);
		 * itemizedOverlay.addOverlay(start_overlay);
		 * mapOverlays.add(itemizedOverlay);
		 */
		if (first != null) {
			start_overlay = new OverlayItem(first, GEO_LAT, GEO_LAT);
			itemizedOverlay.addOverlay(start_overlay);
			mapOverlays.add(itemizedOverlay);
		}
		current_overlay = new OverlayItem(point, GEO_LAT, GEO_LAT);
		itemizedOverlay.addOverlay(current_overlay);

		mapOverlays.add(itemizedOverlay);

		// for the texting to show how the markers are working

		// OverlayItem overlayitem = new OverlayItem(point, GEO_LAT, GEO_LAT);
		// itemizedOverlay.addOverlay(overlayitem);
		// mapOverlays.add(itemizedOverlay);

		/*
		 * OverlayItem overlayitem2 = new OverlayItem(points.get(0), "" +
		 * points.get(0).getLatitudeE6(), "" + points.get(0).getLongitudeE6());
		 * img_layer.addOverlay(overlayitem2); mapOverlays.add(img_layer);
		 */
		// for the testing - draw the path projection =
		projection = mapView.getProjection();
		mapOverlays.add(new PathOverlay());

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopService(i);
	}

	public class PathOverlay extends Overlay {

		public PathOverlay() {

		}

		public void draw(Canvas canvas, MapView mapv, boolean shadow) {
			super.draw(canvas, mapv, shadow);

			Paint mPaint = new Paint();
			mPaint.setDither(true);
			mPaint.setColor(Color.RED);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setAlpha(125);
			mPaint.setStrokeWidth(2);

			if (points.size() > 1) {
				for (int i = 0; i < points.size() - 2; ++i) {
					// Log.d("vmi", "" + points.size() + " " + points.get(0));
					Point p1 = new Point();
					Point p2 = new Point();
					Path path = new Path();

					projection.toPixels(points.get(i), p1);
					projection.toPixels(points.get(i + 1), p2);

					path.moveTo(p2.x, p2.y);
					path.lineTo(p1.x, p1.y);

					canvas.drawPath(path, mPaint);

				}
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {
		case R.id.share:
			// Share();
			return true;
		case R.id.save:
			// SaveMap();
			return true;
		case R.id.record: // //+listener timer
			/*
			 * if (isrec == false) { start = new Time(); start.setToNow();
			 * startTime = System.currentTimeMillis(); Log.d("start_map", "" +
			 * startTime); if (HikingInfo.timer != null) {
			 * HikingInfo.timer.start(); } else {
			 * 
			 * rec = true; } isrec = true; }
			 */
			return true;
		case R.id.load: // not ready yet
			openLoadTrack();
			return true;
		case R.id.pause:
			Log.d("shit", "" + HikingInfo.timer.getBase());
			/*
			 * if (!pause && isrec) { pause = true; pause_time =
			 * System.currentTimeMillis(); HikingInfo.timer.stop(); } else if
			 * (pause && isrec) {
			 * HikingInfo.timer.setBase(SystemClock.elapsedRealtime() -
			 * (System.currentTimeMillis() - pause_time));
			 * HikingInfo.timer.start(); pause = false; }
			 */
			return true;
		case R.id.stop:
			/*
			 * if (isrec && !pause) { HikingInfo.timer.stop(); is_stop = true;
			 * isrec = false; }
			 */
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void openLoadTrack() {
		// custom dialog
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.load_dialog);
		dialog.setTitle("Loading tracks");

		Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);
		Button okBtn = (Button) dialog.findViewById(R.id.ok_button);
		final Spinner trackSpin = (Spinner) dialog
				.findViewById(R.id.track_spinner);

		loadlist = new ArrayList<TableTrackList>();
		loadlist = db.getAllTracks();

		db.close();

		List<String> items = new ArrayList<String>();

		for (TableTrackList l : loadlist) {
			Log.d("foscsi",
					"" + l.getDate() + " " + l.getStartTime() + " " + l.getId());
			String item = l.getDate() + " track id: #" + l.getId();
			items.add(item);
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, items);

		trackSpin.setAdapter(adapter);

		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				int selected = trackSpin.getSelectedItemPosition();
				int currentId = loadlist.get(selected).getId();
				dialog.dismiss();

				points.clear();

				List<TablePosition> poslist = db
						.getPositionsByTrackId(currentId);
				db.close();
				Log.d("-------------------------------", "" + currentId);
				for (TablePosition t : poslist) {
					Log.d("positions",
							"" + t.getLatitude() + ", " + t.getLongitude());
					points.add(new GeoPoint((int)t.getLatitude(), (int)t.getLongitude()));
				}

				mapOverlays.clear();
				projection = mapView.getProjection();
				mapOverlays.add(new PathOverlay());
				mapView.invalidate();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();

			}
		});

		dialog.show();
	}

}
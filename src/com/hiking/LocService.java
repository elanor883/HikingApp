package com.hiking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class LocService extends Service implements LocationListener {

	String GEO_LAT = "latitude";
	String GEO_LONG = "longitude";

	private LocationManager locationManager;
	private String provider;
	Context mContext;

	@Override
	public void onCreate() { 
		//this.mContext = context;
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
		//locationManager.requestLocationUpdates(provider, 400, 1, this);
		
		if (location != null) {
		      System.out.println("Provider " + provider + " has been selected.");
		     // onLocationChanged(location);
		    } 

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {

			Double geoLat = location.getLatitude() * 1E6;
			Double geoLong = location.getLongitude() * 1E6;

			Intent intent = new Intent();
			intent.putExtra(GEO_LONG, geoLong);
			intent.putExtra(GEO_LAT, geoLat);
			intent.setAction(GEO_LONG);
			intent.setAction(GEO_LAT);

			sendBroadcast(intent);
			Toast.makeText(LocService.this, "Broadcast sent",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(LocService.this, "Location = null",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Disabled provider " + provider,
		        Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Enabled new provider " + provider,
		        Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}

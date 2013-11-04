package com.hiking;

public class TablePosition {

	int _track_id;
	int _longitude;
	int _latitude;
	double _elevation;
	double _distance;

	public TablePosition() {

	}

	public TablePosition(int track_id, int longitude, int latitude, double elevation, double distance) {
		this._track_id = track_id;
		this._longitude = longitude;
		this._latitude = latitude;
		this._elevation = elevation;
		this._distance = distance;

	}

	public TablePosition(int longitude, int latitude, double elevation, double distance) {
		this._longitude = longitude;
		this._latitude = latitude;
		this._elevation = elevation;
		this._distance = distance;

	}


	public int getId() {
		return this._track_id;
	}

	public void setId(int track_id) {
		this._track_id = track_id;
	}

	public int getLongitude() {
		return this._longitude;
	}

	public void setLongitude(int longitude) {
		this._longitude = longitude;
	}

	public int getLatitude() {
		return this._latitude;
	}

	public void setLatitude(int latitude) {
		this._latitude = latitude;
	}
	
	public double getElevation() {
		return this._elevation;
	}

	public void setElevation(double elevation) {
		this._elevation = elevation;
	}
	
	public double getDistance() {
		return this._distance;
	}

	public void setDistance(double distance) {
		this._distance = distance;
	}
}

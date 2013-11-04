package com.hiking;

public class TableImg {

	int _track_id;
	String _img_path;
	int _longitude;
	int _latitude;

	public TableImg() {

	}

	public TableImg(int track_id, int longitude, int latitude, String img_path) {
		this._track_id = track_id;
		this._img_path = img_path;
		this._longitude = longitude;
		this._latitude = latitude;
	}


	public int getId() {
		return this._track_id;
	}

	public void setId(int track_id) {
		this._track_id = track_id;
	}

	public String getImgPath() {
		return this._img_path;
	}

	public void setImgPath(String img_path) {
		this._img_path = img_path;
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


}

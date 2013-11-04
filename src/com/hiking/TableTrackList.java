package com.hiking;

public class TableTrackList {

	int _track_id;
	String _date;
	long _start_time;
	long _end_time;
	long _duration;
	

	public TableTrackList() {

	}

	public TableTrackList(int track_id, String date, long start_time) {
		this._track_id = track_id;
		this._date = date;
		this._start_time = start_time;
	}

	public TableTrackList(String date, long start_time) {
		this._date = date;
		this._start_time = start_time;
	}

	public int getId() {
		return this._track_id;
	}

	public void setId(int track_id) {
		this._track_id = track_id;
	}

	public String getDate() {
		return this._date;
	}

	public void setDate(String date) {
		this._date = date;
	}
	
	public long getStartTime() {
		return this._start_time;
	}

	public void setStartTime(long start_time) {
		this._start_time = start_time;
	}
	public long getDuration() {
		return this._duration;
	}

	public void setDuration(long duration) {
		this._duration=duration;
	}
	public long getEndTime() {
		return this._end_time;
	}

	public void setEndTime(long end_time) {
		this._end_time = end_time;
	}

}

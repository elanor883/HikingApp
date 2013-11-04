package com.hiking;


import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "TrackManager";

	// Table names
	private static final String TABLE_TRACK_LIST = "track_list";
	private static final String TABLE_POSITION = "position_details";
	private static final String TABLE_IMG = "images";

	// Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_DATE = "date";
	private static final String KEY_START = "start_time";
	private static final String KEY_END = "end_time";
	private static final String KEY_DURATION = "duration";
	private static final String KEY_LONG = "longitude";
	private static final String KEY_LAT = "latitude";
	private static final String KEY_ELEVATION = "elevation";
	private static final String KEY_DISTANCE = "distance";
	private static final String KEY_IMG_PATH = "img_path";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TRACK_LIST = "CREATE TABLE " + TABLE_TRACK_LIST + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT, "
				+ KEY_START + " INTEGER, " + KEY_END + " INTEGER, " + KEY_DURATION + " INTEGER" + ")";

		String CREATE_POSITION = "CREATE TABLE " + TABLE_POSITION + "("
				+ KEY_ID + " INTEGER, " + KEY_LONG + " INTEGER, "
				+ KEY_LAT + " INTEGER, " + KEY_ELEVATION + " REAL, " + KEY_DISTANCE + " REAL" + ")";
		
		String CREATE_IMG = "CREATE TABLE " + TABLE_IMG + "("
				+ KEY_ID + " INTEGER," 
				+ KEY_LONG + " INTEGER, " + KEY_LAT + " INTEGER, " + KEY_IMG_PATH + " TEXT" + ")";
		
		db.execSQL(CREATE_TRACK_LIST);
		db.execSQL(CREATE_POSITION);
		db.execSQL(CREATE_IMG);
		
		Log.d("create tables", CREATE_TRACK_LIST + "\n" + CREATE_POSITION + "\n " + CREATE_IMG);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACK_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMG);
		// Create tables again

		onCreate(db);
	}

	long addTrack(TableTrackList track) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, track.getDate());
		values.put(KEY_START, track.getStartTime());
		values.put(KEY_END, track.getEndTime());
		values.put(KEY_DURATION, track.getDuration());

		long id = db.insert(TABLE_TRACK_LIST, null, values);
		db.close(); // Closing database connection
		
		return id;

	}
	
	void addPosition(TablePosition pos) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, pos.getId());
		values.put(KEY_LONG, pos.getLongitude());
		values.put(KEY_LAT, pos.getLatitude());
		values.put(KEY_ELEVATION, pos.getElevation());
		values.put(KEY_DISTANCE, pos.getDistance());

		db.insert(TABLE_POSITION, null, values);
		db.close(); // Closing database connection

	}

	void addImg(TableImg img) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, img.getId());
		values.put(KEY_LONG, img.getLongitude());
		values.put(KEY_LAT, img.getLatitude());
		values.put(KEY_IMG_PATH, img.getImgPath());

		db.insert(TABLE_IMG, null, values);
		db.close(); // Closing database connection

	}
	
	public List<TableTrackList> getAllTracks() {
		List<TableTrackList> result_list = new ArrayList<TableTrackList>();

		String selectQuery = "SELECT  * FROM " + TABLE_TRACK_LIST
				+ " ORDER BY date DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				TableTrackList track_list = new TableTrackList();
				track_list.setId(Integer.parseInt(cursor.getString(0)));
				track_list.setDate(cursor.getString(1));
				track_list.setStartTime(Long.parseLong(cursor.getString(2)));
				track_list.setEndTime(Long.parseLong(cursor.getString(3)));
				track_list.setDuration(Long.parseLong(cursor.getString(4)));

				result_list.add(track_list);
			} while (cursor.moveToNext());
		}

		cursor.close();

		return result_list;
	}
	
	public TableTrackList getTrackById(int id) {
		
		String selectQuery = "SELECT  * FROM " + TABLE_TRACK_LIST
				+ " WHERE " + KEY_ID + "=" + id;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		TableTrackList track_list = new TableTrackList();
		
		if (cursor.moveToFirst()) {

				track_list.setId(Integer.parseInt(cursor.getString(0)));
				track_list.setDate(cursor.getString(1));
				track_list.setStartTime(Long.parseLong(cursor.getString(2)));
				track_list.setEndTime(Long.parseLong(cursor.getString(3)));
				track_list.setDuration(Long.parseLong(cursor.getString(4)));
		}

		cursor.close();

		return track_list;
	}
	
	public List<TablePosition> getPositionsByTrackId( int id) {
		List<TablePosition> result_list = new ArrayList<TablePosition>();

		String selectQuery = "SELECT  * FROM " + TABLE_POSITION
				+ " WHERE " + KEY_ID + "=" + id;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				TablePosition pos = new TablePosition();
				pos.setId(Integer.parseInt(cursor.getString(0)));
				pos.setLongitude(Integer.parseInt(cursor.getString(1)));
				pos.setLatitude(Integer.parseInt(cursor.getString(2)));
				pos.setElevation(Double.parseDouble(cursor.getString(3)));
				pos.setDistance(Double.parseDouble(cursor.getString(4)));

				result_list.add(pos);
			} while (cursor.moveToNext());
		}

		cursor.close();

		return result_list;
	}
	
	public List<TableImg> getImgsByTrackId( int id) {
		List<TableImg> result_list = new ArrayList<TableImg>();

		String selectQuery = "SELECT  * FROM " + TABLE_IMG
				+ " WHERE " + KEY_ID + "=" + id;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				TableImg img = new TableImg();
				img.setId(Integer.parseInt(cursor.getString(0)));
				img.setLongitude(Integer.parseInt(cursor.getString(1)));
				img.setLatitude(Integer.parseInt(cursor.getString(2)));
				img.setImgPath(cursor.getString(3));


				result_list.add(img);
			} while (cursor.moveToNext());
		}

		cursor.close();

		return result_list;
	}
}

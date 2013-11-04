package com.hiking;


import android.app.Activity;
import android.text.format.Time;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;
import android.os.Bundle;
import android.os.SystemClock;

public class HikingInfo extends Activity {

	static Chronometer timer;
	Time currentTime;
	static TextView sTime;
	static TextView distance;
	static TextView velocity;
	static TextView elevation;
	static TextView cp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_info);
		timer = (Chronometer) findViewById(R.id.chronometer1);
		long current = System.currentTimeMillis();
		Log.d("time-current", "" + current);

		sTime = (TextView) findViewById(R.id.widget72);
		distance = (TextView) findViewById(R.id.widget73);
		velocity = (TextView) findViewById(R.id.widget74);
		elevation = (TextView) findViewById(R.id.widget75);
		cp = (TextView) findViewById(R.id.widget76);

		//start the timer if the user pressed the record
		if (HikingMap.rec == true) {
			currentTime = new Time();
			currentTime.setToNow();
			timer.setBase(SystemClock.elapsedRealtime()
					- (current - HikingMap.startTime));
			timer.start();
			sTime.setText("" + HikingMap.start.hour + ":"
					+ HikingMap.start.minute + ":" + HikingMap.start.second);

		}
		//HikingMap.RefreshInfo();
	}

}

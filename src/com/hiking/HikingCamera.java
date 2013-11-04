package com.hiking;

import android.app.Activity;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.PixelFormat;
import android.hardware.Camera;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HikingCamera extends Activity implements SurfaceHolder.Callback,
		OnClickListener {

	Camera mCamera;
	boolean mPreviewRunning = false;
	static byte[] bitmapdata;
	static boolean is_img;
	Button btn_save;
	Button btn_new_img;
	static byte[] image;
	static public boolean sent_img;
	static boolean there_is_img;
	static String path;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		there_is_img = false;
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.surface_camera);
		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mSurfaceView.setOnClickListener(this);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		btn_save = (Button) findViewById(R.id.button_send_img);
		btn_new_img = (Button) findViewById(R.id.button_new);
		btn_save.setOnClickListener(listener);
		btn_new_img.setOnClickListener(listener);
		btn_save.setEnabled(false);

		sent_img = false;
		is_img = false;

	}

	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {

			if (imageData != null) {

				bitmapdata = imageData;
				image = imageData;

				btn_save.setEnabled(true);

			}

		}
	};

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void surfaceCreated(SurfaceHolder holder) {

		mCamera = Camera.open();

		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCamera.setDisplayOrientation(90);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

		startCamera(holder, w, h);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mPreviewRunning = true;
		mCamera.release();
	}

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;

	public void onClick(View arg0) {

		mCamera.takePicture(null, mPictureCallback, mPictureCallback);
		mCamera.stopPreview();

	}

	View.OnClickListener listener = new View.OnClickListener() {

		// if the user took a photo then save in the HikingApp folder, and
		// refresh the value of the path that will be used at the mapview
		@Override
		public void onClick(View v) {

			// TODO Auto-generated method stub
			if (v.getId() == R.id.button_send_img) {
				is_img = true;
				FileOutputStream outStream;
				try {
					long current = System.currentTimeMillis();
					String name = "/sdcard/HikingApp/" + current + ".jpg";
					outStream = new FileOutputStream(String.format(
							"/sdcard/HikingApp/%d.jpg", current));
					outStream.write(image);
					outStream.close();
					there_is_img = true;
					path = name;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}

			}

			else if (v.getId() == R.id.button_new) {
				mCamera.startPreview();
				btn_save.setEnabled(false);

			}

		}
	};

	@Override
	public void onDestroy() {
		stopCamera();
	}

	private void startCamera(SurfaceHolder sh, int width, int height) {
		Camera.Parameters p = mCamera.getParameters();
		p.setPreviewSize(width, height);

		mCamera.setParameters(p);

		try {
			mCamera.setPreviewDisplay(sh);
		} catch (Exception e) {
		}

		mCamera.startPreview();
	}

	private void stopCamera() {

		mCamera.stopPreview();
		mCamera.release();
	}

}

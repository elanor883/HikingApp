package com.hiking;


import java.io.File;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ImgOverlay extends ItemizedOverlay<OverlayItem> {

	String path;
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;

	public ImgOverlay(Drawable defaultMarker, Context context, String file) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		path=file;
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate(); /*végrehajt minden feldolgozast egy uj itemizedoverlayen*/
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {

		  Dialog dialog = new Dialog(mContext);
          dialog.setContentView(R.layout.alert_layout);
          dialog.setTitle("Picture from here");
          dialog.setCancelable(true);
          //there are a lot of settings, for dialog, check them all out!

          //set up text

          //set up image view
         // ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
          
        File vmi;
        Log.d("dialog", "" + path);
        vmi = new File(path);
          if(vmi.exists()){

              Bitmap myBitmap = BitmapFactory.decodeFile(vmi.getAbsolutePath());

              ImageView image = (ImageView) dialog.findViewById(R.id.ImageView01);
              image.setImageBitmap(myBitmap);

          }
          
          dialog.show();
        //  img.setImageResource(R.drawable.good);
		return true;
	}
}
package com.hiking;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;



public class HikingTabActivity extends TabActivity {
    /** Called when the activity is first created. */
	static public TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        
        
		//set the tabs of the layout
       
        tabHost = getTabHost(); 
        TabHost.TabSpec spec;  
        Intent intent;  

        
        intent = new Intent().setClass(this, HikingMap.class);

        spec = tabHost.newTabSpec("info").setIndicator("Map",null
        	        				)
                      .setContent(intent);
        tabHost.addTab(spec);
        

        intent = new Intent().setClass(this, HikingInfo.class);
        spec = tabHost.newTabSpec("shopping").setIndicator("Info",null
        	        				)
                      .setContent(intent);
        tabHost.addTab(spec);

        
        intent = new Intent().setClass(this, HikingCamera.class);
        spec = tabHost.newTabSpec("cost").setIndicator("Camera", null
                                        		  )
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, HikingCompass.class);
        spec = tabHost.newTabSpec("infok").setIndicator("Compass",null
                           		)
                      .setContent(intent);
        tabHost.addTab(spec);
        

        

        tabHost.setCurrentTab(0);
    }
    
    

    }

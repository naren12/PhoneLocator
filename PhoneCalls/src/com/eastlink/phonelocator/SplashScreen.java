package com.eastlink.phonelocator;


import com.eastlink.phonelocator.R;
import gpslocation.GPSTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends FragmentActivity 
{
    static TextView mDotsText[];
    private int mDotsCount;
    private LinearLayout mDotsLayout;
    private Button cleardata;
    private String myPhoneNumber,substr;
    private Switch sos;
    private int temp=0;
    Timer timer;
	TimerTask timerTask;
	Handler handler;
	GPSTracker gps;
	String latitude;
	String longitude;
	 private GoogleMap map;
	 static final LatLng USA = new LatLng(39.277622,-100.810547);

 
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	    getWindow().addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setContentView(R.layout.activity_splash_screen);
        
        
        Button share=(Button) findViewById(R.id.share);
        Button rate=(Button) findViewById(R.id.rate);
        
        share.setOnClickListener(new View.OnClickListener() {
 		
 		@Override
 		public void onClick(View v) {
 			// TODO Auto-generated method stub
 			Intent shareIntent = new Intent(Intent.ACTION_SEND);
 			  shareIntent.setType("text/plain");
 			  shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.eastlink.phonelocator");
 			  startActivity(Intent.createChooser(shareIntent, "Share..."));
 			
 		}
 	});
        
        rate.setOnClickListener(new View.OnClickListener() {
 		
 		@Override
 		public void onClick(View v)
 		{
 			// TODO Auto-generated method stub
 			final String appPackageName ="com.eastlink.phonelocator"; // getPackageName() from Context or Activity object
 			try {
 			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
 			} catch (android.content.ActivityNotFoundException anfe) {
 			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
 			}
 			
 		}
 	});
        
        
        
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fm.getMap(); // Getting Map for the SupportMapFragment
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(USA, 4));
		map.animateCamera(CameraUpdateFactory.zoomTo(4), 2000, null);
		map.setMyLocationEnabled(true);		
		fm.getView().setVisibility(View.GONE);
        
        TelephonyManager tm = (TelephonyManager)this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE); 
        substr = tm.getLine1Number();
		
		//Log.d("log", substr);
		
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//	    Editor editor = pref.edit();
//	    String number=pref.getString("myPhoneNumber", null); // Storing string
//	    substr=number.substring(1);
	   		
		cleardata=(Button) findViewById(R.id.button1);
		sos=(Switch) findViewById(R.id.switch1);
//		if(myPhoneNumber==null)
//	        {
//	        	cleardata.setVisibility(View.GONE);
//	        }
//	        
//		 else
//		 {
			  
		        cleardata.setVisibility(View.VISIBLE);
		        cleardata.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						myPhoneNumber=3+substr;
						deleteDetails();
					}
				});
		        
		// }
		        
		        sos.setChecked(false);
		        
		        //attach a listener to check for changes in state
		        sos.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		       
		         @Override
		         public void onCheckedChanged(CompoundButton buttonView,
		           boolean isChecked) {
		         
		         
		        	 
		          if(isChecked){
		        	//set a new Timer
		      		timer = new Timer();
		      		
		      		//initialize the TimerTask's job
		      		initializeTimerTask();
		      		
		      		//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
		      		timer.schedule(timerTask, 5000, 30000); //

		          }else{
		           
		          }
		       
		         }

				private void initializeTimerTask() {
					timerTask = new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							myPhoneNumber=1+substr;
							Thread thread=new Thread(doInBackground);
					   		thread.start();
							
						}
						
					};
				}
		        });        
      
		
 
        //here we create the gallery and set our adapter created before
        Gallery gallery = (Gallery)findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));
 
 
        mDotsLayout = (LinearLayout)findViewById(R.id.image_count);
        //here we count the number of images we have to know how many dots we need
        mDotsCount = gallery.getAdapter().getCount();
 
        //here we create the dots
        //as you can see the dots are nothing but "."  of large size
        mDotsText = new TextView[mDotsCount];
 
        //here we set the dots
        for (int i = 0; i < mDotsCount; i++) {
            mDotsText[i] = new TextView(this);
            mDotsText[i].setText(".");
            mDotsText[i].setTextSize(45);
            mDotsText[i].setTypeface(null, Typeface.BOLD);
            mDotsText[i].setTextColor(android.graphics.Color.GRAY);
            mDotsLayout.addView(mDotsText[i]);
            
        }
 
 
        //when we scroll the images we have to set the dot that corresponds to the image to White and the others
        //will be Gray
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int pos, long l) {
 
                for (int i = 0; i < mDotsCount; i++) {
                    SplashScreen.mDotsText[i].setTextColor(Color.GRAY);
                    
                   
                }
 
                SplashScreen.mDotsText[pos].setTextColor(Color.WHITE);
                
            }
 
            @Override
            public void onNothingSelected(AdapterView adapterView) {
            	
            	
 
            }
        });
    }
    Runnable doInBackground=new Runnable()
	 {

		@Override
		public void run() 
		{
			try
			{
				
			}
			catch(Exception exception)
			{
				
			}
			try{
				runOnUiThread(runOnUi);
			}
			catch (Exception exception)
			{
				
			}
		}
		 
	 };
	 Runnable runOnUi=new Runnable()
	 {

		@Override
		public void run() 
		{
			insertDetails();
		}
		 
	 };
	 
	 
	 
	 @SuppressLint("NewApi")
	private void deleteDetails() {
			// TODO Auto-generated method stub
			  
		        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
				
				
				
				if(activeNetworkInfo!=null)
				{
			  
				  	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					 
					StrictMode.setThreadPolicy(policy);
					//Log.d("number", myPhoneNumber);
					
					String strUrl = "http://office.eastlinkhost.com/epenny/sell/css/phonecall_json.php";					
					URL url = null;
					try {
						url = new URL(strUrl);
						
						
		
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.setRequestMethod("POST");
						connection.setDoOutput(true);
						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
								connection.getOutputStream());
		
						outputStreamWriter.write("phonenumbers=" +myPhoneNumber);				
						outputStreamWriter.flush();
						outputStreamWriter.close();
						
						InputStream iStream = connection.getInputStream();
						BufferedReader reader = new BufferedReader(new
						InputStreamReader(iStream));
						
						StringBuffer sb = new StringBuffer();
						
						String line = "";
						
						while( (line = reader.readLine()) != null)
						{
							sb.append(line);
						}
		
						reader.close();
						iStream.close();
									
		
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					finally
					{
						  //Log.d("finally", myPhoneNumber);
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "no internet", Toast.LENGTH_SHORT).show();
				}
			  
			
		}
	 private void insertDetails() {
			// TODO Auto-generated method stub
			  
			  
			  gps = new GPSTracker(SplashScreen.this);

				// check if GPS enabled		
		        if(gps.canGetLocation()){
		        	
		        	 latitude = Double.toString(gps.getLatitude());
		        	 longitude = Double.toString(gps.getLongitude());
		        	
		        	
		        	
		        	
		        }else{
		        
		        	gps.showSettingsAlert();
		        }
		        

		        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
				
				if(activeNetworkInfo!=null)
				{
			  
				  	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					 
					StrictMode.setThreadPolicy(policy);

					String strUrl = "http://office.eastlinkhost.com/epenny/sell/css/phonecall_json.php";					
					URL url = null;
					try {
						url = new URL(strUrl);
						
						//Log.d("details", myPhoneNumber+"=>"+latitude+"=>"+longitude);
		
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.setRequestMethod("POST");
						connection.setDoOutput(true);
						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
								connection.getOutputStream());
		
						outputStreamWriter.write("phonenumbers=" +myPhoneNumber + "&lat="+latitude+"&lng="+longitude);				
						outputStreamWriter.flush();
						outputStreamWriter.close();
						
						InputStream iStream = connection.getInputStream();
						BufferedReader reader = new BufferedReader(new
						InputStreamReader(iStream));
						
						StringBuffer sb = new StringBuffer();
						
						String line = "";
						
						while( (line = reader.readLine()) != null)
						{
							sb.append(line);
						}
		
						reader.close();
						iStream.close();
									
		
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "no internet", Toast.LENGTH_SHORT).show();
				}
			  
			
		}

}

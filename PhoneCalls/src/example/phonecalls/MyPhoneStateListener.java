package example.phonecalls;


import gpslocation.GPSTracker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyPhoneStateListener extends FragmentActivity 
{
	  static final LatLng USA = new LatLng(39.277622,-100.810547);
	  private static final String tag = null;
	  private String phonenumber;
	  public String cname="";
	  public String msgs="";
	  private GoogleMap map;
	  
	  ArrayList<LatLng> markerPoints;
	  int mMode=0;
	  
	  //double latitude,longitude;
	  //GPSTracker gps;
	  
		
	  @Override
	  protected void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);
	    
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	    getWindow().addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    
	    getWindow().addFlags(
	            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
	                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
	    
	    
//	    PackageManager pm = getApplicationContext().getPackageManager();
//	    pm.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	    

	    Intent value=getIntent();
	    String value_in=value.getStringExtra("value");
	    
	    Intent values=getIntent();
	    String value_out=values.getStringExtra("value_out");
	   
	    
	    if(value_in!=null)
	    {
	    	setContentView(R.layout.activity_main_out);
	    }
	    else if(value_out!=null)
	    {
	    	setContentView(R.layout.activity_main);
	    }
	    else
	    {
	    	setContentView(R.layout.activity_main_main);
	    	LinearLayout layout=(LinearLayout) findViewById(R.id.relativeLayout1);
	    	layout.setVisibility(View.GONE);
	    	
	    	
	    	Intent intentDemo=new Intent(getApplicationContext(),SplashScreen.class);		
            startActivity(intentDemo);
	    	
	    }
	    
	    String incoming_number = getIntent().getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

	    Intent i=getIntent();
		String outgoing_number=i.getStringExtra("number");
		
		
	    if(incoming_number!= null && !incoming_number.isEmpty())
	    {
	    	phonenumber=incoming_number.substring(0,3) ;
	    	Log.d("Incoming call from ",phonenumber);
	    }
	    else if(outgoing_number!= null && !outgoing_number.isEmpty())
	    {
	    	phonenumber=outgoing_number.substring(0,3);
	    	Log.d("out going call from ",phonenumber);
	    }
	    else
	    {
	    	phonenumber=Integer.toString(319);
	    	
	    }
	    	

		markerPoints = new ArrayList<LatLng>(); 	// Initializing 

		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
		map = fm.getMap(); // Getting Map for the SupportMapFragment
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(USA, 4));
		map.animateCamera(CameraUpdateFactory.zoomTo(4), 2000, null);
		map.setMyLocationEnabled(true);		// Enable MyLocation Button in the Map
		
		

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		
		if(activeNetworkInfo!=null)
		{
			
			new RetrieveTask().execute();
		}
		else
		{
			
		}
		new RetrieveTask().execute();
				 
	   
	  }
	  private void addMarker(LatLng latlng) 
		{
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(latlng);

			markerOptions.title("Client").snippet("");
			map.addMarker(markerOptions);

		}

	  
	  private class RetrieveTask extends AsyncTask<Void, Void, String>
	  {

			@Override
			protected String doInBackground(Void... params) {
				
				URL url = null;
				StringBuffer sb = new StringBuffer();
				Log.d("back", "backgroud");
				try {
					url = new URL("http://office.eastlinkhost.com/epenny/sell/css/phonecall_json.php?value="+URLEncoder.encode(phonenumber));
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.connect();
					InputStream iStream = connection.getInputStream();				
					BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));			
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
				return sb.toString();
			}
			
			@Override
			protected void onPostExecute(String result)
			{			
				super.onPostExecute(result);
				new ParserTask().execute(result);
			}
			
		}
		
		// Background thread to parse the JSON data retrieved from MySQL server
		private class ParserTask extends AsyncTask<String, Void, List<HashMap<String, String>>>{
			@Override
			protected List<HashMap<String,String>> doInBackground(String... params) {
				MarkerJSONParser markerParser = new MarkerJSONParser();
				JSONObject json = null;
				try {
					json = new JSONObject(params[0]);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				List<HashMap<String, String>> markersList = markerParser.parse(json);
				return markersList;
			}
			
			@Override
			protected void onPostExecute(List<HashMap<String, String>> result) 
			{
				for(int i=0; i<result.size();i++)
				{
					HashMap<String, String> marker = result.get(i);
					LatLng latlng = new LatLng(Double.parseDouble(marker.get("lat")), Double.parseDouble(marker.get("lng")));
					
					
					Log.d("lat lng", latlng+"");
					
					addMarker(latlng);
				}
			}
		}

	  
		@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	  }

	} 
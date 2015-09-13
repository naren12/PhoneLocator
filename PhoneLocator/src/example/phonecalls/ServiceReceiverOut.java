package example.phonecalls;

import gpslocation.GPSTracker;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

public class ServiceReceiverOut extends BroadcastReceiver {
	
	private String number,value="value";
	GPSTracker gps;
	
	@SuppressLint("NewApi")
	public void onReceive(final Context context, final Intent intent) {

		
		final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		
	
        Thread pageTimer = new Thread(){
            public void run(){
                try{
                    sleep(700);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                	
                	
                	{
	                    Intent i = new Intent();
	                    i.setClass(context, MyPhoneStateListener.class);
	                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  
	                    i.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	                    i.putExtras(intent);
	                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	                    i.setAction(Intent.ACTION_MAIN);
	                    i.addCategory(Intent.CATEGORY_LAUNCHER);
	                    i.putExtra("number", number);
	                    i.putExtra("value_out", value);
	                    context.startActivity(i);
	                    
	                    
                	}
                	
                }
            }
        };
        pageTimer.start();
    }
	}




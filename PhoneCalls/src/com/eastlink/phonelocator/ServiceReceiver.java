package com.eastlink.phonelocator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

public class ServiceReceiver extends BroadcastReceiver {
	
    private String value="value";
	public void onReceive(final Context context, final Intent intent) {

		
		final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		
        Thread pageTimer = new Thread(){
            public void run(){
                try{
                    sleep(700);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                	if (state.equals(TelephonyManager.EXTRA_STATE_RINGING) || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) 
                	{
	                    Intent i = new Intent();
	                    i.setClass(context, MyPhoneStateListener.class);
	                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  
	                    i.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	                    i.putExtras(intent);
	                    i.putExtra("value", value);
	                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	                    i.setAction(Intent.ACTION_MAIN);
	                    i.addCategory(Intent.CATEGORY_LAUNCHER);

	                    context.startActivity(i);
	                    
	                    
                	}
                	else
                	{
                		//System.exit(0);
                		
                	}
                }
            }
        };
        pageTimer.start();
    }
	}

package com.union.x100xpress;

import org.ksoap2.serialization.SoapPrimitive;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity1 extends Activity {
	
	private PendingIntent pendingIntent;
	private AlarmManager manager;
	
	Button but;
	View view;
	
	
	private static final String NAMESPACE = "http://webpackage/";
	private static final String URL = 
			//			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
			/*"http://192.168.43.32:8680/web_service/HelloWorldPortType";*/
	/*"http://192.168.1.26:8680/web_service/HelloWorldPortType"; */   
	/*"http://192.168.2.17:9999/web_service/HelloWorldPortType";*/ /*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*"http://197.251.247.4/web_service/HelloWorldPortType";*/
			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
			
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;
//	static PropertyInfo pi;

	Thread thread;
	static String sendLocation;
	static String reqChoice = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loc_activity);
		
		// Retrieve a PendingIntent that will perform a broadcast
	    Intent alarmIntent = new Intent(this, AlarmReceiver.class);
	    pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
	    
	    startAlarm(view);
	    /*String msg = "LOC" + " " + AlarmReceiver.lat + " " + AlarmReceiver.lng;
	    sendRequest(msg);
	    thread.start();*/
	    
	    
//	    new LocationWebService();
	    
	    
//	    Intent alarmIntent1 = new Intent(this, AlarmReceiver.class);
//	    pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//	    manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//	    manager.cancel(pendingIntent);
//	    Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
		
		
	}
	
	
	public void startAlarm(View view) {
	    manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    int interval = 10000;

	    manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//	    Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
	    
	}
	
	public void cancelAlarm(View view) {
	    if (manager != null) {
	        manager.cancel(pendingIntent);
	        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
	    }
	}

}

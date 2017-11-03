package com.union.x100xpress;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver implements LocationListener {
	
	private Activity activity; //activity is defined as a global variable in your AsyncTask
	
	MainActivity mainActivity = new MainActivity();
	
	/*public AlarmReceiver(){
		
	}

	public AlarmReceiver(Activity activity) {

	    this.activity = activity;
	}*/

	//	private LocationManager locationManager;
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
	Context context;
	
	Context arg0;

	static double lng;
	static double lat;

	GPSTracker gps;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MIN_TIME_BW_UPDATES = 1000; // in Milliseconds

	protected LocationManager mLocationManager;
	Location location;

	protected Button retrieveLocationButton;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// For our recurring task, we'll just display a message
		//        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();\
		//    	new AndroidLocationServices();

		//    	locationManager = (LocationManager) arg0
		//				.getSystemService(Context.LOCATION_SERVICE);
		//
		//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		//				5000, 5, listener);


		/*gps = new GPSTracker(arg0);

		// check if GPS enabled       
		if(gps.canGetLocation()){

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			Toast.makeText(arg0, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

//			AccountNum.choice = "LOC";

//			String message = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + latitude + " " + longitude;

//			LocationWebService.pi.setValue(message);

//			sendRequest(message); 
//			thread.start();
//			new LocationWebService().execute();

		}else{
			// can't get location
			// GPS or Network is not enabled

//			        	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//			        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			        	arg0.startActivity(intent);      	

			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
			intent.putExtra("enabled", true);
			arg0.sendBroadcast(intent);

			//        	Disable GPS
			//        	Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
			//        	intent.putExtra("enabled", false);
			//        	sendBroadcast(intent);


		}*/


		
//		isBetterLocation(location, currentBestLocation)(location, 3);



		try {
			mLocationManager = (LocationManager) arg0.getSystemService(Context.LOCATION_SERVICE);
			
//			ConnectivityManager cm = (ConnectivityManager) arg0.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

			// getting GPS status
			boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
//				Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//				intent.putExtra("enabled", true);
//				arg0.sendBroadcast(intent);
			} else {
				// First get location from Network Provider
				if (isNetworkEnabled) {
					mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					
					if (mLocationManager != null) {
						location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							lat = location.getLatitude();
							lng = location.getLongitude();
							Toast.makeText(arg0, "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_SHORT).show();

						}
					}
				}
				/*if (activeNetwork.isConnected()) {
	                mLocationManager.requestLocationUpdates(
	                        LocationManager.NETWORK_PROVIDER,
	                        MIN_TIME_BW_UPDATES,
	                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                Log.d("Network", "Network");
	                if (mLocationManager != null) {
	                    location = mLocationManager
	                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

	                }
	            }*/
				
				//get the location by gps
				if (isGPSEnabled) {
					if (location == null) {
						mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (mLocationManager != null) {
							location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								lng = location.getLongitude();
								Toast.makeText(arg0, "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_SHORT).show();

							}
						}
					}
				}
				
				String msg = "LOC" + " " + lat + " " + lng;
				System.out.println(msg);
			    sendRequest(msg);
			    thread.start();
				
			}



		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		




//		    	getLocation();
		//    	Toast.makeText(arg0, "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_SHORT).show(); 


		//		String status = NetworkUtil.getConnectivityStatusString(arg0);

		//		Toast.makeText(arg0, status, Toast.LENGTH_LONG).show();


		/*LocationManager manager = (LocationManager) arg0.getSystemService(Context.LOCATION_SERVICE);

    	Location location = null;

    	List<String> providers = manager.getProviders(enabledProvidersOnly)

    	for(String provider : providers){

    	location = manager.getLastKnownLocation(provider);
    	//maybe try adding some Criteria here

    	if(location != null) return location;
    	}

    	//at this point we've done all we can and no location is returned
    	return null;*/




		/*gps = new GPSTracker(arg0);

        // check if GPS enabled       
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();



            // \n is for new line
            Toast.makeText(arg0, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();   
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings

        	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	arg0.startActivity(intent);

//            gps.turnGPSOn();
//        	showSettingsAlert();

//        	Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//        	intent.putExtra("enabled", true);
//        	arg0.sendBroadcast(intent);

//        	Disable GPS
//        	Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//        	intent.putExtra("enabled", false);
//        	sendBroadcast(intent);

        }*/

		//    	new AndroidLocationServices();




	}
	
	
	public void sendRequest(final String message){
		thread = new Thread(new Runnable(){
//			@SuppressWarnings("null")
			@Override
			public void run() {
				
//				Context arg0 = null;
//				Activity activity = null;

				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
				//request.addProperty("Input",enterText.getText().toString());
				PropertyInfo pi = new PropertyInfo();
				pi.setNamespace(NAMESPACE);
				pi.setType(PropertyInfo.STRING_CLASS);
				//pi.setName("name");
				pi.setName("Input");
				//pi.setValue(editText1.getText().toString().trim());
				pi.setValue(message);
				request.addProperty(pi);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11); 
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 10000);

				try{																	
					androidHttpTransport.call(SOAP_ACTION, envelope);

					sp = (SoapPrimitive) envelope.getResponse();

					
					mainActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
//							ratty = sp.toString();
							/*if(ratty.equals("Transaction successful")){
								progress.cancel();
								successful();
								//								openPrintApp();
							}else if(ratty.equals("Transaction unsuccessful1")){
								progress.cancel();
								unsuccessful();

							}	*/																						

						}
					});

				}
				catch (SocketTimeoutException e) {
//					noConnectivity();
				}
				catch (IOException e) {

					e.printStackTrace();

				}

				catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		});
		}




	/*public void successful(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				AlarmReceiver.this);

		// Setting Dialog Title
		alertDialog.setTitle(ratty);
		alertDialog.setMessage("Would you like to return to the main menu?");		 

		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.dismiss();
				DepositChooseAcc.account = "";
				SearchByName.searchAccountNum = "";
				finish();
				Intent back = new Intent(Deposit.this, MainMenuList.class);
				startActivity(back);
			}
		});
		// Setting Negative "NO" Btn
		alertDialog.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				//		                
				dialog.cancel();
				okButton.setVisibility(View.INVISIBLE);
				getAccNameButton.setVisibility(View.VISIBLE);
				accNumEditText.setText("");
				amountEditText.setText("");
				accNameTextView.setText(" ");
				accBalTextView.setText("");
				amountEditText.setEnabled(false);
				narrationEditText.setEnabled(false);
				accNumEditText.requestFocus();
			}
		});

		// Showing Alert Dialog
		alertDialog.show();
	}*/





	/*public Location getLocation() {
        try {
            mLocationManager = (LocationManager) arg0.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    mLocationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,  MIN_TIME_BW_UPDATES,  MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (mLocationManager != null) {
                        location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                    }
                }
                //get the location by gps
                if (isGPSEnabled) {
                    if (location == null) {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (mLocationManager != null) {location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }*/
	
	
	
	
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	
	
	

	public Location getLocation() {
		try {
			mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				// First get location from Network Provider
				if (isNetworkEnabled) {
					mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (mLocationManager != null) {
						location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							lat = location.getLatitude();
							lng = location.getLongitude();
							Toast.makeText(context, "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_SHORT).show();

						}
					}
				}
				//get the location by gps
				if (isGPSEnabled) {
					if (location == null) {
						mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (mLocationManager != null) {
							location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								lng = location.getLongitude();
								Toast.makeText(context, "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_SHORT).show();

							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}







	public void showSettingsAlert(){

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(arg0);

		//        ((AlertDialog) alertDialog).getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				arg0.startActivity(intent);

			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}




	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			String message = String.format(
					"New Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
					);
			Toast.makeText(arg0, message, Toast.LENGTH_LONG).show();
		}

		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(arg0, "Provider status changed",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(arg0,
					"Provider disabled by the user. GPS turned off",
					Toast.LENGTH_LONG).show();
			openGPS();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(arg0,
					"Provider enabled by the user. GPS turned on",
					Toast.LENGTH_LONG).show();
		}

		public void openGPS(){

			AlertDialog.Builder alert = new AlertDialog.Builder(arg0);

			alert.setTitle("Turn on GPS?");

			alert.setPositiveButton("YES", 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

					dialog.cancel();
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					arg0.startActivity(intent);
					//				dialog.dismiss();
				}
			});

			alert.setNegativeButton("NO", 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
			}); 

			AlertDialog alertDialog = alert.create();
			alertDialog.show();

		}


	}




	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}






	//    public AlarmReceiver(context) {
	//		// TODO Auto-generated constructor stub
	//	} AlarmReceiver alarmReceiver = new AlarmReceiver(context);
	//    


	/*public void ourMethod(Context arg0){
    	locationManager = (LocationManager) arg0
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 5, listener);
    }*/



	/*LocationListener listener = new LocationListener() {


		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

			Log.e("Google", "Location Changed");
//			NetworkInfo netInfo = null;

			if (location == null)
				return;

			if (isNetworkConnected()) {
				//            JSONArray jsonArray = new JSONArray();
				//            JSONObject jsonObject = new JSONObject();

				try {
					Log.e("latitude", location.getLatitude() + "");
					Log.e("longitude", location.getLongitude() + "");

					Toast.makeText(context, "latitude: "+ location.getLatitude() + "/n" + "longitude"+ location.getLongitude() + "", Toast.LENGTH_SHORT).show();
//					choice = "LOC";

					String message = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + location.getLatitude() + " " + location.getLongitude();
							"message";

					LocationWebService.pi.setValue(message);

//					new LocationWebService().execute();

					//                new LocationWebService().execute(new String[] {
					//                        Constants.TRACK_URL, jsonArray.toString() });
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else{
				Toast.makeText(context, "newtwork unavailable", Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	};

	public static boolean isConnectingToInternet() {
		Context _context = null;
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}


	protected boolean isNetworkConnected() {
	    Context ctx = null ;
	    ConnectivityManager cm = (ConnectivityManager) ctx
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getActiveNetworkInfo();
	    if (info != null)
	        return info.isAvailable();
	    else
	        return false;
	} 


	private boolean isConnected(){
		  ConnectivityManager connectivityManager=(ConnectivityManager)arg0.getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo networkInfo=null;
		  if (connectivityManager != null) {
		    networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		    if (!networkInfo.isAvailable()) {
		      networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		    }
		  }
		  return networkInfo == null ? false : networkInfo.isConnected();
		}*/



}
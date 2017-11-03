package com.union.x100xpress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketTimeoutException;


public class MainMenuList extends AppCompatActivity {

	
	ListView list;
	String code;
	Thread thread;
//	String msg = "";
	BufferedReader br;
	double lng;
	double lat;
//	Context arg0;
	
	
	private static final String NAMESPACE = "http://webpackage/";
	private static final String URL = /*"http://192.168.1.26:8686/web_service/HelloWorldPortType";*/  
			/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*abii*/
			/*"http://192.168.1.26:8680/web_service/HelloWorldPortType";*/   /*my machine*/
	/*"http://192.168.180.23:8680/web_service/HelloWorldPortType";*/  /*fnsl test local*/
	/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/ /*fnsl public*/
//	"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
//			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
//			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
//	"http://192.168.43.32:8680/web_service/HelloWorldPortType";
	/*"http://192.168.2.15:8680/web_service/HelloWorldPortType";*/
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;
	static String msg = "";

//	View view;
//	PendingIntent pendingIntent;
//	AlarmManager manager;
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MIN_TIME_BW_UPDATES = 1000; // in Milliseconds
	
	protected LocationManager mLocationManager;
	Location location;

	//	private String[] drawerListViewItems;
	//	private DrawerLayout drawerLayout;
	//	private ListView drawerListView;
	//	private ActionBarDrawerToggle actionBarDrawerToggle;
	//	private String[] navMenuTitles = null;
	//	private TypedArray navMenuIcons = null;
	//	private ArrayList<NavDrawerItem> navDrawerItems;
	//	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu_activity);
		
		getSupportActionBar().show();
		getSupportActionBar().setLogo(R.drawable.union1);

		DepositChooseAcc.account = "";
		WithdrawalChooseAcc.acc = "";

		// Retrieve a PendingIntent that will perform a broadcast
//	    Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//	    pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//	    
//	    startAlarm(view);
	    
	    

//		findLocation();
		
//		sendLocation(lat);
	    list = (ListView) findViewById(R.id.list);
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.menulist));
		   
//		adapter = setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.menulist)));	
		list.setAdapter(adapter);
//		list = getListView();

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				//int listPosition = list.getSelectedItemPosition();
				//choice = String.valueOf(list.getItemAtPosition(position));

				switch (position){
				case 0:
					Intent checkBalanceIntent = new Intent(MainMenuList.this, AccountNum.class);
					startActivity(checkBalanceIntent);
					break;
				case 1:
					Intent accListIntent = new Intent(MainMenuList.this, CustomerAccList.class);
					startActivity(accListIntent);
					//									thread.start();
					break;
				case 2:
					Intent custDetailsIntent = new Intent(MainMenuList.this, CustomerDetails.class);
					startActivity(custDetailsIntent);

					break;
				case 3:
					Intent transDetailsIntent = new Intent(MainMenuList.this, TransactionDetails.class);
					startActivity(transDetailsIntent);
					break;
//				case 4:
//					Intent searchIntent = new Intent(MainMenuList.this, SearchByName.class);
//					startActivity(searchIntent);
//					break;
				case 4:
					Intent depositChooseAccIntent = new Intent(MainMenuList.this, DepositChooseAcc.class);
					startActivity(depositChooseAccIntent);
					break;
				case 5:
					Intent depositIntent = new Intent(MainMenuList.this, Deposit.class);
					startActivity(depositIntent);
					break;
				case 6:
					Intent withdrawalChooseAccIntent = new Intent(MainMenuList.this, WithdrawalChooseAcc.class);
					startActivity(withdrawalChooseAccIntent);
					break;

				case 7:
					Intent withdrawalIntent = new Intent(MainMenuList.this, Withdraw.class);
					startActivity(withdrawalIntent);
					break;
				case 8:
					Intent custLoanIntent = new Intent(MainMenuList.this, Cust_Loan_Origination.class);
					startActivity(custLoanIntent);
					break;
				case 9:
					Intent nonCustLoanIntent = new Intent(MainMenuList.this, NonCust_Loan_Origination.class);
					startActivity(nonCustLoanIntent);
					break;
				case 10:
					Intent openAccountIntent = new Intent(MainMenuList.this, AcctCreation.class);
					startActivity(openAccountIntent);
					break;
				case 11:
					Intent sikaIntent = new Intent(MainMenuList.this, SikaPlus.class);
					startActivity(sikaIntent);
					break;
				case 12:
					Intent walletIntent = new Intent(MainMenuList.this, WalletMainMenu.class);
					startActivity(walletIntent);
					break;

				}//end switch statement


			}
		});



	}



	//Code for hardware menu button
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//MenuItems
	public boolean onOptionsItemSelected(MenuItem item){
switch(item.getItemId()){
		
		case R.id.action_settings:
			Intent hoverList = new Intent(MainMenuList.this, HoverSettingsMenu.class);
//			hoverList.putExtra("token", token);
			startActivity(hoverList);
			break;	
		/*case R.id.action_exit:
			exitApp();
			break;*/

		default:
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/*public void startAlarm(View view) {
	    manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    int interval = 10000;

	    manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//	    Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
	    
	}*/
	
	/*public void cancelAlarm(View view) {
	    if (manager != null) {
	        manager.cancel(pendingIntent);
	        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
	    }
	}*/
	
	
	/*public void findLocation(){
	try {
		mLocationManager = (LocationManager) MainMenuList.this.getSystemService(Context.LOCATION_SERVICE);
		
//		ConnectivityManager cm = (ConnectivityManager) arg0.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

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
				
				if (mLocationManager != null) {
					location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						lat = location.getLatitude();
						lng = location.getLongitude();
						Toast.makeText(MainMenuList.this, "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_SHORT).show();

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
							Toast.makeText(MainMenuList.this, "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_SHORT).show();

						}
					}
				}
			}
		}



	} catch (Exception e) {
		e.printStackTrace();
	}
	
	}
*/	
	
	public void sendLocation(final String message){
		thread = new Thread(new Runnable(){
			@Override
			public void run() {

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

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//										progress.cancel();
//							ratty = sp.toString();
//							progress.cancel();
							//										Toast.makeText(MainActivity.this, ratty.trim(), Toast.LENGTH_LONG).show();
							/*if(ratty.trim().equals("00")){
								finish();
								Intent mainmenuIntent = new Intent(MainActivity.this, MainMenuList.class);
								startActivity(mainmenuIntent);										
							}else if(ratty.trim().equals("10")){
								wrongPassword();
							}else if(ratty.trim().equals("11")){
								acctLocked();
							}else if(ratty.trim().equals("15")){
								notApproved();
							}else if(ratty.trim().equals("12")){
								wrongUser();
							}*/


							/*if (ratty.equals("error1")){
											progress.cancel();
											Toast.makeText(MainActivity.this, "Wrong account number", Toast.LENGTH_LONG).show();
										}else{
											StringTokenizer token = new StringTokenizer(ratty, ",");

											progress.cancel();


										}*/

						}
					});

				}catch (SocketTimeoutException e) {
//					progress.cancel();
					noConnectivity();
				}catch (IOException e) {

					e.printStackTrace();
					//System.out.println("Error with response");
				}

				catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	public void exitApp(){
		try{																	

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainMenuList.this);
					alertDialog.setTitle("Confirm exit");
					alertDialog.setMessage("Do you want to exit?");	

					alertDialog.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							MainMenuList.super.onBackPressed();
						}
					});
					// Setting Negative "NO" Btn
					alertDialog.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog

							dialog.cancel();

						}
					});
					alertDialog.show();

				}
			});

		}catch (Exception e) {

			e.printStackTrace();
		}


	}
	
	public void noConnectivity(){

		try{																	

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					errorConnecting();

				}
			});

		}catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void errorConnecting(){
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				MainMenuList.this);

		// Setting Dialog Title
		alertDialog3.setTitle("Error connecting....");
		alertDialog3.setMessage("Would you like to try again?");		 

		alertDialog3.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.cancel();

			}
		});
		// Setting Negative "NO" Btn
		alertDialog3.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				//		                
				dialog.cancel();

			}
		});

		// Showing Alert Dialog
		alertDialog3.show();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		exitApp();
	}



	/*@Override
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
		
	}*/



}

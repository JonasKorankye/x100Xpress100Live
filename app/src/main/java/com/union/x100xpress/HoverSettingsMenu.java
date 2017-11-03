package com.union.x100xpress;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import myUrl.UrlClass;


public class HoverSettingsMenu extends AppCompatActivity {

	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL =  /*"http://192.168.246.1:8680/web_service/HelloWorldPortType";*/
//	/*"http://197.251.247.4/web_service/HelloWorldPortType";*/
//	/*"http://192.168.43.32:8680/web_service_mobi/HelloWorldPortType";*/
//	/*"http://197.221.81.162:8681/web_service_mob/Hello";*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://197.221.80.184:8680/web_service/HelloWorldPortType";  /*bestpoint public*/
//	/*"http://192.168.43.32:8680/web_service_mob/Hello"; */
//	//			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;
	Thread thread;
//	String ratty = "";
	String msg = null;

	int[] arrow = new int[]{	R.drawable.arrow };
	//	int[] alias_pic = new int[]{	R.drawable.ic_action_alias };
	int[] next_pic = new int[] {R.drawable.arrow};
	String token = null;

	String oldPassword = null;
	String newPassword1 = null;
	String newPassword2 = null;
	//	String pull = "Change Password";
	String chPassword = "Change Password";
	String push = "Push";
	String del = "Delete";
	List<HashMap<String,String>> aList  = new ArrayList<HashMap<String,String>>();
	ListView list;

	String accNum = null;
	String custNum = null;
	String name = null;
	String branch = null;
	String balance = null;
	String onlineFlag = null;

	String mb_response = null;
	String stuff[] = null;

	//	String

	//	String mbAcc = null;
	String[] aliasAccDetails = null;
	//	String alias = null;
	//	String aliasAcc = null;

	ProgressDialog progress;
//	String response = null;
	List<HashMap<String,String>> list1 = new ArrayList<HashMap<String,String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_inflater);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		HashMap<String, String> hm = new HashMap<String,String>();

		hm.put("menuitem", chPassword);
		hm.put("pix", Integer.toString(arrow[0]) );
		//		hm.put("imgDataNext", Integer.toString(next_pic[0]) );

		//		HashMap<String,String> hm2 = new HashMap<String, String>();
		//		hm2.put("menuitem",push);
		//		hm2.put("pix", Integer.toString(arrow[0]));
		//		hm.put("imgDataNext", Integer.toString(next_pic[0]) );
		aList.add(hm);
		//		aList.add(hm2);


		String[] from = {"menuitem","pix"};
		int[] to = {R.id.menu_item,R.id.flag};

		//		final MySQLiteHelper db = new MySQLiteHelper(HoverSettingsMenu.this);

		SimpleAdapter adpt = new SimpleAdapter(this, aList, R.layout.mainmenu_list, from, to);
		list = (ListView) findViewById(R.id.function_list);
		list.setAdapter(adpt);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//accountName = ((TextView) v.findViewById(R.id.acc_name)).getText().toString().trim();

				String dataToCompare = ((TextView) v.findViewById(R.id.menu_item)).getText().toString().trim();

				switch (dataToCompare){
//				case ("Pull"):
//				{
//					progress = new ProgressDialog(HoverSettingsMenu.this);
//					progress.setTitle("Sending");
//					progress.setMessage("Please wait...");
//					progress.show();
//					String choice = null;
//					choice = "PULL";
//					msg = choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword ;
//
//					//					db.deleteAllRecords();
//
////					pullRequest(msg);
//					thread.start();
//					break;
//				}
				case ("Change Password"):
				{
					final Dialog dialog = new Dialog(HoverSettingsMenu.this);
					dialog.setContentView(R.layout.dialog);
					dialog.setTitle("Change Password");

					final EditText oldPasswordEdittext = (EditText) dialog.findViewById(R.id.old_password);
					final EditText newPasswordEdittext1 = (EditText) dialog.findViewById(R.id.new_password1);
					final EditText newPasswordEdittext2 = (EditText) dialog.findViewById(R.id.new_password2);

					Button okButton = (Button) dialog.findViewById(R.id.ok_button);
					Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);

					okButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							oldPassword = oldPasswordEdittext.getText().toString().trim();
							newPassword1 = newPasswordEdittext1.getText().toString().trim();
							newPassword2 = newPasswordEdittext2.getText().toString().trim();

							if(oldPassword.length()==0){
								oldPasswordEdittext.setError("Please enter current password");
							}
							else if(newPassword1.length()==0){
								newPasswordEdittext1.setError("Please enter new password");
							}
							else if(newPassword2.length()==0){
								newPasswordEdittext2.setError("Please re-enter new password");
							}
							else if (!newPassword2.equals(newPassword1) ){
								newPasswordEdittext2.setError("Does not match what you typed in the previous field");
							}
							else if(newPassword2.length()<5){
								newPasswordEdittext2.setError("Password must be greater than 4 characters");
							}
							else{
								//							dialog.cancel();
//								progress = new ProgressDialog(HoverSettingsMenu.this);
//								progress.setTitle("Sending");
//								progress.setMessage("Please wait...");
//								progress.show();

								System.out.println(oldPassword);
								System.out.println(newPassword1);
								System.out.println(newPassword2);

								String choice = null;
								choice = "PC";
								msg = choice + " " + MainActivity.userPhoneNum + " " + oldPassword + " " + newPassword1;
								new fireMan().execute();
//								sendRequest(msg);
//								thread.start();
								dialog.cancel();
								}

						}
					});

					cancelButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();

						}
					});

					dialog.show();

					break;
				}
				}
			}



		});

	}


	public void noConnectivity() {

		try {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					errorConnecting();

				}
			});

		} catch (Exception e) {

			e.printStackTrace();
		}
	}


	public void errorConnecting() {
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				HoverSettingsMenu.this);

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



	int responseCode = 0;
	String response = null;


	public class fireMan extends AsyncTask<Void , Void, Void> {

		private Exception e = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(HoverSettingsMenu.this);
			progress.setCancelable(false);
			progress.setTitle("Please wait");
			progress.setMessage("Sending request...");
			progress.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			URL obj = null;
			try {
				obj = new URL(UrlClass.url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			HttpURLConnection con = null;
			try {
				con = (HttpURLConnection) obj.openConnection();
			} catch (IOException e) {
				e.printStackTrace();

			}

			//add request header
			try {
				con.setRequestMethod("POST");

			} catch (ProtocolException e) {
				e.printStackTrace();
			}
//            con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0");
			con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
			con.setReadTimeout(15000); // millis
			con.setConnectTimeout(15000); // millis

//            String urlParameters = choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = null;
			try {
				wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(msg);
				wr.flush();
				wr.close();
			} catch (IOException e) {
				e.printStackTrace();
				noNetworkConn();

			}


			try {
				responseCode = con.getResponseCode();
				System.out.println("ResponseCode is " + responseCode);
			} catch (IOException e) {
				e.printStackTrace();
				progress.cancel();
//                noConnectivity();
			}
			System.out.println("\nSending 'POST' request to URL : " + UrlClass.url);
			System.out.println("Post parameters : " + msg);
//            System.out.println("Response Code : " + responseCode);

			if (responseCode == 200) {

				BufferedReader in = null;
				try {
					String inputLine = null;
					in = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
						response = inputLine;

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (in == null) {

					} else {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				//print result
				System.out.println(response);

			} else if (responseCode == 404) {
				progress.cancel();
				noConnectivity();
			} else if (responseCode == 500) {
				progress.cancel();
				noConnectivity();
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

//			progress.cancel();

			if (response==null){
				System.out.println("RESPONSE EMPTY");
			}
			else if (response.equals("error1")){
				progress.cancel();
				Toast.makeText(HoverSettingsMenu.this, "Wrong account number", Toast.LENGTH_LONG).show();
			}else if (response.equals("20")){
				progress.cancel();
				Toast.makeText(HoverSettingsMenu.this, "Unsuccessful", Toast.LENGTH_LONG).show();

			}else if (response.equals("10")){
				progress.cancel();
				Toast.makeText(HoverSettingsMenu.this, "Wrong Password", Toast.LENGTH_LONG).show();

			}else if (response.equals("00")){
				progress.cancel();

				MainActivity.userPassword = newPassword1;
				Toast.makeText(HoverSettingsMenu.this, "Password changed successfully", Toast.LENGTH_LONG).show();

			}




		}

	}



//	public void sendRequest(final String message){
//		thread = new Thread(new Runnable(){
//			@Override
//			public void run() {
//
//				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//				//request.addProperty("Input",enterText.getText().toString());
//				PropertyInfo pi = new PropertyInfo();
//				pi.setNamespace(NAMESPACE);
//				pi.setType(PropertyInfo.STRING_CLASS);
//				//pi.setName("name");
//				pi.setName("Input");
//				//pi.setValue(editText1.getText().toString().trim());
//				pi.setValue(message);
//				request.addProperty(pi);
//
//				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
//				envelope.setOutputSoapObject(request);
//
//				HttpTransportSE androidHttpTransport = new HttpTransportSE(UrlClass.url, 5000);
//
//				try{
//					androidHttpTransport.call(SOAP_ACTION, envelope);
//
//					sp = (SoapPrimitive) envelope.getResponse();
//
//					runOnUiThread(new Runnable() {
//
//						//						@SuppressWarnings("unchecked")
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							progress.cancel();
//							ratty = sp.toString();
//
//							System.out.println(ratty);
//
//							if (ratty.equals("error1")){
//								progress.cancel();
//								Toast.makeText(HoverSettingsMenu.this, "Wrong account number", Toast.LENGTH_LONG).show();
//							}else if (ratty.equals("20")){
//								progress.cancel();
//								Toast.makeText(HoverSettingsMenu.this, "Unsuccessful", Toast.LENGTH_LONG).show();
//
//							}else if (ratty.equals("10")){
//								progress.cancel();
//								Toast.makeText(HoverSettingsMenu.this, "Wrong Password", Toast.LENGTH_LONG).show();
//
//							}else if (ratty.equals("00")){
//								progress.cancel();
//
//								MainActivity.userPassword = newPassword1;
//								Toast.makeText(HoverSettingsMenu.this, "Password changed successfully", Toast.LENGTH_LONG).show();
//
//							}
//
//						}
//					});
//
//				}catch (SocketTimeoutException e) {
//					progress.cancel();
//					//					noConnectivity();
//				}catch (IOException e) {
//
//					e.printStackTrace();
//					//System.out.println("Error with response");
//				}
//
//				catch (XmlPullParserException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}



	public String confirmJSON(String authToken,String pwd1, String pwd2){
		return "authToken="+authToken+
				"&pwd1="+pwd1+
				"&pwd2="+pwd2;


	}


	/*public String confirmResponse (String a1,String a2,String a3) throws IOException{
//		SendRequest sendReqObj = new SendRequest();

		String json = confirmJSON(a1,a2,a3);
//		String response = sendReqObj.postReq(GlobalCodes.myUrl+"pwdchange" , json);

		System.out.println("response: " + response);

		return response;
	}*/



	public void pullSuccessful(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				HoverSettingsMenu.this);

		// Setting Dialog Title
		alertDialog.setTitle(response);
		alertDialog.setMessage("Pull Successful");		 

		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.dismiss();

			}
		});


		// Showing Alert Dialog
		alertDialog.show();
	}



	public void noNetworkConn() {

		try {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					noNetwork();

				}
			});

		} catch (Exception e) {

			e.printStackTrace();
		}
	}


	public void noNetwork() {
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(
				HoverSettingsMenu.this);

		// Setting Dialog Title
		alertDialog4.setTitle("Network connectivity down");
		alertDialog4.setMessage("Please consider using other SIM");

		alertDialog4.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog
						dialog.cancel();

					}
				});

		// Showing Alert Dialog
		alertDialog4.show();

	}




	public void pushSuccessful(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				HoverSettingsMenu.this);

		// Setting Dialog Title
		alertDialog.setTitle(response);
		alertDialog.setMessage("Push Successful");		 

		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.dismiss();

			}
		});


		// Showing Alert Dialog
		alertDialog.show();
	}

	public class ResponseDialog extends Builder {

		// Store the passed context
		private Context context;

		// Can be used as a regular builder
		public ResponseDialog(Context context) {
			super(context);
		}

		// Or as a custom builder, which we want
		public ResponseDialog(Context context, String title, String message) {
			super(context);
			// Store context
			this.context = context;
			// Set up everything
			setMessage(message);
			setTitle(title);
			setCancelable(false);
			//			setPadding(0, 5, 0, 5);
			setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss(); 
					finish();
					//					Intent confirmTransIntent = new Intent(ConfirmTrans.this,MainMenu.class);
					//					startActivity(confirmTransIntent);
				}
			});
		}

		public void showDialog(){
			// Create and show
			AlertDialog alert = create();
			alert.show();
			// Center align everything
			((TextView)alert.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
			((TextView)alert.findViewById((context.getResources().getIdentifier("alertTitle", "id", "android")))).setGravity(Gravity.CENTER);
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//		menu.findItem(R.id.refresh).setVisible(false);
		return true;
	}  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			//	         NavUtils.navigateUpFromSameTask(this);S
			HoverSettingsMenu.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		//		moveTaskToBack(true); 
		HoverSettingsMenu.this.finish();
	}

}

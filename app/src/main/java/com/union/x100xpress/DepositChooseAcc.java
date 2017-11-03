package com.union.x100xpress;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import myUrl.UrlClass;


public class DepositChooseAcc extends ListActivity {

	private static final String NAMESPACE = "http://webpackage/";
	//	private static final String URL = /*"http://192.168.1.26:8680/web_service/HelloWorldPortType"; */
//	/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*"http://197.251.247.4/web_service/HelloWorldPortType";*/
//			/*"http://192.168.180.23:8680/web_service/HelloWorldPortType";*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/
////			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://197.221.80.184:8680/web_service/HelloWorldPortType";  /*bestpoint public*/
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;

	Thread thread;
	String custNum;
	ListView acc_list;
	EditText enterCustNum;
	static String stuff[] = {"", "", ""};
	//	static String accNo;
	static String account = "";
	static String accNum;
	static String accName;
	File myFile;
	Button okButton;
	ProgressDialog progress;
	String msg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deposit_choose_acc);

//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		enterCustNum = (EditText) findViewById(R.id.cust_number);
		okButton = (Button) findViewById(R.id.okBut);

		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				custNum = enterCustNum.getText().toString();
				if /*(custNum.length() !=0 && custNum.length() != 7){
					enterCustNum.setError("Customer number must be 7 digits");
				}else if*/ (custNum.length() == 0) {
					enterCustNum.setError("Please enter customer number");
				} else {
					AccountNum.choice = "AA";
					msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + custNum;

					new fireMan().execute();
				}
			}
		});



		acc_list = getListView();
		acc_list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				account = String.valueOf(stuff[position]);
				Intent depositIntent = new Intent(DepositChooseAcc.this, Deposit.class);
				startActivity(depositIntent);
			}
		});
				
	}
	
	public void makelist(){
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stuff));	
	}
	
	public void wrongAccNum(){
		AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
				DepositChooseAcc.this);

		// Setting Dialog Title
		alertDialog5.setTitle("Error !");
		alertDialog5.setMessage("Customer does not exist");		

		alertDialog5.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.cancel();
				enterCustNum.setText("");
				enterCustNum.requestFocus();
			}
		});
		// Showing Alert Dialog
		alertDialog5.show();
	}


	public void errorConnecting() {
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				DepositChooseAcc.this);

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
	
	private void saveInFile(String request) throws FileNotFoundException{

		try {
			myFile = new File(Environment.getExternalStorageDirectory(), "myfile.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile, true);
			OutputStreamWriter myOutWriter = 
									new OutputStreamWriter(fOut);
//			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			if (myFile.length() > 0) 
				myOutWriter.append(" ; " + AccountNum.msg);
			else 
				myOutWriter.append(AccountNum.msg);
			
			myOutWriter.close();
			fOut.close();
			Toast.makeText(getBaseContext(),
					"Done writing SD 'myfile.txt'",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}



	int responseCode = 0;
	String response = null;


	public class fireMan extends AsyncTask<Void , Void, Void> {

		private Exception e = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(DepositChooseAcc.this);
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

			}
			else if (response.equals("20")) {
				progress.cancel();
				wrongAccNum();

//											accNumEditText.setText("");
//
//											accNumEditText.requestFocus();
			} else {
				stuff = response.split(",");
//										StringTokenizer token = new StringTokenizer(ratty, ",");
//										progress = new ProgressDialog(DepositChooseAcc.this);
//										progress.setTitle("Sending");
//										progress.setMessage("Please wait...");
//										progress.show();
				makelist();
				progress.cancel();

			}

		}
	}


//	public void sendRequest(final String message){
//		thread = new Thread(new Runnable(){
//			    @Override
//			    public void run() {
//
//			        	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//						//request.addProperty("Input",enterText.getText().toString());
//						PropertyInfo pi = new PropertyInfo();
//						pi.setNamespace(NAMESPACE);
//						pi.setType(PropertyInfo.STRING_CLASS);
//						//pi.setName("name");
//						pi.setName("Input");
//						//pi.setValue(editText1.getText().toString().trim());
//						pi.setValue(message);
//						request.addProperty(pi);
//
//						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
//						envelope.setOutputSoapObject(request);
//
//						HttpTransportSE androidHttpTransport = new HttpTransportSE(UrlClass.url, 10000);
//
//						try{
//								androidHttpTransport.call(SOAP_ACTION, envelope);
//
//								sp = (SoapPrimitive) envelope.getResponse();
//
//								runOnUiThread(new Runnable() {
//
//									@Override
//									public void run() {
//										// TODO Auto-generated method stub
//
//										String ratty = sp.toString();
//										if (ratty.equals("20")){
//											progress.cancel();
//											wrongAccNum();
//
////											accNumEditText.setText("");
////
////											accNumEditText.requestFocus();
//										}else{
//										stuff  = ratty.split(",");
////										StringTokenizer token = new StringTokenizer(ratty, ",");
////										progress = new ProgressDialog(DepositChooseAcc.this);
////										progress.setTitle("Sending");
////										progress.setMessage("Please wait...");
////										progress.show();
//										makelist();
//										progress.cancel();
//										}
//
//									}
//								});
//
//						}catch (SocketTimeoutException e) {
//							progress.cancel();
//							noConnectivity();
//						}catch (IOException e) {
//
//		                    e.printStackTrace();
//
//						}
//
//						catch (XmlPullParserException e) {
//			            e.printStackTrace();
//						}
//			    }
//		});
//	}


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
				DepositChooseAcc.this);

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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	DepositChooseAcc.this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}

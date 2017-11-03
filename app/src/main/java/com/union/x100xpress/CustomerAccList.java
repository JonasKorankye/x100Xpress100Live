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

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import myUrl.UrlClass;


public class CustomerAccList extends AppCompatActivity{

	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL = /*"http://192.168.1.26:8680/web_service/HelloWorldPortType";  */
//			/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*"http://197.251.247.4/web_service/HelloWorldPortType";*/
//			/*"http://192.168.180.23:8680/web_service/HelloWorldPortType";*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/ /*fnsl public*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://197.221.80.184:8680/web_service/HelloWorldPortType";  /*bestpoint public*/
////			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;
//	String ratty = "";
	Thread thread;
	String custNum;
	ListView acc_list;
	EditText enterCustNum;
	static String stuff [] = {"", "", ""};
	//	static String accNo;
	static String account;
	static String accNum;
	static String accName;
	Button okButton;
	File myFile;
	ProgressDialog progress;
	String choice = null;
	String msg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cust_acc_list);
		setTitle("Customer's Accounts");

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		enterCustNum = (EditText) findViewById(R.id.cust_number);
		okButton = (Button) findViewById(R.id.okBut);

		acc_list = (ListView) findViewById(R.id.list);

		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				custNum = enterCustNum.getText().toString();
				if /*(custNum.length() !=0 && custNum.length() != 7){
					enterCustNum.setError("Customer number must be 7 digits");
				}else if*/ (custNum.length() == 0) {
					enterCustNum.setError("Please enter customer number");
				} else {
//					progress = new ProgressDialog(CustomerAccList.this);
//					progress.setTitle("Sending");
//					progress.setMessage("Please wait...");
//					progress.show();
					AccountNum.choice = "AA";
					msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + custNum;

//					Handler h = new Handler();
//
//					h.post(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							runOnUiThread(new Runnable() {
//
//								@Override
//								public void run() {
					new fireMan().execute();
//									thread.start();
					okButton.setEnabled(false);

					TextWatcher textWatcher = new TextWatcher() {

						@Override
						public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

						}

						@Override
						public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

						}

						@Override
						public void afterTextChanged(Editable editable) {
							//here, after we introduced something in the EditText we get the string from it
							//											String answerString = enterCustNum.getText().toString();

							//and now we make a Toast
							//modify "yourActivity.this" with your activity name .this
							//									       Toast.makeText(Withdraw.this,"The string from EditText is: "+answerString,0).show();
							okButton.setEnabled(true);

						}
					};

					//third, we must add the textWatcher to our EditText
					enterCustNum.addTextChangedListener(textWatcher);
				}
			}

		});

	}

	public void makelist(){
		acc_list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stuff));	
	}
	
	
	
	public void wrongAccNum(){
		AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
				CustomerAccList.this);

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



	public void errorConnecting(){
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				CustomerAccList.this);

		// Setting Dialog Title
		alertDialog3.setTitle("Error!");
		alertDialog3.setMessage("Please try again");		 

		alertDialog3.setPositiveButton("TRY AGAIN",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.cancel();
				okButton.setEnabled(true);

			}
		});
		// Setting Negative "NO" Btn
		/*alertDialog3.setNegativeButton("SAVE REQUEST",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				//		                
				dialog.cancel();
				try {

					saveInFile(AccountNum.msg);
					Toast.makeText(CustomerAccList.this, "Request has been saved in file successfully", Toast.LENGTH_LONG).show();
					okButton.setEnabled(true);
					enterCustNum.setText("");
					enterCustNum.requestFocus();
					acc_list.setVisibility(View.VISIBLE);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Toast.makeText(CustomerAccList.this, "Unsuccessful", Toast.LENGTH_LONG).show();
				}


			}
		});*/

		// Showing Alert Dialog
		alertDialog3.show();


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
				CustomerAccList.this);

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
					"Done writing SD 'mysdfile.txt'",
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
			progress = new ProgressDialog(CustomerAccList.this);
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
			if (response.equals("20")){
				progress.cancel();
				wrongAccNum();
			}else{
				progress.cancel();
				stuff  = response.split(",");
				makelist();
				okButton.setEnabled(false);
				enterCustNum.setEnabled(false);
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
//				HttpTransportSE androidHttpTransport = new HttpTransportSE(UrlClass.url, 10000);
//
//				try{
//					androidHttpTransport.call(SOAP_ACTION, envelope);
//
//					sp = (SoapPrimitive) envelope.getResponse();
//
//					runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							ratty = sp.toString();
//							if (ratty.equals("20")){
//								progress.cancel();
//								wrongAccNum();
//							}else{
//								progress.cancel();
//								stuff  = ratty.split(",");
//								makelist();
//								okButton.setEnabled(false);
//								enterCustNum.setEnabled(false);
//							}
//
//						}
//					});
//
//				}catch (SocketTimeoutException e) {
//					noConnectivity();
//					//
//				}catch (IOException e) {
//
//					e.printStackTrace();
//
//				}
//
//				catch (XmlPullParserException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	public void noConnectivity(){

		try{																	

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					progress.cancel();
					errorConnecting();
					//					Toast.makeText(Deposit.this, "An error occurred", Toast.LENGTH_LONG).show();

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
	    	CustomerAccList.this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		SearchByName.searchAccountNum = "";
		DepositChooseAcc.account = "";
//		SearchByName.searchAccountNum = null;
		finish();
		super.onBackPressed();
	}
}

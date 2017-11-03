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
import java.util.Calendar;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import myUrl.UrlClass;


public class NonCust_Loan_Origination extends AppCompatActivity implements OnItemSelectedListener{
	
	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL = /*"http://192.168.1.26:8680/web_service/HelloWorldPortType";  */
//	/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/   /*"http://197.251.247.4/web_service/HelloWorldPortType";*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;

	Thread thread;
	String custNum;
	ListView acc_list;
	EditText enterCustNum;
	static String account;
	static String accNum;
	//	static String accName;
	static String choice;
	static String msg = "";
		
	EditText firstNameEditText;
	EditText midNameEditText;
	EditText lastNameEditText;
	EditText amountEditText;
	EditText phoneNumEditText;
	EditText commentsEditText;
	
	String fname = "";
	String lname = "";
	String midname = "";
	String phoneNum = "";
//	String fname = "";
//	String lname = "";
	
	File myPrintFile = null;
	String mydate = null;
	String message = null;
	
	ProgressDialog progress;
	Button getAccNameButton;
	Button okButton;
	File myFile;
	String amt;
	String narration;
	String accountNum;
	String sectorChoice = "";
	String accountName = "";
	String ratty = "";
	String accDetails="";
	Spinner spinner1;
	String sector[] = { "PLEASE SELECT SECTOR ", "MISCELLANEOUS", "AGRICULTURE AND FORESTRY", "MINING AND QUARRING", "MANUFACTURING EXPORT",  "MANUFACTURING HOME MKT", "CONSTRUCTION", "ELECTRICITY GAS & WATER", "COMMERCE & FINANCE", "TRANSPORT STORAGE & COMMUNICATION", "SERVICES"  };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noncust_loan_origination);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
		midNameEditText = (EditText) findViewById(R.id.middleNameEditText);
		lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		phoneNumEditText = (EditText) findViewById(R.id.phoneNumEditText);
		commentsEditText = (EditText) findViewById(R.id.commentsEditText);
		
		okButton = (Button) findViewById(R.id.okButton);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sector);

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner1.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(this);
		
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				amt =  amountEditText.getText().toString();
				fname = firstNameEditText.getText().toString();
				midname =  midNameEditText.getText().toString();
				lname = lastNameEditText.getText().toString();
				phoneNum =  phoneNumEditText.getText().toString();
//				lname = lastNameEditText.getText().toString();
				
				if (fname.length() == 0){
					firstNameEditText.setError("Please enter first name");
				}else if (midname.length() == 0 ){
					midNameEditText.setError("Please enter middle name");
				}else if (lname.length() == 0 ){
					lastNameEditText.setError("Please enter last name");
				}else if (phoneNum.length() == 0 ){
					phoneNumEditText.setError("Please enter phone number");
				}else if (phoneNum.length() != 10 ){
					phoneNumEditText.setError("Phone number must be 10 digits");
				}else if (amt.length() == 0 ){
					amountEditText.setError("Please enter loan amount");
				}else if (sectorChoice =="00"){
					Toast.makeText(NonCust_Loan_Origination.this, "Choose a sector", Toast.LENGTH_LONG).show();
				}else{
					AccountNum.choice = "NL";
					msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + fname + "," + midname + "," + lname + " " + sectorChoice + " " + phoneNum + " " + amt;
					new fireMan().execute();
//					progress = new ProgressDialog(NonCust_Loan_Origination.this);
//					progress.setTitle("Sending");
//					progress.setMessage("Please wait...");
//					progress.show();
//					sendRequest(AccountNum.msg);
//					thread.start();
				}
				
			}
		});
		
		
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		int arrayPosition = spinner1.getSelectedItemPosition();
		switch (arrayPosition){
		case 0:
			sectorChoice = "00";
			break;
		case 1:
			sectorChoice = "01";
			break;
		case 2:
			sectorChoice = "10";
			break;
		case 3:
			sectorChoice = "20";
			break;
		case 4:
			sectorChoice = "30";
			break;
		case 5:
			sectorChoice = "40";
			break;
		case 6:
			sectorChoice = "50";
			break;
		case 7:
			sectorChoice = "60";
			break;
		case 8:
			sectorChoice = "70";
			break;
		case 9:
			sectorChoice = "80";
			break;
		case 10:
			sectorChoice = "90";
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	public void errorConnecting() {
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				NonCust_Loan_Origination.this);

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
	
	public void openPrintApp(){
		try {
			mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			
//			int balLeft = Integer.parseInt(balance) - Integer.parseInt(amt);
//			fname + "," + midname + "," + lname + " " + sectorChoice + " " + phoneNum + " " + amt;
			message = "************************"
					 +"  MobiTransact Receipt  "
			         +"************************"
					 + mydate + "\n"
					 +"     Loan Origination    \n" 
					 +"First name: " + fname + "\n"
					 +"Middle Name: " + midname + "\n"
					 +"Last name: " + lname + "\n"
					 +"Sector: " + sectorChoice + "\n"
					 +"Contact: " + phoneNum + "\n"
					 +"Amount: " +"GHS" + amt + ".00" + "\n"
					 +"        Thank You       "
					 +"************************\n";
			saveMsg(message);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent i;
		PackageManager manager = getPackageManager();
		try {
			i = manager.getLaunchIntentForPackage("com.bluebamboo.p25demo");
			if (i == null){
				throw new PackageManager.NameNotFoundException();
			}else{
				
//				i.putExtra(AccountNum.msg, true);
				i.addCategory(Intent.CATEGORY_LAUNCHER);
				startActivity(i);
			}
		} catch (PackageManager.NameNotFoundException e) {

		}
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
	
	private void saveMsg(String number) throws FileNotFoundException{

		try {
			myPrintFile = new File(Environment.getExternalStorageDirectory(), "printfile.txt");
			myPrintFile.createNewFile();
			//			userFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myPrintFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			if (myPrintFile.length() > 0) 
				myOutWriter.append("\n" + message);
			else 
				myOutWriter.append(message);
//			Toast.makeText(Withdraw.this, "File saved successfully",Toast.LENGTH_LONG).show();
			myOutWriter.close();
			fOut.close();
			//			FileOutputStream fos = new FileOutputStream(myInternalFile);
			//			fos.write(textMessage.toString().getBytes());
			//			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			Toast.makeText(NonCust_Loan_Origination.this, e.getMessage(),Toast.LENGTH_LONG).show();
		}

	}



	int responseCode = 0;
	String response = null;

	public class fireMan extends AsyncTask<Void, Void, Void> {

		private Exception e = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(NonCust_Loan_Origination.this);
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

			progress.cancel();
			if (response==null){
				System.out.println("RESPONSE EMPTY");
			}
			else {
//				progress.cancel();
				Toast.makeText(NonCust_Loan_Origination.this, response, Toast.LENGTH_LONG).show();

			}

		}

	}

	
	public void sendRequest(final String message){
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

				HttpTransportSE androidHttpTransport = new HttpTransportSE(UrlClass.url, 10000);

				try{																	
					androidHttpTransport.call(SOAP_ACTION, envelope);

					sp = (SoapPrimitive) envelope.getResponse();

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							ratty = sp.toString();
							progress.cancel();
							Toast.makeText(NonCust_Loan_Origination.this, ratty, Toast.LENGTH_LONG).show();																										

						}
					});

				}catch (SocketTimeoutException e) {
					progress.cancel();
					noConnectivity();
				}catch (IOException e) {

					e.printStackTrace();

				}

				catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		});
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
				NonCust_Loan_Origination.this);

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
	    	NonCust_Loan_Origination.this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}

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
import java.util.StringTokenizer;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import myUrl.UrlClass;

public class Cust_Loan_Origination extends AppCompatActivity {

	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL =  /*"http://192.168.1.26:8680/web_service/HelloWorldPortType"; */
//			/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/   /*"http://197.251.247.4/web_service/HelloWorldPortType";*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////	"http://192.168.43.32:8680/web_service/HelloWorldPortType";
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
	File myPrintFile = null;

	EditText accNumEditText;
	EditText amountEditText;
	EditText commentsEditText;
	
	TextView accNameTextView;
	TextView accBalTextView;
	
	Button getAccNameButton;
	Button okButton;
	String name;
	String amt;
	String narration;
	String accountNum;
	String accountName = "";
//	String ratty = "";
	String accDetails="";
	
	ProgressDialog progress;
	String mydate = null;
	String message = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cust_loan_origination);
		setTitle("Loan Origination");

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		accNameTextView = (TextView) findViewById(R.id.nameofAccEditText);
		accBalTextView = (TextView) findViewById(R.id.accBal);
		accNumEditText = (EditText) findViewById(R.id.accnumEditText);
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		amountEditText.setEnabled(false);
		commentsEditText = (EditText) findViewById(R.id.commentsEditText);
		commentsEditText.setEnabled(false);
		commentsEditText.setText("x100Xpress loan request");
		okButton = (Button) findViewById(R.id.okButton);
		okButton.setVisibility(View.INVISIBLE);
		
		if(SearchByName.searchAccountNum != ""){
			accNumEditText.setText(SearchByName.searchAccountNum);					
		}else{}

		getAccNameButton = (Button) findViewById(R.id.getAccNameBut);

		getAccNameButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				accountNum = accNumEditText.getText().toString();
				if (accountNum.length() !=0 && accountNum.length() != 13){
					accNumEditText.setError("Account number must be 13 digits");
				}else if (accountNum.length() == 0 ){
					accNumEditText.setError("Please enter account number");
				}else{
					choice = "BE";
					msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + accountNum;
//					Toast.makeText(Cust_Loan_Origination.this, searchAccountNum, Toast.LENGTH_SHORT).show();
//					progress = new ProgressDialog(Cust_Loan_Origination.this);
//					progress.setTitle("Sending");
//					progress.setMessage("Please wait...");
//					progress.show();
					new fireManAccName().execute();
//					thread.start();
					amountEditText.setEnabled(true);
					commentsEditText.setEnabled(true);
					amountEditText.requestFocus();
					okButton.setVisibility(View.VISIBLE);
					getAccNameButton.setVisibility(View.INVISIBLE);
					
					//second, we create the TextWatcher
					TextWatcher textWatcher = new TextWatcher() {
					 
					    @Override
					    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
					 
					    }
					 
					    @Override
					    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
					    	okButton.setEnabled(true);
					    }
					 
					    @Override
					    public void afterTextChanged(Editable editable) {
					       //here, after we introduced something in the EditText we get the string from it
//					       String answerString = accNumEditText.getText().toString();
					 
					       //and now we make a Toast
					       //modify "yourActivity.this" with your activity name .this
//					       Toast.makeText(Withdraw.this,"The string from EditText is: "+answerString,0).show();
					       okButton.setVisibility(View.INVISIBLE);  
					       getAccNameButton.setVisibility(View.VISIBLE);
					       amountEditText.setText("");
					       accNameTextView.setText(" ");
						   accBalTextView.setText("");
						   amountEditText.setEnabled(false);
						   commentsEditText.setEnabled(false);
					    }
					};
					 
					//third, we must add the textWatcher to our EditText
					accNumEditText.addTextChangedListener(textWatcher);
				}
			}
		});

		
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				amt =  amountEditText.getText().toString();
				if (accountNum.length() == 0 ){
					accNumEditText.setError("Please enter account number");
				}else if (amt.length() == 0 ){
					amountEditText.setError("Please enter loan amount");
				}else{
					choice = "LO";
					msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + accountNum + " " + amt;
//					progress = new ProgressDialog(Cust_Loan_Origination.this);
//					progress.setTitle("Sending");
//					progress.setMessage("Please wait...");
//					progress.show();
					new fireMan().execute();
//					thread.start();
				}

			}
		});

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
			Toast.makeText(Cust_Loan_Origination.this, e.getMessage(),Toast.LENGTH_LONG).show();
		}

	}
	
	
	public void openPrintApp(){
		try {
			mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			
//			int balLeft = Integer.parseInt(balance) - Integer.parseInt(amt);
			message = "************************"
					 +"  MobiTransact Receipt  "
			         +"************************"
					 + mydate + "\n"
					 +"Customer Loan Origination\n" 
					 +"Name: " + name + "\n"
					 +"A/C Num: " + accountNum + "\n"
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
				Cust_Loan_Origination.this);

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
	
	public void successful(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				Cust_Loan_Origination.this);

		// Setting Dialog Title
		alertDialog.setTitle(response);
		alertDialog.setMessage("Would you like to return to the main menu?");		 

		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.dismiss();
				DepositChooseAcc.account = null;
				SearchByName.searchAccountNum = null;
				finish();
				Intent back = new Intent(Cust_Loan_Origination.this, MainMenuList.class);
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
				commentsEditText.setEnabled(false);
				accNumEditText.requestFocus();
			}
		});

		// Showing Alert Dialog
		alertDialog.show();
	}
	
	//dialog for unsuccessful transaction
	public void unsuccessful(){
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				Cust_Loan_Origination.this);

		// Setting Dialog Title
		alertDialog2.setTitle("Error !");
		alertDialog2.setMessage("An error occured");		 

		alertDialog2.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.cancel();
				okButton.setVisibility(View.INVISIBLE);
				getAccNameButton.setVisibility(View.VISIBLE);
				accNumEditText.setText("");
				amountEditText.setText("");
				accNameTextView.setText(" ");
				accBalTextView.setText("");
				amountEditText.setEnabled(false);
				commentsEditText.setEnabled(false);
				accNumEditText.requestFocus();
			}
		});
		// Showing Alert Dialog
		alertDialog2.show();
	}
	
	//dialog for wrong account number
			public void wrongAccNum(){
				AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
						Cust_Loan_Origination.this);

				// Setting Dialog Title
				alertDialog5.setTitle("Error !");
				alertDialog5.setMessage("Wrong Account Number");		

				alertDialog5.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog
						dialog.cancel();
						okButton.setVisibility(View.INVISIBLE);
						getAccNameButton.setVisibility(View.VISIBLE);
						accNumEditText.setText("");
						amountEditText.setText("");
						accNameTextView.setText("");
						accBalTextView.setText("");
						amountEditText.setEnabled(false);
//						narrationEditText.setEnabled(false);
						accNumEditText.requestFocus();
					}
				});
				// Showing Alert Dialog
				alertDialog5.show();
			}

	public void errorConnecting() {
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				Cust_Loan_Origination.this);

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
			File myFile = new File(Environment.getExternalStorageDirectory(), "myfile.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile, true);
			OutputStreamWriter myOutWriter = 
									new OutputStreamWriter(fOut);
//			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			if (myFile.length() > 0) 
				myOutWriter.append(" , " + AccountNum.msg);
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


	public class fireManAccName extends AsyncTask<Void , Void, Void> {

		private Exception e = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(Cust_Loan_Origination.this);
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

			if (accDetails.equals("error1")){
				Toast.makeText(Cust_Loan_Origination.this, "Wrong account number", Toast.LENGTH_LONG).show();
				okButton.setVisibility(View.INVISIBLE);
				getAccNameButton.setVisibility(View.VISIBLE);
				accNumEditText.setText("");
				amountEditText.setText("");
				accNameTextView.setText(" ");
				accBalTextView.setText("");
				amountEditText.setEnabled(false);
				commentsEditText.setEnabled(false);
				accNumEditText.requestFocus();
			}else if (accDetails.equals("20")){
				progress.cancel();
				wrongAccNum();
				okButton.setVisibility(View.INVISIBLE);
				getAccNameButton.setVisibility(View.VISIBLE);
				accNumEditText.setText("");
				amountEditText.setText("");
				accNameTextView.setText(" ");
				accBalTextView.setText("");
				amountEditText.setEnabled(false);
//								narrationEditText.setEnabled(false);
				accNumEditText.requestFocus();
			}else {
				StringTokenizer token = new StringTokenizer(accDetails, ",");
				name = token.nextToken().trim();
				String status = token.nextToken().trim();
				String balance = token.nextToken().trim();
				accNameTextView.setText(" "  + name);
				accBalTextView.setText(balance + ".00");
				progress.cancel();
				getAccNameButton.setVisibility(View.INVISIBLE);
				okButton.setVisibility(View.VISIBLE);
				amountEditText.setEnabled(true);
				commentsEditText.setEnabled(false);
				amountEditText.requestFocus();

		}

		}

	}


	int responseCode2 = 0;
	StringBuffer response2 = new StringBuffer();


	public class fireMan extends AsyncTask<Void , Void, Void> {

		private Exception e = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(Cust_Loan_Origination.this);
			progress.setCancelable(false);
			progress.setTitle("Please wait");
			progress.setMessage("Sending request...");
			progress.show();
		}

		@Override
		protected Void doInBackground(Void ... params) {



//            String url = choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword;
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
			}



			try {
				responseCode2 = con.getResponseCode();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("\nSending 'POST' request to URL : " + UrlClass.url);
			System.out.println("Post parameters : " + msg);
			System.out.println("Response Code : " + responseCode2);

			BufferedReader in = null;
			try {
				String inputLine;
//                StringBuffer response = new StringBuffer();
				in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				while ((inputLine = in.readLine()) != null) {
					response2.append(inputLine);

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}


			//print result
			System.out.println(response2.toString());

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			if (response==null){
				System.out.println("RESPONSE EMPTY");
			}
			else if(response2.toString().equals("Transaction successful")){
				progress.cancel();
				successful();
//								openPrintApp();
			}else if(response2.toString().equals("Transaction unsuccessful")){
				progress.cancel();
				unsuccessful();

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
//							if(ratty.equals("Transaction successful")){
//								progress.cancel();
//								successful();
////								openPrintApp();
//							}else if(ratty.equals("Transaction unsuccessful")){
//								progress.cancel();
//								unsuccessful();
//
//							}
//						}
//					});
//
//				}catch (SocketTimeoutException e) {
//					progress.cancel();
//					noConnectivity();
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
//
//	public void requestAccName(final String message){
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
//							accDetails = sp.toString();
//							if (accDetails.equals("error1")){
//								Toast.makeText(Cust_Loan_Origination.this, "Wrong account number", Toast.LENGTH_LONG).show();
//								okButton.setVisibility(View.INVISIBLE);
//								getAccNameButton.setVisibility(View.VISIBLE);
//								accNumEditText.setText("");
//								amountEditText.setText("");
//								accNameTextView.setText(" ");
//								accBalTextView.setText("");
//								amountEditText.setEnabled(false);
//								commentsEditText.setEnabled(false);
//								accNumEditText.requestFocus();
//							}else if (accDetails.equals("20")){
//								progress.cancel();
//								wrongAccNum();
//								okButton.setVisibility(View.INVISIBLE);
//								getAccNameButton.setVisibility(View.VISIBLE);
//								accNumEditText.setText("");
//								amountEditText.setText("");
//								accNameTextView.setText(" ");
//								accBalTextView.setText("");
//								amountEditText.setEnabled(false);
////								narrationEditText.setEnabled(false);
//								accNumEditText.requestFocus();
//							}else {
//								StringTokenizer token = new StringTokenizer(accDetails, ",");
//								name = token.nextToken().trim();
//								String status = token.nextToken().trim();
//								String balance = token.nextToken().trim();
//								accNameTextView.setText(" "  + name);
//								accBalTextView.setText(balance + ".00");
//								progress.cancel();
//								getAccNameButton.setVisibility(View.INVISIBLE);
//								okButton.setVisibility(View.VISIBLE);
//								amountEditText.setEnabled(true);
//								commentsEditText.setEnabled(false);
//								amountEditText.requestFocus();
//							}
//						}
//					});
//
//				}catch (SocketTimeoutException e) {
//					progress.cancel();
//					noConnectivity();
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
	    	Cust_Loan_Origination.this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		DepositChooseAcc.account = "";
		SearchByName.searchAccountNum = "";
		super.onBackPressed();

	}
}

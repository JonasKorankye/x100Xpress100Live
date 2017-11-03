package com.union.x100xpress;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import org.ksoap2.serialization.SoapPrimitive;

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
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import myUrl.UrlClass;

//import com.example.mobitransact.Deposit.CustomTask;

public class Deposit extends AppCompatActivity {

	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL =
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://197.221.80.184:8680/web_service/HelloWorldPortType";  /*bestpoint public*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
//	/*"http://192.168.1.26:8680/web_service/HelloWorldPortType"; */
//	/*"http://192.168.2.17:9999/web_service/HelloWorldPortType";*/ /*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*"http://197.251.247.4/web_service/HelloWorldPortType";*/
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;

	ProgressDialog progress;
	ProgressDialog progress1;
	Thread thread;
	String custNum;
	ListView acc_list;
	EditText enterCustNum;
	static String account;
	static String accNum;
	//	static String accName;
	static String choice;
	static String msg = "";

	Double amtDouble = null;
	String formattedAmt = null;

	TextView accNameTextView;
	TextView accBalTextView;
	EditText accNumEditText;
	EditText amountEditText;
	EditText narrationEditText;

	Button getAccNameButton;
	Button okButton;
	String amt;
	String narration;
	String accountNum;
	String accountName = "";
//	String ratty = "";
//	String accDetails = "";

	String name = null;
	String status = null;
	String balance = null;
	String mydate = null;
	String message = null;
	File myPrintFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("Deposit");
		setContentView(R.layout.deposit);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		accNumEditText = (EditText) findViewById(R.id.accnumEditText);
		accNameTextView = (TextView) findViewById(R.id.nameofAccEditText);
		accBalTextView = (TextView) findViewById(R.id.accBal);
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		amountEditText.setEnabled(false);
		narrationEditText = (EditText) findViewById(R.id.narrationEditText);
		narrationEditText.setText("x100Xpress deposit");
//		narrationEditText.setEnabled(false);
		okButton = (Button) findViewById(R.id.okButton);
		getAccNameButton = (Button) findViewById(R.id.okBut);

		if(DepositChooseAcc.account != ""){
			System.out.println("accountnum is " + DepositChooseAcc.account);
			//			System.out.println("account variable is not empty");

			accountNum = DepositChooseAcc.account.substring(DepositChooseAcc.account.lastIndexOf(' ') + 1);
			accountName =  DepositChooseAcc.account.substring(0, DepositChooseAcc.account.lastIndexOf(' '));
			accNumEditText.setText(accountNum);
			accNameTextView.setText(accountName);
			amountEditText.setEnabled(true);
			getAccNameButton.setVisibility(View.INVISIBLE);	
			AccountNum.choice = "BE";
			msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + accountNum;
//			progress = new ProgressDialog(Deposit.this);
//			progress.setTitle("Sending");
//			progress.setMessage("Please wait...");
//			progress.show();
			new fireManAccName().execute();
//			okButton.setVisibility(View.VISIBLE);
		}else if(SearchByName.searchAccountNum != ""){
			accNumEditText.setText(SearchByName.searchAccountNum);				
		}else{}

		okButton.setVisibility(View.INVISIBLE);

		getAccNameButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				accountNum = accNumEditText.getText().toString();
				if (accountNum.length() == 0 ){
					accNumEditText.setError("Please enter account number");
				}else{
					AccountNum.choice = "BE";
					msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + accountNum;
//					progress = new ProgressDialog(Deposit.this);
//					progress.setTitle("Sending");
//					progress.setMessage("Please wait...");
//					progress.show();
					new fireManAccName().execute();

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
							//								String answerString = accNumEditText.getText().toString();

							//and now we make a Toast
							//modify "yourActivity.this" with your activity name .this
							//						       Toast.makeText(Withdraw.this,"The string from EditText is: "+answerString,0).show();
							okButton.setVisibility(View.INVISIBLE);  
							getAccNameButton.setVisibility(View.VISIBLE);
							amountEditText.setText("");
							accNameTextView.setText("");
							accBalTextView.setText("");
							amountEditText.setEnabled(false);
//							narrationEditText.setEnabled(false);
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
				narration = narrationEditText.getText().toString();
				if (accountNum.length() == 0 ){
					accNumEditText.setError("Please enter account number");
				}else if (amt.length() == 0 ){
					amountEditText.setError("Please enter amount to deposit");
				}else{
					AccountNum.choice = "DE";
					msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + accountNum + " " + amt + " " + narration.replaceAll("\\s+","#");
//					progress = new ProgressDialog(Deposit.this);
//					progress.setTitle("Sending");
//					progress.setMessage("Please wait...");
//					progress.show();
					new fireMan().execute();


				}
			}
		});


	}


	//	public void formatAmounts(String number){
	//		
	//		amtDouble = Double.parseDouble(number);
	//		DecimalFormat formatter = new DecimalFormat("#,###.00");
	//		
	//		formatter.format(amtDouble);
	//		
	//	}

	public String formatAmounts(String theNumber){

		amtDouble = Double.parseDouble(theNumber);
		DecimalFormat formatter = new DecimalFormat("#,###.00");

		formattedAmt = formatter.format(amtDouble);
		return formattedAmt;
	}

	public void openPrintApp(){
		try {
			mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

			//			double amtDouble = Double.parseDouble(amt);
			//			DecimalFormat formatter = new DecimalFormat("#,###.00");
			//			
			//			formattedAmount = formatter.format(amount);

			int balLeft = Integer.parseInt(balance) + Integer.parseInt(amt);
			String stringOfBalLeft = String.valueOf(balLeft);

			message = "************************"
					+"  MobiTransact Receipt  "
					+"************************"
					+ mydate + "\n"
					+"       Deposit       \n" 
					+"Name: " + name + "\n"
					+"A/C Num: " + accountNum + "\n"
					+"Amount: " +"GHS" + formatAmounts(amt) + "\n"
					+"Balance: " +"GHS"  + formatAmounts(stringOfBalLeft) + "\n"
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
			Toast.makeText(Deposit.this, e.getMessage(),Toast.LENGTH_LONG).show();
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
				Deposit.this);

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
				Deposit.this);

		// Setting Dialog Title
		alertDialog.setTitle(response2);
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
				narrationEditText.setEnabled(true);
				accNumEditText.requestFocus();
			}
		});

		// Showing Alert Dialog
		alertDialog.show();
	}

	//dialog for wrong account number
	public void wrongAccNum(){
		AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
				Deposit.this);

		// Setting Dialog Title
		alertDialog5.setTitle("Error !");
		alertDialog5.setMessage("Customer does not exist");		

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
				narrationEditText.setEnabled(true);
				accNumEditText.requestFocus();
			}
		});
		// Showing Alert Dialog
		alertDialog5.show();
	}

	//dialog for unsuccessful transaction
	public void unsuccessful(){
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				Deposit.this);

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
				accNameTextView.setText("");
				accBalTextView.setText("");
				amountEditText.setEnabled(false);
				narrationEditText.setEnabled(true);
				accNumEditText.requestFocus();
			}
		});
		// Showing Alert Dialog
		alertDialog2.show();
	}

	public void errorConnecting() {
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				Deposit.this);

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


	public class fireManAccName extends AsyncTask<Void , Void, Void> {

		private Exception e = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(Deposit.this);
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
//			accDetails = sp.toString();
			if (response==null){
				System.out.println("RESPONSE EMPTY");
			}
			else if (response.equals("error1")){
				progress.cancel();
				//								Toast.makeText(Deposit.this, "Wrong account number", Toast.LENGTH_LONG).show();
				okButton.setVisibility(View.INVISIBLE);
				getAccNameButton.setVisibility(View.VISIBLE);
				accNumEditText.setText("");
				amountEditText.setText("");
				accNameTextView.setText(" ");
				accBalTextView.setText("");
				amountEditText.setEnabled(false);
				narrationEditText.setEnabled(true);
				accNumEditText.requestFocus();
			}else if (response.equals("20")){
				progress.cancel();
				wrongAccNum();
				okButton.setVisibility(View.INVISIBLE);
				getAccNameButton.setVisibility(View.VISIBLE);
				accNumEditText.setText("");
				amountEditText.setText("");
				accNameTextView.setText(" ");
				accBalTextView.setText("");
				amountEditText.setEnabled(false);
				narrationEditText.setEnabled(true);
				accNumEditText.requestFocus();
			}else {

				StringTokenizer token = new StringTokenizer(response, ",");
				name = token.nextToken().trim();
				status = token.nextToken().trim();
				balance = token.nextToken().trim();
				//										accountName =  ratty.substring(0, ratty.lastIndexOf(' ') + 1);
				accNameTextView.setText(" "  + name);
				accBalTextView.setText(formatAmounts(balance));

				getAccNameButton.setVisibility(View.INVISIBLE);
				okButton.setVisibility(View.VISIBLE);
				amountEditText.setEnabled(true);
				narrationEditText.setEnabled(true);
				amountEditText.requestFocus();
				progress.cancel();
			}

		}

	}






	int responseCode2 = 0;
	String response2 = null;


	public class fireMan extends AsyncTask<Void , Void, Void> {

		private Exception e = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(Deposit.this);
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
				responseCode2 = con.getResponseCode();
				System.out.println("ResponseCode2 is " + responseCode2);
			} catch (IOException e) {
				e.printStackTrace();
				progress.cancel();
//                noConnectivity();
			}
			System.out.println("\nSending 'POST' request to URL : " + UrlClass.url);
			System.out.println("Post parameters : " + msg);
//            System.out.println("Response Code : " + responseCode);

			if (responseCode2 == 200) {

				BufferedReader in = null;
				try {
					String inputLine = null;
					in = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
						response2 = inputLine;

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
				System.out.println(response2);

			} else if (responseCode2 == 404) {
				progress.cancel();
				noConnectivity();
			} else if (responseCode2 == 500) {
				progress.cancel();
				noConnectivity();
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

//			progress.cancel();
			if (response2==null){
				System.out.println("RESPONSE EMPTY");
			}
			else if(response2.equals("Transaction successful")){
				progress.cancel();
				successful();
				//								openPrintApp();
			}else if(response2.equals("Transaction unsuccessful1")){
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
//								//								openPrintApp();
//							}else if(ratty.equals("Transaction unsuccessful1")){
//								progress.cancel();
//								unsuccessful();
//
//							}
//
//						}
//					});
//
//				}
//				catch (SocketTimeoutException e) {
//					noConnectivity();
//				}
//				catch (IOException e) {
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
//								progress.cancel();
//								//								Toast.makeText(Deposit.this, "Wrong account number", Toast.LENGTH_LONG).show();
//								okButton.setVisibility(View.INVISIBLE);
//								getAccNameButton.setVisibility(View.VISIBLE);
//								accNumEditText.setText("");
//								amountEditText.setText("");
//								accNameTextView.setText(" ");
//								accBalTextView.setText("");
//								amountEditText.setEnabled(false);
//								narrationEditText.setEnabled(false);
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
//								narrationEditText.setEnabled(false);
//								accNumEditText.requestFocus();
//							}else {
//
//								StringTokenizer token = new StringTokenizer(accDetails, ",");
//								name = token.nextToken().trim();
//								status = token.nextToken().trim();
//								balance = token.nextToken().trim();
//								//										accountName =  ratty.substring(0, ratty.lastIndexOf(' ') + 1);
//								accNameTextView.setText(" "  + name);
//								accBalTextView.setText(formatAmounts(balance));
//
//								getAccNameButton.setVisibility(View.INVISIBLE);
//								okButton.setVisibility(View.VISIBLE);
//								amountEditText.setEnabled(true);
//								narrationEditText.setEnabled(false);
//								amountEditText.requestFocus();
//								progress.cancel();
//							}
//
//						}
//					});
//
//				}catch (SocketTimeoutException e) {
//					progress.cancel();
//					noConnectivity();
//					//							new CustomTask().execute();
//
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


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		DepositChooseAcc.account = "";
		SearchByName.searchAccountNum = "";
		finish();
		super.onBackPressed();

	}

	/*public class CustomTask extends AsyncTask<Void, Void, Void> {

	    protected Void doInBackground(Void... param) {
	        //Do some work
	        return null;
	    }

	    protected void onPostExecute(Void param) {
	        //Print Toast or open dialog
	    	Toast.makeText(Deposit.this, "An error occurred", Toast.LENGTH_LONG).show();
//	    	errorConnecting();
	    }
	}*/

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
			Deposit.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

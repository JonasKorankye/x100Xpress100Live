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
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import myUrl.UrlClass;

//import com.bluebamboo.p25demo.bluetooth.BlueToothServicesP25.ReceiveThread;

public class AccountNum extends AppCompatActivity{

	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL =  /*"http://192.168.246.1:8680/web_service/HelloWorldPortType";*/
//			/*"http://192.168.1.26:8680/web_service/HelloWorldPortType";*/
//	/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/    /*"http://197.251.247.4/web_service/HelloWorldPortType";*/
////			"http://197.221.81.162:8680/web_service/HelloWorldPortType";
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////	"http://197.221.80.184:8680/web_service/HelloWorldPortType";  /*bestpoint public*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
//			/*"http://192.168.10.80:8680/web_service/HelloWorldPortType";*/  /*fnsl apn*/
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;
	Thread thread;
//	String ratty = "";
	ProgressDialog progress;
	static String choice, accNum;
	static String msg = "";
	EditText accNumEditText;
	EditText nameEditText;
	EditText statusEditText;
	EditText bal_clearedEditText;
	EditText bal_unclearedEditText;
	LinearLayout ll;
	Intent intent;
	Button okButton;

	String mydate = null;
	String message = null;
	String statusTokenDesc = null;

	File myFile = null;
	File myPrintFile = null;

	Dialog dialogProgress;
	String nameToken = null;
	String statusToken = null;
	String balanceClearedToken = null;
	String balanceUnclearedToken = null;

	TextView responseTV;
	LinearLayout accName, status, bal_Cleared, bal_Uncleared;
	//	private FileOperation fe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("Balance Enquiry");
		setContentView(R.layout.accountnum_dialog);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		statusEditText = (EditText) findViewById(R.id.statusEditText);
		bal_clearedEditText = (EditText) findViewById(R.id.bal_cleared_EditText);
		bal_unclearedEditText = (EditText) findViewById(R.id.bal_uncleared_EditText);
		okButton = (Button) findViewById(R.id.checkBalBut);
		accNumEditText = (EditText) findViewById(R.id.acc_editText);
		accName = (LinearLayout) findViewById(R.id.name);
		status = (LinearLayout) findViewById(R.id.status);
		bal_Cleared = (LinearLayout) findViewById(R.id.bal_cleared);
		bal_Uncleared = (LinearLayout) findViewById(R.id.bal_uncleared);
		
		if(SearchByName.searchAccountNum != ""){
			accNumEditText.setText(SearchByName.searchAccountNum);					
		}else{}
		
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				accNum = accNumEditText.getText().toString();
				if (accNum.length() == 0 ){
					accNumEditText.setError("Please enter account number");
				}else{
					okButton.setEnabled(false);
//					progress = new ProgressDialog(AccountNum.this);
//					progress.setTitle("Sending");
//					progress.setMessage("Please wait...");
//					progress.show();
					choice = "BE";
					msg = choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + accNum;

					new fireMan().execute();
//					thread.start();
					//					okButton.setVisibility(View.INVISIBLE);
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
//							String answerString = accNumEditText.getText().toString();
							
							//and now we make a Toast
							//modify "yourActivity.this" with your activity name .this
							//					       Toast.makeText(Withdraw.this,"The string from EditText is: "+answerString,0).show();
							//					       okButton.setVisibility(View.VISIBLE);  
							accNumEditText.requestFocus();
							
							
						}
					};

					//third, we must add the textWatcher to our EditText
					accNumEditText.addTextChangedListener(textWatcher);


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
				AccountNum.this);

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




	
	
	public void wrongAccNum(){
		AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
				AccountNum.this);

		// Setting Dialog Title
		alertDialog5.setTitle("Error !");
		alertDialog5.setMessage("Wrong Account Number");		

		alertDialog5.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.cancel();
				accNumEditText.setText("");
				accNumEditText.requestFocus();
			}
		});
		// Showing Alert Dialog
		alertDialog5.show();
	}


	int responseCode = 0;
	String response = null;


	public class fireMan extends AsyncTask<Void , Void, Void> {

		private Exception e = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(AccountNum.this);
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
				Toast.makeText(AccountNum.this, "Wrong account number", Toast.LENGTH_LONG).show();
			}else if (response.equals("20")){
				progress.cancel();
				wrongAccNum();

//								accNumEditText.setText("");
//
//								accNumEditText.requestFocus();
			}else{
				StringTokenizer token = new StringTokenizer(response, ",");
				nameToken = token.nextToken().trim();
				statusToken = token.nextToken().trim();
				balanceClearedToken = token.nextToken().trim();
				balanceUnclearedToken = token.nextToken().trim();
				progress.cancel();
				SearchByName.searchAccountNum = null;

				accName.setVisibility(View.VISIBLE);
				nameEditText.setText(nameToken);
				nameEditText.setEnabled(false);
				status.setVisibility(View.VISIBLE);
				if (statusToken.equals("N")){
					statusTokenDesc = "Normal";
				}else if(statusToken.equals("DO")){
					statusTokenDesc = "Dormant";
				}else if(statusToken.equals("CR")){
					statusTokenDesc = "Locked from cr transactions";
				}else if(statusToken.equals("DR")){
					statusTokenDesc = "Locked from db transactions";
				}else if(statusToken.equals("BO")){
					statusTokenDesc = "Locked from db and cr transactions";
				}
				statusEditText.setText(statusTokenDesc);
				statusEditText.setEnabled(false);
				bal_Cleared.setVisibility(View.VISIBLE);
				bal_clearedEditText.setText(formatAmounts(balanceClearedToken));
				bal_clearedEditText.setEnabled(false);
				bal_Uncleared.setVisibility(View.VISIBLE);
				bal_unclearedEditText.setText(formatAmounts(balanceUnclearedToken));
				bal_unclearedEditText.setEnabled(false);

//								openPrintApp();

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
//							//										progress.cancel();
//							ratty = sp.toString();
//
//							if (ratty.equals("error1")){
//								progress.cancel();
//								Toast.makeText(AccountNum.this, "Wrong account number", Toast.LENGTH_LONG).show();
//							}else if (ratty.equals("20")){
//								progress.cancel();
//								wrongAccNum();
//
////								accNumEditText.setText("");
////
////								accNumEditText.requestFocus();
//							}else{
//								StringTokenizer token = new StringTokenizer(ratty, ",");
//								nameToken = token.nextToken().trim();
//								statusToken = token.nextToken().trim();
//								balanceClearedToken = token.nextToken().trim();
//								balanceUnclearedToken = token.nextToken().trim();
//								progress.cancel();
//								SearchByName.searchAccountNum = null;
//
//								accName.setVisibility(View.VISIBLE);
//								nameEditText.setText(nameToken);
//								nameEditText.setEnabled(false);
//								status.setVisibility(View.VISIBLE);
//								if (statusToken.equals("N")){
//									statusTokenDesc = "Normal";
//								}else if(statusToken.equals("DO")){
//									statusTokenDesc = "Dormant";
//								}else if(statusToken.equals("CR")){
//									statusTokenDesc = "Locked from cr transactions";
//								}else if(statusToken.equals("DR")){
//									statusTokenDesc = "Locked from db transactions";
//								}else if(statusToken.equals("BO")){
//									statusTokenDesc = "Locked from db and cr transactions";
//								}
//								statusEditText.setText(statusTokenDesc);
//								statusEditText.setEnabled(false);
//								bal_Cleared.setVisibility(View.VISIBLE);
//								bal_clearedEditText.setText(formatAmounts(balanceClearedToken));
//								bal_clearedEditText.setEnabled(false);
//								bal_Uncleared.setVisibility(View.VISIBLE);
//								bal_unclearedEditText.setText(formatAmounts(balanceUnclearedToken));
//								bal_unclearedEditText.setEnabled(false);
//
////								openPrintApp();
//
//							}
//
//						}
//					});
//
//				}catch (SocketTimeoutException e) {
//					progress.cancel();
//					noConnectivity();
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


	//dialog for succcessful transaction
	public void successful(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				AccountNum.this);

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
				Intent back = new Intent(AccountNum.this, MainMenuList.class);
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
				//					okButton.setVisibility(View.INVISIBLE);

				accNumEditText.setText("");

				accNumEditText.requestFocus();
			}
		});

		// Showing Alert Dialog
		alertDialog.show();
	}

	//dialog for unsuccessful transaction
	public void unsuccessful(){
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				AccountNum.this);

		// Setting Dialog Title
		alertDialog2.setTitle("Error !");
		alertDialog2.setMessage("An error occured");		 

		alertDialog2.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.cancel();
				//					okButton.setVisibility(View.INVISIBLE);

				accNumEditText.setText("");
				okButton.setEnabled(true);
				accNumEditText.requestFocus();
			}
		});
		// Showing Alert Dialog
		alertDialog2.show();
	}

	public void errorConnecting() {
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				AccountNum.this);

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

	public String formatAmounts(String theNumber){

		amtDouble = Double.parseDouble(theNumber);
		DecimalFormat formatter = new DecimalFormat("#,###.00");

		formattedAmt = formatter.format(amtDouble);
		return formattedAmt;
	}
	Double amtDouble = null;
	String formattedAmt = null;


	public void openPrintApp(){
		try {
			mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

			//				int balLeft = Integer.parseInt(balance) - Integer.parseInt(amt);
			message = "************************"
					+"  MobiTransact Receipt  "
					+"************************"
					+ mydate + "\n"
					+"     Balance Enquiry    \n" 
					+"Name: " + nameToken + "\n"
					+"Status: " + statusTokenDesc + "\n"
					+"Bal cleared: " +"GHS" + formatAmounts(balanceClearedToken) + "\n"
					+"Bal uncleared: " +"GHS"  + formatAmounts(balanceUnclearedToken) + "\n\n"
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

				//					i.putExtra(AccountNum.msg, true);
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
			//				BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			if (myFile.length() > 0) 
				myOutWriter.append(" ; " + msg);
			else 
				myOutWriter.append(msg);

			myOutWriter.close();
			fOut.close();
			myFile.isHidden();
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
			//				Toast.makeText(Withdraw.this, "File saved successfully",Toast.LENGTH_LONG).show();
			myOutWriter.close();
			fOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			Toast.makeText(AccountNum.this, e.getMessage(),Toast.LENGTH_LONG).show();
		}

	}

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		SearchByName.searchAccountNum = "";
		DepositChooseAcc.account = "";
//		SearchByName.searchAccountNum = null;
		finish();
		super.onBackPressed();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	AccountNum.this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	/*@Override
	public void onBackPressed() {
//	   moveTaskToBack(true); 
	   TransEnquiry.this.finish();
	}*/
	
	



}

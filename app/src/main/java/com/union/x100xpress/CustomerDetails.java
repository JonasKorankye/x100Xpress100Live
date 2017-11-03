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
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.StringTokenizer;

import myUrl.UrlClass;

public class CustomerDetails extends AppCompatActivity {
	
	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL = /*"http://192.168.1.26:8680/web_service/HelloWorldPortType"; */
//			/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*"http://197.251.247.4/web_service/HelloWorldPortType";*/
//	/*"http://192.168.180.23:8680/web_service/HelloWorldPortType";*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/ /*fnsl public*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://197.221.80.184:8680/web_service/HelloWorldPortType";  /*bestpoint public*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
    private static final String SOAP_ACTION = "my_request";
    private static final String METHOD_NAME = "ServiceOperation";
    static SoapPrimitive sp;
    Thread thread;
	String custNum;
//	String ratty = "";
	EditText enterCustNum;
	TextView responseTV;
	EditText firstNameEditText;
//	EditText lastNameEditText;
	EditText dateOfBirthEditText;
	EditText custNumEditText;
	EditText idTypeEditText;
	EditText idNumEditText;
	EditText phoneNumEditText;
//	EditText accNameEditText;
	LinearLayout fname, dob, cNum, idtype, idnum, num;
	File myFile;
	Button okButton;
	
	ProgressDialog progress;
	String firstName;
//	String lastName;
	String dateOfBirth;
	String customerNum;
	String idType;
	String idNum;
	String phoneNum;
	String msg = null;
//	String accName;
	
	String mydate = null;
	String message = null;
//	String statusTokenDesc = null;
	
//	File myFile = null;
    File myPrintFile = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setTitle("Balance Enquiry");
		setContentView(R.layout.cust_details);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		okButton = (Button) findViewById(R.id.okBut);
		enterCustNum = (EditText) findViewById(R.id.custDetails_number);
		firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
//		lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
		dateOfBirthEditText = (EditText) findViewById(R.id.dobEditText);
		custNumEditText = (EditText) findViewById(R.id.custNumEditText);
		idTypeEditText = (EditText) findViewById(R.id.idTypeEditText);
		idNumEditText = (EditText) findViewById(R.id.idNumEditText);
		phoneNumEditText = (EditText) findViewById(R.id.phoneNumEditText);
//		accNameEditText = (EditText) findViewById(R.id.accDescEditText);
		fname = (LinearLayout) findViewById(R.id.ac_name);
//		lname = (LinearLayout) findViewById(R.id.lname);
		dob = (LinearLayout) findViewById(R.id.dob);
		cNum = (LinearLayout) findViewById(R.id.cnum);
		idtype = (LinearLayout) findViewById(R.id.id);
		idnum = (LinearLayout) findViewById(R.id.idnum);
		num = (LinearLayout) findViewById(R.id.num);
//		fullname = (LinearLayout) findViewById(R.id.accDesc);
		
		
    	okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				custNum = enterCustNum.getText().toString();
				if /*(custNum.length() !=0 && custNum.length() != 7){
					enterCustNum.setError("Customer number must be 7 digits");
				}else if*/ (custNum.length() == 0 ){
					enterCustNum.setError("Please enter customer number");
				}else{
				AccountNum.choice = "CD";
//				progress = new ProgressDialog(CustomerDetails.this);
//				progress.setTitle("Sending");
//				progress.setMessage("Please wait...");
//				progress.show();
				msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " "  + MainActivity.userPassword + " " + custNum;
				new fireMan().execute();
//				okButton.setVisibility(View.INVISIBLE);
				 
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
//				       String answerString = enterCustNum.getText().toString();
				 				       
				       okButton.setVisibility(View.VISIBLE);  
				       enterCustNum.requestFocus();
				    }
				};
				 
				//third, we must add the textWatcher to our EditText
				enterCustNum.addTextChangedListener(textWatcher);
				}
			}
		});
						
	}

	public void openPrintApp(){
		try {
			mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			
//			int balLeft = Integer.parseInt(balance) - Integer.parseInt(amt);  
			message = "************************"
					 +"  MobiTransact Receipt  "
			         +"************************"
					 + mydate + "\n"
					 +"    Customer Details    \n\n" 
					 +"Customer Name: " + firstName + "\n"
					 +"Date of birth: " + dateOfBirth + "\n"
					 +"Customer Number: " + customerNum + "\n\n"
					 +"ID Type: " + idType + "\n"
					 +"ID Number: " + idnum + "\n\n"			 
					 +"Phone Number: " + phoneNum + "\n\n"
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
				CustomerDetails.this);

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
				CustomerDetails.this);

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
				CustomerDetails.this);

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
			progress = new ProgressDialog(CustomerDetails.this);
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
			else if (response.equals("20")){
				progress.cancel();
				wrongAccNum();
			}else{
				StringTokenizer token = new StringTokenizer(response, ",");
				firstName = token.nextToken().trim();
//											lastName = token.nextToken().trim();
				dateOfBirth = token.nextToken().trim();
				customerNum = token.nextToken().trim();
				idType = token.nextToken().trim();
				idNum = token.nextToken().trim();
				phoneNum = token.nextToken().trim();
//											accName = token.nextToken().trim();
				progress.cancel();

				fname.setVisibility(View.VISIBLE);
				firstNameEditText.setText(firstName);
				firstNameEditText.setEnabled(false);
//											lname.setVisibility(View.VISIBLE);
//											lastNameEditText.setText(lastName);
				dob.setVisibility(View.VISIBLE);
				dateOfBirthEditText.setText(dateOfBirth);
				dateOfBirthEditText.setEnabled(false);
				cNum.setVisibility(View.VISIBLE);
				custNumEditText.setText(customerNum);
				custNumEditText.setEnabled(false);
				idtype.setVisibility(View.VISIBLE);
				idTypeEditText.setText(idType);
				idTypeEditText.setEnabled(false);
				idnum.setVisibility(View.VISIBLE);
				idNumEditText.setText(idNum);
				idNumEditText.setEnabled(false);
				num.setVisibility(View.VISIBLE);
				phoneNumEditText.setText(phoneNum);
				phoneNumEditText.setEnabled(false);
//											fullname.setVisibility(View.VISIBLE);
//											accNameEditText.setText(accName);

//											openPrintApp();

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
//										ratty = sp.toString();
//										if (ratty == ""){
//											progress.cancel();
//											Toast.makeText(CustomerDetails.this, "An error occured", Toast.LENGTH_LONG).show();
//										}else if (ratty.equals("20")){
//											progress.cancel();
//											wrongAccNum();
//										}else{
//											StringTokenizer token = new StringTokenizer(ratty, ",");
//											firstName = token.nextToken().trim();
////											lastName = token.nextToken().trim();
//											dateOfBirth = token.nextToken().trim();
//											customerNum = token.nextToken().trim();
//											idType = token.nextToken().trim();
//											idNum = token.nextToken().trim();
//											phoneNum = token.nextToken().trim();
////											accName = token.nextToken().trim();
//											progress.cancel();
//
//											fname.setVisibility(View.VISIBLE);
//											firstNameEditText.setText(firstName);
//											firstNameEditText.setEnabled(false);
////											lname.setVisibility(View.VISIBLE);
////											lastNameEditText.setText(lastName);
//											dob.setVisibility(View.VISIBLE);
//											dateOfBirthEditText.setText(dateOfBirth);
//											dateOfBirthEditText.setEnabled(false);
//											cNum.setVisibility(View.VISIBLE);
//											custNumEditText.setText(customerNum);
//											custNumEditText.setEnabled(false);
//											idtype.setVisibility(View.VISIBLE);
//											idTypeEditText.setText(idType);
//											idTypeEditText.setEnabled(false);
//											idnum.setVisibility(View.VISIBLE);
//											idNumEditText.setText(idNum);
//											idNumEditText.setEnabled(false);
//											num.setVisibility(View.VISIBLE);
//											phoneNumEditText.setText(phoneNum);
//											phoneNumEditText.setEnabled(false);
////											fullname.setVisibility(View.VISIBLE);
////											accNameEditText.setText(accName);
//
////											openPrintApp();
//
//										}
//
//									}
//								});
//
//						}catch (SocketTimeoutException e) {
//							progress.cancel();
//							noConnectivity();
//						}
//						catch (IOException e) {
//
//							e.printStackTrace();
////		                    Toast.makeText(CustomerDetails.this, "An error occured", Toast.LENGTH_LONG).show();
//						}
//
//						catch (XmlPullParserException e) {
//			            e.printStackTrace();
////			            Toast.makeText(CustomerDetails.this, "An error occured", Toast.LENGTH_LONG).show();
//						}
//			    }
//		});
//	}
	
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
			Toast.makeText(CustomerDetails.this, e.getMessage(),Toast.LENGTH_LONG).show();
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
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	CustomerDetails.this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
//	   moveTaskToBack(true); 
	   CustomerDetails.this.finish();
	}

}

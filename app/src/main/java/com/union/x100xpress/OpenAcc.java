package com.union.x100xpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OpenAcc extends Activity{

	private static final String NAMESPACE = "http://webpackage/";
	private static final String URL ="http://192.168.1.26:7001/web_service/HelloWorldService"; 
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;
	Thread thread;
	String ratty = "";
	ProgressDialog progress;
	static String choice, accNum;
	static String openAccMsg = "";
	
	EditText firstNameEditText;
	EditText lastNameEditText;
	EditText middleNameEditText;
	EditText dateOfBirthEditText;
	EditText mobileNoEditText;
	LinearLayout ll;
	Button okButton;
	Intent intent;

	String mydate = null;
	String message = null;
	String statusTokenDesc = null;

	File myFile = null;
	File myPrintFile = null;

	Dialog dialogProgress;
	String firstName = null;
	String middleName = null;
	String lastName = null;
	String dateOfBirth = null;
	String mobileNo = null;
//	String balanceUnclearedToken = null;

	TextView responseTV;
	LinearLayout accName, status, bal_Cleared, bal_Uncleared;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_acc);

		okButton = (Button) findViewById(R.id.openAccOk);
		
		firstNameEditText = (EditText) findViewById(R.id.fnameEditText);
		lastNameEditText = (EditText) findViewById(R.id.lnameEditText);
		middleNameEditText = (EditText) findViewById(R.id.mnameEditText);
		dateOfBirthEditText = (EditText) findViewById(R.id.dobEditText);
		mobileNoEditText = (EditText) findViewById(R.id.mobileNumEditText);
		
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				firstName = firstNameEditText.getText().toString();
				middleName = middleNameEditText.getText().toString();
				lastName = lastNameEditText.getText().toString();
				dateOfBirth = dateOfBirthEditText.getText().toString();
				mobileNo = mobileNoEditText.getText().toString();
				
				if (firstName.length() == 0 ){
					firstNameEditText.setError("Please enter first name");
				}else if (middleName.length() == 0 ){
					middleNameEditText.setError("Please enter middle name");
				}else if (lastName.length() == 0 ){
					lastNameEditText.setError("Please enter last name");
				}else if (dateOfBirth.length() == 0 ){
					dateOfBirthEditText.setError("Please enter date of birth");
				}else if (mobileNo.length() == 0 ){
					mobileNoEditText.setError("Please enter mobile number");
				}else{
					okButton.setEnabled(false);
					progress = new ProgressDialog(OpenAcc.this);
					progress.setTitle("Sending");
					progress.setMessage("Please wait...");
					progress.show();
					choice = "OA";
					openAccMsg = firstName + "," + middleName + "," + lastName + "," + dateOfBirth + "," + mobileNo;
					Intent prodDetails = new Intent(OpenAcc.this, ProdDetails.class);
					startActivity(prodDetails);
					try {
						saveMsg(openAccMsg);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});



	}


	private void saveMsg(String number) throws FileNotFoundException{

		try {
			myPrintFile = new File(Environment.getExternalStorageDirectory(), "openacc.txt");
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
			Toast.makeText(OpenAcc.this, e.getMessage(),Toast.LENGTH_LONG).show();
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

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 5000);

				try{																	
					androidHttpTransport.call(SOAP_ACTION, envelope);

					sp = (SoapPrimitive) envelope.getResponse();

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//										progress.cancel();
							ratty = sp.toString();

							if (ratty.equals("error1")){
								progress.cancel();
								Toast.makeText(OpenAcc.this, "Wrong account number", Toast.LENGTH_LONG).show();
							}else{
								StringTokenizer token = new StringTokenizer(ratty, ",");
								

							}

						}
					});

				}catch (SocketTimeoutException e) {
					progress.cancel();
					//							noConnectivity();
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

}

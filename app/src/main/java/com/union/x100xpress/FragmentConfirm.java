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
import java.net.URL;
import java.util.StringTokenizer;

import org.ksoap2.serialization.SoapPrimitive;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import myUrl.UrlClass;


public class FragmentConfirm extends Fragment {

	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL = /*"http://192.168.180.23:8680/web_service/HelloWorldPortType";*/
//			/*"http://192.168.1.26:8680/web_service/HelloWorldPortType";*/
//			/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/
//			/*"http://197.251.247.4/web_service/HelloWorldPortType";*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/ /*fnsl public*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://192.168.1.8:8680/web_service/HelloWorldPortType";
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;

	Thread thread;

	View rootView;
	private ViewPager viewPager;

	EditText fnameEdittext;
	EditText lnameEdittext;
	EditText mnameEdittext;
//	EditText dobEdittext;
	EditText phone1Edittext;
	EditText phone2Edittext;
	EditText emailEdittext;
	EditText addressEdittext;
	EditText cityPostalEdittext;
	EditText regionEdittext;
	EditText locationEdittext;
	EditText cityPhysicalEdittext;
	EditText landmarkEdittext;
	EditText idTypeEdittext;
	EditText idNumEdittext;

	RadioGroup radioGenderGroup;
	RadioButton radioGenderButton;

	Button okButton;
	Button backButton;

	String fname = null;
	String lname = null;
	String mname = null;
	String gender = null;
//	String dob = null;
	String phone1 = null;
	String phone2 = null;
	String email = null;
//	String mydate = null;
	String idtype = null;
	String address = null;
	String cityPostal = null;
	String region = null;
	String location = null;
	String cityPhysical = null;
	String landmark = null;
	String idNum = null;
	int selectedPaymentType;
	String acctDetails = null;

	ProgressDialog progress;
	//	String mydate = null;
	String message = null;
//	String ratty = null;
	String acctNum = null;
	String acctLink = null;
	String custNum = null;
	String statusCode = null;
	String msg = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//		mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		rootView = inflater.inflate(R.layout.fragment_confirm, container, false);
		viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
		
		System.out.println(FragmentDocument.sendID);
		System.out.println(FragmentDocument.idNum);

		fnameEdittext = (EditText) rootView.findViewById(R.id.ac_name);
		lnameEdittext = (EditText) rootView.findViewById(R.id.depAmount);
		mnameEdittext = (EditText) rootView.findViewById(R.id.mname);
//		dobEdittext = (EditText) rootView.findViewById(R.id.dob);
		phone1Edittext = (EditText) rootView.findViewById(R.id.phone1);
		phone2Edittext = (EditText) rootView.findViewById(R.id.phone2);
		emailEdittext = (EditText) rootView.findViewById(R.id.email);
		addressEdittext = (EditText) rootView.findViewById(R.id.address);
		cityPostalEdittext = (EditText) rootView.findViewById(R.id.city_postal);
		regionEdittext = (EditText) rootView.findViewById(R.id.region);
		locationEdittext = (EditText) rootView.findViewById(R.id.location);
		cityPhysicalEdittext = (EditText) rootView.findViewById(R.id.city);
		landmarkEdittext = (EditText) rootView.findViewById(R.id.landmark);
		idTypeEdittext = (EditText) rootView.findViewById(R.id.doc_type);
		idNumEdittext = (EditText) rootView.findViewById(R.id.doc_id);

		radioGenderGroup = (RadioGroup) rootView.findViewById(R.id.radio_gender);

		fnameEdittext.setText(FragmentPersonal.fname);
		lnameEdittext.setText(FragmentPersonal.lname);
		mnameEdittext.setText(FragmentPersonal.mname);
//		dobEdittext.setText(FragmentPersonal.dob);
		phone1Edittext.setText(FragmentPersonal.phone1);
		phone2Edittext.setText(FragmentPersonal.phone2);
		emailEdittext.setText(FragmentPersonal.email);
				
		locationEdittext.setText(FragmentAddress.location);
		addressEdittext.setText(FragmentAddress.address);
		cityPhysicalEdittext.setText(FragmentAddress.cityPhysical);
		regionEdittext.setText(FragmentAddress.region);
		cityPostalEdittext.setText(FragmentAddress.cityPostal);
		landmarkEdittext.setText(FragmentAddress.cityPostal);
		
		idTypeEdittext.setText(FragmentDocument.sendID);
		idNumEdittext.setText(FragmentDocument.idNum);


		System.out.println("GENDER: " + FragmentPersonal.gender);
		okButton = (Button) rootView.findViewById(R.id.personalButton);
		backButton = (Button) rootView.findViewById(R.id.backButton);
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fname = fnameEdittext.getText().toString();
				lname = lnameEdittext.getText().toString();
				mname = mnameEdittext.getText().toString();
//				dob = dobEdittext.getText().toString();
				phone1 = phone1Edittext.getText().toString();
				phone2 = phone2Edittext.getText().toString();
				email = emailEdittext.getText().toString();
				if (FragmentPersonal.gender.equals("Male")){
					gender = "M";
				}else {
					gender = "F"; 
				}

				address = addressEdittext.getText().toString();
				cityPhysical = cityPhysicalEdittext.getText().toString();
				region = regionEdittext.getText().toString();
				cityPostal = cityPostalEdittext.getText().toString();
				landmark = landmarkEdittext.getText().toString();
				idtype = idTypeEdittext.getText().toString();
				idNum = idNumEdittext.getText().toString();

				//				System.out.println(fname+ ","+ lname);
				
//				Random r = new Random();
//				int i1 = r.nextInt(3333 - 2222) + 2222;
//				idNum = String.valueOf(i1);
				AccountNum.choice = "AC";
				System.out.println("GENDER: " + FragmentPersonal.gender);
				System.out.println("GENDER: " + gender);
				acctDetails = /*fname+lname +"~"+*/ lname +"~"+ fname +"~"+ mname +"~"+ phone1 +"~"+ idNum +"~"+ idtype  +"~"+ ProdDetails.sendSubProd +"~"+ 
						gender +"~"+ "Mr" +"~"+ "GHA" +"~"+ ProdDetails.sendProd +"~"+ ProdDetails.sendProd+ProdDetails.sendSubProd +"~"+ phone2 +"~"+ address +"~"+ 
						cityPhysical +"~"+ email + "~" + FragmentSector.sendCode + "~" + FragmentSector.sendSubCode + "~" + FragmentARM.sendARM ;
				
				
				/*acctDetails = "solojohn" +"~"+   "john" +"~"+ "solo" +"~"+ "kofi" +"~"+ "0244333222" +"~"+ idNum +"~"+ "400"  +"~"+ "10" +"~"+ 
						"M" +"~"+ "Mr" +"~"+ "GHA" +"~"+ "1" +"~"+ "110" +"~"+ "0544888777" +"~"+ "dansoman" +"~"+ 
						"accra" +"~"+ "philjohn@yahoo.com" ;*/
				
				System.out.println(acctDetails);
				msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + ProdDetails.sendProd + " " + ProdDetails.sendSubProd + " " + acctDetails.replaceAll("\\s+","!");
//				System.out.println(AccountNum.msg);
//				progress = new ProgressDialog(getActivity());
//				progress.setTitle("Sending");
//				progress.setMessage("Please wait...");
//				progress.show();
				new fireMan().execute();

				// make respected tab selected
				//				viewPager.setCurrentItem(2,true);
			}
		});

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//go to previous tab
				//				getActivity().finish();
				viewPager.setCurrentItem(3,true);
			}
		});

		return rootView;
	}

	public void successful(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				getActivity());

		// Setting Dialog Title
		alertDialog.setTitle("Successful");
		alertDialog.setMessage("Your account number is " + acctNum);		 

		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.dismiss();
				DepositChooseAcc.account = null;
				SearchByName.searchAccountNum = null;
				getActivity().finish();
				Intent back = new Intent(getActivity(), MainMenuList.class);
				startActivity(back);
			}
		});
		// Setting Negative "NO" Btn
		/*alertDialog.setNegativeButton("NO",
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
		});*/

		// Showing Alert Dialog
		alertDialog.show();
	}

	//dialog for unsuccessful transaction
	public void unsuccessful(){
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				getActivity());

		// Setting Dialog Title
		alertDialog2.setTitle("Error !");
		alertDialog2.setMessage("An error occured");		 

		alertDialog2.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.cancel();
//				okButton.setVisibility(View.INVISIBLE);
				/*getAccNameButton.setVisibility(View.VISIBLE);
				accNumEditText.setText("");
				amountEditText.setText("");
				accNameTextView.setText(" ");
				accBalTextView.setText("");
				amountEditText.setEnabled(false);
				commentsEditText.setEnabled(false);
				accNumEditText.requestFocus();*/
			}
		});
		// Showing Alert Dialog
		alertDialog2.show();
	}

	public void errorConnecting() {
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				getActivity());

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
			Toast.makeText(getActivity(),
					"Done writing SD 'mysdfile.txt'",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getActivity(), e.getMessage(),
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
			progress = new ProgressDialog(getActivity());
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
			else {
				StringTokenizer token = new StringTokenizer(response, ",");
				statusCode = token.nextToken().trim();
				//							Toast.makeText(getActivity(), ratty, Toast.LENGTH_LONG).show();
				//							System.out.print(ratty);
				if (statusCode.equals("12")) {
					progress.cancel();
					unsuccessful();

				}

				if (statusCode.equals("00")) {
					progress.cancel();

					acctNum = token.nextToken().trim();
					//								acctLink = token.nextToken().trim();
					//								custNum = token.nextToken().trim();
					successful();
					//								openPrintApp();
				}
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
//					//					System.out.print(sp.toString());
//					getActivity().runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							ratty = sp.toString();
//
//							StringTokenizer token = new StringTokenizer(ratty, ",");
//							statusCode = token.nextToken().trim();
//							//							Toast.makeText(getActivity(), ratty, Toast.LENGTH_LONG).show();
//							//							System.out.print(ratty);
//							if(statusCode.equals("12")){
//								progress.cancel();
//								unsuccessful();
//
//							}
//
//							if(statusCode.equals("00")){
//								progress.cancel();
//
//								acctNum = token.nextToken().trim();
//								//								acctLink = token.nextToken().trim();
//								//								custNum = token.nextToken().trim();
//								successful();
//								//								openPrintApp();
//							}
//						}
//					});
//
//				}catch (SocketTimeoutException e) {
//					//					progress.cancel();
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


	public void noNetworkConn() {

		try {

			getActivity().runOnUiThread(new Runnable() {

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
				getActivity());

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

			getActivity().runOnUiThread(new Runnable() {

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
	    	getActivity().finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}


}

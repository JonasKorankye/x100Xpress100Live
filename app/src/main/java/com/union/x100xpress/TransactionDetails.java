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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import myUrl.UrlClass;


public class TransactionDetails extends AppCompatActivity {

	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL = /*"http://192.168.1.26:8680/web_service/HelloWorldPortType";  */
//	/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/   /*"http://197.251.247.4/web_service/HelloWorldPortType"*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/
////			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://197.221.80.184:8680/web_service/HelloWorldPortType";  /*bestpoint public*/
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;

	//	ListView acclist;
	String code;
	Thread thread;
	String msg = "";
	File myFile;
	Button okButton;
	ProgressDialog progress;
//	String ratty = null;

	String custNum;
	ListView list;
	EditText enterCustNum;
	static String stuff [] = {"", "", ""};
	//	static String accNo;

	static String accNum;
	static String accName;

	String mydate = null;
	String message = null;
	File myPrintFile = null;
	
	Double amtDouble = null;
	String formattedAmt = null;

	static String transDetails = null;
	static String transDate = null;
	static String transBranch = null;
	static String transAmt = null;
	static String transDb = null;
	
	String trnsSummary = null;
	String trnsDate = null;
	String trnsAmt = null;
	String trnsBranch = null;

	EditText accNumEditText;
	static String[] date;
	static String[] transType;
	static String[] amount;
	static String[] trans = new String[10];
	String stuffToPrint = null;
	int done = 0;
	//	static String[][][] itemsArray = { {}, {}, {} };
	ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
	int[] next = new int[]{	R.drawable.ic_next_item};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trans_details);
		setTitle("Transaction Details");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(enterAccNum.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);

		accNumEditText = (EditText) findViewById(R.id.accNumEditText);
		okButton = (Button) findViewById(R.id.okButton);
		
		if(SearchByName.searchAccountNum != ""){
			accNumEditText.setText(SearchByName.searchAccountNum);					
		}else{}

		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				accNum = accNumEditText.getText().toString();
				if (accNum.length() == 0) {
					accNumEditText.setError("Please enter account number");
				} else {
//					progress = new ProgressDialog(TransactionDetails.this);
//					progress.setTitle("Sending");
//					progress.setMessage("Please wait...");
//					progress.show();
					AccountNum.choice = "TD";
					msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + accNum;
					new fireMan().execute();
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
					accNumEditText.addTextChangedListener(textWatcher);
				}
			}
		});
	}







	
	
	public void wrongAccNum(){
		AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
				TransactionDetails.this);

		// Setting Dialog Title
		alertDialog5.setTitle("Error !");
		alertDialog5.setMessage("Customer does not exist");		

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
				TransactionDetails.this);

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




	public void errorConnecting() {
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				TransactionDetails.this);

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
			progress = new ProgressDialog(TransactionDetails.this);
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
			con.setReadTimeout(30000); // millis
			con.setConnectTimeout(30000); // millis

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
//								Toast.makeText(TransactionDetails.this, "Wrong account number", Toast.LENGTH_LONG).show();
				wrongAccNum();
			}else{
				progress.cancel();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(accNumEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				stuff  = response.split("~");

				List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

				for(int a=0;a<stuff.length -1;a++){
//					StringTokenizer myAccToken = new StringTokenizer(stuff[a], ",");


					StringTokenizer myAccToken = new StringTokenizer(stuff[a], "!");
					try {
						while (myAccToken.hasMoreTokens()){
							transDetails = myAccToken.nextToken().trim();
							transDate = myAccToken.nextToken().trim();
							transBranch = myAccToken.nextToken().trim();
							transAmt = myAccToken.nextToken().trim();
							//									if (myAccToken.nextToken().trim().length()==0) transDb = myAccToken.nextToken().trim();
							System.out.println(transDetails);
							System.out.println(transDate);
							System.out.println(transBranch);
							System.out.println(transAmt);
						}

					} catch (NoSuchElementException e) {
						// TODO Auto-generated catch block
						//										Toast.makeText(TransactionDetails.this, "token empty", Toast.LENGTH_SHORT);
						//										done = 1;
						e.printStackTrace();
					}

					//									String data = null;
					//									data = data.concat(data);

//									stuffToPrint = transDetails +"~"+ transDate +"~"+ transAmt;
//									System.out.println(stuffToPrint);

					HashMap<String, String> hm = new HashMap<String,String>();


					hm.put("summary", transDetails);
					hm.put("date", "( " + transDate + " )");
					hm.put("amt", "             " + transAmt);
					hm.put("branch", transBranch);
					//									hm.put("ledgerbal", "Ledger Bal                  " + transAmt);
					//									hm.put("next", Integer.toString(next[0]) );

					aList.add(hm);

				}


				String[] from = { /*"next",*/"summary", "date","amt", "branch"  /*, "ledgerbal"*/ };

				// Ids of views in listview_layout
				int[] to = { /*R.id.flag,*/R.id.summary,R.id.date,R.id.amt, R.id.branch /*, R.id.ledger_bal*/};

				// Instantiating an adapter to store each items
				// R.layout.listview_layout defines the layout of each item
				SimpleAdapter adapter = new SimpleAdapter(TransactionDetails.this, aList, R.layout.list_trans, from, to);

//								setListAdapter(adapter);

				okButton.setEnabled(false);

				list = (ListView) findViewById(R.id.trans_list);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new OnItemClickListener() {

//								list = getListView();

//								list.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View v, int arg2,
											long arg3) {

						trnsSummary = ((TextView) v.findViewById(R.id.summary)).getText().toString().trim();
						trnsDate = ((TextView) v.findViewById(R.id.date)).getText().toString().trim();
						trnsAmt = ((TextView) v.findViewById(R.id.amt)).getText().toString().trim();
						trnsBranch = ((TextView) v.findViewById(R.id.branch)).getText().toString().trim();

						Intent d = new Intent(TransactionDetails.this, TransactionChoice.class);
						d.putExtra("transDetails", trnsSummary);
						d.putExtra("date", trnsDate);
						d.putExtra("branch", trnsBranch);
						if (trnsAmt.matches("-?\\d+(.\\d+)?")) {
							d.putExtra("amount", formatAmounts(trnsAmt.trim()));
						}else {
							d.putExtra("amount", trnsAmt);
						}
						System.out.println("transdetails " + trnsSummary);
						System.out.println(trnsDate);
						System.out.println(trnsDate);
						System.out.println(trnsAmt);
						startActivity(d);
					}
				});


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
////								Toast.makeText(TransactionDetails.this, "Wrong account number", Toast.LENGTH_LONG).show();
//								wrongAccNum();
//							}else{
//								progress.cancel();
//								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//								imm.hideSoftInputFromWindow(accNumEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//								stuff  = ratty.split("~");
//
//								List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
//
//								for(int a=0;a<stuff.length -1;a++){
//									StringTokenizer myAccToken = new StringTokenizer(stuff[a], ",");
//
//									try {
//										while (myAccToken.hasMoreTokens()){
//											transDetails = myAccToken.nextToken().trim();
//											transDate = myAccToken.nextToken().trim();
//											transBranch = myAccToken.nextToken().trim();
//											transAmt = myAccToken.nextToken().trim();
//											//									if (myAccToken.nextToken().trim().length()==0) transDb = myAccToken.nextToken().trim();
//											System.out.println(transDetails);
//											System.out.println(transDate);
//											System.out.println(transBranch);
//											System.out.println(transAmt);
//										}
//
//									} catch (NoSuchElementException e) {
//										// TODO Auto-generated catch block
//										//										Toast.makeText(TransactionDetails.this, "token empty", Toast.LENGTH_SHORT);
//										//										done = 1;
//										e.printStackTrace();
//									}
//
//									//									String data = null;
//									//									data = data.concat(data);
//
////									stuffToPrint = transDetails +"~"+ transDate +"~"+ transAmt;
////									System.out.println(stuffToPrint);
//
//									HashMap<String, String> hm = new HashMap<String,String>();
//
//
//									hm.put("summary", transDetails);
//									hm.put("date", "( " + transDate + " )");
//									hm.put("amt", "             " + transAmt);
//									//									hm.put("ledgerbal", "Ledger Bal                  " + transAmt);
//									//									hm.put("next", Integer.toString(next[0]) );
//
//									aList.add(hm);
//
//								}
//
//
//								String[] from = { /*"next",*/"summary", "date","amt"  /*, "ledgerbal"*/ };
//
//								// Ids of views in listview_layout
//								int[] to = { /*R.id.flag,*/R.id.summary,R.id.date,R.id.amt/*, R.id.ledger_bal*/};
//
//								// Instantiating an adapter to store each items
//								// R.layout.listview_layout defines the layout of each item
//								SimpleAdapter adapter = new SimpleAdapter(TransactionDetails.this, aList, R.layout.list_myaccounts, from, to);
//
////								setListAdapter(adapter);
//
//								okButton.setEnabled(false);
//
//								list = (ListView) findViewById(R.id.trans_list);
//								list.setAdapter(adapter);
//								list.setOnItemClickListener(new OnItemClickListener() {
//
////								list = getListView();
//
////								list.setOnItemClickListener(new OnItemClickListener() {
//
//									public void onItemClick(AdapterView<?> arg0, View v, int arg2,
//											long arg3) {
//
//										trnsSummary = ((TextView) v.findViewById(R.id.summary)).getText().toString().trim();
//										trnsDate = ((TextView) v.findViewById(R.id.date)).getText().toString().trim();
//										trnsAmt = ((TextView) v.findViewById(R.id.amt)).getText().toString().trim();
//
//										Intent d = new Intent(TransactionDetails.this, TransactionChoice.class);
//										d.putExtra("transDetails", trnsSummary);
//										d.putExtra("date", trnsDate);
//										d.putExtra("branch", transBranch);
//										d.putExtra("amount", formatAmounts(trnsAmt.trim()));
//										System.out.println("transdetails " + trnsSummary);
//										System.out.println(trnsDate);
//										System.out.println(trnsDate);
//										System.out.println(trnsAmt);
//										startActivity(d);
//									}
//								});
//
//
//							}
//
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
	
	public String formatAmounts(String theNumber){

		amtDouble = Double.parseDouble(theNumber);
		DecimalFormat formatter = new DecimalFormat("#,###.00");

		formattedAmt = formatter.format(amtDouble);
		return formattedAmt;
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
			Toast.makeText(TransactionDetails.this, e.getMessage(),Toast.LENGTH_LONG).show();
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
	    	TransactionDetails.this.finish();
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
		super.onBackPressed();

	}

}
package com.union.x100xpress;

import java.io.File;
//import android.support.v7.app.ActionBarActivity;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import myUrl.UrlClass;


public class SearchByName extends ListActivity{

	private static final String NAMESPACE = "http://webpackage/";
//	private static final String URL =
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
//	/*"http://192.168.1.26:8680/web_service/HelloWorldPortType"; */
//	/*"http://192.168.1.26:9999/web_service/HelloWorldPortType";*/  /*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*"http://197.251.247.4/web_service/HelloWorldPortType"*/;
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;
	String ratty = "";
	Thread thread;
	String custNum;
	ListView acc_list;
	EditText enterCustNum;
	static String stuff [] = {"", "", ""};
	//	static String accNo;
	static String account;
	static String searchAccountNum = null;
	static String accNum;
	static String accName;
	Button okButton;
	File myFile;
	ProgressDialog progress;
	Dialog dialog;
	TextView balText;
	TextView transText;
	TextView depositText;
	TextView loanText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchbyid);

		enterCustNum = (EditText) findViewById(R.id.cust_name);
		okButton = (Button) findViewById(R.id.okBut);

		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				custNum = enterCustNum.getText().toString();
				if (custNum.length() == 0 ){
					enterCustNum.setError("Please enter name");
				}else{
					progress = new ProgressDialog(SearchByName.this);
					progress.setTitle("Searching");
					progress.setMessage("Please wait...");
					progress.show(); 
					AccountNum.choice = "SE";
					AccountNum.msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " "  + MainActivity.userPassword + " " + custNum;

					Handler h = new Handler();

					h.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									sendRequest(AccountNum.msg);
									thread.start();		
//									okButton.setEnabled(false);

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
											enterCustNum.requestFocus();
										}
									};

									//third, we must add the textWatcher to our EditText
									enterCustNum.addTextChangedListener(textWatcher);
								}
							});
						}
					});
				}

			}
		});

		acc_list = getListView();
		acc_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				account = String.valueOf(stuff[position]);
				searchAccountNum = account.substring(account.lastIndexOf(' ') + 1);
//				Intent depositIntent = new Intent(SearchByName.this, Deposit.class);
//				startActivity(depositIntent);
//				Toast.makeText(SearchByName.this, searchAccountNum, Toast.LENGTH_SHORT).show();
				dialog = new Dialog(SearchByName.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.list_search);
				dialog.setCancelable(true);
				balText = (TextView) dialog.findViewById(R.id.balenq);
				transText = (TextView) dialog.findViewById(R.id.trasenq);
				depositText = (TextView) dialog.findViewById(R.id.dep);
				loanText = (TextView) dialog.findViewById(R.id.loan);
				//				bankIn.setClickable(true);
				balText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						finish();
						Intent createAliasIntent = new Intent(SearchByName.this, AccountNum.class);
//						System.out.println(token);
//						createAliasIntent.putExtra("acct", accountNum);
						startActivity(createAliasIntent);
					}
				});
				
				transText.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						finish();
						Intent createAliasIntent = new Intent(SearchByName.this, TransactionDetails.class);
//						System.out.println(token);
//						createAliasIntent.putExtra("acct", accountNum);
						startActivity(createAliasIntent);
					}
				});
				depositText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						finish();
						Intent createAliasIntent = new Intent(SearchByName.this, Deposit.class);
//						System.out.println(token);
//						createAliasIntent.putExtra("acct", accountNum);
						startActivity(createAliasIntent);
					}
				});
				
				loanText.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						finish();
						Intent createAliasIntent = new Intent(SearchByName.this, Cust_Loan_Origination.class);
//						System.out.println(token);
//						createAliasIntent.putExtra("acct", accountNum);
						startActivity(createAliasIntent);
					}
				});
				
				dialog.show();
				return false;
			}
		});

			/*@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});*/
		
		
	}

	public void makelist(){
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stuff));	
		okButton.setEnabled(false);
	}




	public void errorConnecting(){
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				SearchByName.this);

		// Setting Dialog Title
		alertDialog3.setTitle("Error!");
		alertDialog3.setMessage("Please try again or use other SIM");		 

		alertDialog3.setPositiveButton("TRY AGAIN",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.cancel();

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
					Toast.makeText(SearchByName.this, "Request has been saved in file successfully", Toast.LENGTH_LONG).show();
					okButton.setEnabled(true);
					enterCustNum.setText("");
					enterCustNum.requestFocus();
					acc_list.setVisibility(View.VISIBLE);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SearchByName.this, "Unsuccessful", Toast.LENGTH_LONG).show();
				}


			}
		});*/

		// Showing Alert Dialog
		alertDialog3.show();
	}
	
	public void wrongAccNum(){
		AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
				SearchByName.this);

		// Setting Dialog Title
		alertDialog5.setTitle("Error !");
		alertDialog5.setMessage("Customer does not exist");		

		alertDialog5.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
//				dialog.cancel();
//				okButton.setVisibility(View.INVISIBLE);
				enterCustNum.setText("");
				enterCustNum.requestFocus();
			}
		});
		// Showing Alert Dialog
		alertDialog5.show();
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
							if (ratty.equals("12")){
								progress.cancel();
								Toast.makeText(SearchByName.this, "An error occured", Toast.LENGTH_LONG).show();
							}else if (ratty.equals("20")){
								progress.cancel();
								wrongAccNum();
							}else{
								progress.cancel();
								stuff  = ratty.split(",");
								makelist();
								okButton.setEnabled(false);
//								enterCustNum.setEnabled(false);
							}

						}
					});

				}catch (SocketTimeoutException e) {
					noConnectivity();
					//														
				}catch (IOException e) {

					e.printStackTrace();

				}

				catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		});
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
	    	SearchByName.this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		searchAccountNum = "";
		DepositChooseAcc.account = "";
		finish();
		super.onBackPressed();
	}
}

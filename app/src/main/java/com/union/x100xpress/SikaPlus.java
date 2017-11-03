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
import java.util.StringTokenizer;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import myUrl.UrlClass;


public class SikaPlus extends AppCompatActivity implements OnItemSelectedListener {

    private static final String NAMESPACE = "http://webpackage/";
    //	private static final String URL = /*"http://192.168.1.26:8680/web_service/HelloWorldPortType";*/
//			/*"http://192.168.43.32:8680/web_service/HelloWorldPortType"; */
//			/*"http://192.168.180.23:8680/web_service/HelloWorldPortType";*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/ /*fnsl public*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
    private static final String SOAP_ACTION = "my_request";
    private static final String METHOD_NAME = "ServiceOperation";
    static SoapPrimitive sp;
    static String stuff[] = {"", "", ""};


    //	ActionBar actionBar/* = getSupportActionBar()*/;
    View rootView;
    private ViewPager viewPager;

    EditText senderNameEdittext;
    EditText recNameEdittext;
    EditText senderAddressEdittext;
    EditText recAddressEdittext;
    EditText senderPhoneEdittext;
    EditText recPhoneEdittext;
    EditText answerEdittext;
    EditText amtEdittext;
    EditText remNumEdittext;
    Thread thread;
    Spinner spinner;


    String ratty = null;
    String statusCode = null;
    String remittanceNum = null;
    String msg = null;

    //	static RadioGroup radioGenderGroup;
    //    static RadioButton radioGenderButton;

    Button okButton;
    Button backButton;

    String senderName = null;
    String recName = null;
    String senderAddress = null;
    String recAddress = null;
    String senderPhone = null;
    String recPhone = null;
    String answer = null;
    String question = null;
    String mydate = null;
    String amount = null;

    int selectedPaymentType;

    ProgressDialog progress;


    String prodList[] = {"Father First Name", "Mother First Name", "Name of First Pet", "Model of your first car"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sika_plus_acct);
        setTitle("Cash Transfer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        senderNameEdittext = (EditText) findViewById(R.id.sender_name);
        recNameEdittext = (EditText) findViewById(R.id.receiver_name);
        senderAddressEdittext = (EditText) findViewById(R.id.sender_address);
        recAddressEdittext = (EditText) findViewById(R.id.receiver_address);
        senderPhoneEdittext = (EditText) findViewById(R.id.sender_phone);
        recPhoneEdittext = (EditText) findViewById(R.id.receiver_phone);
        answerEdittext = (EditText) findViewById(R.id.answer);
        amtEdittext = (EditText) findViewById(R.id.amount);
        remNumEdittext = (EditText) findViewById(R.id.remittance_no);

        okButton = (Button) findViewById(R.id.okButton);
        backButton = (Button) findViewById(R.id.backButton);
        spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prodList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                senderName = senderNameEdittext.getText().toString();
                recName = recNameEdittext.getText().toString();
                senderAddress = senderAddressEdittext.getText().toString();
                recAddress = recAddressEdittext.getText().toString();
                senderPhone = senderPhoneEdittext.getText().toString();
                recPhone = recPhoneEdittext.getText().toString();
                answer = answerEdittext.getText().toString();
                amount = amtEdittext.getText().toString();

                AccountNum.choice = "SP";
                String details = senderName + "~" + senderAddress + "~" + senderPhone + "~" + recName + "~" + recAddress + "~" + recPhone + "~" + question + "~" + answer;
                msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + amount + " " + details.replaceAll("\\s+", "!");


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                finish();

            }
        });


    }


    //dialog for unsuccessful transaction
    public void unsuccessful() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                SikaPlus.this);

        // Setting Dialog Title
        alertDialog2.setTitle("Error !");
        alertDialog2.setMessage("An error occured");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                        //					okButton.setVisibility(View.INVISIBLE);
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
                SikaPlus.this);

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
                SikaPlus.this);

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


    private void saveInFile(String request) throws FileNotFoundException {

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
            Toast.makeText(SikaPlus.this,
                    "Done writing SD 'mysdfile.txt'",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(SikaPlus.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
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
            progress = new ProgressDialog(SikaPlus.this);
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
            if (response == null) {
                System.out.println("RESPONSE EMPTY");
            } else {
                StringTokenizer token = new StringTokenizer(response, ",");
                statusCode = token.nextToken().trim();

                if (statusCode.equals("12")) {
                    progress.cancel();
                    unsuccessful();

                } else if (statusCode.equals("00")) {
                    progress.cancel();

                    remittanceNum = token.nextToken().trim();
                    remNumEdittext.setText(remittanceNum);
                    Toast.makeText(SikaPlus.this, "Successful", Toast.LENGTH_LONG).show();
                    //								openPrintApp();
                }
            }

            progress.cancel();


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
//					SikaPlus.this.runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							ratty = sp.toString();
//							System.out.println(ratty);
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
//								remittanceNum = token.nextToken().trim();
//								remNumEdittext.setText(remittanceNum);
//								Toast.makeText(SikaPlus.this, "Successful", Toast.LENGTH_LONG).show();
//								//								openPrintApp();
//							}
//
//							progress.cancel();
//
//
//
//						}
//					});
//
//				}catch (SocketTimeoutException e) {
//					progress.cancel();
//					noConnectivity();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                SikaPlus.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        int arrayPosition = spinner.getSelectedItemPosition();
        switch (arrayPosition) {
            case 0:
                question = String.valueOf(spinner.getSelectedItem());
                break;
            case 1:
                question = String.valueOf(spinner.getSelectedItem());
                break;
            case 2:
                question = String.valueOf(spinner.getSelectedItem());
                break;
            case 3:
                question = String.valueOf(spinner.getSelectedItem());
                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    public void noConnectivity() {

        try {

            SikaPlus.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    errorConnecting();

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}

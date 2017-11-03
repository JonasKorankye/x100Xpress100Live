package com.union.x100xpress;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.ksoap2.serialization.SoapPrimitive;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import myUrl.UrlClass;


public class MainActivity extends AppCompatActivity {

    private static final String NAMESPACE = "http://webpackage/";
    //    private static final String URL = /*"http://192.168.1.26:8686/web_service/HelloWorldPortType";*/
//			/*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*abii*/
//			/*"http://192.168.43.32:8680/web_service/HelloWorldPortType";*/   /*my machine*/
////	"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////            "http://197.221.80.184:8680/web_service/HelloWorldPortType";  /*bestpoint public*/  192.168.1.103:8080
//    /*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/ /*fnsl public*/
//    "http://192.168.1.102:8080/web_service/HelloWorldPortType";
//	/*"http://192.168.10.80:8680/web_service/HelloWorldPortType";*/  /*fnsl apn*/
//	/*"http://197.221.81.162:8681/web_service_test/HelloWorldPortType";*/ /*fnsl public test*/
//			/*"http://192.168.2.17:8680/web_service/HelloWorldPortType";*/
//			/*"http://192.168.1.8:8680/web_service/HelloWorldPortType";*/
    private static final String SOAP_ACTION = "my_request";
    private static final String METHOD_NAME = "ServiceOperation";
    static SoapPrimitive sp;
    static String msg = "";
    Thread thread;
    String ratty = "";
    static String choice = "";
    char current;

    Button loginBut;
    EditText passwordFieldEdittext;
    EditText phoneNumEdittext;
    static String userPassword;
    static String userPhoneNum;
    ProgressDialog progress;


    View view;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("x100Xpress");

        getSupportActionBar().hide();
        System.out.println("print");


//		Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//	    pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
//	    startAlarm(view);

		/*Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

	    startAlarm(view);

	    sendLocation(AlarmReceiver.sendLocation);*/

//	    new AndroidLocationServices();

		/*TextView splashText = (TextView) findViewById(R.id.text);
		Typeface chops = Typeface.createFromAsset(getAssets(), "embossedgermanica.ttf");
		splashText.setTypeface(chops);*/
        passwordFieldEdittext = (EditText) findViewById(R.id.editText2);
        phoneNumEdittext = (EditText) findViewById(R.id.editText1);
        loginBut = (Button) findViewById(R.id.loginButton);
        //		phoneNumEdittext.setEnabled(false);

        loginBut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userPassword = passwordFieldEdittext.getText().toString();
                userPhoneNum = phoneNumEdittext.getText().toString();

                System.out.println("username: " + userPhoneNum);
                System.out.println("userpassword: " + userPassword);
                //				Intent mainmenuIntent = new Intent(MainActivity.this, MainMenuList.class);
                //				startActivity(mainmenuIntent);
                if (userPhoneNum.length() == 0) {
                    phoneNumEdittext.setError("Please enter UserID");
                } else if (userPassword.length() != 0 && userPassword.length() <= 4) {
                    passwordFieldEdittext.setError("Password must be more than 4 characters");
                } else if (userPassword.length() == 0) {
                    passwordFieldEdittext.setError("Please enter password");
                } else {
                    System.out.println("dfdsfddg");
//                    progress = new ProgressDialog(MainActivity.this);
//										progress.setTitle("");
//                    progress.setMessage("Authenticating User");
//                    progress.show();
                    choice = "LN"; //real code
                    msg = choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword;//realcode

//                    sendRequest(msg);
//                    thread.start();

//                    new RequestTask().execute();
                    new fireMan().execute(); //real code

//                    try {
//                        sendPOST();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

//					Intent mainmenuIntent = new Intent(MainActivity.this, MainMenuList.class);
//					startActivity(mainmenuIntent);


//					finish();

                }
            }
        });


    }


//	public void startAlarm(View view) {
//	    manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//	    int interval = 10000;
//
//	    manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
////	    Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//	}


    public void sendRequest(final String message) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    sendPOST();
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//                //request.addProperty("Input",enterText.getText().toString());
//                PropertyInfo pi = new PropertyInfo();
//                pi.setNamespace(NAMESPACE);
//                pi.setType(PropertyInfo.STRING_CLASS);
//                //pi.setName("name");
//                pi.setName("Input");
//                //pi.setValue(editText1.getText().toString().trim());
//                pi.setValue(message);
//                request.addProperty(pi);
//
//                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
//                envelope.setOutputSoapObject(request);
//
//                HttpTransportSE androidHttpTransport = new HttpTransportSE(UrlClass.url, 10000);
//
//                try {
//                    androidHttpTransport.call(SOAP_ACTION, envelope);
//
//                    sp = (SoapPrimitive) envelope.getResponse();
//
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            //										progress.cancel();
//                            ratty = sp.toString();
//                            progress.cancel();
//                            //										Toast.makeText(MainActivity.this, ratty.trim(), Toast.LENGTH_LONG).show();
//                            if (ratty.trim().equals("00")) {
//                                finish();
//                                Intent mainmenuIntent = new Intent(MainActivity.this, MainMenuList.class);
//                                startActivity(mainmenuIntent);
//                            } else if (ratty.trim().equals("10")) {
//                                wrongPassword();
//                            } else if (ratty.trim().equals("11")) {
//                                acctLocked();
//                            } else if (ratty.trim().equals("15")) {
//                                notApproved();
//                            } else if (ratty.trim().equals("12")) {
//                                wrongUser();
//                            }
//
//
//							/*if (ratty.equals("error1")){
//											progress.cancel();
//											Toast.makeText(MainActivity.this, "Wrong account number", Toast.LENGTH_LONG).show();
//										}else{
//											StringTokenizer token = new StringTokenizer(ratty, ",");
//
//											progress.cancel();
//
//
//										}*/
//
//                        }
//                    });
//
//                } catch (SocketTimeoutException e) {
//                    progress.cancel();
//                    noConnectivity();
//                } catch (IOException e) {
//
//                    e.printStackTrace();
//                    //System.out.println("Error with response");
//                } catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }


            }
        });
    }


	/*public void sendLocation(final String message){
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

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 10000);

				try{
					androidHttpTransport.call(SOAP_ACTION, envelope);

					sp = (SoapPrimitive) envelope.getResponse();

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//										progress.cancel();
//							ratty = sp.toString();
							progress.cancel();
							//										Toast.makeText(MainActivity.this, ratty.trim(), Toast.LENGTH_LONG).show();
							if(ratty.trim().equals("00")){
								finish();
								Intent mainmenuIntent = new Intent(MainActivity.this, MainMenuList.class);
								startActivity(mainmenuIntent);
							}else if(ratty.trim().equals("10")){
								wrongPassword();
							}else if(ratty.trim().equals("11")){
								acctLocked();
							}else if(ratty.trim().equals("15")){
								notApproved();
							}else if(ratty.trim().equals("12")){
								wrongUser();
							}


							if (ratty.equals("error1")){
											progress.cancel();
											Toast.makeText(MainActivity.this, "Wrong account number", Toast.LENGTH_LONG).show();
										}else{
											StringTokenizer token = new StringTokenizer(ratty, ",");

											progress.cancel();


										}

						}
					});

				}catch (SocketTimeoutException e) {
					progress.cancel();
					noConnectivity();
				}catch (IOException e) {

					e.printStackTrace();
					//System.out.println("Error with response");
				}

				catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		});
	}*/

    public void emptyResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                MainActivity.this);

        // Setting Dialog Title
        alertDialog2.setTitle("LOGIN FAILED");
        alertDialog2.setMessage("Connection Timed Out. Please try again");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        phoneNumEdittext.setText("");
                        passwordFieldEdittext.setText("");
                        phoneNumEdittext.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    public void wrongPassword() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Wrong Password");
        alertDialog.setMessage("Try Again?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.dismiss();
                        passwordFieldEdittext.setText("");
                        passwordFieldEdittext.requestFocus();
                    }
                });
        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        //
//                        dialog.cancel();
                        finish();
                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
    }


    public void acctLocked() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Account has been locked!");
        alertDialog.setMessage("Call office for assistance?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
//                        dialog.dismiss();
                        Intent whoyougonnacall = new Intent(Intent.ACTION_DIAL,
                                Uri.parse("tel:0244636127"));
                        startActivity(whoyougonnacall);
                    }

                });
        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        //
//                        dialog.cancel();
                        finish();
                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
    }


    public void notApproved() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Sorry!");
        alertDialog.setMessage("User or Device has not been approved. Call office for assistance");

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
//                        dialog.dismiss();
                        phoneNumEdittext.setText("");
                        passwordFieldEdittext.setText("");
                        phoneNumEdittext.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog.show();
    }

    public void wrongUser() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Sorry!");
        alertDialog.setMessage("User does not exist");

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
//                        dialog.dismiss();
                        phoneNumEdittext.setText("");
                        passwordFieldEdittext.setText("");
                        phoneNumEdittext.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void noConnectivity() {

        try {

            runOnUiThread(new Runnable() {

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
                MainActivity.this);

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
                MainActivity.this);

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




    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }


//    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String GET_URL = "http://localhost:9090/SpringMVCExample";

    private static final String POST_URL = "http://197.221.81.162:8680/web_service/HelloWorldPortType";

    private static final String POST_PARAMS = msg;


    public void sendPOST() throws IOException {
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
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
            progress = new ProgressDialog(MainActivity.this);
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

            progress.cancel();
            if (response==null){
                System.out.println("RESPONSE EMPTY");
                //emptyResponse();
            }
            else if (response.equals("00")) {
                System.out.println("Worked");
                finish();
                Intent mainmenuIntent = new Intent(MainActivity.this, MainMenuList.class);
                startActivity(mainmenuIntent);
            } else if (response.equals("10")) {
                wrongPassword();
            } else if (response.equals("11")) {
                acctLocked();
            } else if (response.equals("15")) {
                notApproved();
            } else if (response.equals("12")) {
                wrongUser();
            } else {

            }

        }

    }


//    public class RequestTask extends AsyncTask<Void, Void, Void> {
//
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            progress = new ProgressDialog(MainActivity.this);
//            progress.setCancelable(false);
//            progress.setTitle("Please wait");
//            progress.setMessage("Sending request...");
//            progress.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void ... params) {
//
//            String url = choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword;
//            URL obj = null;
//            try {
//                obj = new URL(UrlClass.url);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//            HttpURLConnection con = null;
//            try {
//                con = (HttpURLConnection) obj.openConnection();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            //add reuqest header
//            try {
//                con.setRequestMethod("POST");
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            }
////            con.setRequestProperty("User-Agent", USER_AGENT);
//            con.setRequestProperty("Accept-Language", "en-US,en;q=0");
//            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
//
//            String urlParameters = choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword;
//
//            // Send post request
//            con.setDoOutput(true);
//            DataOutputStream wr = null;
//            try {
//                wr = new DataOutputStream(con.getOutputStream());
//                wr.writeBytes(urlParameters);
//                wr.flush();
//                wr.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//
//            try {
//                responseCode = con.getResponseCode();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("\nSending 'POST' request to URL : " + UrlClass.url);
//            System.out.println("Post parameters : " + urlParameters);
//            System.out.println("Response Code : " + responseCode);
//
//            BufferedReader in = null;
//            try {
//                String inputLine;
////                StringBuffer response = new StringBuffer();
//                in = new BufferedReader(
//                        new InputStreamReader(con.getInputStream()));
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            //print result
//            System.out.println(response.toString());
//
//
//            return null;
//        }
//
//
//
//        @Override
//        protected void onPostExecute(Void result) {
//            // TODO Auto-generated method stub
//            progress.cancel();
//            if (ratty.trim().equals("00")) {
//                System.out.println("Worked");
//                finish();
//                Intent mainmenuIntent = new Intent(MainActivity.this, MainMenuList.class);
//                startActivity(mainmenuIntent);
//            } else if (ratty.trim().equals("10")) {
//                wrongPassword();
//            } else if (ratty.trim().equals("11")) {
//                acctLocked();
//            } else if (ratty.trim().equals("15")) {
//                notApproved();
//            } else if (ratty.trim().equals("12")) {
//                wrongUser();
//            }
//
//
//
//        }
//
//
//    }


}





























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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import myUrl.UrlClass;


public class Withdraw extends AppCompatActivity {

    private static final String NAMESPACE = "http://webpackage/";
    //	private static final String URL =
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://192.168.246.1:8680/web_service/HelloWorldPortType";
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
//			/*"http://192.168.1.26:8680/web_service/HelloWorldPortType";*/  /*"http://197.251.247.4/web_service/HelloWorldPortType";*/
    private static final String SOAP_ACTION = "my_request";
    private static final String METHOD_NAME = "ServiceOperation";
    static SoapPrimitive sp;

    File myFile = null;
    File myPrintFile = null;
    Intent printIntent;
    ProgressDialog progress;
    ProgressDialog progress2;
    ProgressDialog progress3;

    String name = null;
    String status = null;
    String balance = null;
    String phoneNum = null;
    String passcode = null;
    String enterPasscode = null;

    Thread thread;
    String custNum;
    ListView acc_list;
    EditText enterCustNum;
    static String account;
    static String accNum;
    //	static String accName;
    static String choice;
    static String msg = "";
    String errorMsg = "";
    String fd = "";

    String filename = "requestfile";
    String string = "";
    FileOutputStream outputStream;

    EditText accNumEditText;
    EditText passcodeEditText;
    TextView accNameTextView;
    TextView accBalTextView;
    EditText amountEditText;
    EditText narrationEditText;

    String mydate = null;
    String message = null;
    private String filename2 = "msgfile.txt";
    private String filepath2 = "MyFileStorage";
    File userFile2;
    BufferedReader br2;

    Button getAccNameButton;
    //	Button fetchButton;
    Button okButton;
    String amt;
    String narration;
    String accountNum;
    String accountName = "";
//	String ratty = "";
//	String accDetails = "";

    ImageView idImageView;
    ImageView photoImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTitle("Withdrawal");
        setContentView(R.layout.withdraw);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ContextWrapper contextWrapper2 = new ContextWrapper(Withdraw.this);
        File directory2 = contextWrapper2.getDir(filepath2, Context.MODE_PRIVATE);
        userFile2 = new File(directory2, filename2);

        accNumEditText = (EditText) findViewById(R.id.accnumEditText);
        accNameTextView = (TextView) findViewById(R.id.nameofAccEditText);
        accBalTextView = (TextView) findViewById(R.id.accBal);
        passcodeEditText = (EditText) findViewById(R.id.passcodeEditText);
        passcodeEditText.setEnabled(false);
//		idImageView.setImageResource(R.drawable.img);
        photoImageView = (ImageView) findViewById(R.id.id_pic1);
        idImageView = (ImageView) findViewById(R.id.id_pic2);
//		photoImageView.setImageResource(R.drawable.img);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        amountEditText.setEnabled(false);
        narrationEditText = (EditText) findViewById(R.id.narrationEditText);
        narrationEditText.setEnabled(false);
        okButton = (Button) findViewById(R.id.okButton);
        narrationEditText.setText("x100Xpress withdrawal");
        getAccNameButton = (Button) findViewById(R.id.okBut);
//		fetchButton = (Button) findViewById(R.id.fetch_more);

        if (WithdrawalChooseAcc.acc != "") {
            System.out.println("accountnum is " + WithdrawalChooseAcc.acc);

            accountNum = WithdrawalChooseAcc.acc.substring(WithdrawalChooseAcc.acc.lastIndexOf(' ') + 1);
            accountName = WithdrawalChooseAcc.acc.substring(0, WithdrawalChooseAcc.acc.lastIndexOf(' '));
            accNumEditText.setText(accountNum);
            accNameTextView.setText(accountName);
            amountEditText.setEnabled(true);
            passcodeEditText.setEnabled(true);
            getAccNameButton.setVisibility(View.INVISIBLE);
        } else {

            okButton.setVisibility(View.INVISIBLE);
//			fetchButton.setVisibility(View.INVISIBLE);
            getAccNameButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    accountNum = accNumEditText.getText().toString();
                    if (accountNum.length() == 0) {
                        accNumEditText.setError("Please enter account number");
                    } else {
                        AccountNum.choice = "CN";
                        msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + accountNum;
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
//						       String string = accNumEditText.getText().toString();

                                //and now we make a Toast
                                //modify "yourActivity.this" with your activity name .this
//						       Toast.makeText(Withdraw.this,"The string from EditText is: "+answerString,0).show();
                                okButton.setVisibility(View.INVISIBLE);
                                getAccNameButton.setVisibility(View.VISIBLE);
                                amountEditText.setText("");
                                accNameTextView.setText("");
                                passcodeEditText.setText("");
                                accBalTextView.setText("");
                                amountEditText.setEnabled(false);
                                narrationEditText.setEnabled(false);
                                idImageView.setImageDrawable(getResources().getDrawable(R.drawable.img));
                                photoImageView.setImageDrawable(getResources().getDrawable(R.drawable.img));
                            }
                        };

                        //third, we must add the textWatcher to our EditText
                        accNumEditText.addTextChangedListener(textWatcher);

                    }
                }
            });
        }


        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                amt = amountEditText.getText().toString();
                enterPasscode = passcodeEditText.getText().toString();
                if (accountNum.length() != 0 && accountNum.length() != 13) {
                    accNumEditText.setError("Account number must be 13 digits");
                } else if (accountNum.length() == 0) {
                    accNumEditText.setError("Please enter account number");
                } else if (amt.length() == 0) {
                    amountEditText.setError("Please enter amount to withdraw");
                } else if (Double.parseDouble(amt) > Double.parseDouble(balance)) {
                    amountEditText.setError("You have insufficient funds");
                } else if (!passcode.equals(enterPasscode)) {
                    passcodeEditText.setError("Wrong passcode");
                } else {
                    AccountNum.choice = "WI";
                    msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + accountNum + " " + amt;

                    new fireMan().execute();


//					date, name, withdrawal, d

                }

            }
        });


    }// end of onCreate

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	/*public boolean isIntentAvailable(Context context, String action) {
	    final PackageManager packageManager = context.getPackageManager();
	    printIntent = new Intent(action);
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(printIntent,
	                    PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}*/

    public String formatAmounts(String theNumber) {

        amtDouble = Double.parseDouble(theNumber);
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        formattedAmt = formatter.format(amtDouble);
        return formattedAmt;
    }

    Double amtDouble = null;
    String formattedAmt = null;

    public void openPrintApp() {
        try {
            mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

            int balLeft = Integer.parseInt(balance) - Integer.parseInt(amt);
            String stringOfBalLeft = String.valueOf(balLeft);

            message = "************************"
                    + "  MobiTransact Receipt  "
                    + "************************"
                    + mydate + "\n"
                    + "       Withdrawal       \n"
                    + "Name: " + name + "\n"
                    + "A/C Num: " + accountNum + "\n"
                    + "Amount: " + "GHS" + formatAmounts(amt) + "\n"
                    + "Balance: " + "GHS" + formatAmounts(stringOfBalLeft) + "\n\n"
                    + "        Thank You       "
                    + "************************\n";
            saveMsg(message);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Intent i;
        PackageManager manager = getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage("com.bluebamboo.p25demo");
            if (i == null) {
                throw new PackageManager.NameNotFoundException();
            } else {

//				i.putExtra(AccountNum.msg, true);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    //dialog for succcessful transaction
    public void successful() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Withdraw.this);

        // Setting Dialog Title
        alertDialog.setTitle(response2);
        alertDialog.setMessage("Would you like to return to the main menu?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.dismiss();
                        finish();
                        Intent back = new Intent(Withdraw.this, MainMenuList.class);
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
//				idImageView.setImageBitmap(null);
//				photoImageView.setImageBitmap(null);
                        okButton.setVisibility(View.INVISIBLE);
                        getAccNameButton.setVisibility(View.VISIBLE);
                        accNumEditText.setText("");
                        amountEditText.setText("");
                        accNameTextView.setText("");
                        passcodeEditText.setText("");
                        accBalTextView.setText("");
                        amountEditText.setEnabled(false);
                        narrationEditText.setEnabled(false);
                        accNumEditText.requestFocus();
                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
    }

    //dialog for unsuccessful transaction
    public void unsuccessful() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                Withdraw.this);

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
                        passcodeEditText.setText("");
                        accBalTextView.setText("");
                        amountEditText.setEnabled(false);
                        narrationEditText.setEnabled(false);
                        accNumEditText.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }

    //dialog for unsuccessful transaction
    public void aboveLimit() {
        AlertDialog.Builder alertDialog6 = new AlertDialog.Builder(
                Withdraw.this);

        // Setting Dialog Title
        alertDialog6.setTitle("Limit Exceeded !");
        alertDialog6.setMessage("Maximum withdrawal amount per day exceeded.\nLimit: GHS800.00");

        alertDialog6.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
//					okButton.setVisibility(View.INVISIBLE);
//					getAccNameButton.setVisibility(View.VISIBLE);
//					accNumEditText.setText("");
//					amountEditText.setText("");
//					accNameTextView.setText("");
//					accBalTextView.setText("");
//					amountEditText.setEnabled(false);
//					narrationEditText.setEnabled(false);
                        amountEditText.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog6.show();
    }

    //dialog for wrong account number
    public void wrongAccNum() {
        AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
                Withdraw.this);

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
                        passcodeEditText.setText("");
                        accNameTextView.setText("");
                        accBalTextView.setText("");
                        amountEditText.setEnabled(false);
                        narrationEditText.setEnabled(false);
                        accNumEditText.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog5.show();
    }


    //dialog for wrong account number
    public void noSmsReg() {
        AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
                Withdraw.this);

        // Setting Dialog Title
        alertDialog5.setTitle("Error !");
        alertDialog5.setMessage("Customer has not registered for SMS alerts");

        alertDialog5.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                        okButton.setVisibility(View.INVISIBLE);
                        getAccNameButton.setVisibility(View.VISIBLE);
                        accNumEditText.setText("");
                        amountEditText.setText("");
                        passcodeEditText.setText("");
                        accNameTextView.setText("");
                        accBalTextView.setText("");
                        amountEditText.setEnabled(false);
                        narrationEditText.setEnabled(false);
                        accNumEditText.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog5.show();
    }

    //dialog for insufficient funds
    public void insufficientFunds() {
        AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(
                Withdraw.this);

        // Setting Dialog Title
        alertDialog4.setTitle("Error !");
        alertDialog4.setMessage("You have insufficient funds to withdraw this amount");

        alertDialog4.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
//					okButton.setVisibility(View.INVISIBLE);
//					getAccNameButton.setVisibility(View.VISIBLE);
//					accNumEditText.setText("");
//					amountEditText.setText("");
//					accNameTextView.setText("");
//					accBalTextView.setText("");
//					amountEditText.setEnabled(false);
//					narrationEditText.setEnabled(false);
                        amountEditText.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog4.show();
    }


    public void errorConnecting() {
        // check if external storage is available and not read only

        AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
                Withdraw.this);

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


//	public void errorConnecting(){
//		// check if external storage is available and not read only
//
//        	AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
//    				Withdraw.this);
//
//    		// Setting Dialog Title
//    		alertDialog3.setTitle("Error connecting....");
//    		alertDialog3.setMessage("Would you like to try again?");
//
//    		alertDialog3.setPositiveButton("TRY AGAIN",
//    				new DialogInterface.OnClickListener() {
//    			public void onClick(DialogInterface dialog, int which) {
//    				// Write your code here to execute after dialog
//    				dialog.cancel();
//
//    			}
//    		});
//    		// Setting Negative "NO" Btn
//    		alertDialog3.setNegativeButton("SAVE REQUEST",
//    				new DialogInterface.OnClickListener() {
//    			public void onClick(DialogInterface dialog, int which) {
//    				// Write your code here to execute after dialog
//    				//
//    				dialog.cancel();
//    				try {
//
//    					saveInFile(AccountNum.msg);
////    					Toast.makeText(Withdraw.this, "Request has been saved in file successfully", Toast.LENGTH_LONG).show();
//    					okButton.setVisibility(View.INVISIBLE);
//    					getAccNameButton.setVisibility(View.VISIBLE);
//    					accNumEditText.setText("");
//    					amountEditText.setText("");
//    					accNameTextView.setText(" ");
//    					accBalTextView.setText("");
//    					amountEditText.setEnabled(false);
//    					narrationEditText.setEnabled(false);
//    					accNumEditText.requestFocus();
//    				} catch (FileNotFoundException e) {
//    					// TODO Auto-generated catch block
//    					Toast.makeText(Withdraw.this, "Unsuccessful", Toast.LENGTH_LONG).show();
//    				}
//
//
//    			}
//    		});
//
//    		// Showing Alert Dialog
//    		alertDialog3.show();
//
//
//	}
	
	
	/*private boolean connectionAvailable() {
	    boolean connected = false;
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
	            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
	        //we are connected to a network
	        connected = true;
	    }
	    return connected;
	}*/

    private void saveInFile(String request) throws FileNotFoundException {

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
            myFile.isHidden();
            Toast.makeText(getBaseContext(),
                    "Done writing SD 'myfile.txt'",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void saveMsg(String number) throws FileNotFoundException {

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
        } catch (Exception e) {
            Toast.makeText(Withdraw.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Withdraw.this);

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


    public void noPhoto() {
        // check if external storage is available and not read only

        AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(
                Withdraw.this);

        // Setting Dialog Title
        alertDialog5.setTitle("Cannot authenticate customer");
        alertDialog5.setMessage("Image not found");

        alertDialog5.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                        amountEditText.setEnabled(false);
                        passcodeEditText.setEnabled(false);

                    }
                });

        // Showing Alert Dialog
        alertDialog5.show();

    }



    public void noIdPhoto() {
        // check if external storage is available and not read only

        AlertDialog.Builder alertDialog6 = new AlertDialog.Builder(
                Withdraw.this);

        // Setting Dialog Title
        alertDialog6.setTitle("Cannot authenticate signature");
        alertDialog6.setMessage("Signature image does not  exist");

        alertDialog6.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                    }
                });

        // Showing Alert Dialog
        alertDialog6.show();

    }



    public void acctNoNotFound() {
        // check if external storage is available and not read only

        AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(
                Withdraw.this);

        // Setting Dialog Title
        alertDialog7.setTitle("Error");
        alertDialog7.setMessage("Customer's account number not found");

        alertDialog7.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                        amountEditText.setEnabled(false);
                        narrationEditText.setEnabled(false);

                    }
                });

        // Showing Alert Dialog
        alertDialog7.show();

    }


    int responseCode2 = 0;
    String response2 = null;


    public class fireMan extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(Withdraw.this);
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
                System.out.println("ResponseCode is " + responseCode2);
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
            if (response2 == null) {
                System.out.println("RESPONSE EMPTY");
            } else if (response2.equals("Transaction successful")) {
                progress.cancel();
                successful();
//								openPrintApp();
            } else if (response2.equals("Transaction unsuccessful1")) {
                progress.cancel();
                Toast.makeText(Withdraw.this, "1", Toast.LENGTH_SHORT).show();
                unsuccessful();

            } else if (response2.equals("Transaction unsuccessful2")) {
                progress.cancel();
                Toast.makeText(Withdraw.this, "2", Toast.LENGTH_SHORT).show();
                unsuccessful();

            } else if (response2.equals("21")) {
                progress.cancel();
                insufficientFunds();

            } else if (response2.equals("15")) {
                progress.cancel();
                aboveLimit();

            }

        }

    }


    int responseCode = 0;
    String response = null;


    public class fireManAccName extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(Withdraw.this);
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
            } else if (response.equals("error1")) {
                progress.cancel();
//								Toast.makeText(Withdraw.this, "Wrong account number", Toast.LENGTH_LONG).show();
                okButton.setVisibility(View.INVISIBLE);
                getAccNameButton.setVisibility(View.VISIBLE);
                accNumEditText.setText("");
                amountEditText.setText("");
                accNameTextView.setText(" ");
                accBalTextView.setText("");
                amountEditText.setEnabled(false);
                narrationEditText.setEnabled(false);
                accNumEditText.requestFocus();
            } else if (response.equals("20")) {
                progress.cancel();
                wrongAccNum();
                okButton.setVisibility(View.INVISIBLE);
                getAccNameButton.setVisibility(View.VISIBLE);
                accNumEditText.setText("");
                amountEditText.setText("");
                accNameTextView.setText(" ");
                passcodeEditText.setText("");
                accBalTextView.setText("");
                amountEditText.setEnabled(false);
                narrationEditText.setEnabled(false);
                accNumEditText.requestFocus();
            } else if (response.equals("22")) {
                progress.cancel();
                noSmsReg();
                okButton.setVisibility(View.INVISIBLE);
                getAccNameButton.setVisibility(View.VISIBLE);
                accNumEditText.setText("");
                amountEditText.setText("");
                passcodeEditText.setText("");
                accNameTextView.setText(" ");
                accBalTextView.setText("");
                amountEditText.setEnabled(false);
                narrationEditText.setEnabled(false);
                accNumEditText.requestFocus();
            } else {
                StringTokenizer token = new StringTokenizer(response, ",");
                name = token.nextToken().trim();
//								status = token.nextToken().trim();
                balance = token.nextToken().trim();
                phoneNum = token.nextToken().trim();
                passcode = token.nextToken().trim();
                accNameTextView.setText(" " + name);
                accBalTextView.setText(formatAmounts(balance));
                progress.cancel();
                getAccNameButton.setVisibility(View.INVISIBLE);
                okButton.setVisibility(View.VISIBLE);
                amountEditText.setEnabled(true);
                passcodeEditText.setEnabled(true);
                narrationEditText.setEnabled(false);
                amountEditText.requestFocus();

                AccountNum.choice = "IM";
                msg = AccountNum.choice + " " + accountNum;

                new fireManPhoto().execute();


            }

        }

    }


    int responseCode3 = 0;
    String response3 = null;


    public class fireManPhoto extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(Withdraw.this);
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
                wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(msg);
                wr.flush();
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
                noNetworkConn();

            }


            try {
                responseCode3 = con.getResponseCode();
                System.out.println("ResponseCode3 is " + responseCode3);
            } catch (IOException e) {
                e.printStackTrace();
                progress.cancel();
//                noConnectivity();
            }
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.url);
            System.out.println("Post parameters : " + msg);
//            System.out.println("Response Code : " + responseCode);

            if (responseCode3 == 200) {

                BufferedReader in = null;
                try {
                    String inputLine = null;
                    in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
                        response3 = inputLine;

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
                System.out.println(response3);

            } else if (responseCode3 == 404) {
                progress.cancel();
                noConnectivity();
            } else if (responseCode3 == 500) {
                progress.cancel();
                noConnectivity();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

//			progress.cancel();
            if (response3 == null) {
                progress.cancel();
                System.out.println("RESPONSE EMPTY");
            } else if (response3.equals("20")) {
                progress.cancel();
                System.out.println("Wrong acct number");
                acctNoNotFound();


            } else if (response3.equals("22")) {
                progress.cancel();
//                System.out.println("Wrong acct number");
                acctNoNotFound();



            }else if (response3.equals("21")) {
                progress.cancel();
                noPhoto();


            } else {
                progress.cancel();

                StringTokenizer token = new StringTokenizer(response3, ",");
                String name = token.nextToken().trim();
                String pic = token.nextToken().trim();
                System.out.println(pic);
                byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
//								byte[] picByte = pic.getBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (bitmap != null) {
                    photoImageView.setImageBitmap(bitmap);
                    AccountNum.choice = "IM2";
                    msg = AccountNum.choice + " " + accountNum;

                    new fireManSignature().execute();
                } else {
                    Toast.makeText(Withdraw.this, "null", Toast.LENGTH_LONG).show();
                }


            }


        }

    }


    int responseCode4 = 0;
    String response4 = null;


    public class fireManSignature extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(Withdraw.this);
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
                responseCode4 = con.getResponseCode();
                System.out.println("ResponseCode4 is " + responseCode4);
            } catch (IOException e) {
                e.printStackTrace();
                progress.cancel();
//                noConnectivity();
            }
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.url);
            System.out.println("Post parameters : " + msg);
//            System.out.println("Response Code : " + responseCode);

            if (responseCode4 == 200) {

                BufferedReader in = null;
                try {
                    String inputLine = null;
                    in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
                        response4 = inputLine;

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
                System.out.println(response4);

            } else if (responseCode4 == 404) {
                progress.cancel();
                noConnectivity();
            } else if (responseCode4 == 500) {
                progress.cancel();
                noConnectivity();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

//			progress.cancel();
            if (response4 == null) {
                progress.cancel();
                System.out.println("RESPONSE EMPTY");
            } else if (response4.equals("20")) {
                progress.cancel();
                System.out.println("Wrong acct number");
                acctNoNotFound();



            } else if (response4.equals("22")) {
                progress.cancel();
                System.out.println("Wrong acct number");
                acctNoNotFound();



            }else if (response4.equals("21")) {
                progress.cancel();
//                System.out.println("Wrong acct number");
                noIdPhoto();

            }else {
                progress.cancel();

                StringTokenizer token = new StringTokenizer(response4, ",");
                String name = token.nextToken().trim();
                String pic = token.nextToken().trim();
                System.out.println(pic);
                byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
//								byte[] picByte = pic.getBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (bitmap != null) {

//								image = new BitmapDrawable(Bitmap.createScaledBitmap(bitmap, 90, 100, true));
                    idImageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(Withdraw.this, "null", Toast.LENGTH_LONG).show();
                }


            }


        }

    }


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
//							System.out.println(accDetails);
//							if (accDetails.equals("error1")){
//								progress.cancel();
////								Toast.makeText(Withdraw.this, "Wrong account number", Toast.LENGTH_LONG).show();
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
//								passcodeEditText.setText("");
//								accBalTextView.setText("");
//								amountEditText.setEnabled(false);
//								narrationEditText.setEnabled(false);
//								accNumEditText.requestFocus();
//							}else if (accDetails.equals("22")){
//								progress.cancel();
//								noSmsReg();
//								okButton.setVisibility(View.INVISIBLE);
//								getAccNameButton.setVisibility(View.VISIBLE);
//								accNumEditText.setText("");
//								amountEditText.setText("");
//								passcodeEditText.setText("");
//								accNameTextView.setText(" ");
//								accBalTextView.setText("");
//								amountEditText.setEnabled(false);
//								narrationEditText.setEnabled(false);
//								accNumEditText.requestFocus();
//							}else {
//								StringTokenizer token = new StringTokenizer(accDetails, ",");
//								name = token.nextToken().trim();
////								status = token.nextToken().trim();
//								balance = token.nextToken().trim();
//								phoneNum = token.nextToken().trim();
//								passcode = token.nextToken().trim();
//								accNameTextView.setText(" "  + name);
//								accBalTextView.setText(formatAmounts(balance));
//								progress.cancel();
//								getAccNameButton.setVisibility(View.INVISIBLE);
//								okButton.setVisibility(View.VISIBLE);
//								amountEditText.setEnabled(true);
//								passcodeEditText.setEnabled(true);
//								narrationEditText.setEnabled(false);
//								amountEditText.requestFocus();
//
//							}
//						}
//					});
//
//				}catch (SocketTimeoutException e) {
////					fd = e.getCause().toString();
////					Toast.makeText(Withdraw.this, e.toString(), Toast.LENGTH_LONG).show();
////					errorMsg = e.toString();
////					new CustomTask().execute();
//					progress.cancel();
//					noConnectivity();
//				}
//				catch (IOException e) {
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
	
	
	/*public void requestPic(final String message){
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
							ratty = sp.toString();
							if (ratty.equals("20")){
								progress2.cancel();
								Toast.makeText(Withdraw.this, "Customer's mandate does not exist", Toast.LENGTH_LONG).show();
							}else{
								progress2.cancel();
								System.out.println(ratty);
								StringTokenizer token = new StringTokenizer(ratty, ",");
								String name = token.nextToken().trim();
								String pic1 = token.nextToken().trim();
								System.out.println(pic1);
								byte[] decodedString1 = Base64.decode(pic1, Base64.DEFAULT);
//								byte[] picByte = pic.getBytes();
								Bitmap bitmap1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
								if (bitmap1 != null) {
								
//								image = new BitmapDrawable(Bitmap.createScaledBitmap(bitmap, 90, 100, true));
//								idImageView.setImageBitmap(bitmap1);
								}else
								{
									Toast.makeText(Withdraw.this, "null", Toast.LENGTH_LONG).show();
								}
								
							}

						}
					});

				}catch (SocketTimeoutException e) {
//					fd = e.getCause().toString();
//					Toast.makeText(Withdraw.this, e.toString(), Toast.LENGTH_LONG).show();
//					errorMsg = e.toString();
//					new CustomTask().execute();
					progress2.cancel();
					noConnectivity();
				}
				catch (IOException e) {
					e.printStackTrace();

				}

				catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		});
	}*/
	
	
	
	
	
	/*private class CustomTask extends AsyncTask<Void, Void, Void> {

	    protected Void doInBackground(Void... param) {
	        //Do some work
	        return null;
	    }

	    protected void onPostExecute(Void param) {
	        //Print Toast or open dialog
	    	Toast.makeText(Withdraw.this, "An error occurred", Toast.LENGTH_LONG).show();
//	    	errorConnecting();
	    }
	}*/

    public void noConnectivity() {

        try {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    progress.cancel();
                    errorConnecting();

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Withdraw.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        DepositChooseAcc.account = "";
        SearchByName.searchAccountNum = "";
        finish();
        super.onBackPressed();
    }


}

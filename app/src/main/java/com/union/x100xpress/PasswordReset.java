package com.union.x100xpress;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import myUrl.UrlClass;

import static com.union.x100xpress.R.id.okBut;
import static com.union.x100xpress.R.id.walNumEditText;

/**
 * Created by USG0011 on 05/23/17.
 */

public class PasswordReset extends AppCompatActivity {

    EditText cust_number;
    Button okButton;
    String msg = "";
    String number = null;
    ProgressDialog progress;
    String response2 = null;
    int responseCode2 = 0;
    //String number = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_reset);
        setTitle("Password Reset");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        System.out.println("hiiii");
         cust_number = (EditText) findViewById(R.id.wallet_number);
        okButton = (Button) findViewById(R.id.okBut);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 number = cust_number.getText().toString();
                System.out.println(number);
                if(number.length() < 10){
                    cust_number.requestFocus();
                    cust_number.setText("");
                    cust_number.setError("Enter a valid Phone Number");
                }
                else if (number.length() == 10  || number.length()==12 )
                {    msg = number;
                    new   fireManWalNumber().execute();

                }


            }

        });
    }

    public class fireManWalNumber extends AsyncTask<Void , Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(PasswordReset.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Sending request...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL obj = null;
            try {
                obj = new URL(UrlClass.urlwalnumber);
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
                responseCode2 = con.getResponseCode();
                System.out.println("ResponseCode is " + responseCode2);
            } catch (IOException e) {
                e.printStackTrace();
                progress.cancel();
//                noConnectivity();
            }
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.urlwalnumber);
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
//			accDetails = sp.toString();
            String one = "WALLET NUMBER DOES NOT EXIST";
            String two = "WALLET NUMBER EXISTS";
            progress.cancel();
            if (response2.trim().equals(one)){
                //System.out.println("RESPONSE EMPTY");
                walNumberResponse();
            } else if (response2.trim().equals(two)){
                //System.out.println("RESPONSE EMPTY");
                walNumberOKResponse();
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                PasswordReset.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void walNumberOKResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                PasswordReset.this);

        // Setting Dialog Title
        alertDialog2.setTitle("WALLET NUMBER EXISTS");
        alertDialog2.setMessage("Please Continue...");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                        String agent= MainActivity.userPhoneNum;
                        msg =  agent + "~" + number;
                        new  fireMan().execute();

                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    public void walNumberResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                PasswordReset.this);

        // Setting Dialog Title
        alertDialog2.setTitle("WALLET NUMBER DOES NOT EXIST");
        alertDialog2.setMessage("Please try again");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        cust_number.setText("");
                        cust_number.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    public class fireMan extends AsyncTask<Void , Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(PasswordReset.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Sending request...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL obj = null;
            try {
                obj = new URL(UrlClass.urlPassReset);
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
                responseCode2 = con.getResponseCode();
                System.out.println("ResponseCode2 is " + responseCode2);
            } catch (IOException e) {
                e.printStackTrace();
                progress.cancel();
//                noConnectivity();
            }
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.urlPassReset);
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
                String one ="PASSWORD RESET SUCCESSFUL";
            //System.out.println(one);
            String two = "PASSWORD CANNOT BE RESET BECAUSE OF INSUFFICIENT FUNDS IN YOUR WALLET";
			progress.cancel();
          boolean hi =  response2.isEmpty();
         System.out.println(hi);
            //successful();
            if (response2.isEmpty()){
                System.out.println("RESPONSE EMPTY");
                emptyResponse();
            }
            else if(response2.trim().equals(one) ){
                //progress.cancel();
                System.out.println("jo");
                successful();
                //								openPrintApp();
            }else if(response2.trim().equals(two) ){
                //progress.cancel();
                unsuccessful();

            }

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
    public void successful(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                PasswordReset.this);

        // Setting Dialog Title
        alertDialog.setTitle(response2);
        alertDialog.setMessage("Would you like to return to the main menu?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.dismiss();
//                        DepositChooseAcc.account = "";
//                        SearchByName.searchAccountNum = "";
                        finish();
                        Intent back = new Intent(PasswordReset.this, MainMenuList.class);
                        startActivity(back);
                    }
                });
        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        //

                        cust_number.setText("");

                        cust_number.requestFocus();
                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
    }

    public void unsuccessful(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                PasswordReset.this);

        // Setting Dialog Title
        alertDialog2.setTitle("PASSWORD RESET UNSUCCESSFUL");
        alertDialog2.setMessage(response2);

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        cust_number.setText("");
                        cust_number.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }

    public void emptyResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                PasswordReset.this);

        // Setting Dialog Title
        alertDialog2.setTitle("PASSWORD RESET UNSUCCESSFUL");
        alertDialog2.setMessage("Please try again");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        cust_number.setText("");
                        cust_number.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }


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

    public void noNetwork() {
        // check if external storage is available and not read only

        AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(
                PasswordReset.this);

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
                PasswordReset.this);

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
}
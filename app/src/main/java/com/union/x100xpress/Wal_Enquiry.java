package com.union.x100xpress;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.StringTokenizer;

import myUrl.UrlClass;

/**
 * Created by USG0011 on 05/23/17.
 */

public class Wal_Enquiry extends AppCompatActivity {
    EditText custNameEditText;
    EditText  defAccEditText;
    EditText acc_link1EditText;
    EditText acc_link2EditText;
    EditText clientType;
    EditText status;
    EditText nextKin;
    EditText wal_number;
    EditText nextKinFon;
    Button okButton;
    String msg = "";
    String nameToken = null;
    String def_accToken = null;
    String acc_1Token = null;
    String acc_2Token = null;
    String clientToken = null;
    String statusToken = null;
    String nxtKinToken = null;
    String nxtKinFonToken = null;
    LinearLayout availCustName,availDefAcc,availAccLink1,availAccLink2,availClientType,availStatus,availNextOfKin,availNextOfKinFon;
    ProgressDialog progress;
    String response = null;
    int responseCode = 0;
    String number = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wal_enquiry);
        setTitle("Wallet Enquiry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wal_number =  (EditText) findViewById(R.id.wallet_number);
        okButton = (Button) findViewById(R.id.okBut);
         custNameEditText = (EditText) findViewById(R.id.custNumEditText);
         defAccEditText = (EditText) findViewById(R.id.defAccEditText);
         acc_link1EditText = (EditText) findViewById(R.id.accLinkEditText1);
         acc_link2EditText = (EditText) findViewById(R.id.accLink2EditText);
         clientType = (EditText) findViewById(R.id.clientTypeEditText);
        status = (EditText) findViewById(R.id.statusEditText);
        nextKin = (EditText) findViewById(R.id.next_kin_EditText);
        nextKinFon = (EditText) findViewById(R.id.next_kin_fon_EditText);

        availCustName = (LinearLayout) findViewById(R.id.avail_cust_name);
        availDefAcc = (LinearLayout) findViewById(R.id.avail_def_acc);
        availAccLink1 = (LinearLayout) findViewById(R.id.avail_acc_link1);
        availAccLink2 = (LinearLayout) findViewById(R.id.avail_acc_link2);
        availClientType = (LinearLayout) findViewById(R.id.avail_client_type);
        availStatus = (LinearLayout) findViewById(R.id.avail_status);
        availNextOfKin = (LinearLayout) findViewById(R.id.avail_next_of_kin);
        availNextOfKinFon = (LinearLayout) findViewById(R.id.avail_next_of_kin_fon);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 number = wal_number.getText().toString();
                System.out.println(number);
                if(number.length() < 10){
                    wal_number.requestFocus();
                    wal_number.setText("");
                    wal_number.setError("Enter a valid Phone Number");
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
            progress = new ProgressDialog(Wal_Enquiry.this);
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
                responseCode = con.getResponseCode();
                System.out.println("ResponseCode is " + responseCode);
            } catch (IOException e) {
                e.printStackTrace();
                progress.cancel();
//                noConnectivity();
            }
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.urlwalnumber);
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
//			accDetails = sp.toString();
            String one = "WALLET NUMBER DOES NOT EXIST";
            String two = "WALLET NUMBER EXISTS";
            progress.cancel();
            if (response.trim().equals(one)){
                //System.out.println("RESPONSE EMPTY");
                walNumberResponse();
            } else if (response.trim().equals(two)){
                //System.out.println("RESPONSE EMPTY");
                walNumberOKResponse();
            }

        }

    }

    public void walNumberOKResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                Wal_Enquiry.this);

        // Setting Dialog Title
        alertDialog2.setTitle("WALLET NUMBER EXISTS");
        alertDialog2.setMessage("Please Continue...");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        msg = number;
                        new fireMan().execute();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    public void walNumberResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                Wal_Enquiry.this);

        // Setting Dialog Title
        alertDialog2.setTitle("WALLET NUMBER DOES NOT EXIST");
        alertDialog2.setMessage("Please try again");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        wal_number.setText("");
                        wal_number.requestFocus();
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
            progress = new ProgressDialog(Wal_Enquiry.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Sending request...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL obj = null;
            try {
                obj = new URL(UrlClass.newurlenq);
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
                //msg = "233261140827";
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr;
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

            } catch (IOException e) {
                e.printStackTrace();
                progress.cancel();
//                noConnectivity();
            }
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.newurlenq);
            System.out.println("Post parameters : " + msg);
         System.out.println("Response Code : " + responseCode);

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

                System.out.println("response:" +  response);

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
                emptyResponse();
            }
         else{


                StringTokenizer token = new StringTokenizer(response, "~");

                nameToken = token.nextToken().trim();
                System.out.println(nameToken);
                availCustName.setVisibility(View.VISIBLE);
                custNameEditText.setText("  " + nameToken);
                custNameEditText.setEnabled(false);



                def_accToken = token.nextToken().trim();
                availDefAcc.setVisibility(View.VISIBLE);
                defAccEditText.setText("  " + def_accToken);
                defAccEditText.setEnabled(false);


                acc_1Token = token.nextToken().trim();
                availAccLink1.setVisibility(View.VISIBLE);
                acc_link1EditText.setText("  " + acc_1Token);
                acc_link1EditText.setEnabled(false);


                acc_2Token = token.nextToken().trim();
                availAccLink2.setVisibility(View.VISIBLE);
                acc_link2EditText.setText(" " + acc_2Token);
                acc_link2EditText.setEnabled(false);

                clientToken = token.nextToken().trim();
                availClientType.setVisibility(View.VISIBLE);
                if (clientToken.equals("1")){ clientType.setText("  Normal Customer");}
               else if(clientToken.equals("2")){clientType.setText("  GN Agent");}
                else if (clientToken.equals("3")) {clientType.setText("  Mobile Banker");}
                clientType.setEnabled(false);

                statusToken = token.nextToken().trim();
                availStatus.setVisibility(View.VISIBLE);
                if (statusToken.equals("A") ){
                status.setText("  Fully Registered");
                }else if(statusToken.equals("I")){
                    status.setText("  Incomplete");
                } else if (statusToken.equals("D"))
                {status.setText("  Deactivated");}
                status.setEnabled(false);


                nxtKinToken = token.nextToken().trim();
                availNextOfKin.setVisibility(View.VISIBLE);
                nextKin.setText("  " + nxtKinToken);
                nextKin.setEnabled(false);



                nxtKinFonToken = token.nextToken().trim();
                availNextOfKinFon.setVisibility(View.VISIBLE);
                nextKinFon.setText("  " + nxtKinFonToken);
                nextKinFon.setEnabled(false);


            }





        }

    }

    public void emptyResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                Wal_Enquiry.this);

        // Setting Dialog Title
        alertDialog2.setTitle("WALLET ENQUIRY UNSUCCESSFUL");
        alertDialog2.setMessage("Connection timed out. Please try again");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        wal_number.setText("");
                        wal_number.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    public void errorConnecting() {
        // check if external storage is available and not read only

        AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
                Wal_Enquiry.this);

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


//    public void onBackPressed() {
//        // TODO Auto-generated method stub
////        SearchByName.searchAccountNum = "";
////        DepositChooseAcc.account = "";
////		SearchByName.searchAccountNum = null;
//        super.onBackPressed();
//
//    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Wal_Enquiry.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                Wal_Enquiry.this);

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
}
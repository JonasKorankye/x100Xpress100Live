package com.union.x100xpress;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import myUrl.UrlClass;

import static android.R.id.progress;

/**
 * Created by USG0011 on 06/15/17.
 */

public class WalTokResetDetails extends AppCompatActivity {

    String  details = null;
    ListView listView;
    String[] brokenToken;
    Button resetBtn;
    String msg = "";
    EditText beneNumber;
    EditText dateEditText;
    EditText transNumEditText;
    EditText amtEditText;
    EditText sendNumEditText;
    String transNumber =null;
    ProgressDialog progress;
    String response2 = "";
    int responseCode2 = 0;

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wal_token_trans_details);
        setTitle("Confirm Transaction Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        resetBtn = (Button) findViewById(R.id.resetBtn);
        beneNumber = (EditText) findViewById(R.id.NumEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        transNumEditText = (EditText) findViewById(R.id.transNumEditText);
        amtEditText = (EditText) findViewById(R.id.amtEditText);
        sendNumEditText = (EditText) findViewById(R.id.sendNumEditText);


        details = getIntent().getExtras().getString("tokenResetDetails");
        System.out.println(details);
        brokenToken = details.split("~");
        String beneNum = brokenToken[0];
        String date = brokenToken[1];
        String transNum = brokenToken[2];
        String amt = brokenToken[3];
        String Sender = brokenToken[4];
        System.out.println("Beneficiary's number: " + beneNum);
        beneNumber.setText(beneNum);
        beneNumber.setEnabled(false);

        dateEditText.setText(date);
        dateEditText.setEnabled(false);

        transNumEditText.setText(transNum);
        transNumEditText.setEnabled(false);

        amtEditText.setText(" GH¢ " + amt);
        amtEditText.setEnabled(false);

        sendNumEditText.setText(Sender);
        sendNumEditText.setEnabled(false);


        resetBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                transNumber = transNumEditText.getText().toString();
                String cus_number = WalletTokenReset.customerNum;
                String AgentNum = MainActivity.userPhoneNum;
                msg = cus_number + "~" + AgentNum + "~" + transNumber;
                new fireTokenResetDetail().execute();
            }

        });


    }

    public class fireTokenResetDetail extends AsyncTask<Void , Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(WalTokResetDetails.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Sending request...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL obj = null;
            try {
                obj = new URL(UrlClass.urlTokenReset);
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
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.urlTokenReset);
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
            String one ="TOKEN RESET WAS SUCCESSFUL";
            //System.out.println(one);
            //String two = "PASSWORD CANNOT BE RESET BECAUSE OF INSUFFICIENT FUNDS IN YOUR WALLET";
            progress.cancel();
            boolean hi =  response2.isEmpty();
            System.out.println(hi);
            //successful();
            if (response2.isEmpty()){
                System.out.println("RESPONSE EMPTY");
                 emptyResponse();
            }
            else if(response2.trim().equals(one) ){
                progress.cancel();
                System.out.println("jo");
                successful();



            }

        }

    }


    public void emptyResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                WalTokResetDetails.this);

        // Setting Dialog Title
        alertDialog2.setTitle("TOKEN RESET UNSUCCESSFUL");
        alertDialog2.setMessage("Please try again");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                 finish();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    public void successful(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                WalTokResetDetails.this);

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
                        Intent back = new Intent(WalTokResetDetails.this, MainMenuList.class);
                        startActivity(back);
                    }
                });
        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        //
                        finish();

                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
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
                WalTokResetDetails.this);

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
                WalTokResetDetails.this);

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
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        finish();
        super.onBackPressed();

    }
}

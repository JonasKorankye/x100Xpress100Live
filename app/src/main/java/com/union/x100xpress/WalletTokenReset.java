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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import myUrl.UrlClass;

//import static com.union.x100xpress.AmendBR.responseCode;

/**
 * Created by USG0011 on 05/25/17.
 */

public class WalletTokenReset extends AppCompatActivity {

     EditText cus_number;
    Button okButton;
    String msg ="";
    ProgressDialog progress;
    String response2 = "";
    int responseCode2 = 0;
    String tokenResetMain = null;
    List<String> tokenResetList;
    String[] array;
    String bene_num;
    String senders_nam;
    String selectedText;
    ListView listView;
    TextView textView;
    String key;
     static String customerNum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wal_token_reset);
        setTitle("Wallet Token Reset");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cus_number =  (EditText) findViewById(R.id.cust_number);
        okButton = (Button) findViewById(R.id.okBut);
         tokenResetList = new ArrayList<String>();
         listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                customerNum = cus_number.getText().toString();
                System.out.println(customerNum);
                if(customerNum.length() < 10){
                    cus_number.requestFocus();
                    cus_number.setText("");
                    cus_number.setError("Enter a valid Phone Number");
                }
                else if (customerNum.length() == 10  || customerNum.length()==12 )
                {   msg = customerNum;
                    new fireMan().execute();

                }

            }


        });
        //okButton.setEnabled(true);
    }


    public class fireMan extends AsyncTask<Void , Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(WalletTokenReset.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Sending request...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL obj = null;
            try {
                obj = new URL(UrlClass.urlTokenResetClaims);
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
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.urlTokenResetClaims);
            System.out.println("Post parameters : " + msg);
            System.out.println("Response Code : " + responseCode2);

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
            if (response2.trim().isEmpty()){
                System.out.println("RESPONSE EMPTY");
                emptyResponse();
            } else if (response2.trim().equals("WALLET NUMBER HAS NO RECORD OF UNCLAIMED TRANSACTIONS"))
            {
                System.out.println("Not in claims table");
                numberNotFound();
            }
            else{

                //ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(WalletTokenReset.this, android.R.layout.simple_list_item_1, android.R.id.text1, tokenResetList);
                textView.setVisibility(View.VISIBLE);
                ArrayAdapter adapter = new ArrayAdapter<String>(WalletTokenReset.this,android.R.layout.simple_list_item_1,tokenResetList);
                StringTokenizer tokenResetToken = new StringTokenizer(response2, "|");

                int i= tokenResetToken.countTokens();
                System.out.println(i);

                for (int j=0; j<=i-1; j++ ){
                    tokenResetMain = tokenResetToken.nextToken().trim();
                    array = tokenResetMain.split("~");
                    bene_num = array[0];
                    senders_nam = array[4];

                    tokenResetList.add("FROM " + senders_nam + " TO " + bene_num);
                    listView.setAdapter(adapter);
                    //okButton.setEnabled(false);
//                    listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view,
//                                                   int position, long id) {
//                            System.out.println("hii");
//                            selectedText = (String) parent.getItemAtPosition(position);
//
//                            Toast.makeText(WalletTokenReset.this, (CharSequence) tokenResetList, Toast.LENGTH_SHORT).show();
////                            Intent walletTokenResetIntent = new Intent(WalletTokenReset.this, WalTokResetDetails.class);
////                            walletTokenResetIntent.putExtra("tokenResetDetails", selectedText);
////                            startActivity(walletTokenResetIntent);
//
////                            Bundle b=new Bundle();
////                            b.putStringArray(key, new String[]{array.toString()});
////                            Intent i=new Intent(WalletTokenReset.this, WalTokResetDetails.class);
////                            i.putExtras(b);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//                            // TODO Auto-generated method stub
//                        }
//                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub
                           // Toast.makeText(WalletTokenReset.this, array[position], Toast.LENGTH_SHORT).show();

                            Intent walletTokenResetIntent = new Intent(WalletTokenReset.this, WalTokResetDetails.class);
                            walletTokenResetIntent.putExtra("tokenResetDetails", tokenResetMain);
                            startActivity(walletTokenResetIntent);
                        }
                    });

                }
                }



//                Intent walletTokenResetIntent = new Intent(WalletTokenReset.this, ListWalMain.class);
//                walletTokenResetIntent.putExtra("details", response2);
//                startActivity(walletTokenResetIntent);





            }

        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                WalletTokenReset.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void emptyResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                WalletTokenReset.this);

        // Setting Dialog Title
        alertDialog2.setTitle("TOKEN RESET UNSUCCESSFUL");
        alertDialog2.setMessage("Connection timed out. Please try again.");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        cus_number.setText("");
                        cus_number.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }

    public void numberNotFound(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                WalletTokenReset.this);

        // Setting Dialog Title
        alertDialog2.setTitle("TOKEN RESET UNSUCCESSFUL");
        alertDialog2.setMessage("Wallet Number has no record of unclaimed transactions.");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        cus_number.setText("");
                        cus_number.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
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
                WalletTokenReset.this);

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
                WalletTokenReset.this);

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

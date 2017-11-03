package com.union.x100xpress;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import myUrl.UrlClass;

//import static com.union.x100xpress.AmendBR.response2;

/**
 * Created by USG0011 on 05/23/17.
 */

public class Wallet_Trans_Enquiry extends AppCompatActivity {

     EditText fromDateEtxt;
     EditText toDateEtxt;
     EditText agentText;
     EditText walletText;
     Button button;
     DatePickerDialog fromDatePickerDialog;
     DatePickerDialog toDatePickerDialog;
    ProgressDialog progress;
    private SimpleDateFormat dateFormatter;
    int responseCode = 0;
    String response = null;
    String response2 = null;
    String oneToken = null;
    String twoToken = null;

    //static String msg = "USER~233261140827~15-09-2016~15-09-2016";
     String msg = "";
    String walId = null; String one = null; String three = null; String four  = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wal_trans_enquiry);
        setTitle("Wallet Transaction Enquiry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        //agentText = (EditText) findViewById(R.id.agentEditText);
        //agentText.setInputType(InputType.TYPE_NULL);
        walletText = (EditText) findViewById(R.id.editWalText);
        Calendar newCalendar = Calendar.getInstance();

         button = (Button) findViewById(R.id.trans_button);


        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                String from = dateFormatter.format(newDate.getTime());
                System.out.println(from);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                String to = dateFormatter.format(newDate.getTime());
                System.out.println(to);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        fromDateEtxt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fromDatePickerDialog.show();
                return false;
            }
        });


        toDateEtxt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                toDatePickerDialog.show();
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
//                Intent transEnquiryIntent = new Intent(Wallet_Trans_Enquiry.this, WalletTransDetails.class);
//                startActivity(transEnquiryIntent);
                 //one= agentText.getText().toString();
                one= MainActivity.userPhoneNum;
                walId = walletText.getText().toString();
                four = toDateEtxt.getText().toString();
                three= fromDateEtxt.getText().toString();
                if(walId.length() < 10){
                    walletText.requestFocus();
                    walletText.setText("");
                    walletText.setError("Enter a valid Phone Number");
                }
                else if(three.length()==0){
                    walletText.requestFocus();
                    walletText.setText("");
                    walletText.setError("Enter a valid Phone Number");

                }else if(four.length()==0) {
                    walletText.requestFocus();
                    walletText.setText("");
                    walletText.setError("Enter a valid Phone Number");
                }else if (walId.length() == 10  || walId.length()==12 )
                {    msg = walId;
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
            progress = new ProgressDialog(Wallet_Trans_Enquiry.this);
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
                Wallet_Trans_Enquiry.this);

        // Setting Dialog Title
        alertDialog2.setTitle("WALLET NUMBER EXISTS");
        alertDialog2.setMessage("Please Continue...");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        msg = walId + "~" + one + "~" + three + "~" + four;
                        System.out.println("tins:" + msg);
                        new fireMan().execute();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    public void walNumberResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                Wallet_Trans_Enquiry.this);

        // Setting Dialog Title
        alertDialog2.setTitle("WALLET NUMBER DOES NOT EXIST");
        alertDialog2.setMessage("Please try again");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        walletText.setText("");
                        walletText.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    public class fireMan extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(Wallet_Trans_Enquiry.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Sending request...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL obj = null;
            try {
                obj = new URL(UrlClass.newurl);
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
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.newurl);
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
                        response2 = inputLine;
                        System.out.println(response2);
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
                System.out.println("True TransDetails: "+ response2);

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
            if (response2==null){
                System.out.println("RESPONSE EMPTY");
                emptyResponse();
            }
            else if (response2 != null) {
                System.out.println("Worked");
//                finish();
//
//                StringTokenizer token = new StringTokenizer(response, "|");
//                int i= token.countTokens();
//                oneToken = token.nextToken().trim();
//                System.out.println(i);
//                for (int l=0; l<=i-1; l++){
//
//                    System.out.println(oneToken);
//                    StringTokenizer oT = new StringTokenizer(oneToken, "~");
//                    int j = oT.countTokens();
//                    System.out.println(j);
//                    for (int b=0; b<=j-1; b++){
//                        twoToken = oT.nextToken().trim();
//                        System.out.println(twoToken);
//                    }
//                }






                Intent transEnquiryIntent = new Intent(Wallet_Trans_Enquiry.this, ListWalMain.class);
                transEnquiryIntent.putExtra("details", response2);
                startActivity(transEnquiryIntent);


            }

        }

    }

    public void emptyResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                Wallet_Trans_Enquiry.this);

        // Setting Dialog Title
        alertDialog2.setTitle("WALLET ENQUIRY UNSUCCESSFUL");
        alertDialog2.setMessage("No data found for date range chosen");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        walletText.setText("");
                        walletText.requestFocus();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Wallet_Trans_Enquiry.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void errorConnecting() {
        // check if external storage is available and not read only

        AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
                Wallet_Trans_Enquiry.this);

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
                Wallet_Trans_Enquiry.this);

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

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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

import static com.union.x100xpress.MainActivity.*;

/**
 * Created by USG0011 on 05/25/17.
 */

public class AmendBR extends AppCompatActivity {
    String selectedStatusText = null;
    String selectedIdText = null;
    String selectedClientText = null;
    EditText custNoEditText;
    EditText custNameEditText;
    EditText  defAccEditText;
    EditText acc_link1EditText;
    EditText acc_link2EditText;
    EditText clientType;
    EditText idTypeEditText;
    EditText status;
    EditText nextKin;
     EditText wal_number;
    EditText nextKinFon;
    Button okButton;
    Button amend_button;
     String msg = "";
    String numToken = null;
    String nameToken = null;
    String def_accToken = null;
    String acc_1Token = null;
    String acc_2Token = null;
    String clientToken = null;
    String statusToken = null;
    String nxtKinToken = null;
    String nxtKinFonToken = null;
    LinearLayout  availCustName,availClientType,availIdType,availStatus,availNextOfKin,availNextOfKinFon;
     ProgressDialog progress;
     String response = null;
     int responseCode = 0;
    Spinner dynamicSpinner = null;
    Spinner statusSpinner = null;
    Spinner clientSpinner = null;
     String response2 = null;
    int responseCode2 = 0; int responseCode3 = 0;String response3 = null;
    String num2Token = null;
    String detailsToken = null;
    String customerName;
    String number = null; String agent;String name;String Status;String client;String idType;String nKin;String nKinFon;
    String selectedClientNum; String selectedIdNum; String selectedStatusNum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amend_br);
        setTitle("User Amendment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wal_number =  (EditText) findViewById(R.id.wallet_number);
        okButton = (Button) findViewById(R.id.okBut);
       // custNoEditText = (EditText) findViewById(R.id.custNoEditText);
        custNameEditText = (EditText) findViewById(R.id.custNameEditText);
        //defAccEditText = (EditText) findViewById(R.id.defAccEditText);
        acc_link1EditText = (EditText) findViewById(R.id.accLinkEditText1);
        acc_link2EditText = (EditText) findViewById(R.id.accLink2EditText);
        clientType = (EditText) findViewById(R.id.clientTypeEditText);
        idTypeEditText = (EditText) findViewById(R.id.idTypeEditText);
       // status = (EditText) findViewById(R.id.statusEditText);
        nextKin = (EditText) findViewById(R.id.next_kin_EditText);
        nextKinFon = (EditText) findViewById(R.id.next_kin_fon_EditText);
        amend_button = (Button) findViewById(R.id.amend_button);

        availCustName = (LinearLayout) findViewById(R.id.avail_cust_name);

        availClientType = (LinearLayout) findViewById(R.id.avail_client_type);
        availIdType  = (LinearLayout) findViewById(R.id.availIdType);
        availStatus = (LinearLayout) findViewById(R.id.avail_status);
        availNextOfKin = (LinearLayout) findViewById(R.id.avail_next_of_kin);
        availNextOfKinFon = (LinearLayout) findViewById(R.id.avail_next_of_kin_fon);
        dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);
        statusSpinner = (Spinner) findViewById(R.id.status_spinner);
        clientSpinner = (Spinner) findViewById(R.id.client_spinner);

        amend_button.setVisibility(View.INVISIBLE);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 number = wal_number.getText().toString();
                System.out.println(number.length() == 10  || number.length()==12);
                if(number.length() < 10){
                    wal_number.requestFocus();
                    wal_number.setText("");
                    wal_number.setError("Enter a valid Phone Number");
                }
                else if (number.length() == 10  || number.length()==12 )
                {    msg = number;
                    new   fireManWalNumber().execute();
//                    wal_number.requestFocus();
//                    wal_number.setText("");
                    //wal_number.setError("Enter a valid Phone Number");
                }
            }

        });

        amend_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 number = wal_number.getText().toString().trim();
                 agent= MainActivity.userPhoneNum.trim();
                //String custNo =  custNoEditText.getText().toString(); String defAcc = defAccEditText.getText().toString();
                 name = wal_number.getText().toString().trim();
                //String acc1 =  acc_link1EditText.getText().toString();
                //String acc2 =  acc_link2EditText.getText().toString();
                customerName= custNameEditText.getText().toString().trim();
                 Status =  selectedStatusNum.trim();
                 client =  selectedClientNum.trim();
                System.out.println(client);   idType =  selectedIdNum.trim();
                System.out.println(idType);
                 nKin =  nextKin.getText().toString().trim();   nKinFon =  nextKinFon.getText().toString().trim();

                msg = number + "~" + agent + "~"  + customerName + "~" + Status + "~" + client + "~" +idType + "~" + nKin + "~" + nKinFon;

                confirmSave();
                //new  fireManSave().execute();
            }

        });
    }


    public class fireManWalNumber extends AsyncTask<Void , Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(AmendBR.this);
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
    public class fireMan extends AsyncTask<Void , Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(AmendBR.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Sending request...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL obj = null;
            try {
                obj = new URL(UrlClass.urlamendOK);
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
                responseCode2 = con.getResponseCode();

            } catch (IOException e) {
                e.printStackTrace();
                progress.cancel();
//                noConnectivity();
            }
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.urlamendOK);
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

                System.out.println("response:" +  response2);

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

            progress.cancel();

            if (response2.isEmpty()){
                System.out.println("RESPONSE EMPTY");
                emptyResponse();
            }
            else{
                amend_button.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.INVISIBLE);

                StringTokenizer token = new StringTokenizer(response2, "~");

                nameToken = token.nextToken().trim();
                System.out.println(nameToken);
                if(!nameToken.trim().isEmpty()){

                    custNameEditText.setText("  " + nameToken);
                }else {
                    custNameEditText.setText("");
                }
                availCustName.setVisibility(View.VISIBLE);
                custNameEditText.setEnabled(true);


                clientToken = token.nextToken().trim();
                availClientType.setVisibility(View.VISIBLE);
                final String[] clients = new String[] { "NORMAL CUSTOMER", "GN AGENT" };

                ArrayAdapter<String> clientAdapter = new ArrayAdapter<String>(AmendBR.this, android.R.layout.simple_spinner_item, clients);

                clientSpinner.setAdapter(clientAdapter);
                clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        selectedClientText = (String) parent.getItemAtPosition(position);
                        //String IdSelected = parent.getItemAtPosition(position).toString();
                        if (selectedClientText.equals(clients[0])){
                            dynamicSpinner.setEnabled(true);
                            selectedClientNum = "1";
                        }
                        else {
                            selectedClientNum = "2";
                            dynamicSpinner.setEnabled(true);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

                if (clientToken.equals("1")  )
               {  clientSpinner.setSelection(0);
                    availIdType.setVisibility(View.VISIBLE);
                   //dynamicSpinner.setEnabled(true);
                    final String[] items = new String[] { "National ID", "Other ID" };

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AmendBR.this, android.R.layout.simple_spinner_item, items);

                    dynamicSpinner.setAdapter(adapter);
                    dynamicSpinner.setEnabled(true);

                    dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                             selectedIdText = (String) parent.getItemAtPosition(position);
                            //String IdSelected = parent.getItemAtPosition(position).toString();
                            if (selectedIdText.equals(items[0])){

                                selectedIdNum = "1";
                            }else {
                                selectedIdNum = "2";
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                });
                }


                else if ( clientToken.equals("2") )
                {  clientSpinner.setSelection(1);
                    availIdType.setVisibility(View.VISIBLE);
                    //dynamicSpinner.setEnabled(true);
                    final String[] items = new String[] { "National ID", "Other ID" };

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AmendBR.this, android.R.layout.simple_spinner_item, items);

                    dynamicSpinner.setAdapter(adapter);
                    dynamicSpinner.setEnabled(true);

                    dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            selectedIdText = (String) parent.getItemAtPosition(position);
                            //String IdSelected = parent.getItemAtPosition(position).toString();
                            if (selectedIdText.equals(items[0])){

                                selectedIdNum = "1";
                            }else {
                                selectedIdNum = "2";
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            //
                            //TODO Auto-generated method stub
                        }
                    });
                }

//                if(clientSpinner.equals(1)){
//                    availIdType.setVisibility(View.VISIBLE);
//                    dynamicSpinner.setEnabled(false);
//                }
                //else { clientSpinner.setSelection(2); availIdType.setVisibility(View.VISIBLE); dynamicSpinner.setEnabled(true);}
                //clientType.setEnabled(true);

                statusToken = token.nextToken().trim();
                availStatus.setVisibility(View.VISIBLE);
                final String[] statusItems = new String[] { "Fully Registered", "Incomplete", "Deactivated" };

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AmendBR.this, android.R.layout.simple_spinner_item, statusItems);

                statusSpinner.setAdapter(adapter);
                statusSpinner.setEnabled(true);

                statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                         selectedStatusText = (String) parent.getItemAtPosition(position);
                        if (selectedStatusText.equals(statusItems[0])){
                            statusSpinner.setSelection(0);
                            selectedStatusNum = "A";
                        }else if(selectedStatusText.equals(statusItems[1])){
                            statusSpinner.setSelection(1);
                            selectedStatusNum = "I";
                        } else{
                            statusSpinner.setSelection(2);
                            selectedStatusNum = "D";
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

//                if (statusToken == "A"){
//                    status.setText("  Active");
//                }else if(statusToken == "I"){
//                    status.setText("  Incompleted");
//                } else{status.setText("  Deactivated");}
//                status.setEnabled(true);


                nxtKinToken = token.nextToken().trim();
                if(!nxtKinToken.isEmpty()){

                    nextKin.setText("  " + nxtKinToken);
                }else{
                    nextKin.setText("");
                }

                     availNextOfKin.setVisibility(View.VISIBLE);
                     nextKin.setEnabled(true);



                String ds = token.nextToken();
                System.out.println("ds: " + ds);
                nxtKinFonToken = ds;
                System.out.println(!nxtKinFonToken.isEmpty());
                if(!nxtKinFonToken.isEmpty())
                {
                    nextKinFon.setText("  " + nxtKinFonToken);
                }else if(nxtKinFonToken.trim().equals("null")){
                    nextKinFon.setText("");
                }
                nextKinFon.setEnabled(true);
                availNextOfKinFon.setVisibility(View.VISIBLE);






            }





        }

    }

    public class fireManSave extends AsyncTask<Void , Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(AmendBR.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Sending request...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL obj = null;
            try {
                obj = new URL(UrlClass.urlamendSave);
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
                responseCode3 = con.getResponseCode();
                System.out.println("ResponseCode3 is " + responseCode3);
            } catch (IOException e) {
                e.printStackTrace();
                progress.cancel();
//                noConnectivity();
            }
            System.out.println("\nSending 'POST' request to URL : " + UrlClass.urlamendSave);
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
            System.out.println(response3);
            StringTokenizer saveToken = new StringTokenizer(response3, "~");
              detailsToken = saveToken.nextToken().trim();
              num2Token  =  saveToken.nextToken().trim();
            System.out.println(response3.trim().isEmpty());
			progress.cancel();
            if (response3.trim().isEmpty()){
                progress.cancel();
                System.out.println("RESPONSE EMPTY");
                emptyOKResponse();
            }
            else if(num2Token.trim().equals("21")){
                progress.cancel();
                successful();
                //								openPrintApp();
            }else if(num2Token.trim().equals("27")){
                progress.cancel();
                pendingSuccessful();

            } else if(num2Token.trim().equals("22")|| num2Token.trim().equals("23")|| num2Token.trim().equals("24")|| num2Token.trim().equals("25")|| num2Token.trim().equals("26")){
                progress.cancel();
                unsuccessful();

            }

        }

    }


    public void confirmSave(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                AmendBR.this);

        // Setting Dialog Title
        alertDialog.setTitle("CONFIRM USER AMENDMENT");
        alertDialog.setMessage("CUSTOMER NAME: " + customerName + "\n" +  "\n"
                +"CLIENT TYPE: " + " " + selectedClientText + "\n" +  "\n"
                +"ID TYPE: " + " " + selectedIdText + "\n" +  "\n"
                +"STATUS: " + " " + selectedStatusText + "\n" +  "\n"
                +"NEXT OF KIN: " + " "+ nKin + "\n" +  "\n"
                +"Next OF KIN PHONE: " + " "+ nKinFon + "\n\n"
               );

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                       // dialog.dismiss();
                        new  fireManSave().execute();
//                        DepositChooseAcc.account = "";
//                        SearchByName.searchAccountNum = "";
                        //finish();

                    }
                });
        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        //
                        dialog.cancel();
                        //finish();

                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                AmendBR.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void successful(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                AmendBR.this);

        // Setting Dialog Title
        alertDialog.setTitle(detailsToken);
        alertDialog.setMessage("Pending Approval");

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.dismiss();
//                        DepositChooseAcc.account = "";
//                        SearchByName.searchAccountNum = "";
                        finish();
                        Intent back = new Intent(AmendBR.this, MainMenuList.class);
                        startActivity(back);
                    }
                });
        // Setting Negative "NO" Btn


        // Showing Alert Dialog
        alertDialog.show();
    }
    public void pendingSuccessful(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                AmendBR.this);

        // Setting Dialog Title
        alertDialog.setTitle("USER AMENDMENT PENDING APPROVAL");
        alertDialog.setMessage(detailsToken);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.dismiss();
//                        DepositChooseAcc.account = "";
//                        SearchByName.searchAccountNum = "";
                        finish();
                        Intent back = new Intent(AmendBR.this, MainMenuList.class);
                        startActivity(back);
                    }
                });
        // Setting Negative "NO" Btn


        // Showing Alert Dialog
        alertDialog.show();
    }
    public void walNumberResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                AmendBR.this);

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

    public void walNumberOKResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                AmendBR.this);

        // Setting Dialog Title
        alertDialog2.setTitle("WALLET NUMBER EXISTS");
        alertDialog2.setMessage("Please Continue...");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                        System.out.println(number);
                        msg = number;
                        new  fireMan().execute();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }
    public void unsuccessful(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                AmendBR.this);

        // Setting Dialog Title
        alertDialog2.setTitle("USER AMENDMENT UNSUCCESSFUL");
        alertDialog2.setMessage(detailsToken);

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

    public void emptyOKResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                AmendBR.this);

        // Setting Dialog Title
        alertDialog2.setTitle("USER AMENDMENT UNSUCCESSFUL");
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
    public void emptyResponse(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                AmendBR.this);

        // Setting Dialog Title
        alertDialog2.setTitle("USER AMENDMENT UNSUCCESSFUL");
        alertDialog2.setMessage("Connection Timed Out. Please try again");

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
                AmendBR.this);

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



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        finish();
        super.onBackPressed();

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
                AmendBR.this);

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

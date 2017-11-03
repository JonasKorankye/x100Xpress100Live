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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import myUrl.UrlClass;


public class ProdDetails extends Fragment /*implements OnItemSelectedListener*/ {

    View rootView;
    private ViewPager viewPager;

    private static final String NAMESPACE = "http://webpackage/";
    //    private static final String URL = /*"http://192.168.1.26:8680/web_service/HelloWorldPortType";*/
//			/*"http://192.168.43.32:8680/web_service/HelloWorldPortType"; */
//			/*"http://192.168.180.23:8680/web_service/HelloWorldPortType";*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/ /*fnsl public*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
//            "http://192.168.1.102:8080/web_service/HelloWorldPortType";
//    //			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
////			"http://192.168.1.8:8680/web_service/HelloWorldPortType";
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
    private static final String SOAP_ACTION = "my_request";
    private static final String METHOD_NAME = "ServiceOperation";
    static SoapPrimitive sp;
    Thread thread;
//    String ratty = "";

    ProgressDialog progress;
    static String choice, accNum;
    static String openAccMsg = "";
    String prodMsg = null;
    String currType = null;
    String currMsg = null;
    static String stuff[] = {"", "", ""};
    String noOfLeaves[] = {"25", "50", "100"};
    String prodList[] = {"Savings Account"/*, "Current Account"*/};
    ArrayAdapter<String> adapter;
    String acctType = null;
    Button button1;

    static String stuff2[] = {"", "", ""};
    static String sectors = null;

    String account = "";
    ListView acc_list;
    Spinner spinner;
    File myFile;

    Button prodButton;
    Spinner spinner1;

    String prod = null;
    String product = null;
    String prodDesc = null;

    String prodCode = null;
    String prodSGroup = null;
    String prodGrpDesc = null;

    String pCode = null;
    String pSGroup = null;
    String pGrpDesc = null;

    static String sendProd = null;
    static String sendSubProd = null;

    ListView list;
    String msg = null;

    int[] next = new int[]{R.drawable.more};


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //		mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        rootView = inflater.inflate(R.layout.fragment_prod_details, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        viewPager.setBackgroundResource(R.color.action_bar_background);

        prodButton = (Button) rootView.findViewById(R.id.prod_button);
        spinner1 = (Spinner) rootView.findViewById(R.id.choose_prod);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, prodList);

        spinner1.setAdapter(adapter);

//		AccountNum.choice = "PD";

//		AccountNum.msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword;

		/*progress = new ProgressDialog(getActivity());
        progress.setTitle("Sending");
		progress.setMessage("Please wait...");
		progress.show();
//		retrieveProducts(AccountNum.msg);
		thread.start();*/


        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //				accountNum = parent.getItemAtPosition(pos).toString();
//				prod = ((TextView) view.findViewById(R.id.prod_code)).getText().toString().trim();
                System.out.println("prod " + prod);
                switch (position) {
                    case 0:
                        prod = "2";
                        sendProd = "2";
                        break;
				/*case 1:
					prod = "1";
					sendProd = "1";
					break;*/
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing

            }
        });

        prodButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (prod.equals("")) {
                    prod = "2";
                    sendProd = "2";
                } else {
                }
                AccountNum.choice = "LE";

                msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + prod;
                new fireMan().execute();

//                progress = new ProgressDialog(getActivity());
//                progress.setTitle("Sending");
//                progress.setMessage("Please wait...");
//                progress.show();
//                retrieveSubProducts(AccountNum.msg);
//                thread.start();
            }
        });

        return rootView;

    }


    public void noNetworkConn() {

        try {

            getActivity().runOnUiThread(new Runnable() {

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
                getActivity());

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
                getActivity());

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
            Toast.makeText(getActivity(),
                    "Done writing SD 'myfile.txt'",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void makelist() {
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stuff);
    }


//    public void retrieveProducts(final String message){
//        thread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//
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
//                try{
//                    androidHttpTransport.call(SOAP_ACTION, envelope);
//
//                    sp = (SoapPrimitive) envelope.getResponse();
//
//                    getActivity().runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            ratty = sp.toString();
//                            System.out.println(ratty);
//                            stuff = ratty.split("~");
//
//                            progress.cancel();
//
//                            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
//
//                            for(int a=0;a<stuff.length -1;a++){
//                                StringTokenizer myAccToken = new StringTokenizer(stuff[a], ",");
//
//                                try {
//                                    while (myAccToken.hasMoreTokens()){
//                                        prodDesc = myAccToken.nextToken().trim();
//                                        product = myAccToken.nextToken().trim();
//
//                                        System.out.println(prodDesc);
//                                        System.out.println(product);
//                                    }
//
//                                } catch (NoSuchElementException e) {
//                                    // TODO Auto-generated catch block
//                                    //										Toast.makeText(TransactionDetails.this, "token empty", Toast.LENGTH_SHORT);
//                                    //										done = 1;
//                                    e.printStackTrace();
//                                }
//
//                                HashMap<String, String> hm = new HashMap<String,String>();
//
//                                hm.put("desc", prodDesc);
//                                hm.put("code",  product  );
////								hm.put("next", Integer.toString(next[0]));
//                                aList.add(hm);
//
//                            }
//
//                            String[] from = { /*"next",*/ "prod","desc" };
//                            int[] to = { /*R.id.flag,*/R.id.prod_code,R.id.prod_desc};
//
////							SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.list_prod , from , to);
////
////							spinner1.setAdapter(adapter);
//
//
//                        }
//                    });
//
//                }catch (SocketTimeoutException e) {
//                    progress.cancel();
//                    noConnectivity();
//
//                }catch (IOException e) {
//
//                    e.printStackTrace();
//
//                }
//
//                catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


    int responseCode = 0;
    String response = null;


    public class fireMan extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
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
                stuff = response.split("~");
                sectors = stuff[1];
                stuff2 = stuff[0].split(";");

                progress.cancel();

                List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

                for (int a = 0; a < stuff2.length; a++) {
                    StringTokenizer myAccToken = new StringTokenizer(stuff2[a], ",");

//								try {
//									while (myAccToken.hasMoreTokens()){
                    prodCode = myAccToken.nextToken().trim();
                    prodSGroup = myAccToken.nextToken().trim();
                    prodGrpDesc = myAccToken.nextToken().trim();

                    System.out.println(prodCode);
                    System.out.println(prodSGroup);
                    System.out.println(prodGrpDesc);
//									}

//								} catch (NoSuchElementException e) {
//									// TODO Auto-generated catch block
//									//										Toast.makeText(TransactionDetails.this, "token empty", Toast.LENGTH_SHORT);
//									//										done = 1;
//									e.printStackTrace();
//								}

                    HashMap<String, String> hm = new HashMap<String, String>();

                    hm.put("code", prodCode);
                    hm.put("prod", prodSGroup);
                    hm.put("desc", prodGrpDesc);
                    hm.put("next", Integer.toString(next[0]));
                    aList.add(hm);

                }

                String[] from = {"next", "code", "prod", "desc"};
                int[] to = {R.id.flag, R.id.p_code, R.id.sub_grp, R.id.grp_desc};

                SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.list_sub_prod, from, to);
                list = (ListView) rootView.findViewById(R.id.trans_list);
                list.setAdapter(adapter);


                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                            long arg3) {

                        pCode = ((TextView) v.findViewById(R.id.p_code)).getText().toString().trim();
                        pSGroup = ((TextView) v.findViewById(R.id.sub_grp)).getText().toString().trim();
                        pGrpDesc = ((TextView) v.findViewById(R.id.grp_desc)).getText().toString().trim();
                        sendSubProd = pSGroup;
                        System.out.println("finally......" + pSGroup);
                        System.out.println("finally......" + pGrpDesc);
//									Intent d = new Intent(TransactionDetails.this, TransactionChoice.class);
//									d.putExtra("transDetails", transDetails);
//									d.putExtra("date", transDate);
//									d.putExtra("amount", formatAmounts(transAmt));

                        viewPager.setCurrentItem(1, true);
                    }
                });
            }

        }

    }


//    public void retrieveSubProducts(final String message){
//        thread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//
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
//                try{
//                    androidHttpTransport.call(SOAP_ACTION, envelope);
//
//                    sp = (SoapPrimitive) envelope.getResponse();
//
//                    getActivity().runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            ratty = sp.toString();
//                            System.out.println(ratty);
//                            stuff = ratty.split("~");
//                            sectors = stuff[1];
//                            stuff2 = stuff[0].split(";");
//
//                            progress.cancel();
//
//                            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
//
//                            for(int a=0;a<stuff2.length;a++){
//                                StringTokenizer myAccToken = new StringTokenizer(stuff2[a], ",");
//
////								try {
////									while (myAccToken.hasMoreTokens()){
//                                prodCode = myAccToken.nextToken().trim();
//                                prodSGroup = myAccToken.nextToken().trim();
//                                prodGrpDesc = myAccToken.nextToken().trim();
//
//                                System.out.println(prodCode);
//                                System.out.println(prodSGroup);
//                                System.out.println(prodGrpDesc);
////									}
//
////								} catch (NoSuchElementException e) {
////									// TODO Auto-generated catch block
////									//										Toast.makeText(TransactionDetails.this, "token empty", Toast.LENGTH_SHORT);
////									//										done = 1;
////									e.printStackTrace();
////								}
//
//                                HashMap<String, String> hm = new HashMap<String,String>();
//
//                                hm.put("code", prodCode);
//                                hm.put("prod",  prodSGroup);
//                                hm.put("desc",  prodGrpDesc);
//                                hm.put("next", Integer.toString(next[0]));
//                                aList.add(hm);
//
//                            }
//
//                            String[] from = { "next", "code",  "prod", "desc" };
//                            int[] to = { R.id.flag, R.id.p_code, R.id.sub_grp, R.id.grp_desc};
//
//                            SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.list_sub_prod , from , to);
//                            list = (ListView) rootView.findViewById(R.id.trans_list);
//                            list.setAdapter(adapter);
//
//
//
//                            list.setOnItemClickListener(new OnItemClickListener() {
//
//                                @Override
//                                public void onItemClick(AdapterView<?> arg0, View v, int arg2,
//                                                        long arg3) {
//
//                                    pCode = ((TextView) v.findViewById(R.id.p_code)).getText().toString().trim();
//                                    pSGroup = ((TextView) v.findViewById(R.id.sub_grp)).getText().toString().trim();
//                                    pGrpDesc = ((TextView) v.findViewById(R.id.grp_desc)).getText().toString().trim();
//                                    sendSubProd = pSGroup;
//                                    System.out.println("finally......" + pSGroup);
//                                    System.out.println("finally......" + pGrpDesc);
////									Intent d = new Intent(TransactionDetails.this, TransactionChoice.class);
////									d.putExtra("transDetails", transDetails);
////									d.putExtra("date", transDate);
////									d.putExtra("amount", formatAmounts(transAmt));
//
//                                    viewPager.setCurrentItem(1,true);
//                                }
//                            });
//
//
//                        }
//                    });
//
//                }catch (SocketTimeoutException e) {
//                    progress.cancel();
//                    noConnectivity();
//
//                }catch (IOException e) {
//
//                    e.printStackTrace();
//
//                }
//
//                catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


//    public void retrieveCurrency(final String message){
//        thread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//
//                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//                PropertyInfo pi = new PropertyInfo();
//                pi.setNamespace(NAMESPACE);
//                pi.setType(PropertyInfo.STRING_CLASS);
//                pi.setName("Input");
//                pi.setValue(message);
//                request.addProperty(pi);
//                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
//                envelope.setOutputSoapObject(request);
//
//                HttpTransportSE androidHttpTransport = new HttpTransportSE(UrlClass.url, 10000);
//
//                try{
//                    androidHttpTransport.call(SOAP_ACTION, envelope);
//
//                    sp = (SoapPrimitive) envelope.getResponse();
//
//                    getActivity().runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            ratty = sp.toString();
//                            Toast.makeText(getActivity(), ratty, Toast.LENGTH_LONG).show();
//
//                        }
//                    });
//
//                }catch (SocketTimeoutException e) {
//                    progress.cancel();
//                    noConnectivity();
//
//                }catch (IOException e) {
//
//                    e.printStackTrace();
//
//                }
//
//                catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


//    public void sendRequest(final String message){
//        thread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//
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
//                try{
//                    androidHttpTransport.call(SOAP_ACTION, envelope);
//
//                    sp = (SoapPrimitive) envelope.getResponse();
//
//                    getActivity().runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            //										progress.cancel();
//                            ratty = sp.toString();
//
//                        }
//                    });
//
//                }catch (SocketTimeoutException e) {
//                    progress.cancel();
//                    //							noConnectivity();
//                }catch (IOException e) {
//
//                    e.printStackTrace();
//                    //System.out.println("Error with response");
//                }
//
//                catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public void noConnectivity() {

        try {

            getActivity().runOnUiThread(new Runnable() {

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


	/*public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
//		int arrayPosition = spinner.getSelectedItemPosition();

		acctType = String.valueOf(spinner.getSelectedItem());

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}*/


}

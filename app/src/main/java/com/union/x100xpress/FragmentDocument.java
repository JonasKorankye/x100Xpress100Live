package com.union.x100xpress;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import myUrl.UrlClass;


public class FragmentDocument extends Fragment {

    private static final String NAMESPACE = "http://webpackage/";
    //	private static final String URL = /*"http://192.168.1.26:8680/web_service/HelloWorldPortType";*/
//			/*"http://192.168.43.32:8680/web_service/HelloWorldPortType"; */
//			/*"http://192.168.180.23:8680/web_service/HelloWorldPortType";*/
//			/*"http://197.221.81.162:8680/web_service/HelloWorldPortType";*/ /*fnsl public*/
////			"http://192.168.10.80:8680/web_service/HelloWorldPortType";  /*fnsl apn*/
////			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
//			"http://192.168.1.102:8080/web_service/HelloWorldPortType";
////	"http://192.168.1.8:8680/web_service/HelloWorldPortType";
////			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
    private static final String SOAP_ACTION = "my_request";
    private static final String METHOD_NAME = "ServiceOperation";
    static SoapPrimitive sp;
    static String stuff[] = {"", "", ""};
    static String stuff2[] = {"", "", ""};
    static String sectors = null;

    View rootView;
    private ViewPager viewPager;
    File myFile;

    //	EditText idTypeEdittext;
    EditText idNumEdittext;
    Button idButton;

    Button okButton;
    Button backButton;

    static String idType = null;
    static String idNum = null;
    ProgressDialog progress = null;
    Thread thread;
    //	String ratty = "";
    String msg = null;

//	String id = null;
//	String prodDesc = null;

    String id = null;
    String idDesc = null;

    String pCode = null;
    String pGrpDesc = null;

    static String sendProd = null;
    static String sendID = null;

    ListView list;

    int[] next = new int[]{R.drawable.more};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_document, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);

//		idTypeEdittext = (EditText) rootView.findViewById(R.id.frag_doc_type);
        idNumEdittext = (EditText) rootView.findViewById(R.id.frag_doc_id);
//		idImageView=(ImageView)rootView.findViewById(R.id.id_pic);
//		idImageView.setImageResource(R.drawable.img);

//		idImageView.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				takephoto(rootView);
//			}
//		});

        idButton = (Button) rootView.findViewById(R.id.id_button);

        idButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                AccountNum.choice = "ID";

                msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword;

                progress = new ProgressDialog(getActivity());
                progress.setTitle("Sending");
                progress.setMessage("Please wait...");
                progress.show();
                new fireMan().execute();
            }
        });

		/*okButton = (Button) rootView.findViewById(R.id.idButton);
		backButton = (Button) rootView.findViewById(R.id.backButton);
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				idType = idTypeEdittext.getText().toString();
				idNum = idNumEdittext.getText().toString();
				
				// make respected tab selected
				viewPager.setCurrentItem(4,true);
			}
		});
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//go to previous tab
				viewPager.setCurrentItem(2,true);
				
			}
		});*/

        return rootView;
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


    int responseCode = 0;
    String response = null;


    public class fireMan extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//			progress = new ProgressDialog(AccountNum.this);
//			progress.setCancelable(false);
//			progress.setTitle("Please wait");
//			progress.setMessage("Sending request...");
//			progress.show();
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

                stuff = response.split(";");

                progress.cancel();

                List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

                for (int a = 0; a < stuff.length; a++) {
                    StringTokenizer myAccToken = new StringTokenizer(stuff[a], ",");

//								try {
//									while (myAccToken.hasMoreTokens()){
                    id = myAccToken.nextToken().trim();
//										prodSGroup = myAccToken.nextToken().trim();
                    idDesc = myAccToken.nextToken().trim();

                    System.out.println(id);
//										System.out.println(prodSGroup);
                    System.out.println(idDesc);


                    HashMap<String, String> hm = new HashMap<String, String>();

                    hm.put("code", id);
//								hm.put("prod",  prodSGroup);
                    hm.put("desc", idDesc);
                    hm.put("next", Integer.toString(next[0]));
                    aList.add(hm);

                }

                String[] from = {"next", "code",  /*"prod",*/ "desc"};
                int[] to = {R.id.flag, R.id.p_code,/* R.id.sub_grp,*/ R.id.grp_desc};

                SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.list_id, from, to);
                list = (ListView) rootView.findViewById(R.id.id_list);
                list.setAdapter(adapter);


                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                            long arg3) {

                        pCode = ((TextView) v.findViewById(R.id.p_code)).getText().toString().trim();
//									pSGroup = ((TextView) v.findViewById(R.id.sub_grp)).getText().toString().trim();
                        pGrpDesc = ((TextView) v.findViewById(R.id.grp_desc)).getText().toString().trim();
                        sendID = pCode;
                        System.out.println("finally......" + pGrpDesc);
                        System.out.println("finally......" + pCode);
//									Intent d = new Intent(TransactionDetails.this, TransactionChoice.class);
//									d.putExtra("transDetails", transDetails);
//									d.putExtra("date", transDate);
//									d.putExtra("amount", formatAmounts(transAmt));

                        idNum = idNumEdittext.getText().toString();
                        System.out.println(sendID);
                        System.out.println(idNum);

                        if (idNum.equals("")) {
                            idNumEdittext.setError("Please enter id number");
                        } else {
                            // make respected tab selected
                            System.out.println("current item: " + viewPager.getCurrentItem());
                            viewPager.setCurrentItem(4, true);
                        }
                    }
                });

            }


        }

    }


//	public void retrieveIDs(final String message){
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
//					getActivity().runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							ratty = sp.toString();
//							System.out.println(ratty);
////							stuff = ratty.split("~");
////							sectors = stuff[1];
//							stuff = ratty.split(";");
//
//							progress.cancel();
//
//							List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
//
//							for(int a=0;a<stuff.length;a++){
//								StringTokenizer myAccToken = new StringTokenizer(stuff[a], ",");
//
////								try {
////									while (myAccToken.hasMoreTokens()){
//										id = myAccToken.nextToken().trim();
////										prodSGroup = myAccToken.nextToken().trim();
//										idDesc = myAccToken.nextToken().trim();
//
//										System.out.println(id);
////										System.out.println(prodSGroup);
//										System.out.println(idDesc);
////									}
//
////								} catch (NoSuchElementException e) {
////									// TODO Auto-generated catch block
////									//										Toast.makeText(TransactionDetails.this, "token empty", Toast.LENGTH_SHORT);
////									//										done = 1;
////									e.printStackTrace();
////								}
//
//								HashMap<String, String> hm = new HashMap<String,String>();
//
//								hm.put("code", id);
////								hm.put("prod",  prodSGroup);
//								hm.put("desc",  idDesc);
//								hm.put("next", Integer.toString(next[0]));
//								aList.add(hm);
//
//							}
//
//							String[] from = { "next", "code",  /*"prod",*/ "desc" };
//							int[] to = { R.id.flag, R.id.p_code,/* R.id.sub_grp,*/ R.id.grp_desc};
//
//							SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.list_id , from , to);
//							list = (ListView) rootView.findViewById(R.id.id_list);
//							list.setAdapter(adapter);
//
//
//
//							list.setOnItemClickListener(new OnItemClickListener() {
//
//								@Override
//								public void onItemClick(AdapterView<?> arg0, View v, int arg2,
//										long arg3) {
//
//									pCode = ((TextView) v.findViewById(R.id.p_code)).getText().toString().trim();
////									pSGroup = ((TextView) v.findViewById(R.id.sub_grp)).getText().toString().trim();
//									pGrpDesc = ((TextView) v.findViewById(R.id.grp_desc)).getText().toString().trim();
//									sendID = pCode;
//									System.out.println("finally......" + pGrpDesc);
//									System.out.println("finally......" + pCode);
////									Intent d = new Intent(TransactionDetails.this, TransactionChoice.class);
////									d.putExtra("transDetails", transDetails);
////									d.putExtra("date", transDate);
////									d.putExtra("amount", formatAmounts(transAmt));
//
//									idNum = idNumEdittext.getText().toString();
//									System.out.println(sendID);
//									System.out.println(idNum);
//
//									if (idNum.equals("")){
//										idNumEdittext.setError("Please enter id number");
//									}else {
//									// make respected tab selected
//										System.out.println("current item: " + viewPager.getCurrentItem());
//									viewPager.setCurrentItem(4,true);
//									}
//								}
//							});
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	/*public void takephoto(View v){
	       Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	         startActivityForResult(cameraIntent, CAMERA_REQUEST);
	  }
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      if (requestCode == CAMERA_REQUEST) {
	    	  try{
	                  Bitmap photo = (Bitmap) data.getExtras().get("data");
	                  idImageView.setImageBitmap(photo);
	                  idImageView.setClickable(true);
	                  
//	                  Bitmap yourBitmap = null;
	                  ByteArrayOutputStream bos = new ByteArrayOutputStream();
	                  photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
	                  byte[] bArray = bos.toByteArray();
	                  
	                  Toast.makeText(getActivity(), bArray.toString(), Toast.LENGTH_LONG).show();
	    	  }catch (Exception e) {
				System.out.println(e.toString());
				Toast.makeText(getActivity(), "Please take a picture", Toast.LENGTH_LONG).show();
			}


	      }
	}*/
}

package com.union.x100xpress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class ImgTest extends Activity{

	private static final String NAMESPACE = "http://webpackage/";
	private static final String URL = "http://192.168.43.32:8680/web_service/HelloWorldPortType";  /*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*"http://197.251.247.4/web_service/HelloWorldPortType"*/;  
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;
	String ratty = "";
	Thread thread;
	String custNum;
	//	ListView acc_list;
	EditText enterCustNum;
	static String stuff [] = {"", "", ""};
	//	static String accNo;
	static String account;
	static String searchAccountNum = null;
	static String accNum;
	static String accName;
	Button okButton;
	File myFile;
	ProgressDialog progress;
	Dialog dialog;
	TextView balText;
	TextView transText;
	TextView depositText;
	TextView loanText;

	private static final int CAMERA_REQUEST = 1888;
	ImageView idImageView;
	byte[] bArray;
	String photo;
	String encodedimage1;
	Drawable image;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img_test);

		enterCustNum = (EditText) findViewById(R.id.cust_name);
		okButton = (Button) findViewById(R.id.okBut);

		idImageView=(ImageView)findViewById(R.id.id_pic);
		idImageView.setImageResource(R.drawable.img);

		idImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				takephoto(v);
			}
		});

		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				custNum = enterCustNum.getText().toString();
				if (custNum.length() == 0 ){
					enterCustNum.setError("Please enter name");
				}else{
					progress = new ProgressDialog(ImgTest.this);
					progress.setTitle("Searching");
					progress.setMessage("Please wait...");
					progress.show(); 
					AccountNum.choice = "TEST";
//					AccountNum.msg = AccountNum.choice + " " + custNum + " " + bArray.toString();
					
					String imgString = Base64.encodeToString(bArray, 
		                       Base64.DEFAULT);
					
					AccountNum.msg = AccountNum.choice + " " + MainActivity.userPhoneNum + " " + MainActivity.userPassword + " " + imgString;
					Handler h = new Handler();

					h.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									sendRequest(AccountNum.msg);
									thread.start();		
									okButton.setEnabled(true);

									TextWatcher textWatcher = new TextWatcher() {

										@Override
										public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

										}

										@Override
										public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

										}

										@Override
										public void afterTextChanged(Editable editable) {
											//here, after we introduced something in the EditText we get the string from it
											//											String answerString = enterCustNum.getText().toString();

											//and now we make a Toast
											//modify "yourActivity.this" with your activity name .this
											//									       Toast.makeText(Withdraw.this,"The string from EditText is: "+answerString,0).show();
											okButton.setEnabled(true);  
											enterCustNum.requestFocus();
										}
									};

									//third, we must add the textWatcher to our EditText
									enterCustNum.addTextChangedListener(textWatcher);
								}
							});
						}
					});
				}

			}
		});




	}
	
	

	
	// convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


	public void takephoto(View v){
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			idImageView.setImageBitmap(photo);

			//	                  Bitmap yourBitmap = nuzll;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bArray = bos.toByteArray();
			
			if( bos != null){
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}

			Toast.makeText(ImgTest.this, bArray.toString(), Toast.LENGTH_LONG).show();


		}
	}

	/*private String getEncodeData(String filePath) {
        String encodedimage1 = null;
        if (filePath != null && filePath.length() > 0) {
            try {
                Bitmap bm = decodeFile(new File (filePath));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();  
                bm.compress(Bitmap.CompressFormat.PNG, 50, baos); //bm is the bitmap object 
                byte[] b = baos.toByteArray();
                encodedimage1= Base64.encodeToString(b, Base64.DEFAULT);
            } catch (Exception e) {
                System.out
                .println("Exception: In getEncodeData" + e.toString());
            }
        }
        return encodedimage1;
    }*/

	private Bitmap decodeFile(File f){
		Bitmap b = null;
		final int IMAGE_MAX_SIZE = 100;
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();
			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(2.0, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			//            Log.v("Exception in decodeFile() ",e.toString()+"");
		}
		return b;
	}


	/*public void onActivityResult(int requestCode, int resultCode, Intent data) {
		encodedimage1 = null;
		if (requestCode == CAMERA_REQUEST) {
			try {
				FileInputStream fis;

//				photo = (File) data.getExtras().get("data");
				fis = new FileInputStream(photo);
                Bitmap bm = decodeFile(new File (photo));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();  
                bm.compress(Bitmap.CompressFormat.PNG, 50, baos); //bm is the bitmap object 
                byte[] b = baos.toByteArray();
                encodedimage1= Base64.encodeToString(b, Base64.DEFAULT);
            } catch (Exception e) {
                System.out
                .println("Exception: In getEncodeData" + e.toString());
            }*/


	/*try{
				photo = (Bitmap) data.getExtras().get("data");
				idImageView.setImageBitmap(photo);
				idImageView.setClickable(true);

				//	                  Bitmap yourBitmap = null;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
				bArray = bos.toByteArray();


				Toast.makeText(this, bArray.toString(), Toast.LENGTH_LONG).show();
			}catch (Exception e) {
				System.out.println(e.toString());
				Toast.makeText(this, "Please take a picture", Toast.LENGTH_LONG).show();
			}*/






	public void errorConnecting(){
		// check if external storage is available and not read only

		AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
				ImgTest.this);

		// Setting Dialog Title
		alertDialog3.setTitle("Error!");
		alertDialog3.setMessage("Please try again or use other SIM");		 

		alertDialog3.setPositiveButton("TRY AGAIN",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				dialog.cancel();

			}
		});
		// Setting Negative "NO" Btn
		/*alertDialog3.setNegativeButton("SAVE REQUEST",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				//		                
				dialog.cancel();
				try {

					saveInFile(AccountNum.msg);
					Toast.makeText(SearchByName.this, "Request has been saved in file successfully", Toast.LENGTH_LONG).show();
					okButton.setEnabled(true);
					enterCustNum.setText("");
					enterCustNum.requestFocus();
					acc_list.setVisibility(View.VISIBLE);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SearchByName.this, "Unsuccessful", Toast.LENGTH_LONG).show();
				}


			}
		});*/

		// Showing Alert Dialog
		alertDialog3.show();


	}


	private void saveInFile(String request) throws FileNotFoundException{

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
			Toast.makeText(getBaseContext(),
					"Done writing SD 'mysdfile.txt'",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}


	public void sendRequest(final String message){
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


//						@SuppressWarnings("deprecation")
						public void run() {
							// TODO Auto-generated method stub
							
							
							/*ratty = sp.toString();
							if (ratty.equals("20")){
								progress.cancel();
								Toast.makeText(ImgTest.this, "Wrong account number", Toast.LENGTH_LONG).show();
							}else{
								progress.cancel();
								System.out.println(ratty);
								StringTokenizer token = new StringTokenizer(ratty, ",");
								String name = token.nextToken().trim();
								String pic= token.nextToken().trim();
								System.out.println(pic);
								byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
//								byte[] picByte = pic.getBytes();
								Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
								if (bitmap != null) {
								
//								image = new BitmapDrawable(Bitmap.createScaledBitmap(bitmap, 90, 100, true));
								idImageView.setImageBitmap(bitmap);
								}else
								{
									Toast.makeText(ImgTest.this, "null", Toast.LENGTH_LONG).show();
								}*/
								
							
							
								/*byte[] picByte = pic.getBytes();
								
								
								Bitmap bm = BitmapFactory.decodeByteArray(picByte, 0 ,picByte.length);
								
								if (bm != null){
								idImageView.setBackground(image);
								}else
								{
									Toast.makeText(ImgTest.this, "null", Toast.LENGTH_LONG).show();
								}*/
//								stuff  = ratty.split(",");

								//								okButton.setEnabled(false);
								//								enterCustNum.setEnabled(false);
							}

						}
					);

				}catch (SocketTimeoutException e) {
					noConnectivity();
					//														
				}/*catch (IOException e) {

					e.printStackTrace();

				}*/ catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*catch (XmlPullParserException e) {
					e.printStackTrace();
				}*/
			}
		});
	}

	public void noConnectivity(){

		try{																	

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					errorConnecting();
					//					Toast.makeText(Deposit.this, "An error occurred", Toast.LENGTH_LONG).show();

				}
			});

		}catch (Exception e) {

			e.printStackTrace();
		}
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		searchAccountNum = "";
		DepositChooseAcc.account = "";
		finish();
		super.onBackPressed();
	}
}

package com.union.x100xpress;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

public class LocationWebService extends AsyncTask<String , Void, Void> {

	private static final String NAMESPACE = "http://webpackage/";
	private static final String URL = 
			//			"http://197.221.81.162:8680/web_service_test/HelloWorldPortType"; /*fnsl public test*/
//			"http://192.168.43.32:8680/web_service/HelloWorldPortType";
			"http://197.251.247.5:8680/web_service/HelloWorldPortType";  /*office public*/
	/*"http://192.168.1.26:8680/web_service/HelloWorldPortType"; */   
	/*"http://192.168.2.17:9999/web_service/HelloWorldPortType";*/ /*"http://197.159.131.185:8686/web_service/HelloWorldPortType";*/  /*"http://197.251.247.4/web_service/HelloWorldPortType";*/ 
	private static final String SOAP_ACTION = "my_request";
	private static final String METHOD_NAME = "ServiceOperation";
	static SoapPrimitive sp;
	static PropertyInfo pi;

	Thread thread;

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}



	@Override
	protected Void doInBackground(Void ... params) {

		//			public void sendRequest(final String message){



		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
		//request.addProperty("Input",enterText.getText().toString());
		PropertyInfo pi = new PropertyInfo();
		pi.setNamespace(NAMESPACE);
		pi.setType(PropertyInfo.STRING_CLASS);
		//pi.setName("name");
		pi.setName("Input");
		//pi.setValue(editText1.getText().toString().trim());
		//						pi.setValue(message);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11); 
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 10000);

		try{																	
			androidHttpTransport.call(SOAP_ACTION, envelope);

			sp = (SoapPrimitive) envelope.getResponse();
			
			
			
			
			
		}



		catch (SocketTimeoutException e) {
			//							noConnectivity();
		}
		catch (IOException e) {

			e.printStackTrace();

		}

		catch (XmlPullParserException e) {
			e.printStackTrace();
		}





		return null;
	}



	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub





	}*/
	
	
	
	
	
	/*public void sendRequest(final String message){
		thread = new Thread(new Runnable(){
			@Override
			public void run() {
				
				Context arg0;
				Activity activity;

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

					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
//							ratty = sp.toString();
							if(ratty.equals("Transaction successful")){
								progress.cancel();
								successful();
								//								openPrintApp();
							}else if(ratty.equals("Transaction unsuccessful1")){
								progress.cancel();
								unsuccessful();

							}																							

						}
					});

				}
				catch (SocketTimeoutException e) {
//					noConnectivity();
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

	/*public void requestAccName(final String message){
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
//							accDetails = sp.toString();
							

						}
					});

				}catch (SocketTimeoutException e) {
//					progress.cancel(); 
					noConnectivity();
					//							new CustomTask().execute();

				}catch (IOException e) {

					e.printStackTrace();

				}

				catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/*protected Void doInBackground(String message... params) {
		// TODO Auto-generated method stub
		
		sendRequest(message);
		return null;
	}*/
	
	






	/*@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}*/
	




	/*@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		sendRequest(message);
		return null;
	}*/
}
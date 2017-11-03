package com.union.x100xpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


public class TransactionChoice extends AppCompatActivity {
	
	EditText dateEditText;
	EditText transTypeEditText;
	EditText creditEditText;
	EditText debitEditText;
	EditText branchEditText;
	
	String transDetails = null;
	String date = null;
	String amount = null;
	String branch = null;
	
	String mydate = null;
	String message = null;
	File myPrintFile = null;
	
	Double amtDouble = null;
	String formattedAmt = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction_choice);  
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		dateEditText = (EditText) findViewById(R.id.dateEditText);
		transTypeEditText = (EditText) findViewById(R.id.transTypeEditText);
		creditEditText = (EditText) findViewById(R.id.creditEditText);
		branchEditText = (EditText) findViewById(R.id.branchEditText);
		
		transDetails = getIntent().getExtras().getString("transDetails").trim();
		date = getIntent().getExtras().getString("date").trim();
		branch = getIntent().getExtras().getString("branch").trim();
		amount = getIntent().getExtras().getString("amount").trim();
		
		
		
		transTypeEditText.setText(transDetails);
		transTypeEditText.setEnabled(false);
		dateEditText.setText(date);
		dateEditText.setEnabled(false);
		branchEditText.setText(branch);
		branchEditText.setEnabled(false);
		if(amount.startsWith("-")){
			creditEditText.setTextColor(Color.RED);
		}else creditEditText.setTextColor(getResources().getColor(R.color.positive_bal));
		creditEditText.setText(amount);
		creditEditText.setEnabled(false);
//		debitEditText.setText(debitedAmt);
		
//		openPrintApp();
				
			
	}
	
	
	public void openPrintApp(){
		try {
			mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			
				message = "************************"
						+"  MobiTransact Receipt  "
						+"************************"
						+ mydate + "\n"
						+"    Transaction Details    \n\n" 
						+"Transaction Type: " + transDetails + "\n"
						+"Transaction Date: " + date + "\n"
						+"Amount: " + amount + "\n\n"
												
						+"************************\n\n";


				/*+"        Thank You       "
						+"************************\n";*/
				saveMsg(message);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(message);
		Intent i;
		PackageManager manager = getPackageManager();
		try {
			i = manager.getLaunchIntentForPackage("com.bluebamboo.p25demo");
			if (i == null){
				throw new PackageManager.NameNotFoundException();
			}else{

				//				i.putExtra(AccountNum.msg, true);
				i.addCategory(Intent.CATEGORY_LAUNCHER);
				startActivity(i);
			}
		} catch (PackageManager.NameNotFoundException e) {

		}
	}
	
	
	private void saveMsg(String number) throws FileNotFoundException{

		try {
			myPrintFile = new File(Environment.getExternalStorageDirectory(), "printfile.txt");
			myPrintFile.createNewFile();
			//			userFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myPrintFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			if (myPrintFile.length() > 0) 
				myOutWriter.append("\n" + message);
			else 
				myOutWriter.append(message);
//			Toast.makeText(Withdraw.this, "File saved successfully",Toast.LENGTH_LONG).show();
			myOutWriter.close();
			fOut.close();
			//			FileOutputStream fos = new FileOutputStream(myInternalFile);
			//			fos.write(textMessage.toString().getBytes());
			//			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			Toast.makeText(TransactionChoice.this, e.getMessage(),Toast.LENGTH_LONG).show();
		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	TransactionChoice.this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();

	}
	

}

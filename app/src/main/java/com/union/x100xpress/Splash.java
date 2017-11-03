package com.union.x100xpress;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Splash extends Activity{
	
	TextView text1;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
						
		text1 = (TextView) findViewById(R.id.TextView1);
		
		
//		text1.setTextColor(Color.LTGRAY);
		Typeface chops1 = Typeface.createFromAsset(getAssets(), "salte28.ttf");
//		text1.setTypeface(chops1);
//		text1.setText("MobiTransact");
//		text1.setTextScaleX((float) 1.6 );
		/*Typeface chops2 = Typeface.createFromAsset(getAssets(), "chops.ttf");
		text2.setTypeface(chops2);
		text2.setText("chops");*/
		/*Typeface chops3 = Typeface.createFromAsset(getAssets(), "embossedgermanica.ttf");
		text3.setTypeface(chops3);
		text3.setText("embossed");
		Typeface chops4 = Typeface.createFromAsset(getAssets(), "oldscript.ttf");
		text4.setTypeface(chops4);
		text4.setText("oldscript");
		Typeface chops5 = Typeface.createFromAsset(getAssets(), "rothenbg.ttf");
		text5.setTypeface(chops5);
		text5.setText("rothenbg");
		Typeface chops6 = Typeface.createFromAsset(getAssets(), "salte28.ttf");
		text6.setTypeface(chops6);
		text6.setText("salte");*/
			
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(2000);
				} catch(InterruptedException e){
					e.printStackTrace();
					
				}finally{
					Intent openMainActivity = new Intent(Splash.this, MainActivity.class);
					startActivity(openMainActivity);
				}
			}
		};
		timer.start();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onPause();
		
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		finishFromChild(Splash.this);
	}
	
	
	

}




package com.union.x100xpress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class FragmentPersonal extends Fragment {
	
	
//	ActionBar actionBar/* = getSupportActionBar()*/;
	View rootView;
	private ViewPager viewPager;
	
	EditText fnameEdittext;
	EditText lnameEdittext;
	EditText mnameEdittext;
//	EditText dobEdittext;
	EditText phone1Edittext;
	EditText phone2Edittext;
	EditText emailEdittext;
	
	static RadioGroup radioGenderGroup;
    static RadioButton radioGenderButton;
    
    Button okButton;
    Button backButton;
	
	static String fname = null;
	static String lname = null;
	static String mname = null;
	static String gender = null;
//	static String dob = null;
	static String phone1 = null;
	static String phone2 = null;
	static String email = null;
	String mydate = null;
	int selectedPaymentType;
	
	/*public static FragmentPersonal newInstance(CharSequence title, int indicatorColor,
            int dividerColor) {
		Bundle bundle=new Bundle();
		bundle.putString("fname", fname);
//		bundle.putString("lname", lname);
//		bundle.putString("mname", mname);
//		bundle.putString("dob", dob);
//		bundle.putString("phone1", phone1);
//		bundle.putString("phone2", phone2);
//		bundle.putString("email", email);
//		bundle.putString("gender", gender);
		
		  //set Fragmentclass Arguments
		FragmentPersonal fragobj=new FragmentPersonal();
		fragobj.setArguments(bundle);

        FragmentPersonal fragment = new FragmentPersonal();
        fragment.setArguments(bundle);

        return fragment;
    }
	
	Fragment createFragment() {
		return FragmentAddress.newInstance(fname);
	}*/
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		rootView = inflater.inflate(R.layout.fragment_personal, container, false);
		viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
				
		fnameEdittext = (EditText) rootView.findViewById(R.id.ac_name);
		lnameEdittext = (EditText) rootView.findViewById(R.id.depAmount);
		mnameEdittext = (EditText) rootView.findViewById(R.id.mname);
//		dobEdittext = (EditText) rootView.findViewById(R.id.dob);
		phone1Edittext = (EditText) rootView.findViewById(R.id.phone1);
		phone2Edittext = (EditText) rootView.findViewById(R.id.phone2);
		emailEdittext = (EditText) rootView.findViewById(R.id.email);
		
		radioGenderGroup = (RadioGroup) rootView.findViewById(R.id.radio_gender);
		
		okButton = (Button) rootView.findViewById(R.id.personalButton);
		backButton = (Button) rootView.findViewById(R.id.backButton);
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fname = fnameEdittext.getText().toString();
				lname = lnameEdittext.getText().toString();
				mname = mnameEdittext.getText().toString();
//				dob = dobEdittext.getText().toString();
				phone1 = phone1Edittext.getText().toString();
				phone2 = phone2Edittext.getText().toString();
				email = emailEdittext.getText().toString();

				selectedPaymentType = radioGenderGroup.getCheckedRadioButtonId();
				radioGenderButton = (RadioButton) rootView.findViewById(selectedPaymentType);
				if (radioGenderButton.getText().toString().equals("Male"))
					gender = "Male";
				else
					gender = "Female";
				// make respected tab selected
				/*Bundle bundle=new Bundle();
				bundle.putString("fname", fname);
				bundle.putString("lname", lname);
				bundle.putString("mname", mname);
				bundle.putString("dob", dob);
				bundle.putString("phone1", phone1);
				bundle.putString("phone2", phone2);
				bundle.putString("email", email);
				bundle.putString("gender", gender);
				
				  //set Fragmentclass Arguments
				FragmentAddress fragobj=new FragmentAddress();
				fragobj.setArguments(bundle);*/
								
//				createFragment();
				
				if (fname.equals("")){
					fnameEdittext.setError("Please enter first name");
				}else if (lname.equals("")){
					lnameEdittext.setError("Please enter last name");
				}/*else if (mname.equals("")){
					mnameEdittext.setError("Please enter middle name");
				}*//*else if (dob.equals("")){
					dobEdittext.setError("Please fill in date");
				}*/else if (phone1.equals("")){
					phone1Edittext.setError("Please enter phone number");
				}else if (phone2.equals("") && (email.equals("")) && (mname.equals(""))){
					phone2 = phone1;
					email = "N/A";
					mname = "N/A";
					viewPager.setCurrentItem(2,true);
				}else if (phone2.equals("") && (!email.equals("")) && (!mname.equals(""))){
					phone2 = phone1;
					viewPager.setCurrentItem(2,true);
				}else if (!phone2.equals("") && (email.equals("")) && (!mname.equals(""))){
					email = "N/A";
					viewPager.setCurrentItem(2,true);
				}else if (!phone2.equals("") && (!email.equals("")) && (mname.equals(""))){
					mname = "N/A";
					viewPager.setCurrentItem(2,true);
				}/*else if (email.equals("")){     1009122000097
//					emailEdittext.setError("Please enter name field");
					email = "N/A";
				}*/else {
				viewPager.setCurrentItem(2,true);
				}
				
			}
		});
		
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//go to previous tab
				viewPager.setCurrentItem(0,true);

			}
		});
		
		return rootView;
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
	
	
}

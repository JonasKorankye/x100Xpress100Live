package com.union.x100xpress;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@SuppressLint("NewApi")
public class FragmentAddress extends Fragment {

	View rootView;
	private ViewPager viewPager;

	EditText addressEdittext;
	EditText cityPostalEdittext;
	EditText regionEdittext;
	EditText locationEdittext;
	EditText cityPhysicalEdittext;
	EditText landmarkEdittext;

	Button okButton;
	Button backButton;

	static String address = null;
	static String cityPostal = null;
	static String region = null;
	static String location = null;
	static String cityPhysical = null;
	static String landmark = null;
	static String fnme = null;
	
	/*public static FragmentAddress newInstance(String fname) {
		Bundle bundle=new Bundle();
		fname = bundle.getString("fname",fname);
//		bundle.putString("lname", lname);
//		bundle.putString("mname", mname);
//		bundle.putString("dob", dob);
//		bundle.putString("phone1", phone1);
//		bundle.putString("phone2", phone2);
//		bundle.putString("email", email);
//		bundle.putString("gender", gender);
		
		  //set Fragmentclass Arguments
//		FragmentAddress fragobj=new FragmentAddress();
//		fragobj.setArguments(bundle);
		
        FragmentAddress fragment = new FragmentAddress();
        fragment.setArguments(bundle);

        return fragment;
    }*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
//		String fname=getArguments().getString("fname");
//		String lname=getArguments().getString("lname");
//		String mname=getArguments().getString("mname");
//		String dob=getArguments().getString("dob");
//		String phone1=getArguments().getString("phone1");
//		String phone2=getArguments().getString("phone2");
//		String email=getArguments().getString("email");
//		String gender=getArguments().getString("gender");
//		
//		System.out.println(fname);
//		System.out.println(lname);
//		System.out.println(mname);
//		System.out.println(dob);
//		System.out.println(phone1);
//		System.out.println(phone2);
//		System.out.println(email);
//		System.out.println(gender);
		
		rootView = inflater.inflate(R.layout.fragment_address, container, false);
		viewPager = (ViewPager) getActivity().findViewById(R.id.pager);

		addressEdittext = (EditText) rootView.findViewById(R.id.address);
		cityPostalEdittext = (EditText) rootView.findViewById(R.id.city_postal);
		regionEdittext = (EditText) rootView.findViewById(R.id.region);
		locationEdittext = (EditText) rootView.findViewById(R.id.location);
		cityPhysicalEdittext = (EditText) rootView.findViewById(R.id.city);
		landmarkEdittext = (EditText) rootView.findViewById(R.id.landmark);

		okButton = (Button) rootView.findViewById(R.id.addressButton);
		backButton = (Button) rootView.findViewById(R.id.backButton);
		
//		Bundle args = getArguments();
//		fnme = savedInstanceState.getString("fname");
//		System.out.println(fnme);
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				address = addressEdittext.getText().toString();
				cityPostal = cityPostalEdittext.getText().toString();
				region = regionEdittext.getText().toString();
				location = locationEdittext.getText().toString();
				cityPhysical = cityPhysicalEdittext.getText().toString();
				landmark = landmarkEdittext.getText().toString();
				
//				Bundle bundle=new Bundle();
//				bundle.putString("fname", fname);
//				bundle.putString("lname", lname);
//				bundle.putString("mname", mname);
//				bundle.putString("dob", dob);
//				bundle.putString("phone1", phone1);
//				bundle.putString("phone2", phone2);
//				bundle.putString("email", email);

				
				if (address.equals("")){
					addressEdittext.setError("Please enter address");
				}/*else if (cityPostal.equals("")){
					cityPostal = "N/A";
				}*/else if (region.equals("")){
					regionEdittext.setError("Please enter region");
				}else if (location.equals("")){
					locationEdittext.setError("Please enter location");
				}else if (cityPhysical.equals("")){
					cityPhysicalEdittext.setError("Please enter city");
				}else if ((cityPostal.equals("")) && (!landmark.equals(""))){
					cityPostal = "N/A";
					viewPager.setCurrentItem(3,true);
				}else if ((!cityPostal.equals("")) && (landmark.equals(""))){
					landmark = "N/A";
					viewPager.setCurrentItem(3,true);
				}else {
				// make respected tab selected
				viewPager.setCurrentItem(3,true);
				}
			}
		});

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//go to previous tab
				viewPager.setCurrentItem(1,true);

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

package com.union.x100xpress;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.Window;

import liststuff.CustomViewPager;
import liststuff.TabsPagerAdapter;


@SuppressWarnings("deprecation")
public class AcctCreation extends ActionBarActivity implements
ActionBar.TabListener{

	private CustomViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;

	private String[] tabs = {"Product Details", "Personal Details", "Address Details",  "Document Details", "Sector", "ARM", "Confirm Details" };

//	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.acct_creation);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Initilization
		viewPager = (CustomViewPager) findViewById(R.id.pager);
		viewPager.setPagingEnabled(false);
		viewPager.setClickable(false);
		viewPager.setFocusable(false);
//		viewPager.setHovered(false);

//		setContentView(R.layout.main);

		actionBar = getSupportActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		
		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		
//		viewPager.
		
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// on tab selected
				// show respected fragment view
//				viewPager.setCurrentItem(tab.getPosition());

	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				AcctCreation.this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
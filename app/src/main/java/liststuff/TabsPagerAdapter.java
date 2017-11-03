package liststuff;

import com.union.x100xpress.FragmentARM;
import com.union.x100xpress.FragmentAddress;
import com.union.x100xpress.FragmentConfirm;
import com.union.x100xpress.FragmentDocument;
import com.union.x100xpress.FragmentPersonal;
import com.union.x100xpress.FragmentSector;
import com.union.x100xpress.ProdDetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new ProdDetails();
		case 1:
			// Games fragment activity
			return new FragmentPersonal();
		case 2:
			// Movies fragment activity
			return new FragmentAddress();
		case 3:
			// Movies fragment activity
			return new FragmentDocument();
		case 4:
			// Movies fragment activity
			return new FragmentSector();
		case 5:
			// Movies fragment activity
			return new FragmentARM();
		case 6:
			// Movies fragment activity
			return new FragmentConfirm();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 7;
	}

}

package com.telecomexperience.ui.adapter;

import com.telecomexperience.R;
import com.telecomexperience.fragment.ImageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ImageFragmentAdapter extends FragmentPagerAdapter {
	protected static final int[] CONTENT = new int[] {R.drawable.image_big1,R.drawable.image_big2,R.drawable.image_big3,R.drawable.image_big4,};
	
	private int mCount = CONTENT.length;

	public ImageFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return ImageFragment.newInstance(CONTENT[position % CONTENT.length]);
	}

	@Override
	public int getCount() {
		return mCount;
	}
	
	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}
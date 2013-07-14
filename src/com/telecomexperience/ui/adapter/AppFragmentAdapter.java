package com.telecomexperience.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.telecomexperience.app.App;
import com.telecomexperience.fragment.AppFragment;

public class AppFragmentAdapter extends FragmentStatePagerAdapter {

	private static final String TAG = "AppFragmentAdapter";
	private int mCount = 10;
	private List<App> list = new ArrayList<App>();
	private Context mContext;
	private int pageSize = 12;
	private FragmentManager fm;

	public AppFragmentAdapter(FragmentManager fm, Context context,
			List<App> list) {
		super(fm);
		this.fm = fm;
		mContext = context;
		if (list != null)
			this.list.addAll(list);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Fragment getItem(int position) {
		AppFragment fragment = null;
		fragment = new AppFragment(position, list);
		fragment.setPageSize(pageSize);
		Bundle args =new Bundle();
        args.putInt("pageNO", position);
        fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		Log.d(TAG, "arg1:" + arg1);
		AppFragment appFragment = (AppFragment) super.instantiateItem(arg0,
				arg1);
		appFragment.setList(list);
		appFragment.setPageSize(pageSize);
		appFragment.setPageNo(arg1);
		appFragment.refresh();
		return appFragment;
	}

	@Override
	public int getCount() {
		if (list == null)
			return 0;
		int size = list.size();
		mCount = (size / pageSize) + ((size % pageSize) == 0 ? 0 : 1);
		return mCount;
	}

	public List<App> getList() {
		return list;
	}

	public void setList(List<App> list) {
		if (this.list != null)
			this.list.clear();
		if (list != null)
			this.list.addAll(list);
	}

	public int getPagesize() {
		return pageSize;
	}

	public void setPagesize(int pagesize) {
		this.pageSize = pagesize;
	}

	public void clear() {
		// TODO Auto-generated method stub
		if (list != null)
			list.clear();
	}
}
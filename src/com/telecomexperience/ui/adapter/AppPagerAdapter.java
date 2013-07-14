package com.telecomexperience.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.telecomexperience.app.App;
import com.telecomexperience.ui.AppGridView;

public class AppPagerAdapter extends PagerAdapter {

	private static final String TAG = "AppPagerAdapter";
	private int mCount = 10;
	private List<App> mList = new ArrayList<App>();
	private Context mContext;
	private int pageSize = 12;

	public AppPagerAdapter(Context context, List<App> list) {
		super();
		mContext = context;
		if (this.mList != null) {
			this.mList.clear();
		}
		if (list != null)
			this.mList.addAll(list);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return false;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		Log.d(TAG, "arg1:" + arg1);
		AppGridView appGridView = new AppGridView(mContext, mList);
		appGridView.setList(mList);
		appGridView.setPageSize(pageSize);
		appGridView.setPageNo(arg1);
		appGridView.refresh();
		return appGridView;
	}

	@Override
	public int getCount() {
		if (mList == null)
			return 0;
		int size = mList.size();
		mCount = (size / pageSize) + ((size % pageSize) == 0 ? 0 : 1);
		return mCount;
	}

	public List<App> getList() {
		return mList;
	}

	public void setList(List<App> list) {
		if (this.mList != null)
			this.mList.clear();
		if (list != null)
			this.mList.addAll(list);
	}

	public int getPagesize() {
		return pageSize;
	}

	public void setPagesize(int pagesize) {
		this.pageSize = pagesize;
	}

	public void clear() {
		if (mList != null)
			mList.clear();
	}

	public void destroyItem(ViewGroup container, int position, View object) {
		if (getCount() > 1) {
			container.removeViewAt(position);
		}

	}

}

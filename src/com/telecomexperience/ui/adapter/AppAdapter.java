package com.telecomexperience.ui.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.telecomexperience.R;
import com.telecomexperience.app.App;
import com.telecomexperience.utils.XmlHelper;


public class AppAdapter extends BaseAdapter{
	private Context mContext;
	public List<App> mAppList = null;
	private static final String TAG = "AppAdapter";
	private int pageNo =0;
	private int pageSize =12;
	private int count = 0;
	
	public AppAdapter(){
		int size = mAppList.size();
		count = (pageNo+1)*pageSize>size?(size-pageNo*pageSize):pageSize;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<App> getAppList() {
		return mAppList;
	}

	public void setAppList(List<App> appList) {
		this.mAppList = appList;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		Log.d(TAG,"setPageNo:"+pageNo);
		this.pageNo = pageNo;
	}

	public AppAdapter(Context context){
		this.mContext = context;
	}
	

	@Override
	public int getCount(){
		if(mAppList==null)return 0;
		return pageSize;
	}

	@Override
	public Object getItem(int position){
		if(mAppList==null) return null;
		else return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class Holder{
		ImageView image;
		TextView name;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView==null){
			holder = new Holder();
			LayoutInflater inflate = LayoutInflater.from(mContext);
			convertView = inflate.inflate(R.layout.app_item, null);
			holder.image = (ImageView) convertView.findViewById(R.id.app_img);
			holder.name = (TextView) convertView.findViewById(R.id.app_name);
			convertView.setTag(holder);
		}
		
		Log.d(TAG,"pageNo:"+pageNo);
		holder = (Holder) convertView.getTag();
		if(mAppList.size()<=pageSize*pageNo+position){
			if(convertView!=null){
				convertView.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}
		else{
			convertView.setVisibility(View.VISIBLE);
		}
		App app = mAppList.get(pageSize*pageNo+position);		
		
		holder.name.setText(app.name);
		XmlHelper.setImage(mContext, holder.image, app.residStr);
		return convertView;
	}

	public App getApp(int position) {
		if(mAppList==null) return null;
		if((pageSize*pageNo+position)>mAppList.size()) return null;
		return mAppList.get(pageSize*pageNo+position);
	}

}
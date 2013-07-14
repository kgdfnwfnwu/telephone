package com.telecomexperience.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.telecomexperience.R;
import com.telecomexperience.AppDetailActivity;
import com.telecomexperience.app.App;
import com.telecomexperience.ui.adapter.AppAdapter;

public class AppGridView extends LinearLayout{
	
	private static final String TAG = "AppGridView";
	private Context mContext;
	private GridView gridView;
	private AppAdapter adapter;
	private List<App> mList = new ArrayList<App>();
	private int pageSize = 12;
	private int mPageNo =0;

	public AppGridView(Context context,List<App> list) {
		super(context);
		mContext = context;
		if(list!=null)  mList.addAll(list);
		initView();
		initEvent();
		
	}
	
	public void initView(){
		LayoutInflater.from(mContext).inflate(R.layout.gridview, this, true);
		gridView = (GridView)this.findViewById(R.id.gridview);
		adapter = new AppAdapter(mContext);
		gridView.setAdapter(adapter);
		adapter.setAppList(mList);		
		adapter.setPageNo(mPageNo);		
	}
	
	public void initEvent(){
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(mContext, AppDetailActivity.class);
				mContext.startActivity(intent);
			}
		});
	}
	
	public void setParams(int pageNo){
		this.mPageNo = pageNo;		
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		if(adapter!=null){
			adapter.setPageSize(pageSize);
		}
	}
	
	public void setList(List<App> list) {
		if(this.mList!=null)this.mList.clear();
		if(list!=null)this.mList.addAll(list);
		adapter.setAppList(mList);
	}

	public int getmPageNo() {
		return mPageNo;
	}

	public void setPageNo(int pageNo) {
		Log.i(TAG,"setPageNo:"+pageNo);
		mPageNo = pageNo;
	}
	
	public void refresh(){
		adapter.notifyDataSetChanged();
	}

}

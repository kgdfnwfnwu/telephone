package com.telecomexperience.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.telecomexperience.AppDetailActivity;
import com.telecomexperience.R;
import com.telecomexperience.app.App;
import com.telecomexperience.ui.adapter.AppAdapter;

@SuppressLint("ValidFragment")
public final class AppFragment extends Fragment {
	
	private static final String TAG = "AppFragment";

	private static int mPageNo = 0;
	
	private GridView gridView;
	private AppAdapter adapter;
	private List<App> mList = new ArrayList<App>();
	private int pageSize = 12;
	
	public AppFragment(){
		
	}
	
	public AppFragment (int pageNo,List<App> list){
		if(mPageNo<0) mPageNo =0;
		mPageNo = pageNo;
		if(list!=null)mList.addAll(list);
	}
	
	public int getPageSize() {
		return pageSize;
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
	}

	public static int getmPageNo() {
		return mPageNo;
	}

	public void setPageNo(int pageNo) {
		Log.i(TAG,"setPageNo:"+pageNo);
		mPageNo = pageNo;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gridview, null);
		
		gridView = (GridView)view.findViewById(R.id.gridview);
		adapter = new AppAdapter(getActivity());
		gridView.setAdapter(adapter);
		adapter.setAppList(mList);		  
		adapter.setPageNo(mPageNo);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), AppDetailActivity.class);
				App app =  adapter.getApp(arg2);
				if(app!=null)intent.putExtra("appname", app.name);
				getActivity().startActivity(intent);
			}
			
		});
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();          
		if(args!=null&&args.containsKey("pageNO")){
			mPageNo = args.getInt("pageNO");
		}
		
		Log.i(TAG,"onCreate:"+mPageNo);
	}

	@Override
	public void onStart() {
		super.onStart();
		if(adapter!=null){
//			adapter.setPageNo(mPageNo);
//			adapter.notifyDataSetChanged();
		}
	}
	
	public void refresh(){
		if(adapter!=null){
//			adapter.setPageNo(mPageNo);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}

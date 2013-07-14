package com.telecomexperience;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.telecomexperience.R;
import com.telecomexperience.app.App;
import com.telecomexperience.manager.Manager;
import com.telecomexperience.ui.adapter.AppFragmentAdapter;
import com.telecomexperience.utils.XmlHelper;
import com.viewpagerindicator.CirclePageIndicator;

public class SoftwareActivity extends MainBaseActivity {

	
	protected AppFragmentAdapter mAdapter;
	protected ViewPager mPager;
	protected LinearLayout searchLayout;
	protected final int ONEPAGE= 6;
	protected final int TWOPAGE = 12;
	protected int pageSize =12;
	protected CirclePageIndicator indicator;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		List<App> list = XmlHelper.getInstance().getSoftwareAppList();
		mAdapter = new AppFragmentAdapter(getSupportFragmentManager(),this,list);
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		
		indicator = (CirclePageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		curPageNo =SOFTWARE;
		initComponent();
		initEvent();
	}
	

	protected void clear(){
		if(mAdapter!=null)mAdapter.clear();
	}
	
	
	public void setSearchResult(List<App> list){
		mAdapter.setList(list);
	}
	
	public void initComponent(){
		super.initComponent();
		searchLayout =(LinearLayout) this.findViewById(R.id.layout_search);
		titleText.setText("”¶”√ Software");
		bgView.currentPage =curPageNo;
		
	}
}

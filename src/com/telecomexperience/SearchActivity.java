package com.telecomexperience;

import java.util.ArrayList;
import java.util.List;

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

public class SearchActivity extends MainBaseActivity {

	
	protected AppFragmentAdapter mAdapter;
	protected ViewPager mPager;
	protected LinearLayout searchLayout;
	protected final int TWOPAGE = 12;
	protected int pageSize =12;
	protected CirclePageIndicator indicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		curPageNo =SEARCH;
		pageSize = TWOPAGE;
		initComponent();
		initEvent();
		List<App> list =XmlHelper.getInstance().getRandomApp();
		mAdapter = new AppFragmentAdapter(getSupportFragmentManager(),this,list);
		mAdapter.setPagesize(pageSize);
		mPager.setAdapter(mAdapter);
		searchLayout.setVisibility(View.VISIBLE);
		titleText.setText("ËÑË÷ Search");
		contextEdit.setText("");
		indicator = (CirclePageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
	}
	
	
	protected void clear(){
		if(mAdapter!=null)mAdapter.clear();
	}
	
	public void setSearchResult(List<App> list){
		mAdapter.setList(list);
	}
	
	public void initComponent(){
		super.initComponent();
		
		mPager = (ViewPager)findViewById(R.id.pager);
		searchLayout =(LinearLayout) this.findViewById(R.id.layout_search);
		titleText.setText("ËÑË÷ Search");
		bgView.currentPage =curPageNo;
		
	}

	@Override
	public void searchApp() {
		
		List<App> resultList = Manager.search(contextEdit.getText().toString());
		mAdapter.setList(resultList);
		mAdapter.setPagesize(pageSize);
		indicator.setCurrentItem(0);
		indicator.invalidate();		
		mAdapter.notifyDataSetChanged();
		if(imm!=null&&contextEdit.getWindowToken()!=null)imm.hideSoftInputFromWindow(contextEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);	
	}
}

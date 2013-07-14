package com.telecomexperience;

import com.telecomexperience.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

public abstract class MainBaseActivity extends AppBaseActivity{

	protected EditText contextEdit;
	protected InputMethodManager imm;
	protected ImageButton appSearchBtn;
	
	public static final int SOFTWARE=2;
	public static final int GAME=3;
	public static final int SEARCH=4;

	public void initComponent(){
		super.initComponent();
		appSearchBtn =(ImageButton) this.findViewById(R.id.btn_search);
		contextEdit =(EditText) this.findViewById(R.id.edit_search);
		imm = (InputMethodManager)contextEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	protected void onResume(){
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	protected void initEvent(){
		super.initEvent();
		appSearchBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.btn_search:
			searchApp();
			break; 
		}
	}
	
	public void searchApp(){
		
	}

	@Override
	public void switchGameView() {
		UmengUpdateAgent.update(this);
		if(curPageNo==GAME){
			return ;
		}
		else{
			Intent intent = new Intent(this,GameActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void switchSoftwareView() {
		if(curPageNo==SOFTWARE){
			return ;
		}
		else{
			Intent intent = new Intent(this,SoftwareActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void switchSearchView() {
		if(curPageNo==SEARCH){
			return ;
		}
		else{
			Intent intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
		}
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (event.getRepeatCount() == 0) {
				Intent intent = new Intent(this,HomeActivity.class);
				startActivity(intent);
				return false;
			}
		}

		if (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
			switchSearchView();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}	
	
}

package com.telecomexperience;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.telecomexperience.ui.MyLineView;
import com.telecomexperience.utils.NetWorkUtils;

public abstract class AppBaseActivity extends FragmentActivity implements OnClickListener {

	protected LinearLayout leftLayout;
	protected LinearLayout centerLayout;
	protected LinearLayout rightLayout;
	protected RelativeLayout homeLayout;
	protected RelativeLayout headerLayout;
	protected MyLineView bgView;
	protected ImageButton wifiBtn;
	
	protected Button homeBtn;
	protected Button searchBtn;
	protected Button appBtn;
	protected Button gameBtn;
	protected int curPageNo =1;
	protected TextView titleText;
	
	
	public void initComponent(){
		wifiBtn = (ImageButton)this.findViewById(R.id.header_img_wifi);
		leftLayout = (LinearLayout) this.findViewById(R.id.header_layout_left);
		centerLayout = (LinearLayout) this.findViewById(R.id.header_layout_center);
		rightLayout = (LinearLayout) this.findViewById(R.id.header_layout_right);
		homeLayout = (RelativeLayout) this.findViewById(R.id.header_layout_home);
		headerLayout = (RelativeLayout) this.findViewById(R.id.header_layout);
		homeBtn = (Button)this.findViewById(R.id.btn_item_one);
		appBtn = (Button)this.findViewById(R.id.btn_item_two);
		gameBtn = (Button)this.findViewById(R.id.btn_item_three);
		searchBtn = (Button)this.findViewById(R.id.btn_item_four);
		bgView =(MyLineView)this.findViewById(R.id.header_view_bg);
		titleText = (TextView)this.findViewById(R.id.text_title);
		initHight();
	}
	
	protected void initEvent(){
		homeBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		appBtn.setOnClickListener(this);
		gameBtn.setOnClickListener(this);
	}
	
	protected void onStart(){
		super.onStart();
		if(NetWorkUtils.isWifiConnected(this)){
			wifiBtn.setBackgroundResource(R.drawable.bt_wifi_over);
		}
		else{
			wifiBtn.setBackgroundResource(R.drawable.bt_wifi_normal);
		}
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		StackManager.putFragmentActivity(this);
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		StackManager.pullFragmentActivity(this);
	}

	public void initHight(){
		headerLayout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		int headerHight = 0;
		bgView.windowWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		int leftLayoutHight = (headerHight+leftLayout.getMeasuredHeight());
		int leftLayoutWidth = headerLayout.getPaddingLeft()+leftLayout.getMeasuredWidth();
		int rightLayoutWidth = rightLayout.getMeasuredWidth();
		int rightLayoutHight = (headerHight+rightLayout.getMeasuredHeight());
		int centerhight = (headerHight+centerLayout.getMeasuredHeight());
		bgView.height2 = centerhight;
		int width = leftLayoutWidth>rightLayoutWidth?leftLayoutWidth:rightLayoutWidth;
		bgView.centerWidth = bgView.windowWidth-2*width;
		bgView.height1 = leftLayoutHight>rightLayoutHight?leftLayoutHight:rightLayoutHight;
		setLeftAndRightWidth(width);
		bgView.leftWidth= width;
		bgView.postInvalidate();
		RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams)titleText.getLayoutParams();
		layout.topMargin = MyLineView.diffy1*2;
	}
	
	public void setLeftAndRightWidth(int width){
		rightLayout.getLayoutParams().width = width;
		leftLayout.getLayoutParams().width = width;
		centerLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		centerLayout.setPadding(20, centerLayout.getPaddingTop(),20, centerLayout.getPaddingBottom());
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_item_one:
			gotoAcitivity(HomeActivity.class);
			break;
		case R.id.btn_item_two:
			if(curPageNo ==2) return;
			switchSoftwareView();
			break;
		case R.id.btn_item_three:
			if(curPageNo ==3) return;
			switchGameView();
			break;
		case R.id.btn_item_four:
			if(curPageNo ==4) return;
			switchSearchView();
			break;
		}
	}
	

	public abstract void switchGameView();
	
	public abstract void switchSoftwareView();
	
	public abstract void switchSearchView();
	
	public void gotoAcitivity(Class cls){
		Intent intent = new Intent ();
		intent.setClass(this, cls);
		startActivity(intent);
	}
	
}

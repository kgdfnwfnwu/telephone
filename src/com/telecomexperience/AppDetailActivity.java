package com.telecomexperience;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.aphidmobile.flip.FlipViewController;
import com.telecomexperience.R;
import com.telecomexperience.app.App;
import com.telecomexperience.net.NetThreadHelper;
import com.telecomexperience.net.SendMessageUtils;
import com.telecomexperience.ui.adapter.AppDeatailAdapter;
import com.telecomexperience.utils.XmlHelper;

public class AppDetailActivity extends AppBaseActivity {

	private FlipViewController flipView;
	private Button preBtn;
	private Button nextBtn;
	private int currentPosition =0;
	private AppDeatailAdapter adapter;
	private AlertDialog dialog;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.appdetail_activity);
		initComponent();
		initEvent();
		if(getIntent()!=null){
			String appname = getIntent().getStringExtra("appname");
			currentPosition = XmlHelper.getInstance().getPosition(appname);
		}
	}

	@Override
	public void initComponent() {
		// TODO Auto-generated method stub
		super.initComponent();
		flipView = (FlipViewController) this.findViewById(R.id.flip);
		flipView.init(this, FlipViewController.HORIZONTAL);
		adapter = new AppDeatailAdapter(this,flipView);
		flipView.setAdapter(adapter);
		preBtn = (Button) this.findViewById(R.id.appdetail_btn_left);
		nextBtn = (Button) this.findViewById(R.id.appdetail_btn_right);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		flipView.onResume();
		registerReceiverBroad();
		if(currentPosition>0&&currentPosition<adapter.getCount()){
			flipView.setSelection(currentPosition);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		flipView.onPause();
		unRegisterReceiverBroad();
	}

	@Override
	protected void initEvent() {
		super.initEvent();
		preBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(currentPosition>1){
					flipView.setSelection(--currentPosition);
				}
			}
		});
		
		nextBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(currentPosition<adapter.getCount()-1){
					flipView.setSelection(++currentPosition);
				}
			}
		});
		flipView.setOnViewFlipListener(new FlipViewController.ViewFlipListener() {
			@Override
			public void onViewFlipped(View view, int position) {
				currentPosition = position;
				App app = XmlHelper.getInstance().getAllApps().get(position);
				if(app!=null){
					titleText.setText(app.typename);
				}
				
			}
		});
	}

	@Override
	public void switchGameView() {
		Intent intent = new Intent(AppDetailActivity.this,SoftwareActivity.class);
		intent.putExtra("id", SoftwareActivity.GAME);
		startActivity(intent);
		finish();
		
	}

	@Override
	public void switchSoftwareView() {
		Intent intent = new Intent(AppDetailActivity.this,SoftwareActivity.class);
		intent.putExtra("id", SoftwareActivity.SOFTWARE);
		startActivity(intent);
		finish();

	}

	@Override
	public void switchSearchView() {
		Intent intent = new Intent(AppDetailActivity.this,SoftwareActivity.class);
		intent.putExtra("id", SoftwareActivity.SEARCH);
		startActivity(intent);
		finish();
	}
	
	
	protected BaseMsgBroadCastReceiver baseMsgBroadCastReceiver = null;
	protected boolean registerBroadCast = false;
	
	protected void unRegisterReceiverBroad(){
		if(registerBroadCast){
			AppDetailActivity.this.unregisterReceiver(baseMsgBroadCastReceiver);
			registerBroadCast = false;
		}
	}
	
	protected void registerReceiverBroad(){
		if(registerBroadCast==false){
			baseMsgBroadCastReceiver = new BaseMsgBroadCastReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ServerApplication.mContext.getPackageName()+".msg");
			this.registerReceiver(baseMsgBroadCastReceiver, filter);	
			registerBroadCast = true;
		}
	}
	
	class BaseMsgBroadCastReceiver extends BroadcastReceiver {

		private static final String TAG = "BaseMsgBroadCastReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			
			int receiveCmd = intent.getIntExtra("cmd",0);
			Log.i(TAG,"收到广播命令:"+receiveCmd);
			switch(receiveCmd){
			case 2:		
				handler_push();
				break;
			default:
				break;
			}
		}
	}
	
	public void handler_push(){
		
		App app = XmlHelper.getInstance().getAllApps().get(currentPosition);
		if(app==null) return;
		if(app.type!=null&&app.type.equals(App.DICE)&&dialog==null){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("电信体验系统");
			builder.setMessage("是否开始博饼游戏");
			builder.setPositiveButton("取消",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.setNegativeButton("确定", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent();
					intent.setClass(AppDetailActivity.this, DiceActivity.class);
					AppDetailActivity.this.startActivity(intent);
					SendMessageUtils.sendSwitchShakeMessage();
				}
			});
			dialog = builder.create();
			dialog.show();
		}
		else{
			StringBuffer str = new StringBuffer();
			str.append("点击下载\""+app.name+"\",下载地址："+app.getDownloadUrl());
			SendMessageUtils.sendPushMessage(str.toString());
		}
	}

}

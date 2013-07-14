package com.telecomexperience.net;

import com.telecomexperience.ServerApplication;
import com.telecomexperience.StackManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class BaseReceiveActivity extends Activity{
	
	protected BaseMsgBroadCastReceiver baseMsgBroadCastReceiver = null;
	protected boolean registerBroadCast = false;
	
	@Override
	public void onResume(){
        super.onResume();
        registerReceiverBroad();
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		unRegisterReceiverBroad();
	}
	
	protected void unRegisterReceiverBroad(){
		if(registerBroadCast){
			BaseReceiveActivity.this.unregisterReceiver(baseMsgBroadCastReceiver);
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
			case 1:		
				handler_shake();			//摇骰 子
				break;
			case 2:
				handler_push();				//推送
				break;
			case 3:
				handler_status();
				break;
			default:
				break;
			}
		}
	}
	
	public void handler_push(){
		
	}
	
	public void handler_shake(){
		
	}
	
	public void handler_status(){
		
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		StackManager.put(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		StackManager.pull(this);
	}
	
}

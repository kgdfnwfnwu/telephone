package com.telecomexperience.utils;

import android.content.Intent;
import android.os.Bundle;

import com.telecomexperience.ServerApplication;

public class SendBroadcastMsg {
	private static final String TAG = "SendBroadcastMsg";
	private Intent intent = null;
	private static SendBroadcastMsg instance=null;
	
	public SendBroadcastMsg(){
		intent = new Intent(ServerApplication.mContext.getPackageName()+".msg");
	}
	
	public static final String CMD="cmd";
	public static final String DATA="data";
	
	/*
	 * 返回一个实例
	 */
	public static SendBroadcastMsg getInstance(){
		if(instance==null){
			instance= new SendBroadcastMsg();
		}
		return instance;
	}

	public void sendBroadMsg(Bundle bundle) {
		intent.putExtras(bundle);
		ServerApplication.mContext.sendBroadcast(intent);     //发送广播
	}
	
	/**
	 * 摇一摇骰子回馈
	 * @param result
	 */
	public void sendBroadShakeMsg() {
		intent.putExtra(CMD, 1);
		ServerApplication.mContext.sendBroadcast(intent);     //发送广播
	}
	
	/**
	 * 用户状态刷新
	 * @param result
	 */
	public void sendBroadStatusMsg() {
		intent.putExtra(CMD, 3);
		ServerApplication.mContext.sendBroadcast(intent);     //发送广播
	}
	
	/**
	 * 信息推送
	 * @param result
	 */
	public void sendBroadPushMsg() {
		intent.putExtra(CMD, 2);
		ServerApplication.mContext.sendBroadcast(intent);     //发送广播
	}
}


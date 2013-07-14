package com.telecomexperience;

import com.telecomexperience.utils.XmlHelper;

import android.app.Application;
import android.content.Context;

public class ServerApplication extends  Application{
	
	public static Context mContext;
	
	private static ServerApplication instance ;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		mContext = this.getApplicationContext();
		XmlHelper.getInstance();
	}
	
	public static ServerApplication getInstance(){
		return instance;
	}
	
	
	public Context getContext (){
		return mContext;
	}

}

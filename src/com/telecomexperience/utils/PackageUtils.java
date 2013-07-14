package com.telecomexperience.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import com.telecomexperience.ServerApplication;

public class PackageUtils {
	


	public static boolean hasPackageName(String packagename){
		PackageInfo packageInfo;
		try {
			packageInfo = ServerApplication.getInstance().getContext().getPackageManager().getPackageInfo(packagename,0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
//			e.printStackTrace();
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
    }
	
	public static boolean jumpPackage( String packagename,String appname){
		if(hasPackageName(packagename)){
			jumpActivity(ServerApplication.getInstance().getContext(),packagename);
			return true;
		}
		else{
//			Toast.makeText(ServerApplication.getInstance().getContext(), "未安装"+appname+"请先下载",Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	private static void jumpActivity(Context context,String packageName){
		Intent intent=context.getPackageManager().getLaunchIntentForPackage(packageName);
		try {
			PackageInfo  pageageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			if(pageageInfo!=null){
//				System.out.println("版本号:"+pageageInfo.versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(intent != null){
			context.startActivity(intent);
		}
	}
}
package com.telecomexperience;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

public class StackManager {

	public static List< WeakReference<Activity>> activityList = new ArrayList<WeakReference<Activity>>();
	public static List< WeakReference<FragmentActivity>> fragmentActivityList = new ArrayList<WeakReference<FragmentActivity>>();
	
	
	public static void putFragmentActivity(
			FragmentActivity activity) {
		if(!fragmentActivityList.contains(activity)){
			fragmentActivityList.add(new WeakReference<FragmentActivity>(activity) );	
		}
	}
	
	public static void put(Activity activity){
		if(!activityList.contains(activity)){
			activityList.add(new WeakReference<Activity>(activity) );	
		}
	}
	
	public static void closeActivitys(){
		for(WeakReference<Activity> wrf:activityList){
			if(wrf!=null&&wrf.get()!=null){
				wrf.get().finish();
			}
		}
		activityList.clear();
		for(WeakReference<FragmentActivity> wrf:fragmentActivityList){
			if(wrf!=null&&wrf.get()!=null){
				wrf.get().finish();
			}
		}
		fragmentActivityList.clear();
	}
	
	public static int getCount(){
		int count =0;
		for(WeakReference<Activity> wrf:activityList){
			if(wrf!=null&&wrf.get()!=null){
				count++;
			}
		}
		return count;
	}
	
	public static void pull(Activity activity){
		for(WeakReference<Activity> wrf:activityList){
			if(wrf!=null&&wrf.get()!=null){
				if(wrf.get().equals(activity)){
					activityList.remove(wrf);
					wrf.clear();
					return;
				}
			}
		}
	}

	public static void pullFragmentActivity(FragmentActivity activity){
		for(WeakReference<FragmentActivity> wrf:fragmentActivityList){
			if(wrf!=null&&wrf.get()!=null){
				if(wrf.get().equals(activity)){
					fragmentActivityList.remove(wrf);
					wrf.clear();
					return;
				}
			}
		}
	}
}

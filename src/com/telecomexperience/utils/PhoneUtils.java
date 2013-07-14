package com.telecomexperience.utils;

import com.telecomexperience.ServerApplication;

import android.app.Activity;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneUtils {

	private static final String TAG = "PhoneUtils";

	public void getInfo() {
		TelephonyManager mTm = (TelephonyManager) ServerApplication.mContext
				.getSystemService(Activity.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		String imsi = mTm.getSubscriberId();
		String mtype = android.os.Build.MODEL; // ÊÖ»úÐÍºÅ
		String numer = mTm.getLine1Number();
		Log.d(TAG, String.format("imei=%s,imsi=%s,mtype=%s,numer=%s", imei,
				imsi, mtype, numer));
	}

}

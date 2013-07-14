package com.telecomexperience.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetWorkUtils {

	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 判断wifi是否打开
	public static boolean isWifiActive(Context ctx) {
		ConnectivityManager mConnectivity = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectivity != null) {
			NetworkInfo[] infos = mConnectivity.getAllNetworkInfo();

			if (infos != null) {
				for (NetworkInfo ni : infos) {
					if ("WIFI".equals(ni.getTypeName()) && ni.isConnected())
						return true;
				}
			}
		}

		return false;
	}

	// 得到本机IP地址
	public static String getLocalIpAddress() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = nif.getInetAddresses();
				while (enumIpAddr.hasMoreElements()) {
					InetAddress mInetAddress = enumIpAddr.nextElement();
					if (!mInetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(mInetAddress
									.getHostAddress())) {
						return mInetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("获取本地IP", "获取本地IP地址失败");
		}

		return null;
	}

	// 获取本机MAC地址
	public static String getLocalMacAddress(Context ctx) {
		WifiManager wifi = (WifiManager) ctx
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

}

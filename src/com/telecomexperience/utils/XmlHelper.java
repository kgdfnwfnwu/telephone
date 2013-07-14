package com.telecomexperience.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Xml;
import android.widget.ImageView;

import com.telecomexperience.R;
import com.telecomexperience.ServerApplication;
import com.telecomexperience.app.App;

/**
 * 解析应用类
 * @author Administrator
 */
public class XmlHelper {
	
	private static final String TAG = "Smileyhelper";
	private Map<String, App> gameAppsMap;
	private static List<App> gameAppList;
	private static List<App> softwareList;	
	private Map<String, App> appsMap;
	private List<App> allApps = new ArrayList<App>();
	
	private XmlHelper() {
		gameAppList= initAppList(R.raw.games,"game");			//初始化游戏应用
		gameAppsMap = initGameAppMap();
		softwareList= initAppList(R.raw.apps,"app");					//初始化应用
		appsMap = initAppMap();
		if(softwareList!=null){
			for(App app1:softwareList){
				app1.typename = App.SOFTWARE;
				allApps.add(app1);
			}
		}
		if(gameAppList!=null){
			for(App app1:gameAppList){
				app1.typename = App.GAMETYPE;
				allApps.add(app1);
			}
		}
	}
	
	public int getPosition(String appname){
		if(appname==null||"".equals(appname)) return 0;
		if(allApps == null) return 0;
		int index = 0;
		for(App app:allApps){
			if(app!=null&&appname.equals(app.name)){
				return index;
			}
			index++;
		}
		return 0;
	}
	
	private static XmlHelper instance = new XmlHelper();
	
	public static XmlHelper getInstance(){
		return instance;
	}
	
	public List<App> getRandomApp(){
		List<Integer> list = new ArrayList<Integer>();
		List<App> list1 = new ArrayList<App>();
		int size = allApps.size();
		int position =0;
		for(int i=0;i<3;i++){
			do{
				position = (int)(Math.random()*(size-1));
			}while(list.contains(position));
			if(position<size){
				App app = allApps.get(position);
				list.add(position);
				list1.add(app);
			}
		}
		return list1;
	}
	
	public List<App> getSoftwareAppList() {
		return softwareList;
	}

	public App getGameApp(int id){
		if(gameAppsMap!=null&&gameAppsMap.containsKey(id)){
			return gameAppsMap.get(id);
		}
		else return null;
	}

	
	public App getApp(int id){
		if(appsMap!=null&&appsMap.containsKey(id)){
			return appsMap.get(id);
		}
		else return null;
	}
	
	/**
	 * 设置图片
	 */
	public static void setImage(Context context, ImageView imgView, String name) {
		int resId = getImageRid(context, name);
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
		if (bitmap != null){
			imgView.setImageBitmap(bitmap);
		}
	}

	public static int getImageRid(Context context, String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resId = context.getResources().getIdentifier(name, "drawable",appInfo.packageName);
		return resId;
	}

	private List<App> initAppList(int resid,String tag) {
		InputStream inStream = null;
		inStream = ServerApplication.getInstance().getContext().getResources().openRawResource(resid);
		List<App> gameList = null;
		App currentApp = null;
		XmlPullParser xmlPull = Xml.newPullParser();
		try {
			xmlPull.setInput(inStream, "UTF-8");
			int eventCode = xmlPull.getEventType();
			while (eventCode != XmlPullParser.END_DOCUMENT) {
				switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: 					// 文档开始事件,可以做一些数据初始化处理
					gameList = new ArrayList<App>();
					break;
				case XmlPullParser.START_TAG:						// 元素开始.
					String name = xmlPull.getName();
					if (name.equalsIgnoreCase(tag)) {
						currentApp = new App();
						currentApp.name=xmlPull.getAttributeValue(null,"name");
						currentApp.packagename=xmlPull.getAttributeValue(null,"packagename");
						currentApp.residStr=xmlPull.getAttributeValue(null,"resid");
						currentApp.webUrl=xmlPull.getAttributeValue(null,"weburl");
						currentApp.type=xmlPull.getAttributeValue(null, "type");
						currentApp.pinYin = PinYinHelper.getPingYin(currentApp.name);
					}
					break;
				case XmlPullParser.END_TAG: 						// 元素结束,
					if (currentApp != null && xmlPull.getName().equalsIgnoreCase(tag)) {
						gameList.add(currentApp);
						currentApp = null;
					}
					break;
				}
				eventCode = xmlPull.next();// 进入到一下一个元素.
			}
		} catch (XmlPullParserException e) {
//			Log.i(TAG, e.toString());
		} catch (IOException e) {
//			Log.i(TAG, e.toString());
		}
		return gameList;
	}
	
	private Map<String, App> initGameAppMap() {
		Map<String, App> appMap = null;
		appMap = new HashMap<String, App>();
		if (gameAppList == null || gameAppList.size() == 0) {
			return appMap;
		}
		for (App app : gameAppList) {
			appMap.put(app.name, app);
		}
		return appMap;
	}

	private Map<String, App> initAppMap() {
		Map<String, App> appMap = null;
		appMap = new HashMap<String, App>();
		if (softwareList == null || softwareList.size() == 0) {
			return appMap;
		}
		for (App app : softwareList) {
			appMap.put(app.name, app);
		}
		return appMap;
	}
	
	
	public boolean isContainSimleyId(int smileyId){
		if(gameAppsMap.containsKey(smileyId)){
			return true;
		}
		return false;
	}

	public List<App> getGameAppAllList() {
		
		return gameAppList;
	}

	public List<App> getAllApps() {
		return allApps;
	}

}
package com.telecomexperience.app;

public class App {	
	
	public String name;
	public int resid;
	public String residStr;
	public String packagename;
	public String webUrl;
	public String type;
	public String pinYin;
	
	public static final String OTHERAPP="other app";
	public static final String DICE="local app";
	public static final String GAMETYPE="游戏 Game";
	public static final String SOFTWARE="应用 Software";
	public String typename;
	
	public boolean isContain(String src){
		if(name!=null&&!"".equals(name)&&name.contains(src)){
			return true;
		}
		else if(pinYin!=null&&!"".equals(pinYin)&&pinYin.contains(src)){
			return true;
		}
		return false;
	}
	
	public String getDownloadUrl(){
		return webUrl+"/download?pos=www/detail";
	}
	
	public App(){}
}
	

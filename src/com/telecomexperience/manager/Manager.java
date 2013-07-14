package com.telecomexperience.manager;

import java.util.ArrayList;
import java.util.List;

import com.telecomexperience.app.App;
import com.telecomexperience.utils.XmlHelper;

public class Manager {

	public static List<App> search(String name){
		List<App> searchResultList = new ArrayList<App>();
		if(name==null||"".equals(name)) return searchResultList;
		List<App> list = XmlHelper.getInstance().getSoftwareAppList();
		if(list!=null){
			for(App app:list){
				if(app.name.contains(name)){
					searchResultList.add(app);
				}
			}
		}
		
		list = XmlHelper.getInstance().getGameAppAllList();
		if(list!=null){
			for(App app:list){
				if(app.isContain(name)){
					searchResultList.add(app);
				}
			}
		}
		
		return searchResultList;
	}
}

package com.telecomexperience.data;

import java.util.Collection;

import com.telecomexperience.net.NetThreadHelper;
import com.telecomexperience.net.User;

public class GlobalVar {
	
	private User mUser ;
	
	private static GlobalVar instance = new GlobalVar();
	
	private GlobalVar(){}
	
	public static GlobalVar getInstance(){
		return instance;
	}
	
	public User getUser(){
		if(null!=mUser){
			return mUser;
		}
		else{
			Collection<User> list = NetThreadHelper.newInstance().getUsers().values();
			if(list==null) return null;
			for(User user : list){
				this.mUser =  user; 
				return user;
			}
		}
		return null;
	}
	
	public void setUser(User user){
		this.mUser  = user;
	}

}

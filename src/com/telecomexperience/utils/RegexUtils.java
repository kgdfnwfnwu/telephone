package com.telecomexperience.utils;

import java.util.regex.Pattern;

public class RegexUtils {
	
	/**
	 * 用正则表达式判断整型
	 */
	public static boolean isNumeric(String str){   
		if(str==null||"".equals(str)) return false;
	    Pattern pattern = Pattern.compile("-?[0-9]*");   
	    return pattern.matcher(str).matches();      
	} 
}

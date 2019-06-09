package com.yc.blog.utils;

public class Util {
	
	public static String subTag(String content) {
		return content.replaceAll("<.+?>","").replaceAll("&.+?;", "").replaceAll("\\s", " ");
	}
}

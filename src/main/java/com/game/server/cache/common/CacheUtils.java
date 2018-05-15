package com.game.server.cache.common;


import java.io.Serializable;
import java.util.HashMap;





public class CacheUtils {

	public static final String TABLE_PK_CONFIG = "TABLE_PK_CONFIG";
	public static final String TABLE_SEQ_CONDIG = "TABLE_SEQ_CONDIG";

	public static <T> T getCache(String key){
		INetCache cache =SpringContextUtil.getBean("iNetCache");
		return (T) cache.get(1, key);
	}
	
	public static <T> T getCache(int namespace,String key){
		INetCache cache = SpringContextUtil.getBean("iNetCache");
		return (T) cache.get(namespace, key);
	}

	public static void addCache(String key,
			HashMap<String, String> serial) {
		INetCache cache = SpringContextUtil.getBean("iNetCache");
		cache.set(1, key, serial);
	}
	public static void addCache(String key,
			Serializable  serial) {
		INetCache cache = SpringContextUtil.getBean("iNetCache");
		cache.set(1, key, serial);
	}

}

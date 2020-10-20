package com.game.server.cache.common;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Set;





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
	public static void lpush(String key, Serializable value) {
		INetCache cache = SpringContextUtil.getBean("iNetCache");
		cache.r_lpush(key, value, 36000);
	}
	public static <T> List<T> lrange(String key){
		INetCache cache = SpringContextUtil.getBean("iNetCache");
		return (List<T>) cache.r_lrange(key);
		
	}
	public static  void lrem(String key, Serializable value) {
		INetCache cache = SpringContextUtil.getBean("iNetCache");
		cache.r_lrem(key, value);
	}

	public static Set<String> h_hkeys(String key) {
		// TODO Auto-generated method stub
		INetCache cache = SpringContextUtil.getBean("iNetCache");
		return cache.h_hkeys(key);
	}

	
	public static void hdel(String key, String... fileds) {
		// TODO Auto-generated method stub
		INetCache cache = SpringContextUtil.getBean("iNetCache");
		cache.h_hdel(key, fileds);
	}
	
}

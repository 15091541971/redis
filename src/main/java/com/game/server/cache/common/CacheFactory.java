package com.game.server.cache.common;

import java.util.ArrayList;
import java.util.HashMap;

import com.game.server.cache.conf.CacheValues;

import com.game.server.cache.redis.RedisCache;
import com.game.server.cache.redis.RedisJCache;
import com.game.server.cache.tair.TairCache;


/**
 * @author Reason.Yea 
 * @version 创建时间：Jan 8, 2014
 */
public final class CacheFactory {
	
	public static INetCache getCache(){
		return getCacheByType(CacheValues.CORE_CACHE_TYPE);
	}
	
	public static INetCache getCacheByType(String cacheType){
		//未配置cache类型，则默认使用配置级别
		if(cacheType==null || cacheType.equals("")){
			cacheType=CacheValues.CORE_CACHE_TYPE;
		}
		INetCache cache = null;
		if(cacheType.equals(CacheValues.CACHE_TYPE_MEMCACHED)){//memcached
			//支持2种cached驱动
			/*if(CacheValues.MEMCACHED_SPY.equals( CacheValues.MEMCACHED_CONTEXT)){
				cache= SpyCache.getCache();
			}else{
				cache= XmCache.getCache();
			}*/
		}else if(cacheType.equals(CacheValues.CACHE_TYPE_TAIR)){//tair
			cache = TairCache.getCache();
		}else if(cacheType.equals(CacheValues.CACHE_TYPE_REDISS)){//redis单机
			cache =RedisCache.getCache();
		}else if(cacheType.equals(CacheValues.CACHE_TYPE_REDISJ)){//redis集群
			cache =RedisJCache.getCache();
		}else if(cacheType.equals(CacheValues.CACHE_TYPE_COHERENCE)){//coherence
			/*cache = CoherenceCache.getCache();*/
		}else if(cacheType.equals(CacheValues.CACHE_TYPE_DY_MEMCACHED)){//
			/*cache =DyXmCache.getCache();*/
		}else if(cacheType.equals(CacheValues.CACHE_TYPE_ZREDIS)){//zredis
			/*cache =ZredisCache.getCache();*/
		}
		return cache;
	}
	
	public static NetFastCache getFastCache(){
		NetFastCache fastCache = new NetFastCache();
		return fastCache;
	}
	
	public static RedisJCache getRedisCache(){
		return RedisJCache.getCache();
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		System.setProperty("CONFIG","E:/ebusinessn/base/src/main/java/config/");
		RedisJCache cache = getRedisCache();
		String key = "Hello";
		HashMap val = new HashMap();
//		val.put("ORDER_ATTR_PRIM_KEYS_FIXZBWO201412101429388710", "2eeb40e4ebc846e7a33a69fd7b8e869e");
//		val.put("bb", "23333");
//		String val = "world";
		cache.set("ORDER_ATTR_PRIM_KEYS_FIXZBWO201412101429388710", "wuhui");
		System.out.println(cache.get("ORDER_ATTR_PRIM_KEYS_FIXZBWO201412101429388710"));
//		
//		Object m = cache.getMapFieldVal(0, key, "aa");
//		utils.SystemUtils.printLog(m);
//		
//		cache.setMapFieldVal(0, key, "aa", "0000000", 10);
//		cache.setMapFieldVal(0, key, "bb", "11111111", 10);
//		Object f = cache.getMapFieldVal(0, key, "aa");
//		utils.SystemUtils.printLog(f);
//		
//		Object b = cache.get(0, key);
//		utils.SystemUtils.printLog(b);
		
		
//		utils.SystemUtils.printLog(":::"+cache.get("look"));
//		HashMap map =new HashMap();
//		map.put("aaa","111");
//		map.put("xx",(new ArrayList()).add("xxxxxxxxxxx"));
//		
//		cache.set("look", map);
//		
//		HashMap lookMap = (HashMap) cache.get("look");
//		utils.SystemUtils.printLog(":::"+lookMap);
//		
//		
//		map.put("aaa", "333");
//		
//		cache.set("look", map);
//		lookMap = (HashMap) cache.get("look");
//		utils.SystemUtils.printLog(":::"+lookMap);
		
//		INetCache cache = getCache();
//		INetCache cache = getCacheByType(CacheValues.CACHE_TYPE_EHCACHE);
		
//		long t1 = System.currentTimeMillis();
//		HashMap map = getObj();
//		int t = 1000;
//		for (int i = 0; i < t; i++) {
//			cache.set(i+"_time", map);
//		}
//		long t2 = System.currentTimeMillis();
//		for (int i = 0; i < t; i++) {
//			HashMap cur=(HashMap) cache.get(i+"_time");
//			String c = (String) cur.get("aaa");
//			utils.SystemUtils.printLog("::::::::::::::::::::::::::::::::::::::::::::::::::::::>"+c);
//		}
//		long t3 = System.currentTimeMillis();
//		
//		utils.SystemUtils.printLog("===========set:::::"+(t2-t1));
//		utils.SystemUtils.printLog("===========get:::::"+(t3-t2));
	}
	
	public static HashMap getObj(){
		HashMap map = new HashMap();
		map.put("aaa","111");
		map.put("xx",(new ArrayList()).add("xxxxxxxxxxx"));
		return map;
	}
}

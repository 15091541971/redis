package com.game.server.cache.common;

import com.game.server.cache.conf.CacheValues;



/**
 * @author 
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
		}else if(cacheType.equals(CacheValues.CACHE_TYPE_COHERENCE)){//coherence
			/*cache = CoherenceCache.getCache();*/
		}else if(cacheType.equals(CacheValues.CACHE_TYPE_DY_MEMCACHED)){//
			/*cache =DyXmCache.getCache();*/
		}else if(cacheType.equals(CacheValues.CACHE_TYPE_ZREDIS)){//zredis
			/*cache =ZredisCache.getCache();*/
		}
		return cache;
	}
	
}

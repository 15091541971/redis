package com.game.server.cache.ehcache;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.server.cache.common.INetCache;
import com.game.server.cache.redis.DefaultRedisClient;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
@Service("iNetCache")
public class RedisCacheImpl implements INetCache {
	private static Logger logger = Logger.getLogger(RedisCacheImpl.class);
   @Autowired
    private DefaultRedisClient defaultRedisClient;
	
	/*private static CacheManager manager =AdminServerContext.getBean("cacheManagerFactory");*/
	
	private RedisCacheImpl() {
	}

	private static final RedisCacheImpl inst = new RedisCacheImpl();

	public static RedisCacheImpl getCache() {
		return inst;
	}

	private Cache getCacheByNameSpace(String nameSpace) {
		Cache cache = null;
		try {
//			CacheManager manager = CacheManager.getInstance();
//			String configPath = System.getProperty("CONFIG");
//			configPath+="ehcache.xml";
//			CacheManager manager = new CacheManager(configPath);
			/*cache = manager.getCache(nameSpace);
			if (cache == null) {
				manager.addCache(nameSpace);
				cache = manager.getCache(nameSpace);
			}*/
		} catch (Exception e) {
			throw new CacheException(e);
		}
		return cache;
	}

	@Override
	public void clear() {
		clear(defaltNameSpace);
	}

	@Override
	public void clear(int nameSpace) {
		Cache cache = getCacheByNameSpace(nameSpace + "");
		try {
			cache.removeAll();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		throw new CacheException("method not implemented!");
	}

	@Override
	public void delete(String key) {
		delete(defaltNameSpace, key);
	}

	@Override
	public void delete(int nameSpace, String key) {
		Cache cache = getCacheByNameSpace(nameSpace + "");
	
		try {
			cache.remove((Serializable) key);
		} catch (CacheException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Serializable get(String key) {
		return get(defaltNameSpace, key);
	}

	@Override
	public synchronized Serializable get(int nameSpace, String key) {
		
		return (Serializable) defaultRedisClient.r_get(nameSpace+"_"+key);
	}

	@Override
	public void set(String key, Serializable value) {
		set(defaltNameSpace, key, value);
	}

	@Override
	public synchronized void set(int nameSpace, String key, Serializable value) {
		defaultRedisClient.r_set(nameSpace+"_"+key, value,3600);
		
	

	}

	@Override
	public synchronized void set(int nameSpace, String key, Serializable value, int expireTime) {
		// TODO Auto-generated method stub
		defaultRedisClient.r_set(nameSpace+"_"+key, value, expireTime);
	}

	
	

}

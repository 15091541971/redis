package com.game.server.cache.common;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.game.server.cache.common.zte.FastCacheRequest;
import com.game.server.cache.conf.CacheValues;

/**
 * @author Reason.Yea 
 * @version 创建时间：2014-11-25
 */
public class NetFastCache implements INetCache {
	//快速cache，目前暂定为ecache
	public static INetCache fastCache = CacheFactory.getCacheByType(CacheValues.CACHE_TYPE_EHCACHE);
	
	//默认cache，主要指memcached和tair
	public static INetCache normalCache = CacheFactory.getCache();
	
	//默认cache是否为ecache，如果相同，防止重复取值和设值
	boolean theSame=CacheValues.CORE_CACHE_TYPE.equals(CacheValues.CACHE_TYPE_EHCACHE);
//	ThreadPoolExecutor executor = ThreadPoolFactory.getSingleThreadPoolExector();
//	FastCacheRequest request = new FastCacheRequest();
	
	@Override
	public Serializable get(String key) {
		return get(CacheValues.CORE_CACHE_DEFAULT_NAMESPACE,key);
	}

	@Override
	public Serializable get(int nameSpace, String key) {
		Serializable obj = fastCache.get(nameSpace,key);
		Serializable obj1 = normalCache.get(nameSpace,key);
		if(obj==null && !theSame){
			obj = normalCache.get(nameSpace,key);
		}
		return obj;
	}

	@Override
	public void set(String key, Serializable value) {
		this.set(CacheValues.CORE_CACHE_DEFAULT_NAMESPACE,key, value);
	}

	@Override
	public void set(int nameSpace, String key, Serializable value) {
		this.set(CacheValues.CORE_CACHE_DEFAULT_NAMESPACE,key, value,CacheValues.CORE_CACHE_SESSION_TIMEOUT);
	}

	@Override
	public void set(int nameSpace, String key, Serializable value,
			int expireTime) {
		Object obj2 = fastCache.get(nameSpace, key);
		fastCache.delete(nameSpace, key);
		fastCache.set(nameSpace,key, value,expireTime);
		Object obj = fastCache.get(nameSpace, key);
		if(!theSame){
			FastCacheRequest request = new FastCacheRequest();
			request.setNameSpace(nameSpace);
			request.setValue(value);
			request.setKey(key);
			request.setExpireTime(expireTime);
//			TaskThreadPool taskThreadPool = new TaskThreadPool(new Task(request) {
//				public ZteResponse execute(ZteRequest request) {
//					FastCacheRequest req =(FastCacheRequest)request;
			normalCache.set(request.getNameSpace(),request.getKey(), request.getValue(),request.getExpireTime());
//					return new ZteResponse();
//				}
//			});
//			ThreadPoolFactory.submit(taskThreadPool, executor);
		}
		
	}

	@Override
	public void delete(String key) {
		delete(CacheValues.CORE_CACHE_DEFAULT_NAMESPACE,key);
	}

	@Override
	public void delete(int nameSpace, String key) {
		fastCache.delete(nameSpace,key);
		if(!theSame){
			normalCache.delete(nameSpace,key);
		}
		
	}

	@Override
	public void clear() {
		clear(CacheValues.CORE_CACHE_DEFAULT_NAMESPACE);
	}

	@Override
	public void clear(int nameSpace) {
		fastCache.clear(nameSpace);
		if(!theSame){
			normalCache.clear(nameSpace);
		}
	}

	@Override
	public void close() {
		fastCache.close();
		if(!theSame){
			normalCache.close();
		}
	}

	@Override
	public List<Serializable> r_lrange(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void r_lpush(String key, Serializable value, int expireTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void r_lrem(String key, Serializable value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> h_hkeys(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void h_hdel(String key, String... fileds) {
		// TODO Auto-generated method stub
		
	}
}

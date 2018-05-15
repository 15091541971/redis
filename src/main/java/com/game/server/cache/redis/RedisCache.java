package com.game.server.cache.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.game.server.cache.common.CacheKeyUtil;
import com.game.server.cache.common.INetCache;

/**
 * @author Reason.Yea 
 * @version 创建时间：2014-12-9
 */
public class RedisCache implements INetCache {

	public DefaultRedisClient client = null;
	
	public RedisCache(){
		client =new DefaultRedisClient();
	}
	
	private static final RedisCache inst = new RedisCache();
	
	public  void connect(){
		client.connect("");
	}

	public static RedisCache getCache(){
		return inst ;
	}
	
	@Override
	public Serializable get(String key) {
		
		Serializable obj =  get(defaltNameSpace,key);
		return obj;
	}

	@Override
	public Serializable get(int nameSpace, String key) {
		Serializable obj = client.r_get(CacheKeyUtil.getKey(nameSpace, key));
		return obj;
	}

	@Override
	public void set(String key, Serializable value) {
		set(defaltNameSpace,key,value);
	}

	@Override
	public void set(int nameSpace, String key, Serializable value) {
		set(nameSpace, key, value, 3600*24*30);
	}

	@Override
	public void set(int nameSpace, String key, Serializable value,
			int expireTime) {
		
		client.r_set(CacheKeyUtil.getKey(nameSpace, key), value, expireTime);
	}


	public void setMapFieldVal(int nameSpace,String key,String field,Serializable value,int expireTime){
		client.h_set(key, field, value, expireTime);
	}
	
	
	public Serializable getMapFieldVal(int nameSpace,String key,String field){
		return client.h_get(key, field);
	}
	
	
	@Override
	public void delete(String key) {
		client.r_delete(key);
	}

	@Override
	public void delete(int nameSpace, String key) {
		client.r_delete(CacheKeyUtil.getKey(nameSpace, key));
	}

	@Override
	public void clear() {
		
	}

	@Override
	public void clear(int nameSpace) {
		
	}

	@Override
	public void close() {
		client.close();
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

package com.game.server.cache.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.game.server.cache.common.INetCache;

/**
 * @author wu.hui
 * @version 创建时间：2015-07-20
 */
public class RedisJCache implements INetCache {

	public DefaultJRedisClient client = null;
	public RedisJCache(){
		client =new DefaultJRedisClient();
	}
	
	private static final RedisJCache inst = new RedisJCache();
	
	public static RedisJCache getCache(){
		return inst ;
	}
	@Override
	public Serializable get(String key) {
		Serializable obj =  get(defaltNameSpace,key);
		return obj;
	}

	@Override
	public Serializable get(int nameSpace, String key) {
		
		Serializable obj = client.get(nameSpace,"",key);
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
		client.put(nameSpace,"", key,value, 1,expireTime*60);
	}
	
	@Override
	public void delete(String key) {
		client.delete(defaltNameSpace,"",key);
	}

	@Override
	public void delete(int nameSpace, String key) {
		client.delete(nameSpace,"",key);
	}

	@Override
	public void clear() {
		
	}

	@Override
	public void clear(int nameSpace) {
		
	}

	@Override
	public void close() {
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

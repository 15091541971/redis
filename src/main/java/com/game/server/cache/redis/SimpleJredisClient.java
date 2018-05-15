package com.game.server.cache.redis;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class SimpleJredisClient {
	private String host;
	private String password;
	private String port;
	private int maxTotal = 100;
	private int maxIdel = 20;
	private long maxWaitMillis = 5000L;
	private boolean testOnBorrow = true;
	private boolean testOnReturn = true;
	public SimpleJredisClient(String host, String port, String password) {
		super();
		this.host = host;
		this.password = password;
		this.port = port;
	}
    public ShardedJedis getConn() {
    	
    		
    	
    	List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
    	ShardedJedis jedis=null;
		JedisShardInfo info=new JedisShardInfo(host, port);
		if(password!=null&&!"".equals(password)) {
			info.setPassword(password);	
			
		shards.add(info);
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(3000); // jedis的最大分配对象#
		config.setMaxIdle(3000);// jedis最大保存idel状态对象数
		config.setMaxWaitMillis(50000); // jedis池没有对象返回时，最大等待时间 #
		config.setTestOnBorrow(true); // jedis调用borrowObject方法时，是否进行有效检查#
		config.setTestOnReturn(true); // jedis调用returnObject方法时，是否进行有效检查
		ShardedJedisPool shardPool=new ShardedJedisPool(config, shards);
		 jedis=  shardPool.getResource();
    	}
		
		return jedis;
    }
    
    public String getValue(String key) {
    	return getConn().get(key);
    }
   
}

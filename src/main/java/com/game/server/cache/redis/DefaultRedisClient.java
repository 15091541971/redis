package com.game.server.cache.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

public class DefaultRedisClient  {
	private List<JedisShardInfo> shards = null;
	private ShardedJedisPool shardPool = null;

	private ShardedJedis shardJedis = null;
	private int maxTotal = 100;
	private int maxIdel = 20;
	private long maxWaitMillis = 5000L;

	private boolean testOnBorrow = true;
	private boolean testOnReturn = true;
	private String password;
	private String host;
	private int port;
	private String servers = "";
    public DefaultRedisClient() {
    	
    }
	public DefaultRedisClient(String  host,int port,String password) {
		this.host=host;
		this.port=port;
		this.password=password;
		init();
		connect(null);
	}
	public DefaultRedisClient(String  host,int port) {
		this.host=host;
		this.port=port;
		
		init();
		connect(null);
	}
	public static void main(String[] args) {
		DefaultRedisClient client = new DefaultRedisClient("127.0.0.1", 6379, "123456");
		String key = "Hello";
		Map<String, Object> val = new HashMap();
		val.put("aa", "1");
		val.put("bb", "2");
		
		List<Map<String, Object> > list=new ArrayList<>();
		list.add(val);
		
		/*
		Object o =client.r_set(key, user, 100);*/
		Object p = client.r_get(key);
		
	     System.out.println(p);
		
		
		
	}
	
public void initParams() {
	
		shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo info=new JedisShardInfo(host, port);
		info.setPassword(password);
		shards.add(info);
	  /*  for (JedisShardInfo jedisShardInfo : shards) {
	    	System.out.println("password===================="+password);
	    	jedisShardInfo.setPassword(password);
	    	shards.add(jedisShardInfo);
		}*/
			
		
		maxTotal = 1000;
		maxIdel = 50;
		maxWaitMillis =5000;
		testOnBorrow = true;
		testOnReturn =true;
	}
	
	public void init() {
	initParams();
		
		if(shards == null || shards.isEmpty()) {
			shards = new ArrayList<JedisShardInfo>();
			String[] as = getServers().split(",");
			for(String server : as) {
				String[] sp = server.split(":");
				shards.add(new JedisShardInfo(sp[0], Integer.parseInt(sp[1])));
			}
		}
		
		shardPool = new ShardedJedisPool(initConfig(), shards);
	}

	private JedisPoolConfig initConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		
		config.setMaxTotal(maxTotal); // jedis的最大分配对象#
		config.setMaxIdle(maxIdel);// jedis最大保存idel状态对象数
		config.setMaxWaitMillis(maxWaitMillis); // jedis池没有对象返回时，最大等待时间 #
		config.setTestOnBorrow(testOnBorrow); // jedis调用borrowObject方法时，是否进行有效检查#
		config.setTestOnReturn(testOnReturn); // jedis调用returnObject方法时，是否进行有效检查
		return config;
	}

	/**
	 * @param key
	 */
	public void connect(Object key) {
		shardJedis = shardPool.getResource();
	}

	public void close() {
		if (shardJedis == null)
			return;
		shardPool.returnBrokenResource(shardJedis);
	}

	public ShardedJedis getJedis() {
		return shardJedis;
	}

	private byte[] objectToByte(java.lang.Object obj) {
		byte[] bytes =null;
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
			bo.close();
			oo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (bytes);
	}

	private static Object byteToObject(byte[] bytes){
        java.lang.Object obj=null;
        try {
	        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
	        ObjectInputStream oi = new ObjectInputStream(bi);
	        obj = oi.readObject();
	        bi.close();
	        oi.close();
        }catch(Exception e) {
            //e.printStackTrace();
        }
        return obj;
    }
	
	public ResultCode r_set(String key, Serializable value, int expireTime) {
		ResultCode rc = ResultCode.SUCCESS;
		if (shardJedis == null) {
			rc = ResultCode.CONNERROR;
			return rc;
		}
		try {
			shardJedis.set(key.getBytes(), objectToByte(value));
			shardJedis.expire(key, expireTime);
		} catch (Exception ex) {
			ex.printStackTrace();
			rc = ResultCode.SERVERERROR;
		}

		return rc;
	}

	
	public ResultCode r_mset(int expireTime, String... keysvalues) {
		// TODO Auto-generated method stub
		ResultCode rc = ResultCode.SUCCESS;
		if (shardJedis == null) {
			rc = ResultCode.CONNERROR;
			return rc;
		}
		try {
			int num = 0;
			ShardedJedisPipeline pipeline = shardJedis.pipelined();			
			for (int i = 0; i < keysvalues.length;) {
				try {
					Response<String> rsp = pipeline.set(keysvalues[i],
							keysvalues[i + 1]);
					pipeline.expire(keysvalues[i], expireTime);
				} catch (Exception ex) {
					num++;
				}
				i = i + 2;
			}
			//
			List<Object> results = pipeline.syncAndReturnAll();
			if (results.size() == 0) {
				rc = ResultCode.SENDFAILED;
			} else if (num > 0 && results.size() > 0) {
				rc = ResultCode.PARTSUCC;
			}
		} catch (Exception ex) {
			rc = ResultCode.SERVERERROR;
		}

		return rc;
	}

	
	public Serializable r_get(String key) {
		if (shardJedis == null) {
			return null;
		}
		try {
			byte[] value = shardJedis.get(key.getBytes());
			Object obj = byteToObject(value);
			return (Serializable)obj;
		} catch (Exception ex) {
			return null;
		}
	}

	
	public List<Object> r_mget(String... keys) {
		// TODO Auto-generated method stub
		if (shardJedis == null) {
			return null;
		}
		ShardedJedisPipeline pipeline = shardJedis.pipelined();
		// Map<String,Response<String>> result = new
		// HashMap<String,Response<String>>();
		try {
			for (String key : keys) {
				Response<String> rsp = pipeline.get(key);
			}
			List<Object> results = pipeline.syncAndReturnAll();
			return results;
		} catch (Exception ex) {
			return null;
		}

	}

	public Serializable h_get(String key,String field) {
		if (shardJedis == null) {
			return null;
		}
		try {
			byte[] value = shardJedis.hget(objectToByte(key), objectToByte(field));
			Object obj = byteToObject(value);
			return (Serializable)obj;
		} catch (Exception ex) {
			return null;
		}
	}
	public ResultCode h_set(String key, String field ,Serializable value, int expireTime) {
		ResultCode rc = ResultCode.SUCCESS;
		if (shardJedis == null) {
			rc = ResultCode.CONNERROR;
			return rc;
		}
		try {
			shardJedis.hsetnx(objectToByte(key), objectToByte(field), objectToByte(value));
			//shardJedis.set(key.getBytes(), objectToByte(value));
			shardJedis.expire(key, expireTime);
		} catch (Exception ex) {
			rc = ResultCode.SERVERERROR;
		}

		return rc;
	}
	
	public long r_delete(String key) {
		// TODO Auto-generated method stub
		if (shardJedis == null) {
			return -1;
		}
		try {
			long l = shardJedis.del(key);
			return l;
		} catch (Exception ex) {
			return -2;
		}
	}

	
	public long r_mdelete(String... keys) {
		// TODO Auto-generated method stub
		if (shardJedis == null) {
			return -1;
		}
		int num = 0;
		//
		try {
			ShardedJedisPipeline pipeline = shardJedis.pipelined();
			for (String key : keys) {
				try {
					Response<Long> rsp = pipeline.del(key);
				} catch (Exception ex) {
					num++;
				}
			}
			List<Object> results = pipeline.syncAndReturnAll();
			if (num > 0 && results.size() > 0) {
				return -2;
			} else {
				return results.size();
			}

		} catch (Exception ex) {
			return -1;
		}
	}

	
	public long r_incr(String key, int value, int defaultValue, int expireTime) {
		// TODO Auto-generated method stub
		if (shardJedis == null) {
			return -1;
		}
		try {
			Long l = shardJedis.incr(key);
			return l;
		} catch (Exception ex) {
			return -1;
		}
	}

	
	public long r_decr(String key, int value, int defaultValue, int expireTime) {
		// TODO Auto-generated method stub
		if (shardJedis == null) {
			return -1;
		}
		try {
			long l = shardJedis.decr(key);
			return l;
		} catch (Exception ex) {
			return -1;
		}
	}

	public Object getOrigin(Serializable key) {
		// TODO Auto-generated method stub

		connect(key);
		return shardJedis;
	}

	public List<JedisShardInfo> getShards() {
		return shards;
	}

	public void setShards(List<JedisShardInfo> shards) {
		this.shards = shards;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdel() {
		return maxIdel;
	}

	public void setMaxIdel(int maxIdel) {
		this.maxIdel = maxIdel;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	
	public Result<Integer> seqIncr(Serializable seqName, int initValue) {
		return null;
	}
	

	public Result<Long> seqMonth(Serializable seqName, int initValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public Result<Long> seqDate(Serializable seqName, int initValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public void recySeqCache() {
		// TODO Auto-generated method stub
		
	}


	public String getServers() {
		return servers;
	}


	public void setServers(String servers) {
		this.servers = servers;
	}


	public Result<Long> sequence(Serializable seqName) {
		// TODO Auto-generated method stub
		return null;
	}


	public String executeSetOrGetCommend(String commend, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public ShardedJedisPool getShardPool() {
		return shardPool;
	}

	public void setShardPool(ShardedJedisPool shardPool) {
		this.shardPool = shardPool;
	}

	public ShardedJedis getShardJedis() {
		return shardJedis;
	}

	public void setShardJedis(ShardedJedis shardJedis) {
		this.shardJedis = shardJedis;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}


	
}

package com.game.server.cache.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.game.server.cache.common.KryoUtils;
import com.game.server.cache.conf.CacheValues;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
public class DefaultJRedisClient  {
	private int maxTotal = 100;
	private int maxIdel = 20;
	private long maxWaitMillis = 5000L;
	private boolean testOnBorrow = true;
	private boolean testOnReturn = true;
	private String servers = "";
	private final static String CHARSET_NAME = "ISO-8859-1";
	private final static String SET_RESULT_OK = "OK";

	public DefaultJRedisClient() {
		initParams();
		String[] as = servers.split(",");
		Set<redis.clients.jedis.HostAndPort> clusterNodes = new HashSet<redis.clients.jedis.HostAndPort>();
		for (String server : as) {
			String[] sp = server.split(":");
			clusterNodes.add(new HostAndPort(sp[0], Integer.parseInt(sp[1])));
		}
		jedisCluster = new JedisCluster(clusterNodes);
	}
	

	private JedisCluster jedisCluster = null;


	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	
	
	public void initParams() {
		servers = CacheValues.REDIS_SERVER_LIST;
		maxTotal = CacheValues.REDIS_MAX_TOTAL;
		maxIdel = CacheValues.REDIS_MAX_IDEL;
		maxWaitMillis = CacheValues.REDIS_MAX_WAIT_MILLIS;
		testOnBorrow = CacheValues.REDIS_TEST_ON_BORROW;
		testOnReturn = CacheValues.REDIS_TEST_ON_RETURN;
	}
	
	
	
	/**
	 * 组装key前缀
	 * @param namespace
	 * @param pkey
	 * @return
	 */
	private String genPrefixKey(int namespace, String pkey){
		StringBuilder sb = new StringBuilder();
		sb.append(namespace).append("_").append(pkey).append("_");
		return sb.toString();
	}
	
	/**
	 * 获取数据
	 * redis没有namespace，此处把namespace和pkey都当作key的前缀
	 * key的组合形式为：namespace&pkey&key
	 * @param <T>
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 要获取的数据的key
	 * @return
	 */
	public <T> T get(int namespace, String pkey, String key){
		try{
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			String result = getJedisCluster().get(comb_key);
			if(result!=null){
				return (T)KryoUtils.fromBytes(result.getBytes(CHARSET_NAME));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取指定的key值，redis的get方法不支持修改失效时间，所以expireTime参数没有用到
	 * @param <T>
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 要获取的数据的key
	 * @param expireTime 数据的有效时间，单位为秒（该参数无用）
	 * @return
	 */
	
	public <T> T get(int namespace, String pkey, String key, int expireTime){
		try{
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			String result = getJedisCluster().get(comb_key);
			if(result!=null){
				return (T)KryoUtils.fromBytes(result.getBytes(CHARSET_NAME));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 批量获取数据
	 * @param <T>
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param keys 要获取的数据的key列表
	 * @return 如果成功，返回的数据对象为一个Map<Key, Value>
	 */
	
	public List<Map<String, ?>> mget(int namespace, String pkey, List<String> keys){
		List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		//TODO
		return list;
	}
	
	/**
	 * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增，如果是新增，则有效时间为0，即不失效，如果是更新，则不检查版本，强制更新
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 数据的value
	 * @return
	 */
	
	public boolean put(int namespace, String pkey, String key, Object value){
		try{
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			String result = getJedisCluster().set(comb_key, KryoUtils.toString(value));
			return SET_RESULT_OK.equals(result);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 设置数据，redis不支持版本号，此处的version参数没有用到
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 数据的value
	 * @param version 数据的版本，如果和系统中数据的版本不一致，则更新失败（该参数无用）
	 * @return
	 */
	
	public boolean put(int namespace, String pkey, String key, Object value, int version){
		return put(namespace,pkey,key,value);
	}
	
	/**
	 * 设置数据，redis不支持版本号，此处的version参数没有用到
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 数据的value
	 * @param version 数据的版本，如果和系统中数据的版本不一致，则更新失败（该参数无用）
	 * @param expireTime 数据的有效时间，单位为秒
	 * @return
	 */
	
	public boolean put(int namespace, String pkey, String key, Object value, int version, int expireTime){
		try{
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			String result = getJedisCluster().setex(comb_key,expireTime, KryoUtils.toString(value));
			return SET_RESULT_OK.equals(result);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 异步设置数据
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 数据的value
	 */
	
	public void putAsync(int namespace, String pkey, String key, Object value){
		try {
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			getJedisCluster().set(comb_key, KryoUtils.toString(value));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 异步设置数据
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 数据的value
	 * @param version 数据的版本，如果和系统中数据的版本不一致，则更新失败（该参数无用）
	 */
	
	public void putAsync(int namespace, String pkey, String key, Object value, int version){
		putAsync(namespace,pkey,key,value);
	}
	
	/**
	 * 异步设置数据
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 数据的value
	 * @param version 数据的版本，如果和系统中数据的版本不一致，则更新失败（该参数无用）
	 * @param expireTime 数据的有效时间，单位为秒
	 */
	
	public void putAsync(int namespace, String pkey, String key, Object value, int version, int expireTime){
		try {
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			getJedisCluster().setex(comb_key,expireTime, KryoUtils.toString(value));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除key对应的数据
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @return
	 */
	
	public boolean delete(int namespace, String pkey, String key){
		try{
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			return getJedisCluster().del(comb_key)>0;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 异步删除key对应的数据，redis不支持异步delete，此处方法调用的是同步delete
	 * @param namespace namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 */
	
	public void deleteAsync(int namespace, String pkey, String key){
		try {
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			getJedisCluster().del(comb_key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量删除
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param keys 要删除的数据的key列表
	 * @return 被删除key的list
	 */
	@SuppressWarnings({ "rawtypes"})
	
	public List mdelete(int namespace, String pkey, List<String> keys){
		//TODO
		return new ArrayList();
	}
	
	/**
	 * 异步批量删除
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param keys 要删除的数据的key列表
	 */
	
	public void mdeleteAsync(int namespace, String pkey, List<String> keys){
		//TODO
	}
	
	/**
	 * 获取namespace的数据：key大于等于key_start，小于等于key_end范围中从offset个开始最多limit个KV的数据(仅ldb有效)
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key_start 起始key，如果为null表示从第一个开始获取
	 * @param key_end 结束key，如果为null表示获取到前缀key下最后一个
	 * @param offset 跳过前offset个key
	 * @param limit 返回的数据数量限制，最大值为Integer.MAX_VALUE
	 * @param reverse 是否要逆序查找，建议使用false，逆序遍历代价比顺序要高
	 * @return KV对的List
	 */
	
	public List<Map<String, ?>> getRange(int namespace, String pkey, String key_start, String key_end, int offset, int limit, boolean reverse){
		List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		//TODO
		return list;
	}
	
	/**
	 * 获取namespace的数据(只有value)(仅ldb有效)
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key_start 起始key，如果为null表示从第一个开始获取
	 * @param key_end 结束key，如果为null表示获取到前缀key下最后一个
	 * @param offset 跳过前offset个key
	 * @param limit 返回的数据数量限制，最大值为Integer.MAX_VALUE
	 * @param reverse 是否要逆序查找，建议使用false，逆序遍历代价比顺序要高
	 * @return Value的List
	 */
	
	public List<?> getRangeValue(int namespace, String pkey, String key_start, String key_end, int offset, int limit, boolean reverse){
		List<Object> list = new ArrayList<Object>();
		//TODO
		return list;
	}
	
	/**
	 * 获取namespace的Key(仅ldb有效)
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key_start 起始key，如果为null表示从第一个开始获取
	 * @param key_end 结束key，如果为null表示获取到前缀key下最后一个
	 * @param offset 跳过前offset个key
	 * @param limit 返回的数据数量限制，最大值为Integer.MAX_VALUE
	 * @param reverse 是否要逆序查找，建议使用false，逆序遍历代价比顺序要高
	 * @return Key的List
	 */
	
	public List<String> getRangeKey(int namespace, String pkey, String key_start, String key_end, int offset, int limit, boolean reverse){
		List<String> list = new ArrayList<String>();
		try {
			String prefix_key = this.genPrefixKey(namespace, pkey);
			Set<String> keyList = getCluesterNodesKeys(prefix_key);
			list.addAll(new ArrayList<String>(keyList));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 删除namespace的数据：key大于等于key_start， 小于等于key_end范围中从offset个开始最多limit个KV的数据(仅ldb有效)
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key_start 起始key，如果为null表示从第一个开始获取
	 * @param key_end 结束key，如果为null表示获取到前缀key下最后一个
	 * @param offset 跳过前offset个key
	 * @param limit 返回的数据数量限制，最大值为Integer.MAX_VALUE
	 * @param reverse 是否要逆序查找，建议使用false，逆序遍历代价比顺序要高
	 * @return 被删除key的list
	 */
	
	public List<String> deleteRange(int namespace, String pkey, String key_start, String key_end, int offset, int limit, boolean reverse){
		List<String> list = new ArrayList<String>();
		try {
			String prefix_key = this.genPrefixKey(namespace, pkey);
			Set<String> keyList = getCluesterNodesKeys(prefix_key);
			for(String key:keyList){
				getJedisCluster().del(key);
			}
			list.addAll(new ArrayList<String>(keyList));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 将key对应的数据加上value，如果key对应的数据不存在，则新增，并将值设置为initValue
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 要加的值
	 * @param initValue 不存在时的默认值
	 * @return 更新后的值
	 */
	
	public int incr(int namespace, String pkey, String key, int value, int initValue){
		try{
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			long result = getJedisCluster().incrBy(comb_key, value);
			return (int) result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * 将key对应的数据加上value，如果key对应的数据不存在，则新增，并将值设置为initValue
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 要加的值
	 * @param initValue 不存在时的默认值
	 * @param expireTime 数据的有效时间，单位为秒
	 * @return 更新后的值
	 */
	
	public int incr(int namespace, String pkey, String key, int value, int initValue, int expireTime){
		return incr(namespace,pkey,key,value,initValue);
	}
	
	/**
	 * 将key对应的数据减去value，如果key对应的数据不存在，则新增，并将值设置为initValue
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 要减去的值
	 * @param initValue 不存在时的默认值
	 * @return 更新后的值
	 */
	
	public int decr(int namespace, String pkey, String key, int value, int initValue){
		try{
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			long result = getJedisCluster().decrBy(comb_key, value);
			return (int)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * 将key对应的数据减去value，如果key对应的数据不存在，则新增，并将值设置为initValue
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @param value 要减去的值
	 * @param initValue 不存在时的默认值
	 * @param expireTime 数据的有效时间，单位为秒
	 * @return 更新后的值
	 */
	
	public int decr(int namespace, String pkey, String key, int value, int initValue, int expireTime){
		return decr(namespace,pkey,key,value,initValue);
	}
	
	/**
	 * 获取count值
	 * @param namespace 数据所在的namespace
	 * @param pkey 前缀key
	 * @param key 数据的key
	 * @return
	 */
	
	public int getCount(int namespace, String pkey, String key){
		try{
			String comb_key = this.genPrefixKey(namespace, pkey)+key;
			String result = getJedisCluster().get(comb_key);
			if(result!=null){
				return Integer.parseInt(result);
			}else{
				return -1;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 循环集群节点，根据前缀模糊获取keys
	 * @param prefix_key
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Set<String> getCluesterNodesKeys(String prefix_key){
		Set<String> keyList = new HashSet<String>();
		Map<String, JedisPool> nodes = getJedisCluster().getClusterNodes();//获取集群所有节点
		Iterator<Map.Entry<String, JedisPool>> iterNodes = nodes.entrySet().iterator();
		while (iterNodes.hasNext()) {//循环所有节点，根据key的前缀模糊获取所有key值
			Map.Entry<String, JedisPool> entry = iterNodes.next();	
			try {
				JedisPool pool = entry.getValue();
				Jedis jedis = pool.getResource();
				try {
					Set<String> keys = jedis.keys(prefix_key + "*");
					if(keys.size()>0)
						keyList.addAll(keys);//不同节点可能存在相同的key，通过set去重
				} catch (Exception e) {
				}finally{
					pool.returnResource(jedis);
				}
			} catch (Exception e) {
			}
		}
		return keyList;
	}
	
}

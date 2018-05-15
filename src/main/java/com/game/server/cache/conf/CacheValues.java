package com.game.server.cache.conf;

/**
 * @author Reason.Yea 
 * @version 创建时间：Jan 8, 2014
 */
public class CacheValues {
	
	public static final String CORE_CACHE_TYPE = Config.get("core.cache.type") ;
	public static final int CORE_CACHE_DEFAULT_NAMESPACE=0;//
	public static final int  CORE_CACHE_NAMESPAC_LIST=1;//列表数据缓存
	public static final int  CORE_CACHE_NAMESPAC_SQL=2;//sql语句缓存
	public static final int CORE_CACHE_XIANLAIWAN=3;
	public static final int CORE_CACHE_DASHENG=4;
	
	public static final int CORE_CACHE_SESSION_TIMEOUT=Integer.parseInt(Config.get("core.cache.default.session.timeout"));
	public static final int CORE_CACHE_SESSION_NAMESPACE=Integer.parseInt(Config.get("core.cache.default.session.namespace"));
	public static final String CORE_CACHE_SESSION_TYPE=Config.get("core.cache.busi.default.type");
	
	public static final String CACHE_TYPE_MEMCACHED="memcached";
	public static final String CACHE_TYPE_DY_MEMCACHED="dymemcached";
	public static final String CACHE_TYPE_EHCACHE="ehcache";
	public static final String CACHE_TYPE_TAIR="tair";
	public static final String CACHE_TYPE_REDISS="redis_s"; //redis单机模式
	public static final String CACHE_TYPE_REDISJ="redis";//redis集群模式，缺省
	public static final String CACHE_TYPE_COHERENCE ="coherence";
	public static final String CACHE_TYPE_ZREDIS="zredis";
	
	
	public static final String MEMCACHED_SERVERS = Config.get("memcached.servers") ;
	public static final String MEMCACHED_CONTEXT = Config.get("memcached.context") ;
	
	
	public static final int MEMCACHED_POOLSIZE = Integer.parseInt(Config.get("memcached.poolsize")) ;
	public static final long MEMCACHED_TIMEOUT = Long.parseLong(Config.get("memcached.timeout")) ;
	public static final long MEMCACHED_MULTIGETS_TIMEOUT = Long.parseLong(Config.get("memcached.multiGetTimeout")) ;
	public static final String MEMCACHED_XM = "xm";
	public static final String MEMCACHED_SPY = "spy";
	
	
	public static final String TAIR_MANAGE_SERVERS = Config.get("tair.manage.servers") ;
	public static final String TAIR_GROUPNAME = Config.get("tair.groupname") ;
	public static final String TAIR_CHARSET = Config.get("tair.charset") ;
	public static final int TAIR_MAXWAITTHREAD = Integer.parseInt(Config.get("tair.MaxWaitThread")) ;

	
	public static final String REDIS_SERVER_LIST=Config.get("redis.server.list") ;
	public static final int REDIS_MAX_TOTAL=Integer.parseInt(Config.get("redis.maxTotal")) ;
	public static final int REDIS_MAX_IDEL=Integer.parseInt(Config.get("redis.maxIdel")) ;
	public static final long REDIS_MAX_WAIT_MILLIS=Long.parseLong(Config.get("redis.maxWaitMillis"));
	public static final boolean REDIS_TEST_ON_BORROW=Boolean.parseBoolean(Config.get("redis.testOnBorrow"));
	public static final boolean REDIS_TEST_ON_RETURN=Boolean.parseBoolean(Config.get("redis.testOnReturn")) ;
	
	public static final String CORE_CACHE_BUSI_DEFAULT_TYPE = Config.get("core.cache.busi.default.type") ;
	public static final int CORE_CACHE_BUSI_DEFAULT_TIME = Config.get("core.cache.busi.default.time")==null?24*60:Integer.parseInt(Config.get("core.cache.busi.default.time"));
	public static final int CACHE_DISASTER_TOLERANT_NAMESPACE = 2;
}




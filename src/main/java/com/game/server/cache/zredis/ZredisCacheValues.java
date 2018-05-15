package com.game.server.cache.zredis;

import com.game.server.cache.conf.Config;

/**
 * @Description
 * @author  zhangJun
 * @date    2016年1月20日
 * @version 1.0
 */
public class ZredisCacheValues {
	
	public static String SERVER_LIST = Config.get("zredis.serverList");
	public static String CLUSTER_NAME = Config.get("zredis.clusterName");
	public static String CLIENT_NAME = Config.get("zredis.clientName");
	public static int MAX_TOTAL = Integer.parseInt(Config.get("zredis.maxTotal"));   	// max total connections in a pool
	public static int MAX_IDLE = Integer.parseInt(Config.get("zredis.maxIdle"));    	// max idle connections in a pool
	public static int MIN_IDLE = Integer.parseInt(Config.get("zredis.minIdle"));     	// min idle connections in a pool
	public static int CONN_TIMEOUT = Integer.parseInt(Config.get("zredis.connTimeout")); // connect timeout
	public static int SO_TIMEOUT = Integer.parseInt(Config.get("zredis.soTimeout"));   // read timeout
	public static int MAX_REDIRECTIONS = Integer.parseInt(Config.get("zredis.maxRedirections"));// max redirections 
	public static int MAX_SCANCOUNT = Integer.parseInt(Config.get("zredis.maxScanCount"));// scan count
	
	public static long MAX_WAITMILLIS = Long.parseLong(Config.get("zredis.maxWaitMillis")) ;
	public static boolean TEST_ON_BORROW = Boolean.parseBoolean(Config.get("zredis.testOnBorrow")) ;
	public static boolean TEST_ON_RETURN = Boolean.parseBoolean(Config.get("zredis.testOnReturn")) ;

}




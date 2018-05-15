package demo.redisCacheDemo;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.game.server.cache.common.CacheUtils;
import com.game.server.cache.common.INetCache;
import com.game.server.cache.common.SpringContextUtil;
import com.game.server.cache.redis.DefaultRedisClient;

/**
 * Unit test for simple App.
 */
public class AppTest 
   
{    @org.junit.Test
	public void test() {
		ApplicationContext atc=new ClassPathXmlApplicationContext("spring-config.xml");
		assertNotNull(atc);
	
		DefaultRedisClient defaultRedisClient = SpringContextUtil.getBean(DefaultRedisClient.class);
		
		User u=new User();
		u.setAddress("fdgfdg");
		u.setName("cggjhhj");
		/*com.leyou.common.protobuf.club.ClubGame_pb.ClubGame.Builder clubGame_p=com.leyou.common.protobuf.club.ClubGame_pb.ClubGame.newBuilder();
		clubGame_p.setGameConf("cvc");
		clubGame_p.setGameType(5);*/
		
		
		User u2=new User();
		u2.setAddress("we");
		u2.setName("tr");
		/*CacheUtils.lpush("dfd", u);
		CacheUtils.lpush("dfd", u2);*/
		List<Object> objs=CacheUtils.lrange("dfd");
		for (Object object : objs) {
			System.out.println(object);
		}
		CacheUtils.lrem("dfd", u2);
		System.out.println("==================================================");
		List<Object> objs1=CacheUtils.lrange("dfd");
		for (Object object : objs1) {
			System.out.println(object);
		}
		/*CacheUtils.addCache("dfd", u2);
		defaultRedisClient.r_lpush("dfd", u2, 10000);
		defaultRedisClient.r_lpush("dfd", u, 10000);
		User obj= CacheUtils.getCache("dfd");
	System.out.println(obj.getAddress());
	System.out.println(obj.getName());*/
	}
  
}

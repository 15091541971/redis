package demo.redisCacheDemo;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.game.server.cache.common.CacheUtils;
import com.game.server.cache.common.INetCache;
import com.game.server.cache.common.SpringContextUtil;

/**
 * Unit test for simple App.
 */
public class AppTest 
   
{    @org.junit.Test
	public void test() {
		ApplicationContext atc=new ClassPathXmlApplicationContext("spring-config.xml");
		assertNotNull(atc);
	
		INetCache cache = SpringContextUtil.getBean("iNetCache");
		assertNotNull(cache);
		User u=new User();
		u.setAddress("fdgfdg");
		u.setName("cggjhhj");
		com.leyou.common.protobuf.club.ClubGame_pb.ClubGame.Builder clubGame_p=com.leyou.common.protobuf.club.ClubGame_pb.ClubGame.newBuilder();
		clubGame_p.setGameConf("cvc");
		clubGame_p.setGameType(5);
		
		CacheUtils.addCache("dfd", clubGame_p.build());
		com.leyou.common.protobuf.club.ClubGame_pb.ClubGame obj= CacheUtils.getCache("dfd");
	System.out.println(obj.getGameConf());
	System.out.println(obj.getGameType());
	}
  
}

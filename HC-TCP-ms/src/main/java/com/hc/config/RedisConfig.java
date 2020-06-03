package com.hc.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * redis配置
 * @author
 *
 */
@Configuration
public class RedisConfig {




    @Autowired
	private RedisConnectionFactory factory;





	/**
	 * 本项目缓存Redis对象
	 * @return
	 */
	@Bean
	@Primary
	public RedisTemplateUtil redisTemplateDefault(){
		RedisTemplateUtil bean = new RedisTemplateUtil(factory);
		return bean;
	}

	public static void main(String[] args) {

		String as ="-327.55";
		Double aDouble = Double.valueOf(as);
		System.out.print(aDouble);

      boolean b =aDouble<-200;
      System.out.print(b);


	}

}

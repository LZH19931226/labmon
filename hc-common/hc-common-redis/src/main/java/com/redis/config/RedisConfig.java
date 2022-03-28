package com.redis.config;


import com.redis.util.RedisTemplateUtil;
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
		return new RedisTemplateUtil(factory);
	}



}

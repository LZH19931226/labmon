package com.hc.web.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * redis配置
 *
 * @author
 */
@Configuration
public class RedisConfig {


	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

//    @Value("${spring.redis.pool.max-active}")
//    private int active;
//    @Value("${spring.redis.pool.maxIdle}")
//    private int maxIdle;
//    @Value("${spring.redis.pool.minIdle}")
//    private int minIdle;
//    @Value("${spring.redis.pool.maxWait}")
//    private int maxWait;
//    @Value("${spring.redis.cluster.nodes}")
//    private String nodes;
//
//    /**
//     * redisCluster配置
//     *
//     * @return
//     */
//
//    @Bean
//    @Primary
//    public RedisClusterConfiguration redisProperties() {
//
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
//        List<RedisNode> list = new ArrayList<>();
//        String[] split = nodes.split(",");
//        for (String s : split) {
//
//            String[] split1 = s.split(":");
//            String ip = split1[0];
//            String port = split1[1];
//            RedisNode redisNode = new RedisNode(ip, Integer.parseInt(port));
//            System.out.println(redisNode);
//            redisClusterConfiguration.addClusterNode(redisNode);
//        }
//
//        return redisClusterConfiguration;
//    }
//
//    /**
//     * jedis 连接池
//     *
//     * @return
//     */
//    @Bean
//    public JedisPoolConfig jedisPoolConfig() {
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxTotal(active);
//        config.setMaxIdle(maxIdle);
//        config.setMinIdle(minIdle);
//        config.setMaxWaitMillis(maxWait);
//        return config;
//    }
//
//    @Bean
//    @Primary
//    public JedisConnectionFactory jedisConnectionFactory() {
//
//        JedisConnectionFactory factory = new JedisConnectionFactory(redisProperties(), jedisPoolConfig());
//        return factory;
//
//    }
//
//    @Bean
//    @Primary
//    public CacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        cacheManager.setDefaultExpiration(1800);
//        return cacheManager;
//    }

    /**
     * 本项目缓存Redis对象
     * @return
     */
    @Bean
    @Primary
    public RedisTemplateUtil redisTemplateDefault(){
        RedisTemplateUtil bean = new RedisTemplateUtil(redisConnectionFactory);
        return bean;
    }


//	/**

//	}
//
//    @Bean
//    @Primary
//    public RedisTemplate<Object, Object> redisTemplate() {
//        JedisConnectionFactory jedisConnectionFactory = jedisConnectionFactory();
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        serializer.setObjectMapper(om);
//        redisTemplate.setValueSerializer(serializer);
//        redisTemplate.afterPropertiesSet();
//        //redisTemplate.setEnableTransactionSupport(true);
//        return redisTemplate;
//    }


}

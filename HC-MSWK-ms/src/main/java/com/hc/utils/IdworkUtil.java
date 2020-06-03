package com.hc.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 16956 on 2018-08-10.
 */
@Configuration
public class IdworkUtil {

    @Bean
    public  SnowflakeIdWorker getid(){

        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0,1);
        return  idWorker;
    }
}

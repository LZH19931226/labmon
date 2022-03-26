package com.hc.config;

//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2017-07-29.
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "primary")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary") // application.yml中对应属性的前缀
    public DataSource main() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondary")
    @ConfigurationProperties(prefix = "spring.datasource.secondary") // application.yml中对应属性的前缀
    public DataSource from() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "thirdly")
    @ConfigurationProperties(prefix = "spring.datasource.thirdly") // application.yml中对应属性的前缀
    public DataSource mainJpa() {
        return DataSourceBuilder.create().build();
    }



}

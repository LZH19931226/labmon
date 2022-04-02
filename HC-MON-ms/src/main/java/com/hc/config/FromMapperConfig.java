package com.hc.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Created by tangqinchen on 2017/8/16.
 */

@Configuration
@MapperScan(basePackages = {"com.hc.mapper.laboratoryFrom"}, sqlSessionFactoryRef = "NtlockSqlSessionFactory")
public class FromMapperConfig {
    @Autowired
    @Qualifier("secondary")
    private DataSource ds_ntlock;


    @Bean
    public SqlSessionFactory NtlockSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(ds_ntlock); // 使用ds_patient数据源, 连接ds_patient库
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/laboratoryFrom/*.xml"));
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate NtlockSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(NtlockSqlSessionFactory()); // 使用上面配置的Factory
        return template;
    }

}

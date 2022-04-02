package com.hc.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2017-07-29.
 */


@Configuration
@MapperScan(basePackages = {"com.hc.mapper.laboratoryMain"}, sqlSessionFactoryRef = "NitrogenSqlSessionFactory")

public class MainMapperConfig {

    @Autowired
    @Qualifier("primary")
    private DataSource ds_nitrogen;

    @Bean
    public SqlSessionFactory NitrogenSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(ds_nitrogen); // 使用ds_nitrogen数据源, 连接ds_nitrogen库
        // 多数据源配置必须指定mapper.xml路径
//        factoryBean.setMapperLocations(
//                new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/laboratoryMain/*.xml"));

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate NitrogenSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(NitrogenSqlSessionFactory()); // 使用上面配置的Factory
        return template;
    }

}

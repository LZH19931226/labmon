package data.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author LiuZhiHao
 * @date 2019/10/23 11:21
 * 描述:
 **/
@EnableTransactionManagement
@Configuration
@MapperScan("com.hc.mapper*")
public class MybatisPlusDataConfig {

    public static ThreadLocal<String> TABLE_NAME = new ThreadLocal<>();

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;


    @Bean
    @Primary
    public DataSource dataSourcee() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(dataSourceUrl + "?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
        config.setUsername(user);
        config.setPassword(password);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(120000);
        config.setMaxLifetime(120000);
        config.setMinimumIdle(10);
        config.setMaximumPoolSize(35);
        return new HikariDataSource(config);
    }

    @Bean
    public GlobalConfig globalConfig() {
        // 全局配置文件
        GlobalConfig globalConfig = new GlobalConfig();
        DbConfig dbConfig = new DbConfig();
        // 默认
        dbConfig.setIdType(IdType.UUID);
        globalConfig.setDbConfig(dbConfig);
        return globalConfig;
    }

    @Bean
    public MybatisConfiguration mybatisConfiguration() {
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        //全局地开启或关闭配置文件中的所有映射器已经配置的任何缓存。
        mybatisConfiguration.setCacheEnabled(true);
        mybatisConfiguration.setMapUnderscoreToCamelCase(false);
        mybatisConfiguration.setLogImpl(StdOutImpl.class);
        return mybatisConfiguration;
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

//        /*动态表名*/
//        List<ISqlParser> sqlParserList = new ArrayList<>();
//        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
//        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
//        //User是数据库表名
//        tableNameHandlerMap.put("monitorequipmentlastdata", (metaObject, sql, tableName) -> {
//            return TABLE_NAME.get();//返回null不会替换 注意 多租户过滤会将它一块过滤不会替换@SqlParser(filter=true) 可不会替换
//        });
//        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);
//        sqlParserList.add(dynamicTableNameParser);
//        paginationInterceptor.setSqlParserList(sqlParserList);
        return  paginationInterceptor;
    }

    @Bean
    @Primary
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() throws IOException {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSourcee());
        mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfig());
        mybatisSqlSessionFactoryBean.setConfiguration(mybatisConfiguration());
        Interceptor[] plugins = new Interceptor[1];
        plugins[0] = paginationInterceptor();
        mybatisSqlSessionFactoryBean.setPlugins(plugins);
        // 设置mapper.xml文件的路径
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:mapper/*Dao.xml");
        mybatisSqlSessionFactoryBean.setMapperLocations(resources);
        //设置实体类扫码包路径
        mybatisSqlSessionFactoryBean.setTypeAliasesPackage("com.hc.po");
        return mybatisSqlSessionFactoryBean;
    }
}

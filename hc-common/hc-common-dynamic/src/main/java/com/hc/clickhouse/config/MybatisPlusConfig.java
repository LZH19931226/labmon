package com.hc.clickhouse.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {


    @Bean
    public PaginationInterceptor paginationInterceptor() {

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
        return new PaginationInterceptor();
    }

}

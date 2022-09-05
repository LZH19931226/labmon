package com.hc.clickhouse.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

public class CustomSqlInjector extends DefaultSqlInjector {
    /**
     * @param mapperClass 当前mapper
     * @return
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        //mybatis提供的批量插入方法
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }
}

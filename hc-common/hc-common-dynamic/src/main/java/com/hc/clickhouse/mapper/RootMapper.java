package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface RootMapper<T>  extends BaseMapper<T> {

    int insertBatchSomeColumn(List<T> entityList);
}

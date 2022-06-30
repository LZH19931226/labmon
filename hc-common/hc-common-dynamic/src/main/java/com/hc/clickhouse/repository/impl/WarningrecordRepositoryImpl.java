package com.hc.clickhouse.repository.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.clickhouse.mapper.WarningrecordMapper;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import org.springframework.stereotype.Repository;

@DS("slave")
@Repository
public class WarningrecordRepositoryImpl extends ServiceImpl<WarningrecordMapper, Warningrecord> implements WarningrecordRepository {

}

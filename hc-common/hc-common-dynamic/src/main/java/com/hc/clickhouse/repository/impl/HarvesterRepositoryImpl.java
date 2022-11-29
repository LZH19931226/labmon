package com.hc.clickhouse.repository.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.clickhouse.mapper.HarvesterMapper;
import com.hc.clickhouse.po.Harvester;
import com.hc.clickhouse.repository.HarvesterRepository;
import org.springframework.stereotype.Service;

@Service
@DS("slave")
public class HarvesterRepositoryImpl extends ServiceImpl<HarvesterMapper, Harvester> implements HarvesterRepository {

}

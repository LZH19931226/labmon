package com.hc.clickhouse.repository.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.clickhouse.mapper.MonitorequipmentlastdataMapper;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import org.springframework.stereotype.Service;

@Service
@DS("slave")
public class MonitorequipmentlastdataRepositoryImpl extends ServiceImpl<MonitorequipmentlastdataMapper, Monitorequipmentlastdata> implements MonitorequipmentlastdataRepository {









}

package com.hc.clickhouse.repository.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.clickhouse.mapper.MonitorequipmentlastdataMapper;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("slave")
public class MonitorequipmentlastdataRepositoryImpl extends ServiceImpl<MonitorequipmentlastdataMapper, Monitorequipmentlastdata> implements MonitorequipmentlastdataRepository {

  @Autowired
  private  MonitorequipmentlastdataMapper monitorequipmentlastdataMapper;

    @Override
    public List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(String date, String equipmentNo) {
      return monitorequipmentlastdataMapper.getMonitorEquipmentLastDataInfo(date,equipmentNo);
    }

  @Override
  public List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(String startTime, String endTime, String equipmentNo) {
    return monitorequipmentlastdataMapper.getMonitorEquipmentLastData(startTime,endTime,equipmentNo);
  }
}

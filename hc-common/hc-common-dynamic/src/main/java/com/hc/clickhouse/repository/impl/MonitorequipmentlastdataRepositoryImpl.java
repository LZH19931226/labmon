package com.hc.clickhouse.repository.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.clickhouse.mapper.MonitorequipmentlastdataMapper;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
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

  @Override
  public List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByPeriod(String hospitalCode, String startTime, String endTime, String month) {
    return monitorequipmentlastdataMapper.getMonitorEquipmentLastDataInfoByPeriod(hospitalCode,startTime,endTime,month);
  }

  @Override
  public List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByDate(String hospitalCode, String startTime, String endTime, String date) {
    return monitorequipmentlastdataMapper.getMonitorEquipmentLastDataInfoByDate(hospitalCode,startTime,endTime,date);
  }

  @Override
  public void batchInsert(List<Monitorequipmentlastdata> convert) {
    monitorequipmentlastdataMapper.insertBatchSomeColumn(convert);
  }

  @Override
  public List<Monitorequipmentlastdata> getLastDataByEnoAndMonth(String equipmentNo, String startTime, String endTime, String date) {
    return monitorequipmentlastdataMapper.getLastDataByEnoAndMonth(equipmentNo,startTime,endTime,date);
  }

  @Override
  public List<MonitorequipmentlastdataDto> getWarningCurveData(String equipmentNo, String startTime, String endTime, String instrumentConfigName) {
    return monitorequipmentlastdataMapper.getWarningCurveData(equipmentNo,startTime,endTime,instrumentConfigName);
  }
}

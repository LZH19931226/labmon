package com.hc.clickhouse.repository.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.clickhouse.mapper.MonitorequipmentlastdataMapper;
import com.hc.clickhouse.param.CurveParam;
import com.hc.clickhouse.param.EquipmentDataParam;
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
  public void batchInsert(List<Monitorequipmentlastdata> convert) {
    monitorequipmentlastdataMapper.insertBatchSomeColumn(convert);
  }

  @Override
  public List<MonitorequipmentlastdataDto> getWarningCurveData(String equipmentNo, String startTime, String endTime, String instrumentConfigName,String ym) {
    return monitorequipmentlastdataMapper.getWarningCurveData(equipmentNo,startTime,endTime,instrumentConfigName,ym);
  }

  @Override
  public List<Monitorequipmentlastdata> getEquipmentData(Page page,EquipmentDataParam dataParam) {
    return monitorequipmentlastdataMapper.getEquipmentData(page,dataParam);
  }

  @Override
  public List<Monitorequipmentlastdata> getEquipmentPacketData(Page page, EquipmentDataParam dataParam) {
    return monitorequipmentlastdataMapper.getEquipmentPacketData(page,dataParam);
  }

  @Override
  public List<Monitorequipmentlastdata> getPacketLossColumnar(EquipmentDataParam dataParam) {
    return monitorequipmentlastdataMapper.getPacketLossColumnar(dataParam);
  }

  @Override
  public List<Monitorequipmentlastdata> getPacketLoss(EquipmentDataParam dataParam) {
    return monitorequipmentlastdataMapper.getEquipmentPacketData(null,dataParam);
  }

  @Override
  public List<Monitorequipmentlastdata> getLastDataByTime(EquipmentDataParam dataParam) {
    return monitorequipmentlastdataMapper.getLastDataByTime(dataParam);
  }

  @Override
  public List<Monitorequipmentlastdata> getMonitorEquuipmentLastList(CurveParam curveParam) {
    return monitorequipmentlastdataMapper.getMonitorEquuipmentLastList(curveParam);
  }

  @Override
  public List<Monitorequipmentlastdata> getMT310DcLastDataByTime(EquipmentDataParam dataParam) {
    return monitorequipmentlastdataMapper.getMT310DcLastDataByTime(dataParam);
  }
}

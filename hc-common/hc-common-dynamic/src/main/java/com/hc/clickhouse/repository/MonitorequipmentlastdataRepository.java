package com.hc.clickhouse.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;

import java.util.List;

public interface MonitorequipmentlastdataRepository extends IService<Monitorequipmentlastdata> {

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(String date, String equipmentNo);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(String startTime, String endTime, String equipmentNo);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByPeriod(String hospitalCode, String startTime, String endTime,String month);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByDate(String hospitalCode, String startTime, String endTime, String date);

    void batchInsert(List<Monitorequipmentlastdata> convert);

    List<Monitorequipmentlastdata> getLastDataByEnoAndMonth(String equipmentNo, String startTime, String endTime, String date);

    List<MonitorequipmentlastdataDto> getWarningCurveData(String equipmentNo, String startTime, String endTime, String instrumentConfigName);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo1(String startTime, String endTime, String equipmentNo, String ym);
}

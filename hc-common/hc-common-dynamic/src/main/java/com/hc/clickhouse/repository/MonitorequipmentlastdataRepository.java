package com.hc.clickhouse.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.clickhouse.po.Monitorequipmentlastdata;

import java.util.List;

public interface MonitorequipmentlastdataRepository extends IService<Monitorequipmentlastdata> {

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(String date, String equipmentNo);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(String startTime,String endTime, String equipmentNo);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByPeriod(String hospitalCode, String startTime, String endTime,String month);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByDate(String hospitalCode, String startTime, String endTime, String date);
}

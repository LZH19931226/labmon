package com.hc.clickhouse.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.clickhouse.po.Monitorequipmentlastdata;

import java.util.List;

public interface MonitorequipmentlastdataRepository extends IService<Monitorequipmentlastdata> {

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(String date, String equipmentNo);
}

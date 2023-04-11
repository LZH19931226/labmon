package com.hc.clickhouse.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.clickhouse.param.CurveParam;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;

import java.util.List;

public interface MonitorequipmentlastdataRepository extends IService<Monitorequipmentlastdata> {


    void batchInsert(List<Monitorequipmentlastdata> convert);


    List<MonitorequipmentlastdataDto> getWarningCurveData(String equipmentNo, String startTime, String endTime, String instrumentConfigName,String ym);


    List<Monitorequipmentlastdata> getEquipmentData(Page page,EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getEquipmentPacketData(Page page, EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getPacketLossColumnar(EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getPacketLoss(EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getLastDataByTime(EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getMonitorEquuipmentLastList(CurveParam curveParam);

    List<Monitorequipmentlastdata> getMT310DcLastDataByTime(EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getMultiprobeTypePointInTime(EquipmentDataParam dataParam);
}

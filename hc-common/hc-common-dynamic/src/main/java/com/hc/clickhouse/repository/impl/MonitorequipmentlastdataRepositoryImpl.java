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
import com.hc.my.common.core.util.BeanConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@DS("slave")
public class MonitorequipmentlastdataRepositoryImpl extends ServiceImpl<MonitorequipmentlastdataMapper, Monitorequipmentlastdata> implements MonitorequipmentlastdataRepository {

    @Autowired
    private MonitorequipmentlastdataMapper monitorequipmentlastdataMapper;

    @Override
    public void batchInsert(List<Monitorequipmentlastdata> convert) {
        monitorequipmentlastdataMapper.insertBatchSomeColumn(convert);
    }

    @Override
    public List<Monitorequipmentlastdata> getWarningCurveData(String equipmentNo, String startTime,
                                                                 String endTime, String instrumentConfigName, String ym,String clientTimeZone) {
        List<Monitorequipmentlastdata> warningCurveData = monitorequipmentlastdataMapper.getWarningCurveData(equipmentNo, startTime, endTime, instrumentConfigName, ym);
        return buildLastDataZone(clientTimeZone, warningCurveData);
    }

    @Override
    public List<Monitorequipmentlastdata> getEquipmentData(Page page, EquipmentDataParam dataParam) {
        List<Monitorequipmentlastdata> monitorEquuipmentLastList = monitorequipmentlastdataMapper.getEquipmentData(page, dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), monitorEquuipmentLastList);
    }

    @Override
    public List<Monitorequipmentlastdata> getEquipmentPacketData(Page page, EquipmentDataParam dataParam) {
        List<Monitorequipmentlastdata> equipmentPacketData = monitorequipmentlastdataMapper.getEquipmentPacketData(page, dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), equipmentPacketData);
    }

    @Override
    public List<Monitorequipmentlastdata> getPacketLossColumnar(EquipmentDataParam dataParam) {
        List<Monitorequipmentlastdata> packetLossColumnar = monitorequipmentlastdataMapper.getPacketLossColumnar(dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), packetLossColumnar);
    }

    @Override
    public List<Monitorequipmentlastdata> getPacketLoss(EquipmentDataParam dataParam) {
        List<Monitorequipmentlastdata> equipmentPacketData = monitorequipmentlastdataMapper.getEquipmentPacketData(null, dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), equipmentPacketData);
    }

    @Override
    public List<Monitorequipmentlastdata> getLastDataByTime(EquipmentDataParam dataParam) {
        List<Monitorequipmentlastdata> lastDataByTime = monitorequipmentlastdataMapper.getLastDataByTime(dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), lastDataByTime);
    }

    @Override
    public List<Monitorequipmentlastdata> getMonitorEquuipmentLastList(CurveParam curveParam) {
        List<Monitorequipmentlastdata> monitorEquuipmentLastList = monitorequipmentlastdataMapper.getMonitorEquuipmentLastList(curveParam);
        return buildLastDataZone(curveParam.getClientTimeZone(), monitorEquuipmentLastList);
    }

    @Override
    public List<Monitorequipmentlastdata> getMT310DcLastDataByTime(EquipmentDataParam dataParam) {
        List<Monitorequipmentlastdata> mt310DcLastDataByTime = monitorequipmentlastdataMapper.getMT310DcLastDataByTime(dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), mt310DcLastDataByTime);
    }

    @Override
    public List<Monitorequipmentlastdata> getMultiprobeTypePointInTime(EquipmentDataParam dataParam) {
        List<Monitorequipmentlastdata> multiprobeTypePointInTime = monitorequipmentlastdataMapper.getMultiprobeTypePointInTime(dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), multiprobeTypePointInTime);
    }


    public List<Monitorequipmentlastdata> buildLastDataZone(String clientTimeZone, List<Monitorequipmentlastdata> monitorEquuipmentLastList) {
        if (CollectionUtils.isEmpty(monitorEquuipmentLastList)) {
            return null;
        }
        ZoneId clickHouseZone = ZoneId.of("America/Phoenix");
        ZoneId clientZone = ZoneId.of(clientTimeZone);
        monitorEquuipmentLastList.forEach(lastData -> {
            LocalDateTime inputdatetime = lastData.getInputdatetime();
            ZonedDateTime zonedDateTime = inputdatetime.atZone(clickHouseZone);
            ZonedDateTime clientZonedDateTime = zonedDateTime.withZoneSameInstant(clientZone);
            // 这里可以考虑将 ZonedDateTime 转换回 LocalDateTime 或保留 ZonedDateTime 进行返回
            lastData.setInputdatetime(clientZonedDateTime.toLocalDateTime());
        });
        return monitorEquuipmentLastList;
    }
}

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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@DS("slave")
public class MonitorequipmentlastdataRepositoryImpl extends ServiceImpl<MonitorequipmentlastdataMapper, Monitorequipmentlastdata> implements MonitorequipmentlastdataRepository {

    // 数据库时区固定为America/Phoenix
    private static final ZoneId DB_ZONE = ZoneId.of("America/Phoenix");

    // 客户端时间格式：yyyy-MM-dd HH:mm:ss
    private static final DateTimeFormatter CLIENT_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 时区缓存
    private static final ConcurrentHashMap<String, ZoneId> ZONE_CACHE = new ConcurrentHashMap<>();


    @Autowired
    private MonitorequipmentlastdataMapper monitorequipmentlastdataMapper;

    @Override
    public void batchInsert(List<Monitorequipmentlastdata> convert) {
        monitorequipmentlastdataMapper.insertBatchSomeColumn(convert);
    }

    @Override
    public List<Monitorequipmentlastdata> getWarningCurveData(String equipmentNo, String startTime,
                                                                 String endTime, String instrumentConfigName, String ym,String clientTimeZone) {
        LocalDateTime localStartTime = parseClientTime(startTime, clientTimeZone);
        LocalDateTime localEndTime = parseClientTime(endTime, clientTimeZone);
        List<Monitorequipmentlastdata> warningCurveData = monitorequipmentlastdataMapper.getWarningCurveData(equipmentNo, localStartTime, localEndTime, instrumentConfigName, ym);
        return buildLastDataZone(clientTimeZone, warningCurveData);
    }

    @Override
    public List<Monitorequipmentlastdata> getEquipmentData(Page page, EquipmentDataParam dataParam) {
        buildClientDateTime(dataParam);
        List<Monitorequipmentlastdata> monitorEquuipmentLastList = monitorequipmentlastdataMapper.getEquipmentData(page, dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), monitorEquuipmentLastList);
    }

    @Override
    public List<Monitorequipmentlastdata> getEquipmentPacketData(Page page, EquipmentDataParam dataParam) {
        buildClientDateTime(dataParam);
        List<Monitorequipmentlastdata> equipmentPacketData = monitorequipmentlastdataMapper.getEquipmentPacketData(page, dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), equipmentPacketData);
    }

    @Override
    public List<Monitorequipmentlastdata> getPacketLossColumnar(EquipmentDataParam dataParam) {
        buildClientDateTime(dataParam);
        List<Monitorequipmentlastdata> packetLossColumnar = monitorequipmentlastdataMapper.getPacketLossColumnar(dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), packetLossColumnar);
    }

    @Override
    public List<Monitorequipmentlastdata> getPacketLoss(EquipmentDataParam dataParam) {
        buildClientDateTime(dataParam);
        List<Monitorequipmentlastdata> equipmentPacketData = monitorequipmentlastdataMapper.getEquipmentPacketData(null, dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), equipmentPacketData);
    }

    @Override
    public List<Monitorequipmentlastdata> getLastDataByTime(EquipmentDataParam dataParam) {
        buildClientDateTime(dataParam);
        List<Monitorequipmentlastdata> lastDataByTime = monitorequipmentlastdataMapper.getLastDataByTime(dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), lastDataByTime);
    }

    @Override
    public List<Monitorequipmentlastdata> getMonitorEquuipmentLastList(CurveParam dataParam) {
        String clientTimeZone =  dataParam.getClientTimeZone();
        LocalDateTime localStartTime = parseClientTime(dataParam.getStartTime(), clientTimeZone);
        LocalDateTime localEndTime = parseClientTime(dataParam.getEndTime(), clientTimeZone);
        dataParam.setLocalStartTime(localStartTime);
        dataParam.setLocalEndTime(localEndTime);
        List<Monitorequipmentlastdata> monitorEquuipmentLastList = monitorequipmentlastdataMapper.getMonitorEquuipmentLastList(dataParam);
        return buildLastDataZone(clientTimeZone, monitorEquuipmentLastList);
    }

    @Override
    public List<Monitorequipmentlastdata> getMT310DcLastDataByTime(EquipmentDataParam dataParam) {
        buildClientDateTime(dataParam);
        List<Monitorequipmentlastdata> mt310DcLastDataByTime = monitorequipmentlastdataMapper.getMT310DcLastDataByTime(dataParam);
        return buildLastDataZone(dataParam.getClientTimeZone(), mt310DcLastDataByTime);
    }

    @Override
    public List<Monitorequipmentlastdata> getMultiprobeTypePointInTime(EquipmentDataParam dataParam) {
        buildClientDateTime(dataParam);
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

    public void buildClientDateTime(EquipmentDataParam dataParam){
        String clientTimeZone =  dataParam.getClientTimeZone();
        LocalDateTime localStartTime = parseClientTime(dataParam.getStartTime(), clientTimeZone);
        LocalDateTime localEndTime = parseClientTime(dataParam.getEndTime(), clientTimeZone);
        dataParam.setLocalStartTime(localStartTime);
        dataParam.setLocalEndTime(localEndTime);
    }


    /**
     * 将客户端时间字符串转换为数据库时区的LocalDateTime
     */
    public static LocalDateTime parseClientTime(String clientTimeStr, String clientZoneId) {
        ZoneId clientZone = ZONE_CACHE.computeIfAbsent(clientZoneId, ZoneId::of);
        LocalDateTime clientLocalTime = LocalDateTime.parse(clientTimeStr, CLIENT_TIME_FORMATTER);
        ZonedDateTime clientZonedTime = clientLocalTime.atZone(clientZone);
        return clientZonedTime.withZoneSameInstant(DB_ZONE).toLocalDateTime();
    }
    /**
     * 校验时区有效性
     */
    public static boolean isValidTimeZone(String zoneId) {
        return ZoneId.getAvailableZoneIds().contains(zoneId);
    }

}

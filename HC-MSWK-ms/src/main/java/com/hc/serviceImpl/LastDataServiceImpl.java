package com.hc.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.hc.clickhouse.po.Harvester;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.HarvesterRepository;
import com.hc.device.SnDeviceRedisApi;
import com.hc.my.common.core.redis.dto.HarvesterDto;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.service.LastDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Created by 16956 on 2018-09-04.
 */
@Service
@Slf4j
public class LastDataServiceImpl implements LastDataService {


    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;
    @Autowired
    private HarvesterRepository harvesterRepository;

    private static final ZoneId CLICKHOUSE_ZONE = ZoneId.of("America/Phoenix");

    @Override
    public void saveLastData(Monitorequipmentlastdata monitorequipmentlastdata, String equipmentno, String hospitalcode,String cmdId,String sn) {
        monitorequipmentlastdata.setSn(sn);
        monitorequipmentlastdata.setCmdid(cmdId);
        monitorequipmentlastdata.setEquipmentno(equipmentno);
        // 如果传入的 LocalDateTime 没有指定时区，需要先确定其所在时区
        // 假设传入的是系统默认时区的 LocalDateTime
        // 获取服务器的当前时间
        LocalDateTime serverTime = LocalDateTime.now();
        // 将服务器时间转换为系统默认时区的 ZonedDateTime
        ZonedDateTime serverZonedDateTime = serverTime.atZone(ZoneId.systemDefault());
        // 将其转换为 ClickHouse 时区的 ZonedDateTime
        ZonedDateTime clickhouseZonedDateTime = serverZonedDateTime.withZoneSameInstant(CLICKHOUSE_ZONE);
        // 转换为 ClickHouse 时区的 LocalDateTime
        LocalDateTime clickhouseLocalDateTime = clickhouseZonedDateTime.toLocalDateTime();
        monitorequipmentlastdata.setInputdatetime(clickhouseLocalDateTime);
        monitorequipmentlastdata.setHospitalcode(hospitalcode);
        //数据存储队列
        MonitorequipmentlastdataDto convert = BeanConverter.convert(monitorequipmentlastdata, MonitorequipmentlastdataDto.class);
        snDeviceRedisApi.updateSnCurrentInfo(convert);
    }

    @Override
    public void saveHaverLastData(String messageContent) {
        HarvesterDto harvesterDto = JSONUtil.toBean(messageContent, HarvesterDto.class);
        Harvester convert = BeanConverter.convert(harvesterDto, Harvester.class);
        harvesterRepository.save(convert);
    }


    public static void main(String[] args){
//        LocalDateTime serverTime = LocalDateTime.now();
//        // 将服务器时间转换为系统默认时区的 ZonedDateTime
//        ZonedDateTime serverZonedDateTime = serverTime.atZone(ZoneId.systemDefault());
//        // 将其转换为 ClickHouse 时区的 ZonedDateTime
//        ZonedDateTime clickhouseZonedDateTime = serverZonedDateTime.withZoneSameInstant(CLICKHOUSE_ZONE);
//        // 转换为 ClickHouse 时区的 LocalDateTime
//        LocalDateTime clickhouseLocalDateTime = clickhouseZonedDateTime.toLocalDateTime();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime utcTime = LocalDateTime.now(Clock.systemUTC());
//        ZonedDateTime phoenixTime = utcTime.atZone(ZoneOffset.UTC)
//                .withZoneSameInstant(ZoneId.of("America/Phoenix"));
//        LocalDateTime clickhouseLocalDateTime = phoenixTime.toLocalDateTime();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        System.out.println(clickhouseLocalDateTime.format(formatter));
        LocalDateTime serverTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(serverTime, CLICKHOUSE_ZONE);

        System.out.println("带时区的时间: " + zonedDateTime);
    }
}

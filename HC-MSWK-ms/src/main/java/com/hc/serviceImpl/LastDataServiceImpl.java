package com.hc.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.hc.clickhouse.po.Harvester;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.HarvesterRepository;
import com.hc.device.SnDeviceRedisApi;
import com.hc.my.common.core.redis.dto.HarvesterDto;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.LastDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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


    @Override
    public void saveLastData(Monitorequipmentlastdata monitorequipmentlastdata, String equipmentno, String hospitalcode,String cmdId,String sn) {
        monitorequipmentlastdata.setSn(sn);
        monitorequipmentlastdata.setCmdid(cmdId);
        monitorequipmentlastdata.setEquipmentno(equipmentno);
        // 如果传入的 LocalDateTime 没有指定时区，需要先确定其所在时区
        // 假设传入的是系统默认时区的 LocalDateTime
        ZoneId clickHouseZone = ZoneId.of("America/Phoenix");
        LocalDateTime inputdatetime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = inputdatetime.atZone(clickHouseZone);
        LocalDateTime clickhouseLocalDateTime = zonedDateTime.toLocalDateTime();
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
}

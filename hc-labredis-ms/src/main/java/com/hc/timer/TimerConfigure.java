package com.hc.timer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.hc.application.config.RedisUtils;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.redis.namespace.MswkServiceEnum;
import com.hc.my.common.core.util.BeanConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@EnableScheduling
@DS(value= "slave")
public class TimerConfigure  {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;

    //每分钟执行一次
    @Scheduled(cron = "0 */1 * * * ?")
    public void Time(){
        long size = redisUtils.lGetListSize(MswkServiceEnum.LAST_DATA.getCode());
        if(size == 0) return;
        List<MonitorequipmentlastdataDto> list = new ArrayList<>();
        for (long i = 0; i < size; i++) {
            Object object = redisUtils.lLeftPop(MswkServiceEnum.LAST_DATA.getCode());
            MonitorequipmentlastdataDto monitorequipmentlastdataDto = JSON.parseObject((String)object, new TypeReference<MonitorequipmentlastdataDto>(){});
            list.add(monitorequipmentlastdataDto);
        }
        List<Monitorequipmentlastdata> convert = BeanConverter.convert(list, Monitorequipmentlastdata.class);
        monitorequipmentlastdataRepository.batchInsert(convert);
    }
}

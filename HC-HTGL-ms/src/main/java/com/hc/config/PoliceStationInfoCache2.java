package com.hc.config;

import com.hc.entity.Monitorinstrument;
import com.hc.mapper.laboratoryFrom.MonitorInstrumentMapper;
import com.hc.units.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(value = 1)
public class PoliceStationInfoCache2 implements CommandLineRunner {


    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;
    private final Logger log = LoggerFactory.getLogger(PoliceStationInfoCache2.class);

    @Override
    public void run(String... arg0) {
        try {
            if (redisTemplateUtil.hasKey("hospital:sn")) {
                redisTemplateUtil.delete("hospital:sn");
            }

        } catch (Exception e) {
            log.error("删除redis缓存失败，原因：" + e.getMessage());
        }


        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();

        List<Monitorinstrument> monitorinstrumentList;
        monitorinstrumentList = monitorInstrumentMapper.selectInstrumentInfo();
        for (Monitorinstrument monitorinstrument : monitorinstrumentList) {
            if (StringUtils.isNotEmpty(monitorinstrument.getSn())) {
                if (StringUtils.isEmpty(monitorinstrument.getChannel())) {
                    objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                } else {
                    if ("1".equals(monitorinstrument.getChannel())) {
                        objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                    }
                }
            }
        }
    }

}

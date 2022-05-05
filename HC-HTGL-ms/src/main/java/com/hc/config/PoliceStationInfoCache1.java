package com.hc.config;

import com.hc.po.Monitorinstrument;
import com.hc.mapper.laboratoryFrom.InstrumentMonitorInfoMapper;
import com.hc.mapper.laboratoryFrom.MonitorInstrumentMapper;
import com.hc.model.InstrumentMonitorInfoModel;
import com.hc.units.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.stream.Collectors;

@Component
@Order(value = 2)
public class PoliceStationInfoCache1 implements CommandLineRunner {


    @Autowired
    private InstrumentMonitorInfoMapper instrumentMonitorInfoMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;
    private final Logger log = LoggerFactory.getLogger(PoliceStationInfoCache1.class);

    @Override
    public void run(String... arg0) {
        try {
            if (redisTemplateUtil.hasKey("DOOR:1")) {
                redisTemplateUtil.delete("DOOR:1");
            }
            if (redisTemplateUtil.hasKey("DOOR:2")) {
                redisTemplateUtil.delete("DOOR:2");
            }
        } catch (Exception e) {
            log.error("删除redis缓存失败，原因：" + e.getMessage());
        }

        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();

        List<Monitorinstrument> list;
        list = monitorInstrumentMapper.showMonitorInstrumentChannel();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Monitorinstrument monitorinstrument : list) {
                if (StringUtils.isNotEmpty(monitorinstrument.getSn())) {
                    objectObjectObjectHashOperations.put("DOOR:" + monitorinstrument.getChannel(), monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                }
            }
        }
        List<InstrumentMonitorInfoModel> instrumentMonitorInfoModelList;
        instrumentMonitorInfoModelList = instrumentMonitorInfoMapper.selectInstrumentInfo();

        List<String> collect = instrumentMonitorInfoModelList.stream().map(InstrumentMonitorInfoModel::getHospitalcode).collect(Collectors.toList());
        collect.forEach(s->{
            if (redisTemplateUtil.hasKey("insprobe"+s)) {
                redisTemplateUtil.delete("insprobe"+s);
            }
        });
        for (InstrumentMonitorInfoModel instrumentMonitorInfoModel : instrumentMonitorInfoModelList) {
            String hospitalcode = instrumentMonitorInfoModel.getHospitalcode();
            objectObjectObjectHashOperations.put("insprobe"+hospitalcode, instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid(), JsonUtil.toJson(instrumentMonitorInfoModel));
        }
//        List<Monitorinstrument> monitorinstrumentList;
//        monitorinstrumentList = monitorInstrumentMapper.selectInstrumentInfo();
//        for (Monitorinstrument monitorinstrument : monitorinstrumentList) {
//            if (StringUtils.isNotEmpty(monitorinstrument.getSn())) {
//                if (StringUtils.isEmpty(monitorinstrument.getChannel())) {
//                    objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
//                } else {
//                    if ("1".equals(monitorinstrument.getChannel())) {
//                        objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
//                    }
//                }
//            }
//        }
    }

}

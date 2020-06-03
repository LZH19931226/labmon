package com.hc.config;

import com.hc.dao.HospitalofreginfoDao;
import com.hc.dao.MonitorInstrumentDao;
import com.hc.entity.Monitorinstrument;
import com.hc.mapper.laboratoryFrom.ClientInfoMapper;
import com.hc.mapper.laboratoryFrom.InstrumentMonitorInfoMapper;
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

// 同步缓存  ：  底层原理是一个守护线程
@Component
@Order(value = 1)
public class PoliceStationInfoCache2 implements CommandLineRunner {

    @Autowired
    private ClientInfoMapper clientInfoMapper;
    @Autowired
    private InstrumentMonitorInfoMapper instrumentMonitorInfoMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private MonitorInstrumentDao monitorInstrumentDao;
    @Autowired
    private HospitalofreginfoDao hospitalofreginfoDao;
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

//        List<Hospitalofreginfo> hospitalofreginfoList = new ArrayList<Hospitalofreginfo>();
//        hospitalofreginfoList = hospitalofreginfoDao.findAll();
//        for (Hospitalofreginfo hospitalofreginfo : hospitalofreginfoList) {
//            List<Userright> userrightList = new ArrayList<Userright>();
//            userrightList = clientInfoMapper.selectUserInfoByHospitalcode(hospitalofreginfo.getHospitalcode());
//            if (CollectionUtils.isNotEmpty(userrightList)) {
//                objectObjectObjectHashOperations.put("hospital:phonenum", hospitalofreginfo.getHospitalcode(), JsonUtil.toJson(userrightList));
//            }
//            objectObjectObjectHashOperations.put("hospital:info", hospitalofreginfo.getHospitalcode(), JsonUtil.toJson(hospitalofreginfo));
//        }
//        log.info("执行探头信息同步");
//        List<Monitorinstrument> list = new ArrayList<Monitorinstrument>();
//        list = monitorInstrumentMapper.showMonitorInstrumentChannel();
//        if (CollectionUtils.isNotEmpty(list)) {
//            for (Monitorinstrument monitorinstrument : list) {
//                if (StringUtils.isNotEmpty(monitorinstrument.getSn())) {
//                    objectObjectObjectHashOperations.put("DOOR:" + monitorinstrument.getChannel(), monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
//                }
//            }
//        }
//        List<InstrumentMonitorInfoModel> instrumentMonitorInfoModelList = new ArrayList<InstrumentMonitorInfoModel>();
//        instrumentMonitorInfoModelList = instrumentMonitorInfoMapper.selectInstrumentInfo();
//        for (InstrumentMonitorInfoModel instrumentMonitorInfoModel : instrumentMonitorInfoModelList) {
//            objectObjectObjectHashOperations.put("hospital:instrumentparam", instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid().toString(), JsonUtil.toJson(instrumentMonitorInfoModel));
//        }
        List<Monitorinstrument> monitorinstrumentList = new ArrayList<Monitorinstrument>();
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

package com.hc.config;

import com.hc.dao.MonitorEquipmentDao;
import com.hc.entity.MonitorEquipmentWarningTime;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorinstrument;
import com.hc.mapper.laboratoryFrom.HospitalEquipmentMapper;
import com.hc.mapper.laboratoryFrom.MonitorEquipmentMapper;
import com.hc.mapper.laboratoryFrom.MonitorInstrumentMapper;
import com.hc.model.ResponseModel.HospitalEquipmentTypeInfoModel;
import com.hc.units.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Example;
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

    @Autowired
    private com.hc.dao.MonitorEquipmentWarningTimeDao monitorEquipmentWarningTimeDao;

    @Autowired
    private HospitalEquipmentMapper hospitalEquipmentMapper;

    @Autowired
    private MonitorEquipmentMapper monitorEquipmentMapper;

    @Override
    public void run(String... arg0) {
        try {
            if (redisTemplateUtil.hasKey("hospital:sn")) {
                redisTemplateUtil.delete("hospital:sn");
            }

            if(redisTemplateUtil.hasKey("hospital:equipmenttype")){
                redisTemplateUtil.delete("hospital:equipmenttype");
            }
        } catch (Exception e) {
            log.error("删除redis缓存失败，原因：" + e.getMessage());
        }


        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();

        List<Monitorinstrument> monitorinstrumentList;
        monitorinstrumentList = monitorInstrumentMapper.selectInstrumentInfo();
        for (Monitorinstrument monitorinstrument : monitorinstrumentList) {
            MonitorEquipmentWarningTime monitorEquipmentWarningTime = new MonitorEquipmentWarningTime();
            monitorEquipmentWarningTime.setEquipmentid(monitorinstrument.getEquipmentno());
            monitorEquipmentWarningTime.setEquipmentcategory("EQ");
            monitorEquipmentWarningTime.setHospitalcode(monitorinstrument.getHospitalcode());
            Example<MonitorEquipmentWarningTime> timeExample = Example.of(monitorEquipmentWarningTime);
            if(monitorinstrument.getWarningTimeList() == null){
                List<MonitorEquipmentWarningTime> warningTimeDaoAll = monitorEquipmentWarningTimeDao.findAll(timeExample);
                monitorinstrument.setWarningTimeList(warningTimeDaoAll);
                //设备是否全天报警默认和设备类型保持一致
                List<Monitorequipment> monitorequipmentList  = monitorEquipmentMapper
                        .selectEquipmentByCode(monitorinstrument.getHospitalcode(),monitorinstrument.getEquipmentno());
                if(CollectionUtils.isNotEmpty(monitorequipmentList)){
                    monitorinstrument.setAlwayalarm(monitorequipmentList.get(0).getAlwayalarm());
                }
            }
            if (StringUtils.isNotEmpty(monitorinstrument.getSn())) {
                String toJson = JsonUtil.toJson(monitorinstrument);
                if (StringUtils.isEmpty(monitorinstrument.getChannel())) {
                    objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), toJson);
                } else {
                    if ("1".equals(monitorinstrument.getChannel())) {
                        objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), toJson);
                    }
                }
            }
        }

        List<HospitalEquipmentTypeInfoModel> modelList = hospitalEquipmentMapper.selectAllEquipmentType();
        modelList.forEach(item->{
            MonitorEquipmentWarningTime monitorEquipmentWarningTime = new MonitorEquipmentWarningTime();
            monitorEquipmentWarningTime.setEquipmentid(item.getEquipmenttypeid());
            monitorEquipmentWarningTime.setEquipmentcategory("TYPE");
            monitorEquipmentWarningTime.setHospitalcode(item.getHospitalcode());
            Example<MonitorEquipmentWarningTime> timeExample = Example.of(monitorEquipmentWarningTime);
            List<MonitorEquipmentWarningTime> warningTimeDaoAll = monitorEquipmentWarningTimeDao.findAll(timeExample);
            item.setWarningTimeList(warningTimeDaoAll);
            objectObjectObjectHashOperations.put("hospital:equipmenttype",item.getEquipmenttypeid()+"@"+item.getHospitalcode(),
                    JsonUtil.toJson(item));
        });
    }

}

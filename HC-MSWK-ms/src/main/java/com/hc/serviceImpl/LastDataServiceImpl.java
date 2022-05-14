package com.hc.serviceImpl;

import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.po.Instrumentparamconfig;
import com.hc.mapper.HospitalInfoMapper;
import com.hc.mapper.InstrumentParamConfigMapper;
import com.hc.mapper.MonitorInstrumentMapper;
import com.hc.service.LastDataService;
import com.hc.service.MessagePushService;
import com.hc.utils.JsonUtil;
import com.redis.util.RedisTemplateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 16956 on 2018-09-04.
 */
@Service
public class LastDataServiceImpl implements LastDataService {
    private static final Logger log = LoggerFactory.getLogger(LastDataServiceImpl.class);
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataDao;
    @Autowired
    private MessagePushService service;
    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;
    @Autowired
    private InstrumentParamConfigMapper instrumentParamConfigDao;
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;

    @Value("${hc.pyx.pc}")
    private String pyxPc;
    @Value("${hc.ydg.pc}")
    private String ydgPc;

    @Override
    public void saveLastData(Monitorequipmentlastdata monitorequipmentlastdata, String equipmentno, Date time, String hospitalcode) {
        boolean flag = false;
        if (StringUtils.equals("H34", hospitalcode)) {
            flag = true;
        }
        //判断对象是否为空
        boolean b = checkObjAllFieldsIsNull(monitorequipmentlastdata);
        if (b) {
            return;
        }
        /**
         * 根据equipmentno 获取设备类型ID
         */
        String equipmentTypeId = hospitalInfoMapper.getEquipmentTypeId(equipmentno);
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA" + hospitalcode, equipmentno);
        monitorequipmentlastdata.setEquipmentno(equipmentno);
        monitorequipmentlastdata.setInputdatetime(time);
        monitorequipmentlastdata.setHospitalcode(hospitalcode);
        if (StringUtils.isEmpty(lastdata)) {
            //数据初次上传
            List<Instrumentparamconfig> instrumentparamconfigByEquipmentno = monitorInstrumentMapper.getInstrumentparamconfigByEquipmentno(equipmentno);
            if (instrumentparamconfigByEquipmentno != null & !instrumentparamconfigByEquipmentno.isEmpty()) {
                //过滤为2的氧气探头
                List<Instrumentparamconfig> collect = instrumentparamconfigByEquipmentno.stream().filter(s -> s != null).filter(s -> s.getInstrumentconfigid() == 2).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    collect.forEach(s -> {
                                Date firsttime1 = s.getFirsttime();
                                if (null == firsttime1) {
                                    s.setFirsttime(new Date());
                                    instrumentParamConfigDao.insert(s);
                                }
                            }
                    );
                }

            }

            monitorequipmentlastdataDao.save(monitorequipmentlastdata);
            objectObjectObjectHashOperations.put("LASTDATA" + hospitalcode, equipmentno, JsonUtil.toJson(monitorequipmentlastdata));
        } else {
            Monitorequipmentlastdata monitorequipmentlastdata1 = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
            if (flag) {
                //数据插入
                log.info("数据插入,原始数据为：" + JsonUtil.toJson(monitorequipmentlastdata));
                monitorequipmentlastdataDao.save(monitorequipmentlastdata);
                monitorequipmentlastdata1.setInputdatetime(monitorequipmentlastdata.getInputdatetime());
                service.pushMessage4(JsonUtil.toJson(monitorequipmentlastdata1));
                objectObjectObjectHashOperations.put("LASTDATA" + hospitalcode, equipmentno, JsonUtil.toJson(monitorequipmentlastdata));
            } else {
                String equipmentlastdata = monitorequipmentlastdata1.getEquipmentlastdata();
                if (StringUtils.isNotEmpty(equipmentlastdata)) {
                    //非空  直接 新增一条数据
                    monitorequipmentlastdataDao.save(monitorequipmentlastdata);
                    objectObjectObjectHashOperations.put("LASTDATA" + hospitalcode, equipmentno, JsonUtil.toJson(monitorequipmentlastdata));
                    return;
                }
                //数据插入
                log.info("数据插入,原始数据为：" + JsonUtil.toJson(monitorequipmentlastdata));
                monitorequipmentlastdataDao.save(monitorequipmentlastdata);
                monitorequipmentlastdata1.setInputdatetime(monitorequipmentlastdata.getInputdatetime());
                //咸宁医学院判断
                if (StringUtils.equals(hospitalcode, "166ce81489f84901bdae7a470874df58")) {
                    service.pushMessage4(JsonUtil.toJson(monitorequipmentlastdata));
                } else {
                    service.pushMessage4(JsonUtil.toJson(monitorequipmentlastdata1));
                }
                Monitorequipmentlastdata monitorequipmentlastdata2 = dataAdd(monitorequipmentlastdata1, monitorequipmentlastdata, equipmentTypeId, pc);
                objectObjectObjectHashOperations.put("LASTDATA" + hospitalcode, equipmentno, JsonUtil.toJson(monitorequipmentlastdata2));
            }


        }

    }


    public Monitorequipmentlastdata dataAdd(Monitorequipmentlastdata monitorequipmentlastdata, Monitorequipmentlastdata monitorequipmentlastdata1, String typeid, String pc) {
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentairflow())) {
            monitorequipmentlastdata.setCurrentairflow(monitorequipmentlastdata1.getCurrentairflow());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentups())) {
            monitorequipmentlastdata.setCurrentups(monitorequipmentlastdata1.getCurrentups());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentpm5())) {
            monitorequipmentlastdata.setCurrentpm5(monitorequipmentlastdata1.getCurrentpm5());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentpm05())) {
            monitorequipmentlastdata.setCurrentpm05(monitorequipmentlastdata1.getCurrentpm05());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentcarbondioxide())) {
            monitorequipmentlastdata.setCurrentcarbondioxide(monitorequipmentlastdata1.getCurrentcarbondioxide());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentdoorstate())) {
            monitorequipmentlastdata.setCurrentdoorstate(monitorequipmentlastdata1.getCurrentdoorstate());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentformaldehyde())) {
            monitorequipmentlastdata.setCurrentformaldehyde(monitorequipmentlastdata1.getCurrentformaldehyde());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenthumidity())) {
            monitorequipmentlastdata.setCurrenthumidity(monitorequipmentlastdata1.getCurrenthumidity());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrento2())) {
            monitorequipmentlastdata.setCurrento2(monitorequipmentlastdata1.getCurrento2());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentpm10())) {
            monitorequipmentlastdata.setCurrentpm10(monitorequipmentlastdata1.getCurrentpm10());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentpm25())) {
            monitorequipmentlastdata.setCurrentpm25(monitorequipmentlastdata1.getCurrentpm25());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentqc())) {
            monitorequipmentlastdata.setCurrentqc(monitorequipmentlastdata1.getCurrentqc());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentqcl())) {
            monitorequipmentlastdata.setCurrentqcl(monitorequipmentlastdata1.getCurrentqcl());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature())) {
            monitorequipmentlastdata.setCurrenttemperature(monitorequipmentlastdata1.getCurrenttemperature());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentvoc())) {
            monitorequipmentlastdata.setCurrentvoc(monitorequipmentlastdata1.getCurrentvoc());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentairflow1())) {
            monitorequipmentlastdata.setCurrentairflow1(monitorequipmentlastdata1.getCurrentairflow1());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature1())) {
            monitorequipmentlastdata.setCurrenttemperature1(monitorequipmentlastdata1.getCurrenttemperature1());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature2())) {
            monitorequipmentlastdata.setCurrenttemperature2(monitorequipmentlastdata1.getCurrenttemperature2());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature3())) {
            monitorequipmentlastdata.setCurrenttemperature3(monitorequipmentlastdata1.getCurrenttemperature3());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature4())) {
            monitorequipmentlastdata.setCurrenttemperature4(monitorequipmentlastdata1.getCurrenttemperature4());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature5())) {
            monitorequipmentlastdata.setCurrenttemperature5(monitorequipmentlastdata1.getCurrenttemperature5());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature6())) {
            monitorequipmentlastdata.setCurrenttemperature6(monitorequipmentlastdata1.getCurrenttemperature6());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature7())) {
            monitorequipmentlastdata.setCurrenttemperature7(monitorequipmentlastdata1.getCurrenttemperature7());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature8())) {
            monitorequipmentlastdata.setCurrenttemperature8(monitorequipmentlastdata1.getCurrenttemperature8());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature9())) {
            monitorequipmentlastdata.setCurrenttemperature9(monitorequipmentlastdata1.getCurrenttemperature9());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperature10())) {
            monitorequipmentlastdata.setCurrenttemperature10(monitorequipmentlastdata1.getCurrenttemperature10());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentlefttemperature())) {
            monitorequipmentlastdata.setCurrentlefttemperature(monitorequipmentlastdata1.getCurrentlefttemperature());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentrigthtemperature())) {
            monitorequipmentlastdata.setCurrentrigthtemperature(monitorequipmentlastdata1.getCurrentrigthtemperature());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrenttemperaturediff())) {
            monitorequipmentlastdata.setCurrenttemperaturediff(monitorequipmentlastdata1.getCurrenttemperaturediff());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentleftcovertemperature())) {
            monitorequipmentlastdata.setCurrentleftcovertemperature(monitorequipmentlastdata1.getCurrentleftcovertemperature());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentleftendtemperature())) {
            monitorequipmentlastdata.setCurrentleftendtemperature(monitorequipmentlastdata1.getCurrentleftendtemperature());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentleftairflow())) {
            monitorequipmentlastdata.setCurrentleftairflow(monitorequipmentlastdata1.getCurrentleftairflow());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentrightcovertemperature())) {
            monitorequipmentlastdata.setCurrentrightcovertemperature(monitorequipmentlastdata1.getCurrentrightcovertemperature());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentrightendtemperature())) {
            monitorequipmentlastdata.setCurrentrightendtemperature(monitorequipmentlastdata1.getCurrentrightendtemperature());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentrightairflow())) {
            monitorequipmentlastdata.setCurrentrightairflow(monitorequipmentlastdata1.getCurrentrightairflow());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentn2())) {
            monitorequipmentlastdata.setCurrentn2(monitorequipmentlastdata1.getCurrentn2());
        }

        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getLeftCompartmentHumidity())) {
            monitorequipmentlastdata.setLeftCompartmentHumidity(monitorequipmentlastdata1.getLeftCompartmentHumidity());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getRightCompartmentHumidity())) {
            monitorequipmentlastdata.setRightCompartmentHumidity(monitorequipmentlastdata1.getRightCompartmentHumidity());
        }
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getVoltage())) {
            monitorequipmentlastdata.setVoltage(monitorequipmentlastdata1.getVoltage());
        }
        String current = monitorequipmentlastdata1.getCurrent();
        if (StringUtils.isNotEmpty(current)) {
            monitorequipmentlastdata.setCurrent(current);
        }
        String power = monitorequipmentlastdata1.getPower();
        if (StringUtils.isNotEmpty(power)) {
            monitorequipmentlastdata.setPower(power);
        }
        return monitorequipmentlastdata;
    }
    // 判断对象是否为空方法：
    public boolean checkObjAllFieldsIsNull(Object object) {
        if (null == object) {
            return true;
        }

        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }

            }
        } catch (Exception e) {
            log.error("判断对象是否为空发生异常，原因：" + e);
            e.printStackTrace();
        }
        return true;
    }

}

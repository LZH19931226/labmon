package com.hc.serviceImpl;

import com.hc.config.RedisTemplateUtil;
import com.hc.dao.InstrumentParamConfigDao;
import com.hc.dao.MonitorequipmentlastdataDao;
import com.hc.entity.Instrumentparamconfig;
import com.hc.entity.Monitorequipmentlastdata;
import com.hc.mapper.HospitalInfoMapper;
import com.hc.mapper.MonitorInstrumentMapper;
import com.hc.service.LastDataService;
import com.hc.service.MessagePushService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    private MonitorequipmentlastdataDao monitorequipmentlastdataDao;
    @Autowired
    private MessagePushService service;
    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;
    @Autowired
    private InstrumentParamConfigDao instrumentParamConfigDao;
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;

    public static final List<String> arrListt = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I");

    @Value("${hc.pyx.pc}")
    private String pyxPc;
    @Value("${hc.ydg.pc}")
    private String ydgPc;

    @Override
    public void saveLastData(Monitorequipmentlastdata monitorequipmentlastdata, String equipmentno, Date time, String hospitalcode) {
        boolean flag = false;
        if (StringUtils.equals("H34", hospitalcode)) {
            //澳门镜湖 五分钟上传频率
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
        String pc = "0";
        if (StringUtils.equals(equipmentTypeId, "2")) {
            pc = pyxPc;
        } else if (StringUtils.equals(equipmentTypeId, "3")) {
            pc = ydgPc;
        }
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA", equipmentno);
        monitorequipmentlastdata.setEquipmentno(equipmentno);
        monitorequipmentlastdata.setInputdatetime(time);
        monitorequipmentlastdata.setHospitalcode(hospitalcode);
        monitorequipmentlastdata.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
        if (StringUtils.isEmpty(lastdata)) {
            //数据初次上传
            List<Instrumentparamconfig> instrumentparamconfigByEquipmentno = monitorInstrumentMapper.getInstrumentparamconfigByEquipmentno(equipmentno);
            if (CollectionUtils.isNotEmpty(instrumentparamconfigByEquipmentno)) {
                //过滤为2的氧气探头
                List<Instrumentparamconfig> collect = instrumentparamconfigByEquipmentno.stream().filter(s -> s.getInstrumentconfigid() == 2).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    collect.forEach(s -> {
                                Date firsttime1 = s.getFirsttime();
                                if (null == firsttime1) {
                                    s.setFirsttime(new Date());
                                    instrumentParamConfigDao.saveAndFlush(s);
                                }
                            }
                    );
                }

            }

            monitorequipmentlastdataDao.saveAndFlush(monitorequipmentlastdata);
            objectObjectObjectHashOperations.put("LASTDATA", equipmentno, JsonUtil.toJson(monitorequipmentlastdata));

        } else {
            //       try {
            // 判定是否是十分钟内的数据
            Monitorequipmentlastdata monitorequipmentlastdata1 = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
            double datePoor = TimeHelper.getDatePoor(time, monitorequipmentlastdata1.getInputdatetime());
            log.info("当前设备：" + equipmentno + "时间间隔：" + String.valueOf(datePoor));
            if (flag) {
                //澳门医院五分钟数据
                if (datePoor > 4.8) {
                    //数据插入
                    log.info("数据插入,原始数据为：" + JsonUtil.toJson(monitorequipmentlastdata));
                    // Monitorequipmentlastdata monitorequipmentlastdata2 = dataAdds(monitorequipmentlastdata1, monitorequipmentlastdata);
                    monitorequipmentlastdataDao.saveAndFlush(monitorequipmentlastdata);
                    monitorequipmentlastdata1.setInputdatetime(monitorequipmentlastdata.getInputdatetime());
                    service.pushMessage4(JsonUtil.toJson(monitorequipmentlastdata1));
                    objectObjectObjectHashOperations.put("LASTDATA", equipmentno, JsonUtil.toJson(monitorequipmentlastdata));
                } else {
                    //数据更新
                    log.info("数据更新,原始数据为:" + JsonUtil.toJson(monitorequipmentlastdata1));
                    Monitorequipmentlastdata monitorequipmentlastdata2 = dataAdd(monitorequipmentlastdata1, monitorequipmentlastdata, equipmentTypeId, pc);
                    monitorequipmentlastdataDao.saveAndFlush(monitorequipmentlastdata2);
                    objectObjectObjectHashOperations.put("LASTDATA", equipmentno, JsonUtil.toJson(monitorequipmentlastdata2));
                }
            } else {
                //其余医院十分钟
                String equipmentlastdata = monitorequipmentlastdata1.getEquipmentlastdata();
                if (StringUtils.isNotEmpty(equipmentlastdata)){
                    //非空  直接 新增一条数据
                    monitorequipmentlastdataDao.saveAndFlush(monitorequipmentlastdata1);
                    monitorequipmentlastdataDao.saveAndFlush(monitorequipmentlastdata);
                    if (StringUtils.equals(equipmentTypeId, "1")) {
                        Monitorequipmentlastdata monitorequipmentlastdata2 = dataAdd(monitorequipmentlastdata1, monitorequipmentlastdata, equipmentTypeId, pc);
                        monitorequipmentlastdata2.setPkid(monitorequipmentlastdata.getPkid());
                        monitorequipmentlastdata2.setEquipmentlastdata(null);
                        objectObjectObjectHashOperations.put("LASTDATA", equipmentno, JsonUtil.toJson(monitorequipmentlastdata2));
                    } else {
                        objectObjectObjectHashOperations.put("LASTDATA", equipmentno, JsonUtil.toJson(monitorequipmentlastdata));
                    }
                    return;
                }
                if (datePoor > 9.7) {
                    //数据插入
                    log.info("数据插入,原始数据为：" + JsonUtil.toJson(monitorequipmentlastdata));
                    //   Monitorequipmentlastdata monitorequipmentlastdata2 = dataAdds(monitorequipmentlastdata1, monitorequipmentlastdata);
                    monitorequipmentlastdataDao.saveAndFlush(monitorequipmentlastdata);
                    monitorequipmentlastdata1.setInputdatetime(monitorequipmentlastdata.getInputdatetime());
                    //咸宁医学院判断
                    if (StringUtils.equals(hospitalcode, "166ce81489f84901bdae7a470874df58")) {
                        service.pushMessage4(JsonUtil.toJson(monitorequipmentlastdata));
                    } else {
                        service.pushMessage4(JsonUtil.toJson(monitorequipmentlastdata1));
                    }
                    if (StringUtils.equals(equipmentTypeId, "1")) {
                        Monitorequipmentlastdata monitorequipmentlastdata2 = dataAdd(monitorequipmentlastdata1, monitorequipmentlastdata, equipmentTypeId, pc);
                        monitorequipmentlastdata2.setPkid(monitorequipmentlastdata.getPkid());
                        objectObjectObjectHashOperations.put("LASTDATA", equipmentno, JsonUtil.toJson(monitorequipmentlastdata2));
                    } else {
                        objectObjectObjectHashOperations.put("LASTDATA", equipmentno, JsonUtil.toJson(monitorequipmentlastdata));
                    }

                } else {
                    //数据更新
                    log.info("数据更新,原始数据为:" + JsonUtil.toJson(monitorequipmentlastdata1));
                    Monitorequipmentlastdata monitorequipmentlastdata2 = dataAdd(monitorequipmentlastdata1, monitorequipmentlastdata, equipmentTypeId, pc);
                    monitorequipmentlastdataDao.saveAndFlush(monitorequipmentlastdata2);
                    objectObjectObjectHashOperations.put("LASTDATA", equipmentno, JsonUtil.toJson(monitorequipmentlastdata2));
                }
            }


        }

    }

    /**
     * 新增数据
     *
     * @param monitorequipmentlastdata  上一条数据
     * @param monitorequipmentlastdata1 当前数据
     * @return
     */
    public Monitorequipmentlastdata dataAdds(Monitorequipmentlastdata monitorequipmentlastdata, Monitorequipmentlastdata monitorequipmentlastdata1) {
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentairflow())) {
            monitorequipmentlastdata.setCurrentairflow(monitorequipmentlastdata1.getCurrentairflow());
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
            monitorequipmentlastdata.setCurrentrightairflow(monitorequipmentlastdata1.getCurrentn2());
        }
        monitorequipmentlastdata.setInputdatetime(monitorequipmentlastdata1.getInputdatetime());
        monitorequipmentlastdata.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
        return monitorequipmentlastdata;
    }

    public Monitorequipmentlastdata dataAdd(Monitorequipmentlastdata monitorequipmentlastdata, Monitorequipmentlastdata monitorequipmentlastdata1, String typeid, String pc) {
        if (StringUtils.isNotEmpty(monitorequipmentlastdata1.getCurrentairflow())) {
            monitorequipmentlastdata.setCurrentairflow(monitorequipmentlastdata1.getCurrentairflow());
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
            // 当前地方温度要做比较
//            if (tempMax(monitorequipmentlastdata.getCurrenttemperature(),monitorequipmentlastdata1.getCurrenttemperature(),typeid,pc)) {
//                monitorequipmentlastdata.setCurrenttemperature(monitorequipmentlastdata1.getCurrenttemperature());
//            }else {
//                //新增一条数据
//                monitorequipmentlastdata.setInputdatetime(monitorequipmentlastdata1.getInputdatetime());
//                monitorequipmentlastdata.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
//            }
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
            monitorequipmentlastdata.setCurrenttemperature(monitorequipmentlastdata1.getCurrenttemperature6());
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
        return monitorequipmentlastdata;
    }

    public boolean tempMax(String temp, String temp1, String equipmentTypeid, String pc) {
        if (!StringUtils.equalsAny(equipmentTypeid, "2", "3")) {
            return true;
        }

        if (StringUtils.isEmpty(temp) || StringUtils.isEmpty(temp1)) {
            return true;
        }

        // 当上次记录信息和本次记录温度信息存在异常时，则返回false
        if (arrListt.contains(temp) || arrListt.contains(temp1)) {
            return true;
        }
        // 数字比较大小
        BigDecimal bigDecimal = new BigDecimal(temp);
        BigDecimal bigDecimal1 = new BigDecimal(temp1);
        BigDecimal abs = bigDecimal.subtract(bigDecimal1).abs();//返回两个差值的绝对值
        int i = abs.compareTo(new BigDecimal(pc));
        if (i == -1) {
            //当在差值范围内时候，则展示出来
            return true;
        }

        return false;
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

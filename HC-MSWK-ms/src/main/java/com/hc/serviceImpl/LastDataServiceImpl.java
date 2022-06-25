package com.hc.serviceImpl;

import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.device.SnDeviceRedisApi;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.LastDataService;
import com.hc.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by 16956 on 2018-09-04.
 */
@Service
@Slf4j
public class LastDataServiceImpl implements LastDataService {

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataDao;
    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;

    @Override
    public void saveLastData(Monitorequipmentlastdata monitorequipmentlastdata, String equipmentno, String hospitalcode,String cmdId,String sn) {
        //判断对象是否为空
        boolean b = checkObjAllFieldsIsNull(monitorequipmentlastdata);
        if (b) {
            return;
        }
        monitorequipmentlastdata.setSn(sn);
        monitorequipmentlastdata.setCmdid(cmdId);
        monitorequipmentlastdata.setEquipmentno(equipmentno);
        monitorequipmentlastdata.setInputdatetime(new Date());
        monitorequipmentlastdata.setHospitalcode(hospitalcode);
        log.info("数据插入,原始数据为：" + JsonUtil.toJson(monitorequipmentlastdata));
        //同步redis缓存
        MonitorequipmentlastdataDto convert = BeanConverter.convert(monitorequipmentlastdata, MonitorequipmentlastdataDto.class);
        snDeviceRedisApi.updateSnCurrentInfo(convert);
        monitorequipmentlastdataDao.save(monitorequipmentlastdata);
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

package com.hc.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.hc.clickhouse.po.Harvester;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.device.SnDeviceRedisApi;
import com.hc.my.common.core.redis.dto.HarvesterDto;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.LastDataService;
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
    private SnDeviceRedisApi snDeviceRedisApi;
    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;

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
        //数据存储队列
        MonitorequipmentlastdataDto convert = BeanConverter.convert(monitorequipmentlastdata, MonitorequipmentlastdataDto.class);
        snDeviceRedisApi.updateSnCurrentInfo(convert);
    }

    @Override
    public void saveHaverLastData(String messageContent) {
        HarvesterDto harvesterDto = JSONUtil.toBean(messageContent, HarvesterDto.class);
        Harvester convert = BeanConverter.convert(harvesterDto, Harvester.class);

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
            e.printStackTrace();
        }
        return true;
    }

}

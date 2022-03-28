package com.hc.serviceImpl;

import com.hc.web.config.RedisTemplateUtil;
import com.hc.entity.Monitorinstrument;
import com.hc.model.ResponseModel.CurrentData;
import com.hc.service.CurrentDataService;
import com.hc.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by xxf on 2018/9/28.
 */
@Service
public class CurrentDataServiceImpl implements CurrentDataService {
    private static final Logger log = LoggerFactory.getLogger(CurrentDataServiceImpl.class);
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;


    @Override
    public void getCurrentData(String data, Monitorinstrument monitorinstrument, String type, Date date) {
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        CurrentData currentData = new CurrentData();
        currentData.setCurrentData(data);
        currentData.setEquipmentno(monitorinstrument.getEquipmentno());
        currentData.setHospitalcode(monitorinstrument.getHospitalcode());
        currentData.setUnit(type);
        currentData.setInputdatetime(date);
        try {
            objectObjectObjectHashOperations.put(type, monitorinstrument.getEquipmentno(), JsonUtil.toJson(currentData));
        }catch (Exception e){
            log.error("当前值数据记录产生异常,异常原因："+e.getMessage());
        }
    }
}

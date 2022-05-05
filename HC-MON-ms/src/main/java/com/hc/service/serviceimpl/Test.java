package com.hc.service.serviceimpl;

import com.hc.po.Instrumentparamconfig;
import com.hc.po.Monitorinstrument;
import com.hc.mapper.laboratoryMain.InstrumentparamConfigSetMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 16956 on 2018-08-22.
 */
@Service
public class Test {
    @Autowired
    private InstrumentparamConfigSetMapper instrumentparamConfigSetMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);
    public Object test(){

        List<Instrumentparamconfig> list = new ArrayList<Instrumentparamconfig>();

        try{
            list = instrumentparamConfigSetMapper.test2();

        }catch(Exception e){
            LOGGER.error("获取所有未绑定探头失败："+e.getMessage());
            return "失败";
        }
        try{
            for(Instrumentparamconfig monitorinstrument:list) {
                instrumentparamConfigSetMapper.testInstrumentno(monitorinstrument.getChannel(),monitorinstrument.getInstrumentno());
            }
                return "成功";

        }catch(Exception e){
            LOGGER.error("将探头绑定SN号失败："+e.getMessage());
            return "失败";
        }
    }
    public Integer MTcheck(String id) {

        switch (id){
            case "02":
                return 10;
            case "04":
                return 1;
            case "05":
                return 3;
            case "06":
                return 4;
            case "07":
                return 5;
            case "08":
                return 7;
            case "11":
                return 9;
            case "12":
                return 8;
            case "13":
                return 2;
            case "14":
                return 6;
        }
        return null;

    }
}

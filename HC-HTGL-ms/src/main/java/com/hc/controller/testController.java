package com.hc.controller;

import com.hc.mapper.laboratoryFrom.MonitorequipmentlastdataMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author LiuZhiHao
 * @date 2020/8/31 17:34
 * 描述:
 **/
@RestController
@RequestMapping("/test")
public class testController {

    @Autowired
    private MonitorequipmentlastdataMapper monitorequipmentlastdataDao;

    @GetMapping("/")
    @ApiOperation("测试每月自动生成表格逻辑")
    public void test(){
        LocalDate date = LocalDate.now();
        date = date.minusMonths(1);
        int year = date.getYear();
        int monthValue= date.getMonthValue();
        int dayOfMonth = date.getDayOfMonth();
            //获取上个月表名
            String tableName = "monitorequipmentlastdata"+"_"+year +"0"+monthValue;
            monitorequipmentlastdataDao.altetTableMonitorequipmentlastdata(tableName);
            monitorequipmentlastdataDao.createTableMonitorequipmentlastdata(tableName);

    }
}

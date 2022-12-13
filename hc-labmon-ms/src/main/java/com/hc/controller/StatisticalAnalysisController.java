package com.hc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.StatisticalAnalysisApplication;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.application.response.SummaryOfAlarmsResult;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.my.common.core.jwt.JwtIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequestMapping("/as")
@RestController
public class StatisticalAnalysisController {

    @Autowired
    private StatisticalAnalysisApplication statisticalAnalysisApplication;

    /**
     *
     * @return
     */
    @GetMapping("/getStatisticalData")
    public Map<String, Map<String,Long>> getStatisticalData(@RequestParam("time") String time){
        return statisticalAnalysisApplication.getStatisticalData(time);
    }
    /*
    * 设备数据查询
    * */
    /**
     * 自定义查询 接口1.分页查询
     */
    @JwtIgnore
    @PostMapping("/getEquipmentData")
    public Page<Monitorequipmentlastdata> getEquipmentData(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return statisticalAnalysisApplication.getEquipmentData(equipmentDataCommand);
    }
    /**
     * 自定义查询 接口2.查询报警汇总图
     */
    @JwtIgnore
    @PostMapping("/getSummaryOfAlarms")
    public SummaryOfAlarmsResult getSummaryOfAlarms(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return statisticalAnalysisApplication.getSummaryOfAlarms(equipmentDataCommand);
    }


    /*
    报警数据查询
     */
    /**
     * 获取报警数据
     * @param alarmNoticeCommand
     * @return
     */
    @PostMapping("/getAlarmNotice")
    public Page getAlarmNotice(@RequestBody AlarmNoticeCommand alarmNoticeCommand){
        return statisticalAnalysisApplication.getAlarmNotice(alarmNoticeCommand);
    }

    /**
     * 导出报警数据
     */
    @JwtIgnore
    @GetMapping("/exportAlarmNotice")
    public void exportAlarmNotice(AlarmNoticeCommand alarmNoticeCommand, HttpServletResponse response){
        statisticalAnalysisApplication.exportAlarmNotice(alarmNoticeCommand,response);
    }
}

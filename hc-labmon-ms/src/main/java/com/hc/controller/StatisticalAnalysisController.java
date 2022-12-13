package com.hc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.StatisticalAnalysisApplication;
import com.hc.application.command.AlarmNoticeCommand;
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

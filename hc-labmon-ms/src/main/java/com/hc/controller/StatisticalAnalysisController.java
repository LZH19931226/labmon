package com.hc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.StatisticalAnalysisApplication;
import com.hc.application.command.AlarmDataCommand;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.application.response.PointInTimeDataTableResult;
import com.hc.application.response.SummaryOfAlarmsResult;
import com.hc.application.response.TimePointCurve;
import com.hc.dto.CurveInfoDto;
import com.hc.my.common.core.jwt.JwtIgnore;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequestMapping("/as")
@RestController
public class StatisticalAnalysisController {

    @Autowired
    private StatisticalAnalysisApplication statisticalAnalysisApplication;

//    /**
//     *
//     * @return
//     */
//    @GetMapping("/getStatisticalData")
//    @ApiOperation("获取统计数据")
//    public Map<String, Map<String,Long>> getStatisticalData(@RequestParam("time") String time){
//        return statisticalAnalysisApplication.getStatisticalData(time);
//    }
    /*
    * 设备数据查询
    * */
    /**
     * 自定义查询 接口1.分页查询
     */
    @JwtIgnore
    @PostMapping("/getEquipmentData")
    @ApiOperation("分页查询")
    public Page getEquipmentData(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return statisticalAnalysisApplication.getEquipmentData(equipmentDataCommand);
    }

    /**
     * 导出设备数据
     * @param equipmentDataCommand
     * @param response
     */
    @JwtIgnore
    @PostMapping("/exportEquipmentData")
    @ApiOperation("导出设备数据")
    public void exportEquipmentData(@RequestBody EquipmentDataCommand equipmentDataCommand,HttpServletResponse response){
        statisticalAnalysisApplication.exportEquipmentData(equipmentDataCommand,response);
    }


    /**
     * 自定义查询 接口2.查询报警汇总图
     */
    @JwtIgnore
    @PostMapping("/getSummaryOfAlarms")
    @ApiOperation("查询报警汇总图")
    public SummaryOfAlarmsResult getSummaryOfAlarms(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return statisticalAnalysisApplication.getSummaryOfAlarms(equipmentDataCommand);
    }

    /**
     * 时间点查询 接口1,曲线
     */
    @PostMapping("/getThePointInTimeDataCurve")
    @ApiOperation("获取时间点曲线")
    public  List<TimePointCurve> getThePointInTimeDataCurve(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return statisticalAnalysisApplication.getThePointInTimeDataCurve(equipmentDataCommand);
    }

    /**
     * 时间点查询 接口2：表格
     */
    @PostMapping("/getThePointInTimeDataTable")
    @ApiOperation("获取时间点数据表")
    public    List<Map<String,String>> getThePointInTimeDataTable(@RequestBody EquipmentDataCommand equipmentDataCommand){
       return statisticalAnalysisApplication.getThePointInTimeDataTable(equipmentDataCommand);
    }

    /**
     * 时间点查询 接口3：导出excel
     */
    @JwtIgnore
    @PostMapping("/exportDatePoint")
    @ApiOperation("导出excel")
    public void exportDatePoint(@RequestBody EquipmentDataCommand equipmentDataCommand,HttpServletResponse httpServletResponse){
         statisticalAnalysisApplication.exportDatePoint(equipmentDataCommand,httpServletResponse);
    }

    /*
    报警数据查询
     */
    /**
     * 报警通知 接口1：获取报警通知数据
     * @param alarmNoticeCommand
     * @return
     */
    @PostMapping("/getAlarmNotice")
    @ApiOperation("获取报警通知数据")
    public Page getAlarmNotice(@RequestBody AlarmNoticeCommand alarmNoticeCommand){
        return statisticalAnalysisApplication.getAlarmNotice(alarmNoticeCommand);
    }


    /**
     * 报警通知 接口2：导出报警数据
     */
    @JwtIgnore
    @GetMapping("/exportAlarmNotice")
    @ApiOperation("导出报警数据")
    public void exportAlarmNotice(AlarmNoticeCommand alarmNoticeCommand, HttpServletResponse response){
        statisticalAnalysisApplication.exportAlarmNotice(alarmNoticeCommand,response);
    }

    /**
     * 报警汇总 接口1：分页查询报警数据
     */
    @PostMapping("/getAlarmData")
    @ApiOperation("分页获取报警汇总数据")
    public Page getAlarmData(@RequestBody  AlarmDataCommand alarmDataCommand){
        return statisticalAnalysisApplication.getAlarmData(alarmDataCommand);
    }

    /**
     * 报警汇总 接口2：导出查询报警数据
     */
    @PostMapping("/exportAlarmData")
    @ApiOperation("导出查询报警数据")
    public void exportAlarmData(@RequestBody AlarmDataCommand alarmDataCommand,HttpServletResponse response){
        statisticalAnalysisApplication.exportAlarmData(alarmDataCommand,response);
    }
}

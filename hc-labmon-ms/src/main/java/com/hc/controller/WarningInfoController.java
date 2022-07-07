package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.WarningInfoApplication;
import com.hc.application.command.WarningInfoCommand;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.dto.CurveInfoDto;
import com.hc.dto.WarningRecordInfoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warningInfo")
public class WarningInfoController {

    @Autowired
    private WarningInfoApplication warningInfoApplication;

    @PostMapping("/getWarningInfo")
    @ApiOperation("分页获取报警信息")
    public  Page<Warningrecord> getWarningRecord(@RequestBody WarningInfoCommand warningInfoCommand){
       return warningInfoApplication.getWarningRecord(warningInfoCommand);
    }

    @GetMapping("/getWarningCurveData")
    @ApiOperation("获取报警设备时间段曲线数据")
    public CurveInfoDto getWarningCurveData(@RequestParam("pkId")String pkId,
                                            @RequestParam("startTime")String startTime,
                                            @RequestParam("endTime")String endTime){
        return warningInfoApplication.getWarningCurveData(pkId,startTime,endTime);
    }

    @PostMapping("/insertAlarmRemarks")
    @ApiOperation("插入报警备注信息")
    public void saveInsertAlarmRemarks(@RequestBody WarningRecordInfoDto warningRecordDto){
         warningInfoApplication.saveInsertAlarmRemarks(warningRecordDto);
    }


    @GetMapping("/getWarningRecordInfo")
    @ApiOperation("获取报警备注信息")
    public WarningRecordInfoDto getWarningRecordInfoByWarningRecordId(@RequestParam("pkId") String pkId){
        return warningInfoApplication.getWarningRecordInfo(pkId);
    }
}

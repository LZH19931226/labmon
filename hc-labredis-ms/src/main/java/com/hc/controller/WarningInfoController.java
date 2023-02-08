package com.hc.controller;

import com.hc.application.WarningInfoApplication;
import com.hc.my.common.core.redis.dto.WarningrecordRedisInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wr")
public class WarningInfoController {

    @Autowired
    private WarningInfoApplication warningInfoApplication;

    @PostMapping("/add")
    @ApiOperation("新增或删除报警信息")
    public void add(@RequestBody WarningrecordRedisInfo warningrecordRedisInfo){
        warningInfoApplication.add(warningrecordRedisInfo);
    }

    @GetMapping("/getSize")
    @ApiOperation("获取报警信息缓存数组长度")
    public Long getWarningRecordSize(@RequestParam("listCode")String listCode){
        return warningInfoApplication.getWarningRecordSize(listCode);
    }

    @GetMapping("/getLeftPopWarningRecord")
    @ApiOperation("获取报警信息并删除当前信息")
    public WarningrecordRedisInfo getLeftPopWarningRecord(@RequestParam("listCode")String listCode){
        return  warningInfoApplication.getLeftPopWarningRecord(listCode);
    }
}

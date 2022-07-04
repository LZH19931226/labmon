package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.WarningInfoApplication;
import com.hc.application.command.WarningInfoCommand;
import com.hc.clickhouse.po.Warningrecord;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}

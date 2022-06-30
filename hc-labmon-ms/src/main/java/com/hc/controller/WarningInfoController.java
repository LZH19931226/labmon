package com.hc.controller;

import com.hc.application.WarningInfoApplication;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/warningInfo")
public class WarningInfoController {

    @Autowired
    private WarningInfoApplication warningInfoApplication;

    @GetMapping("/getWarningRecord")
    @ApiOperation(value = "获取当前医院最新二十条报警信息")
    public List<WarningRecordDto> getWarningRecord(@RequestParam(value = "hospitalCode")String hospitalCode)
    {
        return warningInfoApplication.getWarningRecord(hospitalCode);
    }

}

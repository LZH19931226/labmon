package com.hc.controller;


import com.hc.appliction.WarningInfoApplication;
import com.hc.vo.waring.WarningrecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by 16956 on 2018-08-01.
 */
@Api(value = "报警信息")
@RestController
@RequestMapping(value = "/api/warningInfo")
public class WarningInfoController {

    @Autowired
    private WarningInfoApplication warningInfoApplication;


    @GetMapping("/getWarningRecord")
    @ApiOperation(value = "获取当前医院最新二十条报警信息")
    public List<WarningrecordVo> getWarningInfo(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                           @RequestParam(value = "hospitalcode", required = true) String hospitalcode) {
        return warningInfoApplication.getWarningRecord(hospitalcode);
    }



}

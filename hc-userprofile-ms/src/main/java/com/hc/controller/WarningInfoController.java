package com.hc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.WarningInfoApplication;
import com.hc.vo.waring.WarningrecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Created by 16956 on 2018-08-01.
 * @author user
 */
@Api(value = "报警信息")
@RestController
@RequestMapping(value = "/api/warningInfo")
public class WarningInfoController {

    @Autowired
    private WarningInfoApplication warningInfoApplication;


    /**
     * 获取当前医院最新二十条报警信息
     * @param hospitalcode  医院编号
     * @return
     */
    @GetMapping("/getWarningRecord")
    @ApiOperation(value = "获取当前医院最新二十条报警信息")
    public List<WarningrecordVo> getWarningInfo(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                           @RequestParam(value = "hospitalcode", required = true) String hospitalcode) {
        return warningInfoApplication.getWarningRecord(hospitalcode);
    }

    @GetMapping("/getNewRecord")
    @ApiOperation(value = "获取最新的报警记录")
    public Page<WarningrecordVo> getNewWarningRecord(@ApiParam(name = "hospitalcode",value = "医院编号",required = true)
                                                     @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                     @ApiParam(name = "pagesize",value = "页码",required = true)
                                                     @RequestParam(value = "pagesize",required = true)Integer pagesize,
                                                     @ApiParam(name = "pagenum",value = "页数",required = true)
                                                     @RequestParam(value = "pagenum",required = true)Integer pagenum
                                                     ){
        return warningInfoApplication.getNewWarnRecord(hospitalcode,pagesize,pagenum);
    }

    @GetMapping("/getInsTypeRecord")
    @ApiOperation(value = "获取探头监控类型历史报警信息")
    public Page<WarningrecordVo> getInsTypeRecord(@ApiParam(name = "instrumentparamconfigNO", value = "探头类型编号", required = true)
                                                             @RequestParam(value = "instrumentparamconfigNO", required = true) String instrumentparamconfigNO,
                                                             @ApiParam(name = "pagesize", value = "页码", required = true)
                                                             @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                             @ApiParam(name = "pagenum", value = "页数", required = true)
                                                             @RequestParam(value = "pagenum", required = true) Integer pagenum,
                                                             @ApiParam(name = "isphone", value = "是否短信报警", required = true)
                                                             @RequestParam(value = "isphone",required = true) String isphone) {
        return warningInfoApplication.getInstrumentTypeHistoryWarn(instrumentparamconfigNO, pagesize, pagenum,isphone);
    }

    @PostMapping("/isRead")
    @ApiOperation(value = "修改报警信息状态")
    public String isRead(@ApiParam(name = "instrumentparamconfigNO", value = "探头类型编号", required = true)
                                      @RequestParam(value = "instrumentparamconfigNO", required = true) String instrumentparamconfigNO) {
        return warningInfoApplication.isRead(instrumentparamconfigNO);
    }


    @PostMapping("/deleteWarnInfo")
    @ApiOperation(value = "删除报警信息状态")
    public String deleteWarningInfo(@RequestBody @Valid WarningrecordVo warningrecordVo) {
        return warningInfoApplication.deleteWarningInfo(warningrecordVo);
    }

}

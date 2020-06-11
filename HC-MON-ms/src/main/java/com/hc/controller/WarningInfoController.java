package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.entity.WarningRecordInfo;
import com.hc.entity.Warningrecord;
import com.hc.model.CurveInfoModel;
import com.hc.model.NewWarningRecord;
import com.hc.model.PushSetModel;
import com.hc.model.ShowData;
import com.hc.service.WarningInfoService;
import com.hc.service.WarningRecordInfoService;
import com.hc.utils.ApiResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by 16956 on 2018-08-01.
 */
@Api(value = "报警信息", description = "报警信息API")
@RestController
@RequestMapping(value = "/api/warningInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WarningInfoController {
    @Autowired
    private WarningInfoService warningInfoService;
    @Autowired
    private WarningRecordInfoService warningRecordInfoService;

    @GetMapping("/getWarningRecord")
    @ApiOperation(value = "获取当前医院最新二十条报警信息")
    public ApiResponse<List<Warningrecord>> getWarningInfo(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                           @RequestParam(value = "hospitalcode", required = true) String hospitalcode) {
        return warningInfoService.getWarningRecord(hospitalcode);
    }

    @GetMapping("/getNewRecord")
    @ApiOperation(value = "获取当前医院所有设备最新报警信息")
    public ApiResponse<Page<NewWarningRecord>> getNewWarningRecord(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                                   @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                                   @ApiParam(name = "pagesize", value = "页码", required = true)
                                                                   @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                                   @ApiParam(name = "pagenum", value = "页数", required = true)
                                                                   @RequestParam(value = "pagenum", required = true) Integer pagenum) {
        return warningInfoService.getNewWarnRecord(hospitalcode, pagesize, pagenum);
    }

    @GetMapping("/getInsTypeRecord")
    @ApiOperation(value = "获取探头监控类型历史报警信息")
    public ApiResponse<Page<Warningrecord>> getInsTypeRecord(@ApiParam(name = "instrumentparamconfigNO", value = "探头类型编号", required = true)
                                                             @RequestParam(value = "instrumentparamconfigNO", required = true) String instrumentparamconfigNO,
                                                             @ApiParam(name = "pagesize", value = "医院编号", required = true)
                                                             @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                             @ApiParam(name = "pagenum", value = "设备类型编号", required = true)
                                                             @RequestParam(value = "pagenum", required = true) Integer pagenum) {
        return warningInfoService.getInstrumentTypeHistoryWarn(instrumentparamconfigNO, pagesize, pagenum);
    }

    @PostMapping("/isRead")
    @ApiOperation(value = "修改报警信息状态")
    public ApiResponse<String> isRead(@ApiParam(name = "instrumentparamconfigNO", value = "探头类型编号", required = true)
                                      @RequestParam(value = "instrumentparamconfigNO", required = true) String instrumentparamconfigNO) {
        return warningInfoService.isRead(instrumentparamconfigNO);
    }

    @PostMapping("/deleteWarnInfo")
    @ApiOperation(value = "删除报警信息状态")
    public ApiResponse<String> deleteWarningInfo(@RequestBody @Valid PushSetModel pushSetModel) {
        return warningInfoService.deleteWarningInfo(pushSetModel);
    }

    @GetMapping("/showData")
    @ApiOperation(value = "大屏展示报警数据")
    public ApiResponse<List<ShowData>> showData() {
        return warningInfoService.showData();
    }


    @PostMapping
    @ApiOperation("插入报警备注信息")
    public ApiResponse<String> insertwarningRecordInfo(@RequestBody WarningRecordInfo warningRecordInfo){
        return warningRecordInfoService.instwarningrecordinfo(warningRecordInfo);
    }

    @GetMapping("/getWarningCurveData")
    @ApiOperation("获取报警设备时间段曲线数据")
    public ApiResponse<CurveInfoModel> getWarningCurveData(@ApiParam(name = "warningRecordId", value = "报警记录id", required = true)
                                                               @RequestParam(value = "warningRecordId") String warningRecordId,
                                                           @ApiParam(name = "startTime", value = "开始时间", required = true)
                                                           @RequestParam(value = "startTime") String startTime,
                                                           @ApiParam(name = "endTime", value = "结束时间", required = true)
                                                               @RequestParam(value = "endTime") String endTime){
        return warningRecordInfoService.getWarningCurveData(warningRecordId,startTime,endTime);
    }


}

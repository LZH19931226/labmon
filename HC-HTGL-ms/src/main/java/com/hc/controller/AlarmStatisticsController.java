package com.hc.controller;

import com.hc.model.ResponseModel.AlarmData;
import com.hc.model.ResponseModel.AlarmHospitalInfo;
import com.hc.model.ResponseModel.ShowData;
import com.hc.service.AlarmService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xxf on 2018/10/10.
 */
@Api(value = "报警统计", description = "报警统计Api")
@RestController
@RequestMapping(value = "/api/alarmStatistics", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AlarmStatisticsController {
    @Autowired
    private AlarmService alarmService;

    @GetMapping("/alarmStatisticsShow")
    @ApiOperation(value = "获取医院监控信息")
    public ApiResponse<List<AlarmHospitalInfo>> alarm() {

        return alarmService.showAllHospitalAbInfo();
    }
    @GetMapping("/exporeExcle")
    @ApiOperation(value = "导出Excle")
    public ApiResponse<String> exporeExcle(HttpServletResponse response) {

        return alarmService.exportExcle(response);
    }
    @GetMapping("/exporeExcles")
    @ApiOperation(value = "导出Excle")
    public ApiResponse<String> exporeExcles(HttpServletResponse response) {

        return alarmService.exportExcles(response);
    }
    @GetMapping("/getHospitalAlarmInfo")
    @ApiOperation(value = "获取医院设备异常信息")
    public ApiResponse<List<AlarmData>> getHospitalAralmInfo(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                                       @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                             @ApiParam(name = "equipmenttypeid", value = "设备类型编码", required = false)
                                                                       @RequestParam(value = "equipmenttypeid", required = false) String equipmenttypeid,
                                                             @ApiParam(name = "type", value = "异常类型", required = true)
                                                                       @RequestParam(value = "type", required = true) String type) {

        return alarmService.showHospitalAlarmStatistics(hospitalcode, equipmenttypeid, type);
    }

    @GetMapping("/showAllData")
    @ApiOperation(value = "滚动展示所有设备探头当前值")
    public ApiResponse<List<ShowData>> showAllData() {

        return alarmService.showAllData();
    }

}

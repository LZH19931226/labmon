package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorupsrecord;
import com.hc.mapper.laboratoryFrom.EquipmentInfoMapper;
import com.hc.model.*;
import com.hc.model.ResponseModel.EquipmentConfigInfoModel;
import com.hc.service.EquipmentInfoService;
import com.hc.service.serviceimpl.Test;
import com.hc.utils.ApiResponse;

import com.hc.utils.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 16956 on 2018-08-01.
 */
@Api(value = "设备信息", description = "设备信息API")
@RestController
@RequestMapping(value = "/api/equipmentInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class EquipmentInfoController {

    @Autowired
    private EquipmentInfoService equipmentInfoService;
    @Autowired
    private Test test;
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;

    @GetMapping("/getEquipmentInfoByHospitalcode")
    public ApiResponse<List<Monitorequipment>> getEquipmentInfoByHospitalcode(){
        List<Monitorequipment> equipmentInfoByHospitalcode = equipmentInfoMapper.getEquipmentInfoByHospitalcode();
        ApiResponse<List<Monitorequipment>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(equipmentInfoByHospitalcode);
        return apiResponse;
    }

    @PostMapping("/batchOperationType")
    @ApiOperation(value = "批量禁用与启用当前设备类型下所有设备报警探头", response = Monitorequipment.class)
    public ApiResponse<String> batchOperationType(@RequestBody @Valid BatchModel batchModel) {
        return equipmentInfoService.batchOperationType(batchModel);
    }


    @GetMapping("/getEquipmentInfo")
    @ApiOperation(value = "根据医院编号和设备类型编号获取设备信息", response = Monitorequipment.class)
    public ApiResponse<List<Monitorequipment>> getEquipmentByType(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                                  @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                                  @ApiParam(name = "equipmenttypeid", value = "设备类型编号", required = true)
                                                                  @RequestParam(value = "equipmenttypeid", required = true) String equipmenttypeid) {
        return equipmentInfoService.getEquipmentByType(hospitalcode, equipmenttypeid);

    }

    @GetMapping("/getEquipmentCurrentData")
    @ApiOperation(value = "查询所有设备当前值信息", response = EquipmentCurrentDateModel.class)
    public ApiResponse<List<Monitorequipment>> getEquipmentCurrentData(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                                       @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                                       @ApiParam(name = "equipmenttypeid", value = "设备类型编号", required = true)
                                                                       @RequestParam(value = "equipmenttypeid", required = true) String equipmenttypeid) {
        return equipmentInfoService.getEquipmentCurrentData(hospitalcode, equipmenttypeid);
    }

    @GetMapping("/getEquipmentCurrentDataPage")
    @ApiOperation(value = "查询所有设备当前值信息", response = EquipmentCurrentDateModel.class)
    public ApiResponse<Page<Monitorequipment>> getEquipmentCurrentDataPage(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                                           @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                                           @ApiParam(name = "equipmenttypeid", value = "设备类型编号", required = true)
                                                                           @RequestParam(value = "equipmenttypeid", required = true) String equipmenttypeid,
                                                                           @ApiParam(name = "pagesize", value = "医院编号", required = true)
                                                                           @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                                           @ApiParam(name = "pagenum", value = "设备类型编号", required = true)
                                                                           @RequestParam(value = "pagenum", required = true) Integer pagenum,
                                                                           @ApiParam(name = "equipmentname", value = "设备名称", required = false)
                                                                               @RequestParam(value = "equipmentname", required = false) String equipmentname) {
        return equipmentInfoService.getEquipmentCurrentDataPage(hospitalcode, equipmenttypeid, pagesize, pagenum, equipmentname);
    }

    @GetMapping("/getCurrentUps")
    @ApiOperation(value = "获取当前市电", response = EquipmentCurrentDateModel.class)
    public ApiResponse<Monitorupsrecord> getUps(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                @ApiParam(name = "equipmenttypeid", value = "设备类型编号", required = true)
                                                @RequestParam(value = "equipmenttypeid", required = true) String equipmenttypeid) {
        return equipmentInfoService.getCurrentUps(hospitalcode, equipmenttypeid);
    }

    @GetMapping("/getCurveInfo")
    @ApiOperation(value = "获取曲线信息，不包括曲线对比信息")
    public ApiResponse<CurveInfoModel> getCurveInfo(@ApiParam(name = "equipmentno", value = "设备编号", required = true)
                                                    @RequestParam(value = "equipmentno", required = true) String equipmentno,
                                                    @ApiParam(name = "date", value = "日期", required = true)
                                                    @RequestParam(value = "date", required = true) String date) {
        return equipmentInfoService.getCurvelFirst(date, equipmentno);
    }

    @GetMapping("/getCurveInfoOther")
    @ApiOperation(value = "获取曲线信息--曲线对比信息")
    public ApiResponse<CurveInfoModel> getCurveInfo(@ApiParam(name = "equipmentno", value = "设备编号", required = true)
                                                    @RequestParam(value = "equipmentno", required = true) List<String> equipmentno,
                                                    @ApiParam(name = "date", value = "日期", required = true)
                                                    @RequestParam(value = "date", required = true) String date,
                                                    @ApiParam(name = "type", value = "类型", required = true)
                                                    @RequestParam(value = "type", required = true) String type
    ) {
        return equipmentInfoService.getCurveSecond(date, equipmentno, type);
    }

    @GetMapping("/getSearchInfo")
    @ApiOperation(value = "获取查询信息")
    public ApiResponse<List<QueryInfoModel>> getCurveInfo(@ApiParam(name = "equipmentno", value = "设备编号", required = false)
                                                          @RequestParam(value = "equipmentno", required = false) String equipmentno,
                                                          @ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                          @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                          @ApiParam(name = "equipmenttypeid", value = "类型编号", required = false)
                                                          @RequestParam(value = "equipmenttypeid", required = false) String equipmenttypeid,
                                                          @ApiParam(name = "startdate", value = "开始时间", required = true)
                                                          @RequestParam(value = "startdate", required = true) String startdate,
                                                          @ApiParam(name = "enddate", value = "结束时间", required = true)
                                                          @RequestParam(value = "enddate", required = true) String enddate
    ) {
        return equipmentInfoService.queryEquipmentMonitorInfo(equipmenttypeid, equipmentno, hospitalcode, startdate, enddate);
    }

    @GetMapping("/exprotExcle")
    @ApiOperation(value = "导出excle")
    public ApiResponse<String> excleInfo(@ApiParam(name = "equipmentno", value = "设备编号", required = false)
                                         @RequestParam(value = "equipmentno", required = false) String equipmentno,
                                         @ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                         @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                         @ApiParam(name = "equipmenttypeid", value = "类型编号", required = false)
                                         @RequestParam(value = "equipmenttypeid", required = false) String equipmenttypeid,
                                         @ApiParam(name = "startdate", value = "开始时间", required = true)
                                         @RequestParam(value = "startdate", required = true) String startdate,
                                         @ApiParam(name = "enddate", value = "结束时间", required = true)
                                         @RequestParam(value = "enddate", required = true) String enddate,
                                         HttpServletResponse response
    ) {
        return equipmentInfoService.exportExcle(equipmenttypeid, equipmentno, hospitalcode, startdate, enddate, response);
    }

    @GetMapping("/showEquipmentConfigInfo")
    @ApiOperation(value = "显示当前医院所有设备类型配置信息", response = EquipmentCurrentDateModel.class)
    public ApiResponse<List<EquipmentConfigInfoModel>> showEquipmentConfigInfo(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                                               @RequestParam(value = "hospitalcode", required = true) String hospitalcode) {
        return equipmentInfoService.showEquipmentConfigInfo(hospitalcode);
    }

    @PostMapping("/update")
    public Object test() {
        return test.test();
    }


    @GetMapping("/showCurDataByNo")
    @ApiOperation(value = "显示当前医院所有设备类型配置信息", response = EquipmentCurrentDateModel.class)
    public ApiResponse<Monitorequipment> showCurDataByNo(@ApiParam(name = "equipmentno", value = "设备编号", required = true)
                                                         @RequestParam(value = "equipmentno", required = true) String equipmentno) {
        return equipmentInfoService.showInfoByEquipmentNo(equipmentno);
    }

    @GetMapping("/close")
    public ApiResponse<String> test1(@ApiParam(name = "MId", value = "MId", required = true)
                                     @RequestParam(value = "MId", required = true) String MId,
                                     @ApiParam(name = "cmd", value = "cmd", required = true)
                                     @RequestParam(value = "cmd", required = true) String cmd) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("MId", MId);
        map.put("cmd", cmd);
        HttpUtil.get("http://39.104.102.191:8000/sendMsg", map);
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        return apiResponse;
    }

    @GetMapping("/exportSingle")
    @ApiOperation("导出当前医院所有设备数据")
    public ApiResponse<String> exportSingle(@ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                            @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                            @ApiParam(name = "operationdate", value = "开始时间", required = true)
                                            @RequestParam(value = "operationdate", required = true) String operationdate,
                                            @ApiParam(name = "type", value = "开始时间", required = true)
                                                @RequestParam(value = "type", required = true) String type,
                                            HttpServletResponse response){
        return equipmentInfoService.exportExcleOne(response,hospitalcode, operationdate,type);
    }
}

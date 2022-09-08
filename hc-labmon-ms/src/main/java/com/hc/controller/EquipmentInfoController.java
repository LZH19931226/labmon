package com.hc.controller;

import com.hc.application.EquipmentInfoApplication;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.QueryInfoModel;
import com.hc.dto.CurveInfoDto;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.MonitorUpsInfoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/equipmentInfo")
public class EquipmentInfoController {

    @Autowired
    private EquipmentInfoApplication equipmentInfoApplication;


    /**
     * 查询所有设备当前值信息
     * @param hospitalCode 医院code
     * @param equipmentTypeId 设备类型id
     * @return
     */
    @GetMapping("/getEquipmentCurrentData")
    @ApiOperation("查询所有设备当前值信息(卡片)")
    public List<MonitorEquipmentDto> getEquipmentCurrentData(@RequestParam("hospitalCode")String hospitalCode,
                                                             @RequestParam("equipmentTypeId")String equipmentTypeId){
        return equipmentInfoApplication.findEquipmentCurrentData(hospitalCode,equipmentTypeId);
    }

    /**
     * 查询设备曲线值
     * @param equipmentNo 设备id
     * @param date 时间
     * @return
     */
    @GetMapping("/getEuipmentCurveInfo")
    @ApiOperation("查询设备曲线值")
    public CurveInfoDto getCurveInfo( @RequestParam("equipmentNo")String equipmentNo,
                                     @RequestParam("date")String date,
                                      @RequestParam("sn")String sn){
        return equipmentInfoApplication.getCurveFirst(equipmentNo,date,sn);
    }

    /**
     * 查询医院信息
     * @param hospitalCode
     * @return
     */
    @GetMapping("/findHospitalInfo")
    @ApiOperation("查询医院信息")
    public HospitalMadel getHospitalInfO(@RequestParam("hospitalCode")String hospitalCode){
        return equipmentInfoApplication.getHospitalInfO(hospitalCode);
    }

    /**
     * 获取当前市电的值
     * @param hospitalCode
     * @return
     */
    @GetMapping("/findCurrentUpsInfo")
    @ApiOperation("查询设备当前UPS")
    public List<MonitorUpsInfoDto> getCurrentUpsInfo(@RequestParam("hospitalCode") String hospitalCode,@RequestParam("equipmentTypeId")String equipmentTypeId){
        return equipmentInfoApplication.getCurrentUpsInfo(hospitalCode,equipmentTypeId);
    }

    /**
     * 查询当前值信息
     * @param equipmentNo
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/findQueryInfo")
    @ApiOperation("查询当前值信息(查询导出)")
    public QueryInfoModel getQueryInfo(@RequestParam("equipmentNo") String equipmentNo,
                                       @RequestParam("startTime") String startTime,
                                       @RequestParam("endTime") String endTime) {
        return equipmentInfoApplication.getQueryInfo(equipmentNo,startTime,endTime);
    }

    @GetMapping("/exportExcel")
    @ApiOperation("查询导出")
    public void exportExcel(@RequestParam("equipmentNo") String equipmentNo, @RequestParam("startDate") String startDate,
                            @RequestParam("endDate") String endDate,HttpServletResponse response){
        equipmentInfoApplication.getQueryResult(equipmentNo,startDate,endDate,response);
    }

    @GetMapping("/exportSingle")
    @ApiOperation("时间点导出")
    public void exportSingle(@RequestParam("hospitalCode") String hospitalCode,
                             @RequestParam("operationDate") String operationDate,
                             @RequestParam("type") String type,
                             HttpServletResponse response){
        equipmentInfoApplication.exportSingle(hospitalCode,operationDate,type,response);
    }

    @GetMapping("/getCurveInfoByMonthTime")
    @ApiOperation("设备通过月份获取每个时间点数据")
    public CurveInfoDto getCurveInfoByMonthTime(@RequestParam("equipmentNo")String equipmentNo,
                                                @RequestParam("operationDate")String operationDate){
        return equipmentInfoApplication.getCurveInfoByMonthTime(equipmentNo,operationDate);
    }

}

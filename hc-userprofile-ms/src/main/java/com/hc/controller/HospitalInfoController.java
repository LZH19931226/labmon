package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.HospitalInfoApplication;
import com.hc.appliction.command.UserScheduleCommand;
import com.hc.command.labmanagement.hospital.HospitalCommand;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.vo.hospital.HospitalInfoVo;
import com.hc.vo.user.UserSchedulingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医院信息控制层
 * @author hc
 */
@RestController
@RequestMapping("/hospitalInfo")
@Api(tags = "医院管理")
public class HospitalInfoController {

    @Autowired
    private HospitalInfoApplication hospitalInfoApplication;

    @PostMapping("/findHospitalInfoList")
    @ApiOperation(value = "获取医院信息列表")
    public Page<HospitalInfoVo> list(@RequestBody HospitalCommand hospitalCommand){
        return hospitalInfoApplication.selectHospitalInfo(hospitalCommand, hospitalCommand.getPageSize(), hospitalCommand.getPageCurrent());
    }

    @PostMapping("/addHospitalInfo")
    @ApiOperation(value = "新增医院信息")
    public void add(@RequestBody  HospitalCommand hospitalCommand){
        hospitalInfoApplication.insertHospitalInfo(hospitalCommand);
    }

    @PutMapping("/editHospitalInfo")
    @ApiOperation(value = "修改医院信息")
    public void edit(@RequestBody HospitalCommand hospitalCommand){
        hospitalInfoApplication.editHospitalInfo(hospitalCommand);
    }

    @DeleteMapping("/{hospitalCode}")
    @ApiOperation(value = "删除医院信息")
    public void delete(@PathVariable(name = "hospitalCode") String hospitalCode){
        hospitalInfoApplication.deleteHospitalInfoByCode(hospitalCode);
    }

    @GetMapping("/getHospitalNameList")
    @ApiOperation(value = "获取医院名称列表")
    public List<HospitalInfoVo> getHospitalNameList(){
        return hospitalInfoApplication.selectHospitalNameList();
    }

    @PostMapping("/addScheduleInfo")
    @ApiOperation(value = "保存排班")
    public void saveSchedule(@RequestBody UserScheduleCommand userScheduleCommand){
            hospitalInfoApplication.saveSchedule(userScheduleCommand);
    }

    @GetMapping("/getMonthlyHospitalSchedule")
    @ApiOperation("获取当月排班信息")
    public List<UserSchedulingVo> searchScByHosMon(@RequestParam("hospitalCode") String hospitalCode,
                                                   @RequestParam("startMonth") String startMonth,
                                                   @RequestParam("endMonth") String endMonth){
        return hospitalInfoApplication.searchScByHosMon(hospitalCode,startMonth,endMonth);
    }

    @PostMapping("/scheduleCopy")
    @ApiOperation("排班信息复制")
    public void editScheduleInfo(@RequestBody UserScheduleCommand userScheduleCommand) {
        hospitalInfoApplication.editScheduleInfo(userScheduleCommand.getHospitalCode(),
                userScheduleCommand.getOldStartTime(),
                userScheduleCommand.getOldEndTime(),
                userScheduleCommand.getNewStartTime(),
                userScheduleCommand.getNewEndTime());
    }

    @GetMapping("/findScheduleWeek")
    @ApiOperation("获取本周的排班信息")
    public List<UserSchedulingVo> selectScheduleWeekByCode(@RequestParam("hospitalCode") String hospitalCode){
        return hospitalInfoApplication.selectScheduleWeekByCode(hospitalCode);
    }

    @GetMapping("/findHospitalInfo")
    @ApiOperation("查询医院信息")
    public HospitalMadel findHospitalInfo(@RequestParam(value = "hospitalCode") String hospitalCode){
        return hospitalInfoApplication.findHospitalInfoByCode(hospitalCode);
    }

    /**
     * 查询医院id集合
     * @return
     */
    @GetMapping("/findHospitalCodeList")
    public List<String> findHospitalCodeList(){
        return hospitalInfoApplication.selectHospitalCodeList();
    }
}

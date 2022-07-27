package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.EquipmentInfoAppApplication;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.HospitalEquipmentDto;
import com.hc.dto.ProbeCurrentInfoDto;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private EquipmentInfoAppApplication equipmentInfoAppApplication;

    @GetMapping("/getEquipmentNum")
    @ApiOperation("获取首页设备数量信息")
    public List<HospitalEquipmentDto> getEquipmentNum(@Param("hospitalCode")String hospitalCode){
        return equipmentInfoAppApplication.getEquipmentNum(hospitalCode);
    }

    @PostMapping("/getCurrentProbeInfo")
    @ApiOperation("获取探头当前值")
    public Page<ProbeCurrentInfoDto> getTheCurrentValueOfTheProbe(@RequestBody ProbeCommand probeCommand){
        return  equipmentInfoAppApplication.getTheCurrentValueOfTheProbe(probeCommand);
    }

}

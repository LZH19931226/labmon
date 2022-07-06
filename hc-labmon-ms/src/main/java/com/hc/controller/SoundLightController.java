package com.hc.controller;

import com.hc.application.SoundLightApplication;
import com.hc.dto.HospitalInfoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/soundLight")
@ApiOperation("声光控制层")
public class SoundLightController {

    @Autowired
    private SoundLightApplication soundLightApplication;

    @GetMapping("/turnOffSoundAndLight")
    @ApiOperation("关闭声光开关")
    public void turnOff(@RequestParam("hospitalCode") String hospitalCode){
        soundLightApplication.turnOff(hospitalCode);
    }

    @GetMapping("/getHospitalInfo")
    @ApiOperation("获取医院是否开启声光报警")
    public HospitalInfoDto getHospitalInfo(@RequestParam("hospitalCode")String hospitalCode){
        return soundLightApplication.getHospitalInfo(hospitalCode);
    }
}

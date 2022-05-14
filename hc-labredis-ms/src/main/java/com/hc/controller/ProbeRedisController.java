package com.hc.controller;

import com.hc.application.ProbeRedisApplication;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/probe")
public class ProbeRedisController {

    @Autowired
    private ProbeRedisApplication probeRedisApplication;

    @GetMapping("/findProbeRedisInfo")
    public InstrumentInfoDto getProbeRedisInfo(@RequestParam String hospitalCode ,@RequestParam String  instrumentNo){
        return probeRedisApplication.getProbeRedisInfo(hospitalCode,instrumentNo);
    }
}

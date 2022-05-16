package com.hc.controller;

import com.hc.application.ProbeRedisApplication;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/probe")
public class ProbeRedisController {

    @Autowired
    private ProbeRedisApplication probeRedisApplication;

    /**
     * 获取探头的缓存信息
     * @param hospitalCode
     * @param instrumentNo
     * @return
     */
    @GetMapping("/findProbeRedisInfo")
    public InstrumentInfoDto getProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode ,@RequestParam("instrumentNo") String  instrumentNo){
        return probeRedisApplication.getProbeRedisInfo(hospitalCode,instrumentNo);
    }

    /**
     * 添加或更新探头信息
     * @param instrumentInfoDto
     */
    @PostMapping("/addProbeRedisInfo")
    public void addProbeRedisInfo(@RequestBody InstrumentInfoDto instrumentInfoDto){
        probeRedisApplication.addProbeRedisInfo(instrumentInfoDto);
    }

    /**
     * 移除探头信息
     * @param hospitalCode
     * @param instrumentNo
     */
    @GetMapping("/deleteProbeRedisInfo")
    public void removeProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode,@RequestParam("instrumentNo") String  instrumentNo){
        probeRedisApplication.removeProbeRedisInfo(hospitalCode,instrumentNo);
    }
}

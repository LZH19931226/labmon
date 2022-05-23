package com.hc.controller;

import com.hc.application.ProbeRedisApplication;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 同步探头信息
     */
    @GetMapping("/probeRedisInfoCache")
    public void probeRedisInfoCache(){
        probeRedisApplication.probeRedisInfoCache();
    }

    /**
     * 获取探头报警数据
     */
    @GetMapping("/getProbeWarnInfo")
    public List<WarningRecordDto> getProbeWarnInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo){
        return probeRedisApplication.getProbeWarnInfo(hospitalCode,instrumentParamConfigNo);
    }

    /**
     * 新增或修改报警记录信息
     * @param warningRecordDto
     */
    @PostMapping("/addProbeWarnInfo")
    public void addProbeWarnInfo(@RequestBody WarningRecordDto warningRecordDto){
        probeRedisApplication.addProbeWarnInfo(warningRecordDto);
    }

    /**
     * 删除探头报警记录信息
     * @param hospitalCode
     * @param instrumentParamConfigNo
     */
    @DeleteMapping("/removeProbeWarnInfo")
    public void removeProbeWarnInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo){
        probeRedisApplication.removeProbeWarnInfo(hospitalCode,instrumentParamConfigNo);
    }

    /**
     * 判断探头报警记录是否存在
     * @param hospitalCode 医院id
     * @param instrumentParamConfigNo
     * @return
     */
    @GetMapping("/hasKey")
    public boolean hasKey(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo){
        return   probeRedisApplication.hasKey(hospitalCode,instrumentParamConfigNo);
    }
}

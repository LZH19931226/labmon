package com.hc.controller;

import com.hc.application.ProbeRedisApplication;
import com.hc.my.common.core.redis.command.ProbeCommand;
import com.hc.my.common.core.redis.command.ProbeRedisCommand;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @ApiOperation("获取探头的缓存信息")
    public InstrumentInfoDto getProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode ,@RequestParam("instrumentNo") String  instrumentNo){
        return probeRedisApplication.getProbeRedisInfo(hospitalCode,instrumentNo);
    }

    /**
     * 添加或更新探头信息
     * @param instrumentInfoDto
     */
    @PostMapping("/addProbeRedisInfo")
    @ApiOperation("添加或更新探头信息")
    public void addProbeRedisInfo(@RequestBody InstrumentInfoDto instrumentInfoDto){
        probeRedisApplication.addProbeRedisInfo(instrumentInfoDto);
    }

    @PostMapping("/bulkUpdateProbeRedisInfo")
    @ApiOperation("批量更新探头信息")
    public void bulkUpdateProbeRedisInfo(@RequestBody ProbeCommand probeCommand){
        probeRedisApplication.bulkUpdateProbeRedisInfo(probeCommand);
    }

    @PostMapping("/bulkGetProbeRedisInfo")
    @ApiOperation("批量获取探头信息")
    public List<InstrumentInfoDto> bulkGetProbeRedisInfo(@RequestBody ProbeCommand probeCommand){
        return probeRedisApplication.bulkGetProbeRedisInfo(probeCommand);
    }

    /**
     * 移除探头信息
     * @param hospitalCode
     * @param instrumentNo
     */
    @GetMapping("/deleteProbeRedisInfo")
    @ApiOperation("移除探头信息")
    public void removeProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode,@RequestParam("instrumentNo") String  instrumentNo){
        probeRedisApplication.removeProbeRedisInfo(hospitalCode,instrumentNo);
    }

    /**
     * 同步探头信息
     */
    @GetMapping("/probeRedisInfoCache")
    @ApiOperation("同步探头信息")
    public void probeRedisInfoCache(){
        probeRedisApplication.probeRedisInfoCache();
    }

    /**
     * 获取探头报警数据
     */
    @GetMapping("/getProbeWarnInfo")
    @ApiOperation("获取探头报警数据")
    public List<WarningRecordDto> getProbeWarnInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo){
        return probeRedisApplication.getProbeWarnInfo(hospitalCode,instrumentParamConfigNo);
    }

    /**
     * 新增或修改报警记录信息
     * @param warningRecordDto
     */
    @PostMapping("/addProbeWarnInfo")
    @ApiOperation("新增或修改报警记录信息")
    public void addProbeWarnInfo(@RequestBody WarningRecordDto warningRecordDto){
        probeRedisApplication.addProbeWarnInfo(warningRecordDto);
    }

    /**
     * 删除探头报警记录信息
     * @param hospitalCode
     * @param instrumentParamConfigNo
     */
    @DeleteMapping("/removeProbeWarnInfo")
    @ApiOperation("删除探头报警记录信息")
    public void removeProbeWarnInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo){
        probeRedisApplication.removeProbeWarnInfo(hospitalCode,instrumentParamConfigNo);
    }

    /**
     * 判断探头报警记录是否存在
     * @param hospitalCode 医院id
     * @param instrumentParamConfigNo 检测信息id
     * @return
     */
    @GetMapping("/hasKey")
    @ApiOperation("判断探头报警记录是否存在")
    public boolean hasKey(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo){
        return   probeRedisApplication.hasKey(hospitalCode,instrumentParamConfigNo);
    }

    @GetMapping("/getProbeCurrentInfo")
    @ApiOperation("获取探头当前值信息")
    public List<ProbeInfoDto> getCurrentProbeValueInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("equipmentNo")String equipmentNo){
        return probeRedisApplication.getCurrentProbeValueInfo(hospitalCode,equipmentNo);
    }

    @PostMapping("/addProbeCurrentInfo")
    @ApiOperation("添加或更新探头当前值")
    public void addCurrentProbeValueInfo(@RequestBody ProbeInfoDto probeInfoDto){
        probeRedisApplication.addCurrentProbeValueInfo(probeInfoDto);
    }

    @PostMapping("/getProbeInBatches")
    @ApiOperation("批量获取探头当前值")
    public Map<String,List<ProbeInfoDto>> getTheCurrentValueOfTheProbeInBatches(@RequestBody ProbeRedisCommand probeRedisCommand){
        return probeRedisApplication.getTheCurrentValueOfTheProbeInBatches(probeRedisCommand);
    }
}

package com.hc.device;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ApplicationName.REDIS)
public interface ProbeRedisApi {

    @GetMapping("/probe/findProbeRedisInfo")
    ApiResponse<InstrumentInfoDto>  getProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode ,@RequestParam("instrumentNo") String  instrumentNo);

    @PostMapping("/probe/addProbeRedisInfo")
    void addProbeRedisInfo(@RequestBody InstrumentInfoDto instrumentInfoDto);

    @GetMapping("/probe/deleteProbeRedisInfo")
    void removeProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode,@RequestParam("instrumentNo") String  instrumentNo);


    /**
     * 获取探头报警数据
     * @param hospitalCode 医院code
     * @param instrumentParamConfigNo 检测信息id
     */
    @GetMapping("/probe/getProbeWarnInfo")
    List<WarningRecordDto> getProbeWarnInfo(@RequestParam("hospitalCode")String hospitalCode, @RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo);

    /**
     * 新增或修改报警记录信息
     * @param warningRecordDto 警告记录对象
     */
    @PostMapping("/probe/addProbeWarnInfo")
    void addProbeWarnInfo(@RequestBody WarningRecordDto warningRecordDto);

    /**
     * 删除探头报警记录信息
     * @param hospitalCode 医院id
     * @param instrumentParamConfigNo 检测信息id
     */
    @DeleteMapping("/probe/removeProbeWarnInfo")
    void removeProbeWarnInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo);
}


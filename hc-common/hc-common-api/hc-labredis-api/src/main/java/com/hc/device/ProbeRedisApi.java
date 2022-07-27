package com.hc.device;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ApplicationName.REDIS)
public interface ProbeRedisApi {

    /**
     * 获取探头redis信息
     * @param hospitalCode 医院id
     * @param instrumentNo 探头id
     * @return
     */
    @GetMapping("/probe/findProbeRedisInfo")
    ApiResponse<InstrumentInfoDto>  getProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode ,@RequestParam("instrumentNo") String  instrumentNo);

    /**
     * 添加或修改探头redis信息
     * @param instrumentInfoDto 探头信息
     */
    @PostMapping("/probe/addProbeRedisInfo")
    void addProbeRedisInfo(@RequestBody InstrumentInfoDto instrumentInfoDto);

    /**
     * 移除探头缓存信息
     * @param hospitalCode 医院id
     * @param instrumentNo 探头id
     */
    @GetMapping("/probe/deleteProbeRedisInfo")
    void removeProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode,@RequestParam("instrumentNo") String  instrumentNo);

    /**
     * 获取探头报警数据
     * @param hospitalCode 医院code
     * @param instrumentParamConfigNo 检测信息id
     */
    @GetMapping("/probe/getProbeWarnInfo")
    ApiResponse<List<WarningRecordDto>> getProbeWarnInfo(@RequestParam("hospitalCode")String hospitalCode,
                                            @RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo);

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
    void removeProbeWarnInfo(@RequestParam("hospitalCode")String hospitalCode,
                             @RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo);

    /**
     * 判断探头报警记录是否存在
     * @param hospitalCode 医院id
     * @param instrumentParamConfigNo 检测信息id
     * @return
     */
    @GetMapping("/probe/hasKey")
     ApiResponse<Boolean> hasKey(@RequestParam("hospitalCode")String hospitalCode,
                                 @RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo);

    /**
     * 获取探头当天值
     * @param hospitalCode 医院id
     * @param equipmentNo 设备id
     * @return
     */
    @GetMapping("/probe/getProbeCurrentInfo")
    ApiResponse<List<ProbeInfoDto>> getCurrentProbeValueInfo(@RequestParam("hospitalCode")String hospitalCode, @RequestParam("equipmentNo")String equipmentNo);

    /**
     * 添加或更新探头当前值
     * @param probeInfoDto 探头当前值对象
     */
    @PostMapping("/probe/addProbeCurrentInfo")
    void addCurrentProbeValueInfo(@RequestBody ProbeInfoDto probeInfoDto);
}


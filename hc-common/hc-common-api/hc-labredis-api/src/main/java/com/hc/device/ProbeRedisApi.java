package com.hc.device;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = ApplicationName.REDIS)
public interface ProbeRedisApi {

    @GetMapping("/probe/findProbeRedisInfo")
    ApiResponse<InstrumentInfoDto>  getProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode ,@RequestParam("instrumentNo") String  instrumentNo);

    @PostMapping("/probe/addProbeRedisInfo")
    void addProbeRedisInfo(@RequestBody InstrumentInfoDto instrumentInfoDto);

    @GetMapping("/probe/deleteProbeRedisInfo")
    void removeProbeRedisInfo(@RequestParam("hospitalCode") String hospitalCode,@RequestParam("instrumentNo") String  instrumentNo);

}

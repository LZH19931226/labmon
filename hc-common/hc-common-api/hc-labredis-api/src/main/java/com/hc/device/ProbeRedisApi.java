package com.hc.device;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationName.REDIS)
public interface ProbeRedisApi {

    @GetMapping("/probe/findProbeRedisInfo")
    ApiResponse<InstrumentInfoDto>  getProbeRedisInfo(@RequestParam String hospitalCode , @RequestParam String  instrumentNo);
}

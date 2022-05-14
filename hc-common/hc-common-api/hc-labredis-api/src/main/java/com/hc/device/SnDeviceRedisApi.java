package com.hc.device;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationName.REDIS)
public interface SnDeviceRedisApi {

    @PostMapping("/snDevice/updateSnDeviceDtoSync")
    void updateSnDeviceDtoSync(@RequestBody SnDeviceDto snDeviceDto);

    @GetMapping("/snDevice/getSnDeviceDto")
    @ApiOperation("获取设备缓存信息")
    ApiResponse<SnDeviceDto> getSnDeviceDto(@RequestParam("sn") String sn);

    @GetMapping("/snDevice/deleteSnDeviceDto")
    void deleteSnDeviceDto(@RequestParam("sn")String sn);
}

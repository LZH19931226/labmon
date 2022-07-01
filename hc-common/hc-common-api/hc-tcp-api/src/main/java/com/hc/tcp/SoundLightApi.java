package com.hc.tcp;

import com.hc.my.common.core.bean.ApplicationName;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationName.TCP)
public interface SoundLightApi {

    @GetMapping("/tcp/sendMsg")
    @ApiOperation("暂时只支持mt600/mt1100设备查询对应服务器通道信息")
    void sendMsg(@RequestParam("sn") String sn,@RequestParam("message") String message);
}

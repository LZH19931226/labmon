package com.hc.tcp;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationName.REDIS)
public interface TcpClientApi {

    @GetMapping("/tcpclient/addDeviceChannel")
    void addDeviceChannel(@RequestBody ParamaterModel paramaterModel);

    @GetMapping("/tcpclient/deleteDeviceChannel")
    void deleteDeviceChannel(@RequestParam("sn") String sn, @RequestParam("cmdId") String cmdId);

    @GetMapping("/tcpclient/getSnBychannelId")
    ApiResponse<ParamaterModel> getSnBychannelId(@RequestParam("sn") String sn,@RequestParam("cmdId") String cmdId);
}

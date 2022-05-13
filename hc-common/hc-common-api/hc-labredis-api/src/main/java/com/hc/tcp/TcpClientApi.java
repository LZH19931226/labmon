package com.hc.tcp;

import com.hc.my.common.core.bean.ApplicationName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationName.REDIS)
public interface TcpClientApi {

    @GetMapping("/tcpclient/addDeviceChannel")
    void addDeviceChannel(@RequestParam("sn") String sn, @RequestParam("channelId") String channelId);

    @GetMapping("/tcpclient/addChannelDevice")
    void addChannelDevice(@RequestParam("sn") String sn, @RequestParam("channelId") String channelId);

    @GetMapping("/tcpclient/deleteDeviceChannel")
    void deleteDeviceChannel(@RequestParam("sn") String sn, @RequestParam("channelId") String channelId);

    @GetMapping("/tcpclient/getSnBychannelId")
    String getSnBychannelId(@RequestParam("channelId") String channelId);
}

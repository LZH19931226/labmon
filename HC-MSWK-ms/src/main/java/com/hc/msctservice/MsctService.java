package com.hc.msctservice;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by xxf on 2019-03-30.
 */
@Component
@FeignClient("HC-MSCT-ms")
public interface MsctService {

    @GetMapping("/test2")
    @ApiOperation("测试语音接口")
    String test2(@RequestParam("mobile")String mobile, @RequestParam("instrumentname")String instrumentname) ;



}

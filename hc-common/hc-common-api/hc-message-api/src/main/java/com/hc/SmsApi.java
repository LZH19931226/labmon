package com.hc;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = ApplicationName.MESSAGE)
public interface SmsApi {

    @GetMapping("/sms/code")
    ApiResponse<String> senMessagecode(@RequestParam("phonenum")String phontnum, @RequestParam("code")String code);

    @GetMapping("/sms/upsRemind")
    void upsRemind(@RequestParam("phonenum")String phontnum, @RequestParam("eqname")String eqname);

}

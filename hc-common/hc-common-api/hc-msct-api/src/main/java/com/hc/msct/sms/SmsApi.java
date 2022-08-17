//package com.hc.msct.sms;
//
//import com.hc.my.common.core.bean.ApiResponse;
//import com.hc.my.common.core.bean.ApplicationName;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//
//@FeignClient(value = ApplicationName.MSCT)
//public interface SmsApi {
//
//    @GetMapping("/sms/code")
//    ApiResponse<String> senMessagecode(@RequestParam("phonenum")String phontnum, @RequestParam("code")String code);
//
//}

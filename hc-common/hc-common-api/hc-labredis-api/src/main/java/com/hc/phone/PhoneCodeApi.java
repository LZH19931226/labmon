package com.hc.phone;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hc
 */
@FeignClient(name = ApplicationName.REDIS)
public interface PhoneCodeApi {

    @GetMapping("/phone/getPhoneCode")
    @ApiOperation("获取手机验证码")
    ApiResponse<String> getPhoneCode(@RequestParam("phoneNum") String phoneNum);

    @GetMapping("/phone/addPhoneCode")
    @ApiOperation("生成手机验证码")
    void addPhoneCode(@RequestParam("phoneNum")String phoneNum ,@RequestParam("code")String code);
}

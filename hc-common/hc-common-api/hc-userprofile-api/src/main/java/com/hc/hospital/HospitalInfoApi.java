package com.hc.hospital;

import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationName.USER)
public interface HospitalInfoApi {

    @GetMapping(value = "/hospitalInfo/findHospitalInfo")
    ApiResponse<HospitalMadel> findHospitalInfo(@RequestParam(value = "hospitalCode") String hospitalCode);

    @GetMapping(value = "/userBackInfo/findUserInfo")
    ApiResponse<UserBackModel> findUserInfo(@RequestParam(value = "userid")String userid);

}

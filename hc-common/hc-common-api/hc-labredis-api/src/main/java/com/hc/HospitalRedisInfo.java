package com.hc;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationName.REDIS)
public interface HospitalRedisInfo {

    /**
     * 获取医院信息
     * @param hospitalCode 医院id
     * @return 医院信息
     */
    @GetMapping("/hospitalRedisInfo/findHospitalInfo")
    ApiResponse<HospitalInfoDto> findHospitalRedisInfo(@RequestParam("hospitalCode")String hospitalCode);
}

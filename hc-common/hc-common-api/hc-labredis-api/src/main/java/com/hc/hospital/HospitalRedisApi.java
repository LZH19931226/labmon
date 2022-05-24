package com.hc.hospital;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = ApplicationName.REDIS)
public interface HospitalRedisApi {

    /**
     * 获取医院信息
     * @param hospitalCode 医院id
     * @return 医院信息
     */
    @GetMapping("/hospitalRedis/findHospitalInfo")
    ApiResponse<HospitalInfoDto> findHospitalRedisInfo(@RequestParam("hospitalCode")String hospitalCode);

    /**
     * 新增医院缓存信息
     * @param hospitalInfoDto 医院缓存对象
     */
    @PostMapping("/hospitalRedis/insertHospitalRedisInfo")
    void addHospitalRedisInfo(@RequestBody HospitalInfoDto hospitalInfoDto);

    /**
     * 移除医院缓存信息
     * @param hospitalCode 医院id
     */
    @DeleteMapping("/hospitalRedis/removeHospitalRedisInfo")
    void removeHospitalRedisInfo(@RequestParam("hospitalCode")String hospitalCode);
}

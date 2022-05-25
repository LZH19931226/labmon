package com.hc.hospital;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationName.REDIS)
public interface HospitalEquipmentTypeIdApi{

    /**
     * 获取医院设备类型缓存信息
     * @param hospitalCode 医院id
     * @param hospitalEquipmentTypeId 医院设备类型id
     * @return 医院设备类型缓存信息
     */
    @GetMapping("/getHospitalEquipmentTypeRedisInfo")
    ApiResponse<HospitalEquipmentTypeInfoModel> findHospitalEquipmentTypeRedisInfo(@RequestParam("hospitalCode")String hospitalCode,
                                                                                  @RequestParam("hospitalEquipmentTypeId")String hospitalEquipmentTypeId);
}

package com.hc.hospital;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ApplicationName.REDIS)
public interface HospitalEquipmentTypeIdApi{

    /**
     * 获取医院设备类型缓存信息
     * @param hospitalCode 医院id
     * @param hospitalEquipmentTypeId 医院设备类型id
     * @return 医院设备类型缓存信息
     */
    @GetMapping("/hospitalEquipmentType/getHospitalEquipmentTypeRedisInfo")
    ApiResponse<HospitalEquipmentTypeInfoDto> findHospitalEquipmentTypeRedisInfo(@RequestParam("hospitalCode")String hospitalCode,
                                                                                 @RequestParam("hospitalEquipmentTypeId")String hospitalEquipmentTypeId);

    /**
     * 新增或修改医院设备类型缓存信息
     * @param hospitalEquipmentTypeInfoModel 医院设备类型缓存信息
     */
    @PostMapping("/hospitalEquipmentType/addHospitalEquipmentTypeRedisInfo")
    void addHospitalEquipmentTypeRedisInfo(@RequestBody HospitalEquipmentTypeInfoDto hospitalEquipmentTypeInfoModel);

    /**
     * 移除医院设备类型缓存信息
     * @param hospitalCode 医院id
     * @param hospitalEquipmentTypeId 医院设备类型id
     */
    @DeleteMapping("/hospitalEquipmentType/removeHospitalEquipmentTypeRedisInfo")
    void removeHospitalEquipmentTypeRedisInfo(@RequestParam("hospitalCode")String hospitalCode,
                                                     @RequestParam("hospitalEquipmentTypeId")String hospitalEquipmentTypeId);

    /**
     * 批量换取设备类型
     * @return
     */
    @GetMapping("/hospitalEquipmentType/bulkAcquisitionEqType")
    @ApiOperation("批量换取设备类型")
    ApiResponse<List<HospitalEquipmentTypeInfoDto>> bulkAcquisitionEqType(@RequestParam("hospitalCode") String hospitalCode);
}

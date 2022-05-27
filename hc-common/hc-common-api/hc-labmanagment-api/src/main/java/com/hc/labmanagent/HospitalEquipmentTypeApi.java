package com.hc.labmanagent;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = ApplicationName.LABMANAGENMENT)
public interface HospitalEquipmentTypeApi {

    /**
     * 获取所有的设备类型信息
     * @return
     */
    @GetMapping("/hospitalequimentType/getAllHospitalEquipmentTypeInfo")
    @ApiOperation("获取所有的设备类型信息")
    ApiResponse<List<HospitalEquipmentTypeInfoDto>> getAllHospitalEquipmentTypeInfo();
}

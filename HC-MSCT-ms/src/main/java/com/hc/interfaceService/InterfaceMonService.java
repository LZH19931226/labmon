package com.hc.interfaceService;

import com.hc.bean.ApiResponse;
import com.hc.entity.Monitorequipment;
//import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by 15350 on 2020/5/26.
 */
@FeignClient("HC-MON-ms")
@Component
public interface InterfaceMonService {


    @GetMapping("api/equipmentInfo/getEquipmentInfoByHospitalcode")
    ApiResponse<List<Monitorequipment>> getEquipmentInfoByHospitalcode();


}

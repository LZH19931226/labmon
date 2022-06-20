package com.hc.labmanagent;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ApplicationName.LABMANAGENMENT)
public interface MonitorEquipmentApi {

    /**
     * 获取所有的监控设备信息
     * @return
     */
    @GetMapping("/equipmentInfo/getAllMonitorEquipmentInfo")
    ApiResponse<List<SnDeviceDto>> getAllMonitorEquipmentInfo();

    /**
     * 获取医院ups的设备no集合
     * @param hospitalCode
     * @return
     */
    @GetMapping("/equipmentInfo/getHospitalUPS")
    ApiResponse<List<String>> getEquipmentNoList(@RequestParam("hospitalCode")String hospitalCode);

    /**
     * 查询设备信息
     * @param equipmentNo
     * @return
     */
    @GetMapping("/equipmentInfo/getMonitorEquipmentInfoByEno")
    ApiResponse<SnDeviceDto> selectMonitorEquipmentInfoByEno(@RequestParam("equipmentNo")String equipmentNo);
}

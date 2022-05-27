package com.hc.labmanagent;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = ApplicationName.LABMANAGENMENT)
public interface MonitorEquipmentApi {

    @GetMapping("/equipmentInfo/getAllMonitorEquipmentInfo")
    @ApiOperation("获取所有的监控设备信息")
    ApiResponse<List<SnDeviceDto>> getAllMonitorEquipmentInfo();
}

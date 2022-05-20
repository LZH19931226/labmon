package com.hc.device;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.command.EquipmentInfoCommand;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ApplicationName.REDIS)
public interface SnDeviceRedisApi {

    @PostMapping("/snDevice/updateSnDeviceDtoSync")
    void updateSnDeviceDtoSync(@RequestBody SnDeviceDto snDeviceDto);

    @GetMapping("/snDevice/getSnDeviceDto")
    @ApiOperation("获取设备缓存信息")
    ApiResponse<SnDeviceDto> getSnDeviceDto(@RequestParam("sn") String sn);

    @GetMapping("/snDevice/deleteSnDeviceDto")
    @ApiOperation("删除设备信息缓存")
    void deleteSnDeviceDto(@RequestParam("sn")String sn);

    @PostMapping("/snDevice/updateSnCurrentInfo")
    @ApiOperation("新增更新设备当前值信息")
    void  updateSnCurrentInfo(@RequestBody MonitorequipmentlastdataDto monitorequipmentlastdataDto);

    @DeleteMapping("/snDevice/deleteCurrentDataInfo")
    @ApiOperation("删除设备当前值")
    void deleteCurrentInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("equipmentNo")String equipmentNo);

    @GetMapping("/snDevice/getCurrentDataInfo")
    @ApiOperation("获取设备当前值")
    ApiResponse<List<MonitorequipmentlastdataDto>> getCurrentDataInfo(@RequestParam("hospitalCode")String hospitalCode,
                                                                      @RequestParam("equipmentNo")String equipmentNo);
    @PostMapping("/snDevice/getTheCurrentValueOfTheDeviceInBatches")
    @ApiOperation("批量获取设备当前值")
    ApiResponse<List<MonitorequipmentlastdataDto>> getTheCurrentValueOfTheDeviceInBatches(@RequestBody EquipmentInfoCommand equipmentInfoCommand);
}

package com.hc.application;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hc.constants.LabMonEnumError;
import com.hc.device.SnDeviceRedisApi;
import com.hc.dto.InstrumentMonitorInfoDto;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.MonitorinstrumentDto;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.command.EquipmentInfoCommand;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.service.EquipmentInfoService;
import com.hc.service.InstrumentMonitorInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hc
 */
@Component
public class EquipmentInfoApplication {

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private InstrumentMonitorInfoService instrumentMonitorInfoService;

    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;

    /**
     * 查询所有设备当前值信息
     * @param hospitalCode 医院id
     * @param equipmentTypeId 设备类型id
     * @return 监控设备vo对象
     */
    public List<MonitorEquipmentDto> findEquipmentCurrentData(String hospitalCode, String equipmentTypeId) {

        //获取监控设备的信息
        List<MonitorEquipmentDto> monitorEquipmentDtoList = equipmentInfoService.getEquipmentInfoByCodeAndTypeId(hospitalCode,equipmentTypeId);
        if(CollectionUtils.isEmpty(monitorEquipmentDtoList)){
            throw new IedsException(LabMonEnumError.DEVICE_INFORMATION_NOT_FOUND.getMessage());
        }
        List<String> equipmentNoList = monitorEquipmentDtoList.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
        Map<String, List<MonitorEquipmentDto>> monitorEquipmentMap = monitorEquipmentDtoList.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmentno));

        //获取仪器监控的信息
        List<InstrumentMonitorInfoDto> instrumentMonitorInfoDtoList  = instrumentMonitorInfoService.selectInstrumentMonitorInfoByEqNo(equipmentNoList);
        Map<String, List<InstrumentMonitorInfoDto>> InstrumentMonitorInfoDtoMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(instrumentMonitorInfoDtoList)){
            InstrumentMonitorInfoDtoMap = instrumentMonitorInfoDtoList.stream().collect(Collectors.groupingBy(InstrumentMonitorInfoDto::getEquipmentno));
        }

        //查询lowlimit的值
        List<MonitorinstrumentDto> monitorEquipmentLowLimitList = equipmentInfoService.getLowLimitList(equipmentNoList);
        Map<String, List<MonitorinstrumentDto>> monitorEquipmentLowMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(monitorEquipmentLowLimitList)){
            monitorEquipmentLowMap = monitorEquipmentLowLimitList.stream().collect(Collectors.groupingBy(MonitorinstrumentDto::getEquipmentno));
        }

        EquipmentInfoCommand equipmentInfoCommand = new EquipmentInfoCommand();
        equipmentInfoCommand.setEquipmentNoList(equipmentNoList);
        equipmentInfoCommand.setHospitalCode(hospitalCode);
        //查询设备号当前的信息值
        List<MonitorequipmentlastdataDto> resultList = snDeviceRedisApi.getTheCurrentValueOfTheDeviceInBatches(equipmentInfoCommand).getResult();

        Map<String, List<MonitorequipmentlastdataDto>> resultMap = resultList.stream().collect(Collectors.groupingBy(MonitorequipmentlastdataDto::getEquipmentno));

        for (MonitorEquipmentDto monitorEquipmentDto : monitorEquipmentDtoList) {
            String equipmentNo = monitorEquipmentDto.getEquipmentno();
            List<MonitorEquipmentDto> monitorEquipmentDtos = monitorEquipmentMap.get(equipmentNo);
            if(CollectionUtils.isNotEmpty(monitorEquipmentDtos)){
                monitorEquipmentDto.setSn(monitorEquipmentDtos.get(0).getSn());
            }
            List<MonitorequipmentlastdataDto> monitorequipmentlastdataDtos = resultMap.get(equipmentNo);
            if(CollectionUtils.isNotEmpty(monitorequipmentlastdataDtos)){
                MonitorequipmentlastdataDto monitorequipmentlastdataDto = monitorequipmentlastdataDtos.get(0);
                if(StringUtils.isNotBlank(monitorequipmentlastdataDto.getCurrentdoorstate())){
                    // 查找这个设备下开关量的最低值
                    String lowlimit = monitorEquipmentLowMap.get(equipmentNo).get(0).getLowlimit();
                    monitorEquipmentDto.setLowlimit(lowlimit);
                }
                monitorEquipmentDto.setMonitorequipmentlastdataDto(monitorequipmentlastdataDto);
            }
            List<InstrumentMonitorInfoDto> resultIMList = InstrumentMonitorInfoDtoMap.get(equipmentNo);
            if(CollectionUtils.isNotEmpty(resultIMList)){
                monitorEquipmentDto.setInstrumentMonitorInfoDtoList(resultIMList);
            }
        }
        return monitorEquipmentDtoList;
    }

    /**
     * 类型转化
     * @param currentDataInfo
     * @return
     */
    private MonitorequipmentlastdataDto listToObject(List<MonitorequipmentlastdataDto> currentDataInfo) {
        if(CollectionUtils.isEmpty(currentDataInfo)){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        for (MonitorequipmentlastdataDto monitorequipmentlastdataDto : currentDataInfo) {
            Map<String,Object> map1 = JSON.parseObject(JSON.toJSONString(monitorequipmentlastdataDto),new TypeReference<Map<String,Object>>(){});
            map.putAll(map1);
        }
        return JSON.parseObject(JSON.toJSONString(map), new TypeReference<MonitorequipmentlastdataDto>(){});
    }
}

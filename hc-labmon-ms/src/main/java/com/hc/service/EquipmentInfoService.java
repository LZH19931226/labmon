package com.hc.service;

import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.MonitorinstrumentDto;

import java.util.List;

public interface EquipmentInfoService {

    /**
     * 查询所有设备当前值信息
     * @param hospitalCode 医院id
     * @param equipmentTypeId 设备类型id
     * @return 监控设备集合
     */
    List<MonitorEquipmentDto> getEquipmentInfoByCodeAndTypeId(String hospitalCode, String equipmentTypeId);

    List<MonitorinstrumentDto> getSns(List<String> equipmentNoList);

    String getLowlimit(String equipmentNo);
}

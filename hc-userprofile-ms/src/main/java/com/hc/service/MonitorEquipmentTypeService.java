package com.hc.service;

import com.hc.dto.MonitorEquipmentTypeDto;

import java.util.List;

/**
 * @author hc
 */
public interface MonitorEquipmentTypeService {
    /**
     * 获取监控设备类型列表
     * @return
     */
    List<MonitorEquipmentTypeDto> getMonitorEquipmentTypeList( );

}

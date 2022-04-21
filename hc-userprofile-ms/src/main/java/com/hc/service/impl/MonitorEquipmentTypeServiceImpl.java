package com.hc.service.impl;

import com.hc.dto.MonitorEquipmentTypeDto;
import com.hc.repository.MonitorEquipmentTypeRepository;
import com.hc.service.MonitorEquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hc
 */
@Service
public class MonitorEquipmentTypeServiceImpl implements MonitorEquipmentTypeService {

    @Autowired
    private MonitorEquipmentTypeRepository monitorEquipmentTypeRepository;

    /**
     * 获取监控设备类型列表
     * @return
     */
    @Override
    public List<MonitorEquipmentTypeDto> getMonitorEquipmentTypeList( ) {
        return monitorEquipmentTypeRepository.getMonitorEquipmentTypeList();
    }
}

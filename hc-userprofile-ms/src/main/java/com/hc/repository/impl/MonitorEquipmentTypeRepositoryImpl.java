package com.hc.repository.impl;

import com.hc.dto.MonitorEquipmentTypeDto;
import com.hc.infrastructure.dao.MonitorEquipmentTypeDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorEquipmentTypePo;
import com.hc.repository.MonitorEquipmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hc
 */
@Repository
public class MonitorEquipmentTypeRepositoryImpl implements MonitorEquipmentTypeRepository {

    @Autowired
    private MonitorEquipmentTypeDao monitorEquipmentTypeDao;

    /**
     * 获取监控设备类型列表
     *
     * @return
     */
    @Override
    public List<MonitorEquipmentTypeDto> getMonitorEquipmentTypeList() {
        List<MonitorEquipmentTypePo> monitorEquipmentTypePos = monitorEquipmentTypeDao.selectList(null);
        return BeanConverter.convert(monitorEquipmentTypePos,MonitorEquipmentTypeDto.class);
    }
}

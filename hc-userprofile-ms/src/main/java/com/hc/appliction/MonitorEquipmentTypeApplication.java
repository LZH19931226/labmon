package com.hc.appliction;

import com.hc.dto.MonitorEquipmentTypeDto;
import com.hc.service.MonitorEquipmentTypeService;
import com.hc.vo.equimenttype.MonitorEquipmentTypeVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hc
 */
@Component
public class MonitorEquipmentTypeApplication {

    @Autowired
    private MonitorEquipmentTypeService monitorEquipmentTypeService;

    /**
     * 获取监控设备类型列表
     * @return
     */
    public List<MonitorEquipmentTypeVo> getMonitorEquipmentTypeList( ) {
        List<MonitorEquipmentTypeDto> dtoList =  monitorEquipmentTypeService.getMonitorEquipmentTypeList();
        List<MonitorEquipmentTypeVo> voList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dtoList)){
            dtoList.forEach(res->{
                MonitorEquipmentTypeVo build = MonitorEquipmentTypeVo.builder()
                        .equipmentTypeId(res.getEquipmentTypeId())
                        .equipmentTypeName(res.getEquipmentTypeName())
                        .build();
                voList.add(build);
            });
        }
        return voList;
    }
}

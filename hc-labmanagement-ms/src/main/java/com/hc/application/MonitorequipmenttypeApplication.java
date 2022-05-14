package com.hc.application;

import com.hc.dto.MonitorequipmenttypeDTO;
import com.hc.service.MonitorequipmenttypeService;
import com.hc.vo.equimenttype.MonitorEquipmentTypeVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class MonitorequipmenttypeApplication {

    @Autowired
    private MonitorequipmenttypeService monitorequipmenttypeService;


    public List<MonitorEquipmentTypeVo> getAllmonitorequipmentType() {
        List<MonitorequipmenttypeDTO> monitorEquipmentTypeLists = monitorequipmenttypeService.getAllmonitorequipmentType();
        if (CollectionUtils.isNotEmpty(monitorEquipmentTypeLists)) {
            List<MonitorEquipmentTypeVo> monitorequipmenttypeVoList = new ArrayList<>();
            monitorEquipmentTypeLists.forEach(s -> {
                        monitorequipmenttypeVoList.add(
                                MonitorEquipmentTypeVo.builder()
                                        .equipmentTypeId(s.getEquipmenttypeid())
                                        .equipmentTypeName(s.getEquipmenttypename())
                                        .build());
                    }
            );
            return monitorequipmenttypeVoList;
        }
        return null;
    }
}

package com.hc.application;

import com.hc.dto.MonitorequipmenttypeDTO;
import com.hc.service.MonitorequipmenttypeService;
import com.hc.vo.equimenttype.MonitorequipmenttypeVo;
import org.apache.commons.collections4.CollectionUtils;
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


    public List<MonitorequipmenttypeVo> getAllmonitorequipmentType() {
        List<MonitorequipmenttypeDTO> monitorequipmenttypeDTOS = monitorequipmenttypeService.getAllmonitorequipmentType();
        if (CollectionUtils.isNotEmpty(monitorequipmenttypeDTOS)) {
            List<MonitorequipmenttypeVo> monitorequipmenttypeVoList = new ArrayList<>();
            monitorequipmenttypeDTOS.forEach(s -> {
                        monitorequipmenttypeVoList.add(
                                MonitorequipmenttypeVo.builder()
                                        .equipmenttypeid(s.getEquipmenttypeid())
                                        .equipmenttypename(s.getEquipmenttypename())
                                        .build());
                    }

            );
            return monitorequipmenttypeVoList;
        }
        return null;
    }
}

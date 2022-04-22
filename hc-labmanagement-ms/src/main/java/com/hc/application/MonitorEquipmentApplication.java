package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.service.MonitorEquipmentService;
import com.hc.vo.equimenttype.MonitorEquipmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hc
 */
@Component
public class MonitorEquipmentApplication {

   @Autowired
   private MonitorEquipmentService monitorEquipmentService;

    /**
     * 分页获取监控设备信息
     * @param monitorEquipmentCommand 监控设备
     * @return
     */
   public List<MonitorEquipmentVo> getEquipmentInfoList(MonitorEquipmentCommand monitorEquipmentCommand) {
       Page page = new Page(monitorEquipmentCommand.getPageCurrent(),monitorEquipmentCommand.getPageSize());
       List<MonitorEquipmentDto> dtoList = monitorEquipmentService.getEquipmentInfoList(page,monitorEquipmentCommand);
       List<MonitorEquipmentVo> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            dtoList.forEach(res->{
                MonitorEquipmentVo build = MonitorEquipmentVo.builder()
                        .equipmentNo(res.getEquipmentNo())
                        .equipmentBrand(res.getEquipmentBrand())
                        .equipmentName(res.getEquipmentName())
                        .equipmentTypeId(res.getEquipmentTypeId())
                        .hospitalCode(res.getHospitalCode())
                        .hospitalName(res.getHospitalName())
                        .alwaysAlarm(res.getAlwaysAlarm())
                        .clientVisible(res.getClientVisible())
                        .equipmentTypeName(res.getEquipmentTypeName())
                        .sn(res.getSn())
                        .sort(res.getSort())
                        .instrumentTypeName(res.getInstrumentTypeName())
                        .warningTimeList(res.getWarningTimeList())
                        .build();
                list.add(build);
            });

        }
      return list;
   }
}

package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.dto.InstrumentmonitorDTO;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.service.InstrumentmonitorService;
import com.hc.service.MonitorEquipmentService;
import com.hc.vo.equimenttype.InstrumentmonitorVo;
import com.hc.vo.equimenttype.MonitorEquipmentVo;
import com.hc.vo.equimenttype.MonitorinstrumenttypeVo;
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
public class MonitorEquipmentApplication {

   @Autowired
   private MonitorEquipmentService monitorEquipmentService;
   @Autowired
   private InstrumentmonitorService instrumentmonitorService;

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

    public void addMonitorEquipment(MonitorEquipmentCommand monitorEquipmentCommand) {
    }

    public void updateMonitorEquipment(MonitorEquipmentCommand monitorEquipmentCommand) {
    }

    public void deleteMonitorEquipment(String equipmentId) {
    }

    public List<MonitorinstrumenttypeVo> selectMonitorEquipmentType(String instrumenttypeid) {
        List<MonitorinstrumenttypeDTO> monitorinstrumenttypeDTOS = instrumentmonitorService.selectMonitorEquipmentType(instrumenttypeid);
        if (CollectionUtils.isNotEmpty(monitorinstrumenttypeDTOS)){
            List<MonitorinstrumenttypeVo> monitorinstrumenttypeVos  = new ArrayList<>();
            monitorinstrumenttypeDTOS.forEach(s->{
                monitorinstrumenttypeVos.add(
                        MonitorinstrumenttypeVo.builder()
                                .instrumenttypeid(s.getInstrumenttypeid())
                                .instrumenttypename(s.getInstrumenttypename())
                                .instrumentmonitorVos(buildInstrumentmonitorVO(s.getInstrumentmonitorDTOS()))
                                .build()
                );
            });
            return monitorinstrumenttypeVos;
        }
        return null;
    }

    public List<InstrumentmonitorVo> buildInstrumentmonitorVO(List<InstrumentmonitorDTO> instrumentmonitorDTOS){
       if (CollectionUtils.isNotEmpty(instrumentmonitorDTOS)){
           List<InstrumentmonitorVo> instrumentmonitorVos  = new ArrayList<>();
           instrumentmonitorDTOS.forEach(s->{
               instrumentmonitorVos.add(
                       InstrumentmonitorVo.builder()
                               .instrumentconfigid(s.getInstrumentconfigid())
                               .instrumentconfigname(s.getInstrumentconfigname())
                               .lowlimit(s.getLowlimit())
                               .saturation(s.getSaturation())
                               .highlimit(s.getHighlimit())
                               .build()
               );
           });
          return instrumentmonitorVos;
       }
       return null;
    }

}

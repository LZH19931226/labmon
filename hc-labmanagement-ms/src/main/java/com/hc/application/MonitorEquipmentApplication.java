package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.dto.*;
import com.hc.my.common.core.constant.enums.MonitorinstrumentEnumCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.service.*;
import com.hc.vo.equimenttype.InstrumentmonitorVo;
import com.hc.vo.equimenttype.MonitorEquipmentVo;
import com.hc.vo.equimenttype.MonitorinstrumenttypeVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

   @Autowired
   private MonitorinstrumentService monitorinstrumentService;

   @Autowired
   private MonitorequipmentwarningtimeService monitorequipmentwarningtimeService;

   @Autowired
   private InstrumentparamconfigService instrumentparamconfigService;


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

    /**
     * 添加设备
     * @param monitorEquipmentCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMonitorEquipment(MonitorEquipmentCommand monitorEquipmentCommand) {
        //根据sn和医院code查询sn是否重复
        String sn = monitorEquipmentCommand.getSn();
        String hospitalCode = monitorEquipmentCommand.getHospitalCode();
        Integer integer = monitorinstrumentService.selectCount(new MonitorinstrumentDTO().setSn(sn).setHospitalcode(hospitalCode));
        if(integer > 0){
            throw new IedsException(MonitorinstrumentEnumCode.FAILED_TO_ADD_DEVICE.getMessage());
        }
        System.out.println("插入到instrumentmonitor表中........");
        //根据探头信息和设备信息插入到instrumentmonitor表中
        MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = monitorEquipmentCommand.getMonitorinstrumenttypeDTO();
        if(!ObjectUtils.isEmpty(monitorinstrumenttypeDTO)){
            List<InstrumentmonitorDTO> instrumentmonitorDTOS = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
            if(CollectionUtils.isNotEmpty(instrumentmonitorDTOS)){
                instrumentmonitorDTOS.forEach(res->{
                    instrumentmonitorService.insertInstrumentmonitorInfo(res);
                });
            }
        }
        System.out.println("插入成功");
        System.out.println("插入到monitorequipment表中........");
        //插入到monitorequipment表中
        MonitorEquipmentDto monitorEquipmentDto = new MonitorEquipmentDto()
                .setHospitalCode(monitorEquipmentCommand.getHospitalCode())
                .setEquipmentBrand(monitorEquipmentCommand.getEquipmentBrand())
                .setClientVisible(monitorEquipmentCommand.getClientVisible())
                .setEquipmentName(monitorEquipmentCommand.getEquipmentName())
                .setEquipmentTypeId(monitorEquipmentCommand.getEquipmentTypeId())
                .setEquipmentNo(UUID.randomUUID().toString().replaceAll("-", ""))
                .setAlwaysAlarm(monitorEquipmentCommand.getAlwaysAlarm());
        monitorEquipmentService.insertMonitorEquipment(monitorEquipmentDto);
        System.out.println("插入成功");
        System.out.println("插入到monitorinstrument表中........");
        //生成探头
        MonitorinstrumentDTO monitorinstrumentDTO = new MonitorinstrumentDTO()
                 .setInstrumenttypeid(Integer.valueOf(monitorEquipmentCommand.getEquipmentTypeId()))
                 .setEquipmentno(monitorEquipmentDto.getEquipmentNo())
                 .setInstrumentname(monitorEquipmentDto.getEquipmentName()+"探头")
                 .setSn(monitorEquipmentCommand.getSn())
                 .setChannel(monitorEquipmentCommand.getChannel())
                 //默认为3次
                 .setAlarmtime(3)
                 .setInstrumentno(UUID.randomUUID().toString().replaceAll("-", ""))
                 .setHospitalcode(monitorEquipmentCommand.getHospitalCode())
                 .setAlarmtime(Integer.valueOf(monitorEquipmentCommand.getAlwaysAlarm()));
        monitorinstrumentService.insertMonitorinstrumentInfo(monitorinstrumentDTO);
        System.out.println("插入成功");
        System.out.println("插入到monitorequipmentwarningtime表中........");
        //插入报警时段
        List<MonitorequipmentwarningtimeDTO> warningTimeList = monitorEquipmentCommand.getWarningTimeList();
        if(CollectionUtils.isNotEmpty(warningTimeList)){
            warningTimeList.forEach(res->{
                res.setHospitalcode(monitorEquipmentCommand.getHospitalCode())
                        .setEquipmentid(monitorinstrumentDTO.getEquipmentno())
                        .setEquipmentcategory("EQ");
                monitorequipmentwarningtimeService.insetWarningtimeList(res);
            });
        }
        System.out.println("插入成功");
        System.out.println("插入到instrumentparamconfig表中........");
        //插入探头参数表
        List<InstrumentmonitorDTO> instrumentmonitorDTOS = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
        for (InstrumentmonitorDTO instrumentmonitorDTO : instrumentmonitorDTOS) {
            InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                    .setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-",""))
                    .setInstrumentconfigid(instrumentmonitorDTO.getInstrumentconfigid())
                    .setInstrumentname(monitorinstrumentDTO.getInstrumentname())
                    .setWarningphone("0")
                    .setHighlimit(instrumentmonitorDTO.getHighlimit())
                    .setLowlimit(instrumentmonitorDTO.getLowlimit())
                    .setInstrumentno(monitorinstrumentDTO.getInstrumentno())
                    .setInstrumenttypeid(instrumentmonitorDTO.getInstrumenttypeid())
                    .setSaturation(instrumentmonitorDTO.getSaturation())
                    .setAlarmtime(3);
            instrumentparamconfigService.insertInstrumentmonitor(instrumentparamconfigDTO);
        }
        System.out.println("插入成功");
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

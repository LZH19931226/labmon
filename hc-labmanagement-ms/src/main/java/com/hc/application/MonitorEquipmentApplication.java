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
import com.hc.vo.equimenttype.WarningTimeVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

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

   @Autowired
   private MonitorinstrumenttypeService monitorinstrumenttypeService;

    /**
     * 分页获取监控设备信息
     * @param monitorEquipmentCommand 监控设备
     * @return
     */
   public Page<MonitorEquipmentVo> getEquipmentInfoList(MonitorEquipmentCommand monitorEquipmentCommand) {
       Page page = new Page(monitorEquipmentCommand.getPageCurrent(),monitorEquipmentCommand.getPageSize());
       List<MonitorEquipmentDto> dtoList = monitorEquipmentService.getEquipmentInfoList(page,monitorEquipmentCommand);
       List<MonitorEquipmentVo> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            dtoList.forEach(res->{
                String instrumentNo = res.getInstrumentNo();
                List<InstrumentparamconfigDTO> instrumentparamconfigDTOList =  instrumentparamconfigService.slectInfo(instrumentNo);
                //添加仪器信息集合
                MonitorinstrumenttypeDTO  monitorinstrumenttypeDTO = mergeCollections(instrumentparamconfigDTOList);
                List<InstrumentmonitorDTO> instrumentmonitorDTOS = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
                List<InstrumentmonitorVo> instrumentmonitorVOS = new ArrayList<>();
                for (InstrumentmonitorDTO instrumentmonitorDTO : instrumentmonitorDTOS) {
                    InstrumentmonitorVo build = InstrumentmonitorVo.builder()
                            .instrumentconfigid(instrumentmonitorDTO.getInstrumentconfigid())
                            .instrumenttypeid(instrumentmonitorDTO.getInstrumenttypeid())
                            .highlimit(instrumentmonitorDTO.getHighlimit())
                            .lowlimit(instrumentmonitorDTO.getLowlimit()).build();
                    instrumentmonitorVOS.add(build);
                }
                MonitorinstrumenttypeVo build1 = MonitorinstrumenttypeVo.builder()
                        .instrumenttypeid(monitorinstrumenttypeDTO.getInstrumenttypeid())
                        .instrumenttypename(monitorinstrumenttypeDTO.getInstrumenttypename())
                        .instrumentmonitorVos(instrumentmonitorVOS).build();

                //获取超时时段
                String equipmentNo = res.getEquipmentNo();
                String hospitalCode = res.getHospitalCode();
                List<MonitorequipmentwarningtimeDTO> monitorequipmentwarningtimeDTOS =
                        monitorequipmentwarningtimeService.selectWarningtimeByHosCodeAndEno(hospitalCode,equipmentNo);
                List<WarningTimeVo> timeVoList = new ArrayList<>();
                monitorequipmentwarningtimeDTOS.forEach(result->{
                    WarningTimeVo build = WarningTimeVo.builder()
                            .beginTime(result.getBegintime())
                            .endTime(result.getEndtime())
                            .equipmentCategory(result.getEquipmentcategory())
                            .timeBlockId(result.getTimeblockid().longValue())
                            .build();
                    timeVoList.add(build);
                });

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
                        .warningTimeList(timeVoList)
                        .monitorinstrumenttypeDTO(build1)
                        .build();
                list.add(build);
            });

        }
       page.setRecords(list);
      return page;
   }



    /**
     * 添加设备信息
     * @param monitorEquipmentCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMonitorEquipment(MonitorEquipmentCommand monitorEquipmentCommand) {
        //1.根据sn和医院code查询sn是否重复
        String sn = monitorEquipmentCommand.getSn();
        String hospitalCode = monitorEquipmentCommand.getHospitalCode();
        Integer integer = monitorinstrumentService.selectCount(new MonitorinstrumentDTO().setSn(sn).setHospitalcode(hospitalCode));
        if(integer > 0){
            throw new IedsException(MonitorinstrumentEnumCode.FAILED_TO_ADD_DEVICE.getMessage());
        }
        System.out.println("插入到instrumentmonitor表中........");
        //2.根据探头信息和设备信息插入到instrumentmonitor表中
        MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = monitorEquipmentCommand.getMonitorinstrumenttypeDTO();
        if(!ObjectUtils.isEmpty(monitorinstrumenttypeDTO)){
            List<InstrumentmonitorDTO> instrumentmonitorDTOS = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
            if(CollectionUtils.isNotEmpty(instrumentmonitorDTOS)){
                instrumentmonitorDTOS.forEach(res->{
                    boolean flag =  instrumentmonitorService.selectOne(res);
                    if(flag){
                        instrumentmonitorService.insertInstrumentmonitorInfo(res);
                    }else {
                        instrumentmonitorService.updateInstrumentmonitor(res);
                    }
                });
            }
        }

        //3.插入到monitorequipment表中
        MonitorEquipmentDto monitorEquipmentDto = new MonitorEquipmentDto()
                .setHospitalCode(monitorEquipmentCommand.getHospitalCode())
                .setEquipmentBrand(monitorEquipmentCommand.getEquipmentBrand())
                .setClientVisible(monitorEquipmentCommand.getClientVisible())
                .setEquipmentName(monitorEquipmentCommand.getEquipmentName())
                .setEquipmentTypeId(monitorEquipmentCommand.getEquipmentTypeId())
                .setEquipmentNo(UUID.randomUUID().toString().replaceAll("-", ""))
                .setAlwaysAlarm(monitorEquipmentCommand.getAlwaysAlarm());
        monitorEquipmentService.insertMonitorEquipment(monitorEquipmentDto);

        //4.插入监控探头信息
        MonitorinstrumentDTO monitorinstrumentDTO = new MonitorinstrumentDTO()
                 .setInstrumenttypeid(Integer.valueOf(monitorEquipmentCommand.getEquipmentTypeId()))
                 .setEquipmentno(monitorEquipmentDto.getEquipmentNo())
                 .setInstrumentname(monitorEquipmentDto.getEquipmentName()+"探头")
                 .setSn(monitorEquipmentCommand.getSn())
                 .setChannel(monitorEquipmentCommand.getChannel())
                 //默认为3次
                 .setAlarmtime(3)
                 .setInstrumentno(UUID.randomUUID().toString().replaceAll("-", ""))
                 .setHospitalcode(monitorEquipmentCommand.getHospitalCode());
        monitorinstrumentService.insertMonitorinstrumentInfo(monitorinstrumentDTO);

        //5.插入报警时段
        List<MonitorequipmentwarningtimeDTO> warningTimeList = monitorEquipmentCommand.getWarningTimeList();
        if(CollectionUtils.isNotEmpty(warningTimeList)){
            warningTimeList.forEach(res->{
                res.setHospitalcode(monitorEquipmentCommand.getHospitalCode())
                        .setEquipmentid(monitorinstrumentDTO.getEquipmentno())
                        .setEquipmentcategory("EQ");
                monitorequipmentwarningtimeService.insetWarningtimeList(res);
            });
        }

        //6.插入探头参数表
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
    }

    /**
     * 修改设备信息
     * @param monitorEquipmentCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMonitorEquipment(MonitorEquipmentCommand monitorEquipmentCommand) {
//        //1.同医院不可以有相同sn
//        String sn = monitorEquipmentCommand.getSn();
//        String code = monitorEquipmentCommand.getHospitalCode();
//        Integer integer = monitorinstrumentService.selectCount(new MonitorinstrumentDTO().setSn(sn).setHospitalcode(code));
//        if(integer > 0){
//            throw new IedsException(MonitorinstrumentEnumCode.FAILED_TO_UPDATE_DEVICE.getMessage());
//        }

        //2.修改仪器的监控信息instrumentmonitor
        MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = monitorEquipmentCommand.getMonitorinstrumenttypeDTO();
        List<InstrumentmonitorDTO> instrumentmonitorDTOS = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
        if(CollectionUtils.isNotEmpty(instrumentmonitorDTOS)){
            instrumentmonitorDTOS.forEach(res->{
                instrumentmonitorService.updateInstrumentmonitor(res);
            });
        }

        //3.修改监控设备信息（monitorequipment）
        MonitorEquipmentDto monitorEquipmentDto = new MonitorEquipmentDto()
                .setEquipmentNo(monitorEquipmentCommand.getEquipmentNo())
                .setEquipmentName(monitorEquipmentCommand.getEquipmentName())
                .setEquipmentTypeId(monitorEquipmentCommand.getEquipmentTypeId())
                .setClientVisible(monitorEquipmentCommand.getClientVisible())
                .setHospitalCode(monitorEquipmentCommand.getHospitalCode())
                .setEquipmentBrand(monitorEquipmentCommand.getEquipmentBrand())
                .setAlwaysAlarm(monitorEquipmentCommand.getAlwaysAlarm());
        monitorEquipmentService.updateMonitorEquipment(monitorEquipmentDto);


        //4.修改监控探头信息（monitorinstrument）
        List<MonitorinstrumentDTO> monitorinstrumentDTOList = monitorinstrumentService.selectMonitorByEno(monitorEquipmentCommand.getEquipmentNo());
       if(CollectionUtils.isNotEmpty(monitorinstrumentDTOList)){
               monitorinstrumentDTOList.forEach(res->{
                   res.setSn(monitorEquipmentCommand.getSn()).setChannel(monitorEquipmentCommand.getChannel());
                   monitorinstrumentService.updateMonitorinstrumentInfo(res);
               });
       }

       //5.修改报警时间（monitorequipmentwarningtime）
       List<MonitorequipmentwarningtimeDTO> warningTimeList = monitorEquipmentCommand.getWarningTimeList();
       if(CollectionUtils.isNotEmpty(warningTimeList)){
           List<MonitorequipmentwarningtimeDTO> updateList = warningTimeList.stream().filter(res -> res.getTimeblockid() != null).collect(Collectors.toList());
           if(CollectionUtils.isNotEmpty(updateList)){
               monitorequipmentwarningtimeService.updateList(updateList);
           }
           List<MonitorequipmentwarningtimeDTO> insertList = warningTimeList.stream().filter(res -> res.getTimeblockid() == null).collect(Collectors.toList());
           if(CollectionUtils.isNotEmpty(insertList)){
               insertList.forEach(res->{
                   res.setEquipmentid(monitorEquipmentCommand.getEquipmentNo())
                                   .setEquipmentcategory("EQ")
                           .setHospitalcode(monitorEquipmentCommand.getHospitalCode());
                   monitorequipmentwarningtimeService.insetWarningtimeList(res);
               });
           }
       }
       List<MonitorequipmentwarningtimeDTO> deleteWarningTimeList = monitorEquipmentCommand.getDeleteWarningTimeList();
       if(CollectionUtils.isNotEmpty(deleteWarningTimeList)){
           deleteWarningTimeList.forEach(res->monitorequipmentwarningtimeService.deleteInfo(res));
       }
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


    /**
     * 获取硬件设备类型对应监控探头信息
     * @return
     */
    public List<MonitorinstrumenttypeVo> getHardwareTypeProbeInformation() {

        List<MonitorinstrumenttypeVo> mitVo = new ArrayList<>();
        List<MonitorinstrumenttypeDTO> monitorinstrumenttypeVoList =  monitorinstrumenttypeService.seleclAll();
        for (MonitorinstrumenttypeDTO monitorinstrumenttypeDTO : monitorinstrumenttypeVoList) {



            List<InstrumentmonitorDTO> instrumentmonitorDTOS =  instrumentmonitorService.selectMonitorEquipmentList(monitorinstrumenttypeDTO.getInstrumenttypeid());

            List<InstrumentmonitorVo> instrumentmonitorVos = new ArrayList<>();
            instrumentmonitorDTOS.forEach(res->{
                InstrumentmonitorVo build1 = InstrumentmonitorVo.builder()
                        .instrumentconfigid(res.getInstrumentconfigid())
                        .instrumenttypeid(res.getInstrumenttypeid())
                        .lowlimit(res.getLowlimit())
                        .highlimit(res.getHighlimit())
                        .build();
                instrumentmonitorVos.add(build1);
            });
            MonitorinstrumenttypeVo build = MonitorinstrumenttypeVo.builder()
                    .instrumenttypeid(monitorinstrumenttypeDTO.getInstrumenttypeid())
                    .instrumenttypename(monitorinstrumenttypeDTO.getInstrumenttypename())
                    .instrumentmonitorVos(instrumentmonitorVos)
                    .build();
            mitVo.add(build);
        }


        return mitVo;
    }

    /**
     * 将InstrumentparamconfigDTO集合合并的方法
     * @param instrumentparamconfigDTOList
     * @return
     */
    private MonitorinstrumenttypeDTO mergeCollections(List<InstrumentparamconfigDTO> instrumentparamconfigDTOList) {

        Map<MonitorinstrumenttypeDTO,List<InstrumentmonitorDTO>> map =  new HashMap<>();
        List<InstrumentmonitorDTO> list = new ArrayList<>();

        for (InstrumentparamconfigDTO instrumentparamconfigDTO : instrumentparamconfigDTOList) {

            MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = new MonitorinstrumenttypeDTO();
            monitorinstrumenttypeDTO.setInstrumenttypeid(instrumentparamconfigDTO.getInstrumenttypeid());
            monitorinstrumenttypeDTO.setInstrumenttypename(instrumentparamconfigDTO.getInstrumentname().replaceAll("探头",""));

            if(map.containsKey(monitorinstrumenttypeDTO)){
                List<InstrumentmonitorDTO> list1 = map.get(monitorinstrumenttypeDTO);
                InstrumentmonitorDTO instrumentmonitorDTO = new InstrumentmonitorDTO();
                instrumentmonitorDTO.setInstrumentconfigid(instrumentparamconfigDTO.getInstrumentconfigid());
                instrumentmonitorDTO.setInstrumenttypeid(instrumentparamconfigDTO.getInstrumenttypeid());
                instrumentmonitorDTO.setHighlimit(instrumentparamconfigDTO.getHighlimit());
                instrumentmonitorDTO.setLowlimit(instrumentparamconfigDTO.getLowlimit());
                list1.add(instrumentmonitorDTO);
                map.put(monitorinstrumenttypeDTO,list1);

            }else {
                InstrumentmonitorDTO instrumentmonitorDTO = new InstrumentmonitorDTO();
                instrumentmonitorDTO.setInstrumentconfigid(instrumentparamconfigDTO.getInstrumentconfigid());
                instrumentmonitorDTO.setInstrumenttypeid(instrumentparamconfigDTO.getInstrumenttypeid());
                instrumentmonitorDTO.setHighlimit(instrumentparamconfigDTO.getHighlimit());
                instrumentmonitorDTO.setLowlimit(instrumentparamconfigDTO.getLowlimit());
                list.add(instrumentmonitorDTO);
                map.put(monitorinstrumenttypeDTO,list);
            }
        }
        MonitorinstrumenttypeDTO monitorinstrumenttype = new MonitorinstrumenttypeDTO();
        for (Map.Entry<MonitorinstrumenttypeDTO, List<InstrumentmonitorDTO>> monitorinstrumenttypeDTOListEntry : map.entrySet()) {
            monitorinstrumenttype = monitorinstrumenttypeDTOListEntry.getKey();
            monitorinstrumenttype.setInstrumentmonitorDTOS(monitorinstrumenttypeDTOListEntry.getValue());

        }
//        for (MonitorinstrumenttypeDTO monitorinstrumenttypeDTO : map.entrySet()) {
//            monitorinstrumenttype = new MonitorinstrumenttypeDTO()
//                    .setInstrumenttypeid(monitorinstrumenttypeDTO.getInstrumenttypeid())
//                    .setInstrumenttypename(monitorinstrumenttypeDTO.getInstrumenttypename())
//                    .setInstrumentmonitorDTOS(map.get(monitorinstrumenttypeDTO));
//        }
        return monitorinstrumenttype;
    }
}

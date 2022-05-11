package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.command.labmanagement.hospital.MonitorEquipmentLogCommand;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.command.labmanagement.operation.MonitorEquipmentLogInfoCommand;
import com.hc.constants.error.MonitorequipmentEnumErrorCode;
import com.hc.dto.*;
import com.hc.labmanagent.HospitalInfoApi;
import com.hc.my.common.core.constant.enums.MonitorinstrumentEnumCode;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
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
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
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

    @Autowired
    private InstrumentconfigService instrumentconfigService;

    @Autowired
    private OperationlogService operationlogService;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;
    /**
     * 分页获取监控设备信息
     *
     * @param monitorEquipmentCommand 监控设备
     * @return
     */
    public Page<MonitorEquipmentVo> getEquipmentInfoList(MonitorEquipmentCommand monitorEquipmentCommand) {
        Page<MonitorEquipmentVo> page = new Page<>(monitorEquipmentCommand.getPageCurrent(), monitorEquipmentCommand.getPageSize());
        List<MonitorEquipmentDto> dtoList = monitorEquipmentService.getEquipmentInfoList(page, monitorEquipmentCommand);
        List<MonitorEquipmentVo> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtoList)) {
            //将for循环中的操作数据库移植到外面做缓存
            //在探头参数配置表中以instrumentNo为key,instrumentNo对应的InstrumentparamconfigDTO集合为value,做缓存
            List<String> instrumentNos = dtoList.stream().map(MonitorEquipmentDto::getInstrumentNo).collect(Collectors.toList());
            List<InstrumentparamconfigDTO> instrumentParamConfigLists = instrumentparamconfigService.slectInfo(instrumentNos);
            Map<String, List<InstrumentparamconfigDTO>> instrumentNoMap = instrumentParamConfigLists.stream().collect(Collectors.groupingBy(InstrumentparamconfigDTO::getInstrumentno));

            //查询所有的探头检测信息，做缓存
            List<InstrumentconfigDTO> instrumentConfigList = instrumentconfigService.selectAllInfo();
            Map<Integer, InstrumentconfigDTO> instrumentConfigMap = instrumentConfigList.stream().collect(Collectors.toMap(InstrumentconfigDTO::getInstrumentconfigid, t -> t));

            //在超时报警中以equipmentNo为key,equipmentNo对应的MonitorequipmentwarningtimeDTO集合为value做缓存
            List<String> equipmentNoList = dtoList.stream().map(MonitorEquipmentDto::getEquipmentNo).collect(Collectors.toList());
            List<MonitorequipmentwarningtimeDTO> monitorEquipmentWarningTimeDTOList = monitorequipmentwarningtimeService.selectWarningtimeByEnoList(equipmentNoList);
            Map<String, List<MonitorequipmentwarningtimeDTO>> monitorEquipmentWarningTimeMaps =
                    monitorEquipmentWarningTimeDTOList.stream().collect(Collectors.groupingBy(MonitorequipmentwarningtimeDTO::getEquipmentid));

            dtoList.forEach(res -> {

                //添加仪器信息集合
                String instrumentNo = res.getInstrumentNo();
                List<InstrumentparamconfigDTO> instrumentParamConfigList = instrumentNoMap.get(instrumentNo);
                //将集合转化为对象
                MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = mergeCollections(instrumentParamConfigList);
                List<InstrumentmonitorVo> instrumentMonitorVos = new ArrayList<>();

                MonitorinstrumenttypeVo monitorinstrumenttypeVo = MonitorinstrumenttypeVo.builder().build();
                if (!ObjectUtils.isEmpty(monitorinstrumenttypeDTO)) {
                    List<InstrumentmonitorDTO> instrumentMonitorList = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
                    if (CollectionUtils.isNotEmpty(instrumentMonitorList)) {
                        for (InstrumentmonitorDTO instrumentmonitorDTO : instrumentMonitorList) {
                            //查询检测类型名称
                            InstrumentconfigDTO instrumentconfigDTO = instrumentConfigMap.get(instrumentmonitorDTO.getInstrumentconfigid());
                            InstrumentmonitorVo build = InstrumentmonitorVo.builder()
                                    .instrumentconfigid(instrumentmonitorDTO.getInstrumentconfigid())
                                    .instrumenttypeid(instrumentmonitorDTO.getInstrumenttypeid())
                                    .highlimit(instrumentmonitorDTO.getHighlimit())
                                    .lowlimit(instrumentmonitorDTO.getLowlimit())
                                    .instrumentparamconfigno(instrumentmonitorDTO.getInstrumentparamconfigno())
                                    .instrumentconfigname(instrumentconfigDTO == null ? null : instrumentconfigDTO.getInstrumentconfigname())
                                    .saturation(instrumentmonitorDTO.getSaturation()==null?new BigDecimal(0):instrumentmonitorDTO.getSaturation())
                                    .build();
                            instrumentMonitorVos.add(build);
                        }
                    }

                    monitorinstrumenttypeVo = MonitorinstrumenttypeVo.builder()
                            .instrumenttypeid(monitorinstrumenttypeDTO.getInstrumenttypeid())
                            .instrumenttypename(monitorinstrumenttypeDTO.getInstrumenttypename())
                            .instrumentmonitorVos(instrumentMonitorVos).build();
                }

                //获取超时时段
                String equipmentNo = res.getEquipmentNo();
                List<MonitorequipmentwarningtimeDTO> monitorEquipmentWarningTimeList = monitorEquipmentWarningTimeMaps.get(equipmentNo);
                List<WarningTimeVo> timeVoList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(monitorEquipmentWarningTimeList)) {
                    monitorEquipmentWarningTimeList.forEach(result -> {
                        WarningTimeVo build = WarningTimeVo.builder()
                                .beginTime(result.getBegintime())
                                .endTime(result.getEndtime())
                                .equipmentCategory(result.getEquipmentcategory())
                                .timeBlockId(result.getTimeblockid().longValue())
                                .build();
                        timeVoList.add(build);
                    });
                }
                //获取设备有没有绑定探头信息
                boolean deleteOrNot = true;
                if (!ObjectUtils.isEmpty(instrumentNoMap.get(instrumentNo))) {
                    deleteOrNot = false;
                }
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
                        .instrumenttypeid(res.getInstrumentTypeId() == null ? null : Integer.valueOf(res.getInstrumentTypeId()))
                        .instrumentno(res.getInstrumentNo())
                        .warningTimeList(timeVoList)
                        .monitorinstrumenttypeDTO(monitorinstrumenttypeVo)
                        .deleteOrNot(deleteOrNot)
                        .channel(res.getChannel())
                        .build();
                list.add(build);
            });
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 添加设备信息
     *
     * @param monitorEquipmentCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMonitorEquipment(MonitorEquipmentCommand monitorEquipmentCommand) {
        //根据sn和医院code查询sn是否重复
        String sn = monitorEquipmentCommand.getSn();
        String hospitalCode = monitorEquipmentCommand.getHospitalCode();
        Integer integer = monitorinstrumentService.selectCount(new MonitorinstrumentDTO().setSn(sn).setHospitalcode(hospitalCode));
        if (integer > 0) {
            throw new IedsException(MonitorinstrumentEnumCode.FAILED_TO_ADD_DEVICE.getMessage());
        }
        String equipmentName = monitorEquipmentCommand.getEquipmentName();
        Integer  integer1 = monitorEquipmentService.selectCount(new MonitorEquipmentDto().setEquipmentName(equipmentName).setHospitalCode(hospitalCode));
        if(integer1>0){
            throw new IedsException(MonitorequipmentEnumErrorCode.DEVICE_NAME_ALREADY_EXISTS.getMessage());
        }
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

        //插入监控探头信息
        MonitorinstrumentDTO monitorinstrumentDTO = new MonitorinstrumentDTO()
                .setInstrumenttypeid(Integer.valueOf(monitorEquipmentCommand.getEquipmentTypeId()))
                .setEquipmentno(monitorEquipmentDto.getEquipmentNo())
                .setInstrumentname(monitorEquipmentDto.getEquipmentName() + "探头")
                .setSn(monitorEquipmentCommand.getSn())
                .setChannel(monitorEquipmentCommand.getChannel())
                //默认为3次
                .setAlarmtime(3)
                .setInstrumentno(UUID.randomUUID().toString().replaceAll("-", ""))
                .setHospitalcode(monitorEquipmentCommand.getHospitalCode());
        monitorinstrumentService.insertMonitorinstrumentInfo(monitorinstrumentDTO);

        //插入报警时段
        List<MonitorequipmentwarningtimeDTO> warningTimeList = monitorEquipmentCommand.getWarningTimeList();
        if (CollectionUtils.isNotEmpty(warningTimeList)) {
            warningTimeList.forEach(res -> {
                res.setHospitalcode(monitorEquipmentCommand.getHospitalCode())
                        .setEquipmentid(monitorinstrumentDTO.getEquipmentno())
                        .setEquipmentcategory("EQ");
                monitorequipmentwarningtimeService.insetWarningtimeList(res);
            });
        }

        //6.插入探头参数表
        MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = monitorEquipmentCommand.getMonitorinstrumenttypeDTO();
        List<InstrumentmonitorDTO> instrumentmonitorDTOS = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
        for (InstrumentmonitorDTO instrumentmonitorDTO : instrumentmonitorDTOS) {
            InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                    .setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""))
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

        //更新日志表
        MonitorEquipmentLogInfoCommand build = build(Context.getUserId(), equipmentName,new MonitorEquipmentLogCommand(), monitorEquipmentCommand,
                OperationLogEunm.DEVICE_MANAGEMENT.getCode(), OperationLogEunmDerailEnum.ADD.getCode());
        operationlogService.addMonitorEquipmentLogInfo(build);
    }

    private MonitorEquipmentLogInfoCommand build(String userId, String equipmentName,MonitorEquipmentLogCommand oldMonitorEquipmentLogCommand ,MonitorEquipmentCommand newMonitorEquipmentLogCommand, String Type, String operationType) {
        MonitorEquipmentLogInfoCommand logInfoCommand = new MonitorEquipmentLogInfoCommand();
        logInfoCommand.setType(Type);
        logInfoCommand.setOperationType(operationType);
        //根据医院code获取医院名称
        String hospitalCode = oldMonitorEquipmentLogCommand.getHospitalCode() != null ? oldMonitorEquipmentLogCommand.getHospitalCode() : newMonitorEquipmentLogCommand.getHospitalCode();
        HospitalMadel hospitalInfo = hospitalInfoApi.findHospitalInfo(hospitalCode).getResult();
        if(!ObjectUtils.isEmpty(hospitalInfo)){
            logInfoCommand.setHospitalName(hospitalInfo.getHospitalName());
        }
        //根据useid获取用户信息
        UserBackModel userInfo = hospitalInfoApi.findUserInfo(userId).getResult();
        if(!ObjectUtils.isEmpty(userInfo)){
            logInfoCommand.setUsername(userInfo.getUsername());
        }
        //新增是设备no为空 修改时不为空
        String equipmentNo = newMonitorEquipmentLogCommand.getEquipmentNo();
        if(!StringUtils.isEmpty(equipmentNo)){
            logInfoCommand.setEquipmentNo(equipmentNo);
        }
        //设置最新的设备信息
        MonitorEquipmentLogCommand monitorEquipmentCommand1 = new MonitorEquipmentLogCommand();
        monitorEquipmentCommand1.setEquipmentName(equipmentName);
        monitorEquipmentCommand1.setClientVisible(oldMonitorEquipmentLogCommand.getClientVisible());
        logInfoCommand.setNewMonitorEquipmentLogCommand(monitorEquipmentCommand1);
        logInfoCommand.setOldMonitorEquipmentLogCommand(oldMonitorEquipmentLogCommand);
        return logInfoCommand;
    }

    /**
     * 修改设备信息
     *
     * @param monitorEquipmentCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMonitorEquipment(MonitorEquipmentCommand monitorEquipmentCommand) {
        String equipmentName = monitorEquipmentCommand.getEquipmentName();
        String hospitalCode = monitorEquipmentCommand.getHospitalCode();

        MonitorEquipmentDto equipmentDto =
                monitorEquipmentService.selectMonitorEquipmentInfoByNo(monitorEquipmentCommand.getEquipmentNo());
        Integer integer =  monitorEquipmentService.selectCount(new MonitorEquipmentDto().setEquipmentName(equipmentName).setHospitalCode(hospitalCode));
        if(integer>1){
            throw new IedsException(MonitorequipmentEnumErrorCode.DEVICE_NAME_ALREADY_EXISTS.getMessage());
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
        MonitorinstrumentDTO monitorinstrumentDTO = monitorinstrumentService.selectMonitorByEno(monitorEquipmentCommand.getEquipmentNo());
        if (!ObjectUtils.isEmpty(monitorinstrumentDTO)) {
            monitorinstrumentDTO = monitorinstrumentDTO
                    .setSn(monitorEquipmentCommand.getSn())
                    .setChannel(monitorEquipmentCommand.getChannel())
                    .setInstrumentname(monitorEquipmentCommand.getEquipmentName() + "探头");
            monitorinstrumentService.updateMonitorinstrumentInfo(monitorinstrumentDTO);
        }

        //5.修改报警时间（monitorequipmentwarningtime）
        List<MonitorequipmentwarningtimeDTO> warningTimeList = monitorEquipmentCommand.getWarningTimeList();
        if (CollectionUtils.isNotEmpty(warningTimeList)) {
            List<MonitorequipmentwarningtimeDTO> updateList = warningTimeList.stream().filter(res -> res.getTimeblockid() != null).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(updateList)) {
                monitorequipmentwarningtimeService.updateList(updateList);
            }
            List<MonitorequipmentwarningtimeDTO> insertList = warningTimeList.stream().filter(res -> res.getTimeblockid() == null).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(insertList)) {
                insertList.forEach(res -> {
                    res.setEquipmentid(monitorEquipmentCommand.getEquipmentNo())
                            .setEquipmentcategory("EQ")
                            .setHospitalcode(monitorEquipmentCommand.getHospitalCode());
                    monitorequipmentwarningtimeService.insetWarningtimeList(res);
                });
            }
        }
        List<MonitorequipmentwarningtimeDTO> deleteWarningTimeList = monitorEquipmentCommand.getDeleteWarningTimeList();
        if (CollectionUtils.isNotEmpty(deleteWarningTimeList)) {
            deleteWarningTimeList.forEach(res -> monitorequipmentwarningtimeService.deleteInfo(res));
        }

        //6.插入探头参数表
        MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = monitorEquipmentCommand.getMonitorinstrumenttypeDTO();
        List<InstrumentmonitorDTO> instrumentmonitorDTOS = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
        if (CollectionUtils.isNotEmpty(instrumentmonitorDTOS)) {
            InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO();
            List<InstrumentmonitorDTO> dtos = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
            for (InstrumentmonitorDTO dto : dtos) {
                instrumentparamconfigDTO.setInstrumentparamconfigno(dto.getInstrumentparamconfigno());
                instrumentparamconfigDTO.setInstrumenttypeid(dto.getInstrumenttypeid());
                instrumentparamconfigDTO.setLowlimit(dto.getLowlimit());
                instrumentparamconfigDTO.setHighlimit(dto.getHighlimit());
                instrumentparamconfigDTO.setInstrumentname(monitorEquipmentCommand.getEquipmentName() + "探头");
                instrumentparamconfigDTO.setSaturation(dto.getSaturation());
                instrumentparamconfigDTO.setAlarmtime(Integer.valueOf(monitorEquipmentCommand.getAlwaysAlarm()));

                instrumentparamconfigService.updateInfo(instrumentparamconfigDTO);
            }
        }


        //更新日志表
        MonitorEquipmentLogInfoCommand build = build(Context.getUserId(),
                equipmentName,
                BeanConverter.convert(equipmentDto,MonitorEquipmentLogCommand.class),
                monitorEquipmentCommand,
                OperationLogEunm.DEVICE_MANAGEMENT.getCode(),
                OperationLogEunmDerailEnum.EDIT.getCode());
        operationlogService.addMonitorEquipmentLogInfo(build);
    }

    /**
     * 删除监控设备
     *
     * @param equipmentNo
     */
    public void deleteMonitorEquipment(String equipmentNo) {
        MonitorinstrumentDTO monitorinstrumentDTO = monitorinstrumentService.selectMonitorByEno(equipmentNo);
        if (!ObjectUtils.isEmpty(monitorinstrumentDTO)) {
            throw new IedsException(MonitorinstrumentEnumCode.FAILED_TO_DELETE.getMessage());
        }
        //删除探头表中的信息
        monitorinstrumentService.deleteMonitorinstrumentInfo(equipmentNo);
        monitorEquipmentService.deleteMonitorEquipmentInfo(equipmentNo);

        //删除报警时段
        Integer integer = monitorequipmentwarningtimeService.selectWarningtimeByEno(equipmentNo);
        if (integer > 0) {
            monitorequipmentwarningtimeService.deleteInfo(new MonitorequipmentwarningtimeDTO().setEquipmentid(equipmentNo));
        }
        MonitorEquipmentDto monitorEquipmentDto = monitorEquipmentService.selectMonitorEquipmentInfoByNo(equipmentNo);
        MonitorEquipmentLogCommand convert = BeanConverter.convert(monitorEquipmentDto, MonitorEquipmentLogCommand.class);
        MonitorEquipmentLogInfoCommand build =
                build(Context.getUserId(), monitorEquipmentDto.getEquipmentName(), convert, new MonitorEquipmentCommand(), OperationLogEunm.DEVICE_MANAGEMENT.getCode(), OperationLogEunmDerailEnum.REMOVE.getCode());
        operationlogService.addMonitorEquipmentLogInfo(build);
    }

    /**
     * 查询监控设备类型
     *
     * @param instrumenttypeid
     * @return
     */
    public List<MonitorinstrumenttypeVo> selectMonitorEquipmentType(String instrumenttypeid) {
        List<MonitorinstrumenttypeDTO> monitorinstrumenttypeDTOS = instrumentmonitorService.selectMonitorEquipmentType(instrumenttypeid);
        if (CollectionUtils.isNotEmpty(monitorinstrumenttypeDTOS)) {
            List<MonitorinstrumenttypeVo> monitorinstrumenttypeVos = new ArrayList<>();
            monitorinstrumenttypeDTOS.forEach(s -> {
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

    public List<InstrumentmonitorVo> buildInstrumentmonitorVO(List<InstrumentmonitorDTO> instrumentmonitorDTOS) {
        if (CollectionUtils.isNotEmpty(instrumentmonitorDTOS)) {
            List<InstrumentmonitorVo> instrumentmonitorVos = new ArrayList<>();
            instrumentmonitorDTOS.forEach(s -> {
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
     *
     * @return
     */
    public List<MonitorinstrumenttypeVo> getHardwareTypeProbeInformation() {
        List<MonitorinstrumenttypeVo> mitVo = new ArrayList<>();
        List<MonitorinstrumenttypeDTO> monitorinstrumenttypeVoList = monitorinstrumenttypeService.seleclAll();
        for (MonitorinstrumenttypeDTO monitorinstrumenttypeDTO : monitorinstrumenttypeVoList) {
            List<InstrumentmonitorDTO> instrumentmonitorDTOS = instrumentmonitorService.selectMonitorEquipmentList(monitorinstrumenttypeDTO.getInstrumenttypeid());

            List<InstrumentmonitorVo> instrumentmonitorVos = new ArrayList<>();
            instrumentmonitorDTOS.forEach(res -> {
                Integer instrumentconfigid = res.getInstrumentconfigid();
                InstrumentconfigDTO instrumentconfig = instrumentconfigService.selectInfoByConfigid(instrumentconfigid);
                InstrumentmonitorVo build1 = InstrumentmonitorVo.builder()
                        .instrumentconfigid(res.getInstrumentconfigid())
                        .instrumenttypeid(res.getInstrumenttypeid())
                        .lowlimit(res.getLowlimit())
                        .instrumentconfigname(instrumentconfig.getInstrumentconfigname())
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
     * 将InstrumentparamconfigDTO集合转化为对象的方法
     *
     * @param instrumentparamconfigDTOList
     * @return
     */
    private MonitorinstrumenttypeDTO mergeCollections(List<InstrumentparamconfigDTO> instrumentparamconfigDTOList) {
        if (!CollectionUtils.isNotEmpty(instrumentparamconfigDTOList)) {
            return null;
        }
        Map<MonitorinstrumenttypeDTO, List<InstrumentmonitorDTO>> map = new HashMap<>();
        List<InstrumentmonitorDTO> list = new ArrayList<>();
        for (InstrumentparamconfigDTO instrumentparamconfigDTO : instrumentparamconfigDTOList) {
            //将MonitorinstrumenttypeDTO对象作为map中的key,提取集合相同的合并为value
            MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = new MonitorinstrumenttypeDTO();
            monitorinstrumenttypeDTO.setInstrumenttypeid(instrumentparamconfigDTO.getInstrumenttypeid());
            monitorinstrumenttypeDTO.setInstrumenttypename(instrumentparamconfigDTO.getInstrumentname().replaceAll("探头", ""));

            if (map.containsKey(monitorinstrumenttypeDTO)) {
                List<InstrumentmonitorDTO> list1 = map.get(monitorinstrumenttypeDTO);
                InstrumentmonitorDTO instrumentmonitorDTO = new InstrumentmonitorDTO();
                instrumentmonitorDTO.setInstrumentconfigid(instrumentparamconfigDTO.getInstrumentconfigid());
                instrumentmonitorDTO.setInstrumenttypeid(instrumentparamconfigDTO.getInstrumenttypeid());
                instrumentmonitorDTO.setHighlimit(instrumentparamconfigDTO.getHighlimit());
                instrumentmonitorDTO.setLowlimit(instrumentparamconfigDTO.getLowlimit());
                instrumentmonitorDTO.setInstrumentparamconfigno(instrumentparamconfigDTO.getInstrumentparamconfigno());
                instrumentmonitorDTO.setSaturation(instrumentparamconfigDTO.getSaturation());
                list1.add(instrumentmonitorDTO);
                map.put(monitorinstrumenttypeDTO, list1);

            } else {
                InstrumentmonitorDTO instrumentmonitorDTO = new InstrumentmonitorDTO();
                instrumentmonitorDTO.setInstrumentconfigid(instrumentparamconfigDTO.getInstrumentconfigid());
                instrumentmonitorDTO.setInstrumenttypeid(instrumentparamconfigDTO.getInstrumenttypeid());
                instrumentmonitorDTO.setHighlimit(instrumentparamconfigDTO.getHighlimit());
                instrumentmonitorDTO.setLowlimit(instrumentparamconfigDTO.getLowlimit());
                instrumentmonitorDTO.setInstrumentparamconfigno(instrumentparamconfigDTO.getInstrumentparamconfigno());
                instrumentmonitorDTO.setSaturation(instrumentparamconfigDTO.getSaturation());
                list.add(instrumentmonitorDTO);
                map.put(monitorinstrumenttypeDTO, list);
            }
        }
        MonitorinstrumenttypeDTO monitorinstrumenttype = new MonitorinstrumenttypeDTO();
        for (Map.Entry<MonitorinstrumenttypeDTO, List<InstrumentmonitorDTO>> monitorInstrumentTypeListEntry : map.entrySet()) {
            monitorinstrumenttype = monitorInstrumentTypeListEntry.getKey();
            monitorinstrumenttype.setInstrumentmonitorDTOS(monitorInstrumentTypeListEntry.getValue());

        }
        return monitorinstrumenttype;
    }
}

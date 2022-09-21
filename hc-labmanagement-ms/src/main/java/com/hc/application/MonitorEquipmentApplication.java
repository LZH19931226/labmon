package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.application.command.WorkTimeBlockCommand;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.command.labmanagement.model.hospital.MonitorEquipmentLogCommand;
import com.hc.command.labmanagement.operation.MonitorEquipmentLogInfoCommand;
import com.hc.constants.HospitalEnumErrorCode;
import com.hc.constants.error.HospitalequimentEnumErrorCode;
import com.hc.constants.error.MonitorequipmentEnumErrorCode;
import com.hc.constants.error.MonitorinstrumentEnumCode;
import com.hc.device.ProbeRedisApi;
import com.hc.device.SnDeviceRedisApi;
import com.hc.dto.*;
import com.hc.hospital.HospitalInfoApi;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.command.ProbeCommand;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.MonitorEquipmentWarningTimeDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.*;
import com.hc.vo.equimenttype.InstrumentmonitorVo;
import com.hc.vo.equimenttype.MonitorEquipmentVo;
import com.hc.vo.equimenttype.MonitorinstrumenttypeVo;
import com.hc.vo.equimenttype.WarningTimeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
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
    private InstrumentConfigService instrumentconfigService;

    @Autowired
    private OperationlogService operationlogService;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    @Autowired
    private HospitalequimentService hospitalequimentService;

    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;

    @Autowired
    private ProbeRedisApi probeRedisApi;

    /**
     * 分页获取监控设备信息
     *  (用户新增设备页面选择设备后出现的探头检测信息)
     * @param monitorEquipmentCommand 监控设备参数
     * @return 分页对象
     */
    public Page<MonitorEquipmentVo> getEquipmentInfoList(MonitorEquipmentCommand monitorEquipmentCommand) {
        Page<MonitorEquipmentVo> page = new Page<>(monitorEquipmentCommand.getPageCurrent(), monitorEquipmentCommand.getPageSize());
        List<MonitorEquipmentDto> dtoList = monitorEquipmentService.getEquipmentInfoList(page, monitorEquipmentCommand);
        List<MonitorEquipmentVo> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtoList)) {
            //将for循环中的操作数据库移植到外面做缓存
            //在探头参数配置表中以instrumentNo为key,instrumentNo对应的InstrumentparamconfigDTO集合为value
            List<String> instrumentNos = dtoList.stream().map(MonitorEquipmentDto::getInstrumentNo).collect(Collectors.toList());
            List<InstrumentparamconfigDTO> instrumentParamConfigLists = instrumentparamconfigService.slectInfo(instrumentNos);
            Map<String, List<InstrumentparamconfigDTO>> instrumentNoMap = instrumentParamConfigLists.stream().collect(Collectors.groupingBy(InstrumentparamconfigDTO::getInstrumentno));

            //查询所有的探头检测信息
            List<InstrumentConfigDTO> instrumentConfigList = instrumentconfigService.selectAllInfo();
            Map<Integer, InstrumentConfigDTO> instrumentConfigMap = instrumentConfigList.stream().collect(Collectors.toMap(InstrumentConfigDTO::getInstrumentconfigid, t -> t));

            //在超时报警中以equipmentNo为key,equipmentNo对应的MonitorequipmentwarningtimeDTO集合为value
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
                            InstrumentConfigDTO instrumentconfigDTO = instrumentConfigMap.get(instrumentmonitorDTO.getInstrumentconfigid());
                            InstrumentmonitorVo build = buildInstrumentmonitorVo(instrumentconfigDTO,instrumentmonitorDTO);
                            instrumentMonitorVos.add(build);
                        }
                    }
                    monitorinstrumenttypeVo = MonitorinstrumenttypeVo.builder()
                            .instrumenttypeid(monitorinstrumenttypeDTO.getInstrumenttypeid())
                            .instrumenttypename(monitorinstrumenttypeDTO.getInstrumenttypename())
                            .instrumentmonitorVos(instrumentMonitorVos)
                            .build();
                }

                //获取超时时段
                String equipmentNo = res.getEquipmentNo();
                List<MonitorequipmentwarningtimeDTO> monitorEquipmentWarningTimeList = monitorEquipmentWarningTimeMaps.get(equipmentNo);
                List<WarningTimeVo> timeVoList =  bulidWarningTimeVoList(monitorEquipmentWarningTimeList);

                //获取设备有没有绑定探头信息
                boolean deleteOrNot = ObjectUtils.isEmpty(instrumentNoMap.get(instrumentNo));
                MonitorEquipmentVo build = buildMonitorEquipmentVo(res,timeVoList,monitorinstrumenttypeVo,deleteOrNot);
                list.add(build);
            });
        }
        page.setRecords(list);
        return page;
    }

    private MonitorEquipmentVo buildMonitorEquipmentVo(MonitorEquipmentDto res, List<WarningTimeVo> timeVoList, MonitorinstrumenttypeVo monitorinstrumenttypeVo, boolean deleteOrNot) {
        return MonitorEquipmentVo.builder()
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
                .channel(res.getChannel())
                .saturation(StringUtils.isEmpty(res.getSaturation()) ? "" : res.getSaturation())
                .warningTimeList(timeVoList)
                .monitorinstrumenttypeDTO(monitorinstrumenttypeVo)
                .deleteOrNot(deleteOrNot)
                .build();
    }

    private List<WarningTimeVo> bulidWarningTimeVoList(List<MonitorequipmentwarningtimeDTO> monitorEquipmentWarningTimeList) {
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
        return timeVoList;
    }

    private InstrumentmonitorVo buildInstrumentmonitorVo(InstrumentConfigDTO instrumentconfigDTO, InstrumentmonitorDTO instrumentmonitorDTO) {
        return InstrumentmonitorVo.builder()
                .instrumentconfigid(instrumentmonitorDTO.getInstrumentconfigid())
                .instrumenttypeid(instrumentmonitorDTO.getInstrumenttypeid())
                .highlimit(instrumentmonitorDTO.getHighlimit())
                .lowlimit(instrumentmonitorDTO.getLowlimit())
                .instrumentparamconfigno(instrumentmonitorDTO.getInstrumentparamconfigno())
                .instrumentconfigname(instrumentconfigDTO == null ? null : instrumentconfigDTO.getInstrumentconfigname())
                .saturation(instrumentmonitorDTO.getSaturation()==null ? new BigDecimal(0) : instrumentmonitorDTO.getSaturation())
                .build();
    }

    /**
     * 添加设备信息
     *
     * @param monitorEquipmentCommand 监控设备参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMonitorEquipment(MonitorEquipmentCommand monitorEquipmentCommand) {
        String sn = monitorEquipmentCommand.getSn().trim();
        String hospitalCode = monitorEquipmentCommand.getHospitalCode();
        String equipmentTypeId = monitorEquipmentCommand.getEquipmentTypeId();
        //sn不能重复
        Integer integer = monitorinstrumentService.selectCount(new MonitorinstrumentDTO().setSn(sn));
        if (integer > 0) {
            throw new IedsException(MonitorinstrumentEnumCode.FAILED_TO_ADD_DEVICE.getMessage());
        }

        //判断同医院设备名称是否有重复
        String equipmentName = monitorEquipmentCommand.getEquipmentName();
        Integer  integer1 = monitorEquipmentService.selectCount(new MonitorEquipmentDto().setEquipmentName(equipmentName).setHospitalCode(hospitalCode));
        if(integer1>0){
            throw new IedsException(MonitorequipmentEnumErrorCode.DEVICE_NAME_ALREADY_EXISTS.getMessage());
        }

        //判断医院是否存在设备类型
        HospitalequimentDTO hospitalequimentDTO = hospitalequimentService.selectHospitalEquimentInfoByCodeAndTypeId(hospitalCode, equipmentTypeId);
        if(ObjectUtils.isEmpty(hospitalequimentDTO)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_DEVICE_TYPE_DOES_NOT_EXIST.getCode());
        }

        //插入到monitorequipment表中
        String equipmentNo = UUID.randomUUID().toString().replaceAll("-", "");
        MonitorEquipmentDto monitorEquipmentDto = new MonitorEquipmentDto()
                .setHospitalCode(monitorEquipmentCommand.getHospitalCode())
                .setEquipmentBrand(monitorEquipmentCommand.getEquipmentBrand())
                .setClientVisible(monitorEquipmentCommand.getClientVisible())
                .setEquipmentName(monitorEquipmentCommand.getEquipmentName())
                .setEquipmentTypeId(monitorEquipmentCommand.getEquipmentTypeId())
                .setEquipmentNo(equipmentNo)
                .setAlwaysAlarm(monitorEquipmentCommand.getAlwaysAlarm())
                .setCreateTime(new Date());
        monitorEquipmentService.insertMonitorEquipment(monitorEquipmentDto);

        //插入监控探头信息
        String instrumentNo = UUID.randomUUID().toString().replaceAll("-", "");
        String instrumentName = monitorEquipmentDto.getEquipmentName() + "探头";
        MonitorinstrumentDTO monitorinstrumentDTO = new MonitorinstrumentDTO()
                .setInstrumenttypeid(monitorEquipmentCommand.getMonitorinstrumenttypeDTO().getInstrumenttypeid())
                .setEquipmentno(monitorEquipmentDto.getEquipmentNo())
                .setInstrumentname(instrumentName)
                .setSn(monitorEquipmentCommand.getSn())
                .setChannel(monitorEquipmentCommand.getChannel())
                //默认为3次
                .setAlarmtime(3)
                .setInstrumentno(instrumentNo)
                .setHospitalcode(monitorEquipmentCommand.getHospitalCode());
        monitorinstrumentService.insertMonitorinstrumentInfo(monitorinstrumentDTO);

        //插入报警时段
        List<MonitorequipmentwarningtimeDTO> warningTimeList = monitorEquipmentCommand.getWarningTimeList();
        if (CollectionUtils.isNotEmpty(warningTimeList)) {
            List<WorkTimeBlockCommand> workTimeBlockCommandList = BeanConverter.convert(warningTimeList, WorkTimeBlockCommand.class);
            //检测时间是否重叠
            checkWorkTime(workTimeBlockCommandList);
            warningTimeList.forEach(res -> {
                res.setHospitalcode(monitorEquipmentCommand.getHospitalCode())
                        .setEquipmentid(monitorinstrumentDTO.getEquipmentno())
                        .setEquipmentcategory("EQ");
            });
            monitorequipmentwarningtimeService.insetWarningtimeList(warningTimeList);
        }

        //插入探头参数表
        List<InstrumentparamconfigDTO> probeList = new ArrayList<>();
        MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = monitorEquipmentCommand.getMonitorinstrumenttypeDTO();
        List<InstrumentmonitorDTO> instrumentmonitorDTOS = monitorinstrumenttypeDTO.getInstrumentmonitorDTOS();
        for (InstrumentmonitorDTO instrumentmonitorDTO : instrumentmonitorDTOS) {
            InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                    .setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""))
                    .setInstrumentconfigid(instrumentmonitorDTO.getInstrumentconfigid())
                    .setInstrumentconfigname(instrumentmonitorDTO.getInstrumentconfigname())
                    .setInstrumentname(monitorinstrumentDTO.getInstrumentname())
                    .setChannel(instrumentmonitorDTO.getChannel())
                    .setWarningphone("0")
                    .setFirsttime(new Date())
                    .setHighlimit(instrumentmonitorDTO.getHighlimit())
                    .setLowlimit(instrumentmonitorDTO.getLowlimit())
                    .setInstrumentno(monitorinstrumentDTO.getInstrumentno())
                    .setInstrumenttypeid(instrumentmonitorDTO.getInstrumenttypeid())
                    .setSaturation(instrumentmonitorDTO.getSaturation())
                    .setAlarmtime(3);
            probeList.add(instrumentparamconfigDTO);
        }
        instrumentparamconfigService.insertBatch(probeList);


        //更新日志表
        MonitorEquipmentLogInfoCommand build = build(Context.getUserId(), equipmentName,new MonitorEquipmentLogCommand(), monitorEquipmentCommand,
                OperationLogEunm.DEVICE_MANAGEMENT.getCode(), OperationLogEunmDerailEnum.ADD.getCode());
        operationlogService.addMonitorEquipmentLogInfo(build);

        //设备信息存入redis
        updateSnDeviceDtoSync(equipmentNo,monitorEquipmentCommand,equipmentName,instrumentNo,monitorinstrumenttypeDTO,warningTimeList);

        //探头信息存入redis
        updateProbeRedisInfo(instrumentNo,instrumentName,equipmentNo,monitorEquipmentCommand,probeList);
    }

    /**
     * 检测时间
     * @param singletonList
     */
    private void checkWorkTime(List<WorkTimeBlockCommand> singletonList) {
        List<Date> list = new ArrayList<>();
        for (WorkTimeBlockCommand workTimeBlockCommand : singletonList) {
            Date startTime = workTimeBlockCommand.getBegintime();
            Date endTime = workTimeBlockCommand.getEndtime();
            if(startTime ==null || endTime == null){
                continue;
            }
            if(endTime.compareTo(startTime)<=0){
                throw new IedsException(HospitalequimentEnumErrorCode.START_TIME_AND_END_TIME_ARE_ABNORMAL.getCode());
            }
            list.add(buildTime(startTime));
            list.add(buildTime(endTime));
        }
        if(CollectionUtils.isNotEmpty(list)){
            int size = list.size();
            switch (size){
                case 4:
                    Boolean aBoolean = checkTimesHasOverlap(list.get(0), list.get(1), list.get(2), list.get(3));
                    if(aBoolean){
                        throw new IedsException(HospitalequimentEnumErrorCode.THERE_IS_AN_OVERLAP_BETWEEN_THE_TWO_TIME_PERIODS.getCode());
                    }
                    break;
                case 6:
                    //有三段时间需要比三次
                    Boolean one = checkTimesHasOverlap(list.get(0), list.get(1), list.get(2), list.get(3));
                    Boolean two = checkTimesHasOverlap(list.get(0), list.get(1), list.get(4), list.get(5));
                    Boolean three = checkTimesHasOverlap(list.get(2), list.get(3), list.get(4), list.get(5));
                    if(one || two || three){
                        throw new IedsException(HospitalequimentEnumErrorCode.THERE_IS_AN_OVERLAP_OF_THE_THREE_TIME_PERIODS.getCode());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 统一时间
     * @param date
     * @return
     */
    public Date buildTime(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR,1970);
        cal.set(Calendar.MONTH,1);
        cal.set(Calendar.DATE,1);
        return cal.getTime();
    }

    /**
     * 判断两个时间范围是否有交集
     * 1. 比较时间段的结束时间在参考时间段的开始时间之前
     * 2. 比较时间段的开始时间在参考时间段的结束时间之后
     * 取反得到所有的交集
     */
    public static Boolean checkTimesHasOverlap(Date dynaStartTime, Date dynaEndTime, Date fixedStartTime, Date fixedEndTime) {
        return !(dynaEndTime.getTime() < fixedStartTime.getTime() || dynaStartTime.getTime() > fixedEndTime.getTime());
    }


    /**
     * 更新探头reids缓存
     * @param instrumentNo 探头id
     * @param instrumentName 探头名称
     * @param equipmentNo 设备id
     * @param monitorEquipmentCommand 监控设备对象
     * @param probeList 探头信息集合
     */
    private void updateProbeRedisInfo(String instrumentNo, String instrumentName, String equipmentNo, MonitorEquipmentCommand monitorEquipmentCommand, List<InstrumentparamconfigDTO> probeList) {
        Integer instrumenttypeid = monitorEquipmentCommand.getMonitorinstrumenttypeDTO().getInstrumenttypeid();
        List<InstrumentInfoDto> list = new ArrayList<>();
        for (InstrumentparamconfigDTO res : probeList) {
            InstrumentInfoDto instrumentInfoDto = new InstrumentInfoDto();
            instrumentInfoDto.setInstrumentNo(instrumentNo)
                    .setInstrumentName(instrumentName)
                    .setEquipmentNo(equipmentNo)
                    .setInstrumentTypeId(instrumenttypeid)
                    .setHospitalCode(monitorEquipmentCommand.getHospitalCode())
                    .setSn(monitorEquipmentCommand.getSn())
                    .setAlarmTime(3)
                    .setEquipmentName(monitorEquipmentCommand.getEquipmentName())
                    .setSaturation(res.getSaturation())
                    .setInstrumentParamConfigNO(res.getInstrumentparamconfigno())
                    .setInstrumentConfigId(res.getInstrumentconfigid())
                    .setInstrumentConfigName(res.getInstrumentconfigname())
                    .setWarningPhone("0")
                    .setLowLimit(res.getLowlimit())
                    .setHighLimit(res.getHighlimit());
            list.add(instrumentInfoDto);
        }
        List<String> collect1 = list.stream().map(res -> res.getInstrumentNo() + ":" + res.getInstrumentConfigId()).collect(Collectors.toList());
        ProbeCommand probeCommand = new ProbeCommand();
        probeCommand.setHospitalCode(monitorEquipmentCommand.getHospitalCode());
        probeCommand.setInstrumentNo(collect1);
        probeCommand.setInstrumentInfoDtoList(list);
        probeRedisApi.bulkUpdateProbeRedisInfo(probeCommand);
    }

    /**
     * 更新sn设备的方法
     * @param equipmentNo
     * @param monitorEquipmentCommand
     * @param equipmentName
     * @param instrumentNo
     * @param monitorinstrumenttypeDTO
     */
    public  void  updateSnDeviceDtoSync(String equipmentNo,MonitorEquipmentCommand monitorEquipmentCommand,String equipmentName,String instrumentNo,MonitorinstrumenttypeDTO monitorinstrumenttypeDTO,List<MonitorequipmentwarningtimeDTO> warningTimeList){
        List<MonitorEquipmentWarningTimeDto> warningTimeDTOs = BeanConverter.convert(warningTimeList, MonitorEquipmentWarningTimeDto.class);
        //存入redis
        SnDeviceDto snDeviceDto = new SnDeviceDto()
                .setEquipmentNo(equipmentNo)
                .setEquipmentTypeId(monitorEquipmentCommand.getEquipmentTypeId())
                .setHospitalCode(monitorEquipmentCommand.getHospitalCode())
                .setEquipmentName(equipmentName)
                .setEquipmentBrand(monitorEquipmentCommand.getEquipmentBrand())
                .setClientVisible(monitorEquipmentCommand.getClientVisible())
                .setInstrumentNo(instrumentNo)
                .setAlwaysAlarm(monitorEquipmentCommand.getAlwaysAlarm())
                .setInstrumentName(monitorinstrumenttypeDTO.getInstrumenttypename())
                .setInstrumentTypeId(monitorinstrumenttypeDTO.getInstrumenttypeid().toString())
                .setSn(monitorEquipmentCommand.getSn())
                .setAlarmTime(3L)
                .setAlwaysAlarm(monitorEquipmentCommand.getAlwaysAlarm())
                .setChannel(monitorEquipmentCommand.getChannel())
                .setWarningTimeList(warningTimeDTOs);
        snDeviceRedisApi.updateSnDeviceDtoSync(snDeviceDto);
    }

    /**
     * 构建监控设备日志信息
     * @param userId 用户id
     * @param equipmentName 设备名称
     * @param oldMonitorEquipmentLogCommand 旧数据
     * @param newMonitorEquipmentLogCommand 新数据
     * @param Type 菜单类型
     * @param operationType 操作类型
     * @return 监控设备日志信息
     */
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
        monitorEquipmentCommand1.setClientVisible(newMonitorEquipmentLogCommand.getClientVisible());
        logInfoCommand.setNewMonitorEquipmentLogCommand(monitorEquipmentCommand1);
        logInfoCommand.setOldMonitorEquipmentLogCommand(oldMonitorEquipmentLogCommand);
        return logInfoCommand;
    }

    /**
     * 修改设备信息
     *
     * @param monitorEquipmentCommand 监控设备参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMonitorEquipment(MonitorEquipmentCommand monitorEquipmentCommand) {
        String equipmentName = monitorEquipmentCommand.getEquipmentName();
        String hospitalCode = monitorEquipmentCommand.getHospitalCode();

        MonitorEquipmentDto equipmentDto =
                monitorEquipmentService.selectMonitorEquipmentInfoByNo(monitorEquipmentCommand.getEquipmentNo());
        //判断设备名称有没有修改
        if(!equipmentName.equals(equipmentDto.getEquipmentName())){
            //修改时用判断该医院下设备名称是否已存在
            Integer integer =  monitorEquipmentService.selectCount(new MonitorEquipmentDto().setEquipmentName(equipmentName).setHospitalCode(hospitalCode));
            if(integer>0){
                throw new IedsException(MonitorequipmentEnumErrorCode.DEVICE_NAME_ALREADY_EXISTS.getMessage());
            }
        }
        //用于redis判断sn是否修改
        String newSn = monitorEquipmentCommand.getSn();
        String oldSn = equipmentDto.getSn();
        boolean flag = oldSn.equals(newSn);
        if(!flag){
            //如果sn修改了校验新sn是否已存在
            Boolean aBoolean = monitorEquipmentService.checkSn(monitorEquipmentCommand.getSn());
            if(aBoolean){
               throw new IedsException(MonitorinstrumentEnumCode.FAILED_TO_UPDATE_DEVICE.getMessage());
            }
        }
        //修改监控设备信息（monitorequipment）
        MonitorEquipmentDto monitorEquipmentDto = new MonitorEquipmentDto()
                .setEquipmentNo(monitorEquipmentCommand.getEquipmentNo())
                .setEquipmentName(monitorEquipmentCommand.getEquipmentName())
                .setEquipmentTypeId(monitorEquipmentCommand.getEquipmentTypeId())
                .setClientVisible(monitorEquipmentCommand.getClientVisible())
                .setHospitalCode(monitorEquipmentCommand.getHospitalCode())
                .setEquipmentBrand(monitorEquipmentCommand.getEquipmentBrand())
                .setAlwaysAlarm(monitorEquipmentCommand.getAlwaysAlarm());
        monitorEquipmentService.updateMonitorEquipment(monitorEquipmentDto);

        //修改监控探头信息（monitorinstrument）
        List<MonitorinstrumentDTO> monitorinstrumentDTO = monitorinstrumentService.selectMonitorByEno(monitorEquipmentCommand.getEquipmentNo());
        if (CollectionUtils.isNotEmpty(monitorinstrumentDTO)) {
            monitorinstrumentDTO.forEach(res->{
                res.setSn(monitorEquipmentCommand.getSn());
                res.setChannel(monitorEquipmentCommand.getChannel());
                res.setInstrumentname(monitorEquipmentCommand.getEquipmentName()+"探头");
            });
        }
        monitorinstrumentService.bulkUpdate(monitorinstrumentDTO);

        //修改报警时间（monitorequipmentwarningtime）
        List<MonitorequipmentwarningtimeDTO> warningTimeList = monitorEquipmentCommand.getWarningTimeList();
        if (CollectionUtils.isNotEmpty(warningTimeList)) {
            List<WorkTimeBlockCommand> workTimeBlockCommandList = BeanConverter.convert(warningTimeList, WorkTimeBlockCommand.class);
            //检测时间是否重叠
            checkWorkTime(workTimeBlockCommandList);
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
                });
                monitorequipmentwarningtimeService.insetWarningtimeList(insertList);
            }
        }

        List<MonitorequipmentwarningtimeDTO> deleteWarningTimeList = monitorEquipmentCommand.getDeleteWarningTimeList();
        if (CollectionUtils.isNotEmpty(deleteWarningTimeList)) {
            List<Integer> collect = deleteWarningTimeList.stream().map(MonitorequipmentwarningtimeDTO::getTimeblockid).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                monitorequipmentwarningtimeService.bulkRemove(collect);
            }
        }

        //更新探头参数表
        MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = monitorEquipmentCommand.getMonitorinstrumenttypeDTO();
        List<InstrumentmonitorDTO> instrumentmonitorDTOS = monitorinstrumenttypeDTO.getInstrumentmonitorVos();
        List<InstrumentparamconfigDTO> list =  new ArrayList<>();
        if (CollectionUtils.isNotEmpty(instrumentmonitorDTOS)) {
            for (InstrumentmonitorDTO dto : instrumentmonitorDTOS) {
                InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO();
                instrumentparamconfigDTO.setInstrumentconfigid(dto.getInstrumentconfigid());
                instrumentparamconfigDTO.setInstrumentno(monitorEquipmentCommand.getInstrumentno());
                instrumentparamconfigDTO.setInstrumentparamconfigno(dto.getInstrumentparamconfigno());
                instrumentparamconfigDTO.setInstrumenttypeid(dto.getInstrumenttypeid());
                instrumentparamconfigDTO.setLowlimit(dto.getLowlimit());
                instrumentparamconfigDTO.setHighlimit(dto.getHighlimit());
                instrumentparamconfigDTO.setInstrumentname(monitorEquipmentCommand.getEquipmentName() + "探头");
                instrumentparamconfigDTO.setSaturation(dto.getSaturation());
                instrumentparamconfigDTO.setFirsttime(new Date());
                list.add(instrumentparamconfigDTO);
            }
            instrumentparamconfigService.updateBatch(list);
        }

        //更新日志表
        MonitorEquipmentLogInfoCommand build = build(Context.getUserId(),
                equipmentName,
                BeanConverter.convert(equipmentDto,MonitorEquipmentLogCommand.class),
                monitorEquipmentCommand,
                OperationLogEunm.DEVICE_MANAGEMENT.getCode(),
                OperationLogEunmDerailEnum.EDIT.getCode());
        operationlogService.addMonitorEquipmentLogInfo(build);

        //更新redis缓存
        //判断sn是否被修改，如果是就需要先删除该sn的redis信息，重新put信息
        if(!flag){
            snDeviceRedisApi.deleteSnDeviceDto(oldSn);
        }
        List<MonitorEquipmentWarningTimeDto> warningTimeDTOs = BeanConverter.convert(warningTimeList, MonitorEquipmentWarningTimeDto.class);
        SnDeviceDto snDeviceDto =  buildSnDeviceDto(monitorEquipmentCommand,monitorinstrumenttypeDTO,warningTimeDTOs);
        snDeviceRedisApi.updateSnDeviceDtoSync(snDeviceDto);

        //更新探头redis信息
       updateProbeRedisInfo(hospitalCode,list,monitorEquipmentCommand);
    }

    /**
     * 批量更新探头信息
     * @param hospitalCode
     * @param instrumentMonitorDTOList
     * @param monitorEquipmentCommand
     */
    private void updateProbeRedisInfo(String hospitalCode, List<InstrumentparamconfigDTO> instrumentMonitorDTOList,MonitorEquipmentCommand monitorEquipmentCommand) {
        ProbeCommand probeCommand = new ProbeCommand();
        probeCommand.setHospitalCode(hospitalCode);
        List<String> instrumentNoList = instrumentMonitorDTOList.stream().map(res -> res.getInstrumentno() + ":" + res.getInstrumentconfigid()).collect(Collectors.toList());
        probeCommand.setInstrumentNo(instrumentNoList);
        List<InstrumentInfoDto> instrumentInfoDtoList = probeRedisApi.bulkGetProbeRedisInfo(probeCommand).getResult();
        InstrumentparamconfigDTO instrumentparamconfigDTO = instrumentMonitorDTOList.get(0);
        for (InstrumentInfoDto instrumentInfoDto : instrumentInfoDtoList) {
            instrumentInfoDto.setEquipmentName(monitorEquipmentCommand.getEquipmentName());
            instrumentInfoDto.setSn(monitorEquipmentCommand.getSn());
            instrumentInfoDto.setInstrumentName(instrumentparamconfigDTO.getInstrumentname());
        }
        probeCommand.setInstrumentInfoDtoList(instrumentInfoDtoList);
        probeRedisApi.bulkUpdateProbeRedisInfo(probeCommand);
    }

    /**
     * 构建redis设备对象
     * @param monitorEquipmentCommand
     * @param monitorinstrumenttypeDTO
     * @param warningTimeDTOs
     * @return
     */
    public SnDeviceDto buildSnDeviceDto(MonitorEquipmentCommand monitorEquipmentCommand,MonitorinstrumenttypeDTO monitorinstrumenttypeDTO, List<MonitorEquipmentWarningTimeDto> warningTimeDTOs){
        return new SnDeviceDto()
                .setEquipmentNo(monitorEquipmentCommand.getEquipmentNo())
                .setEquipmentTypeId(monitorEquipmentCommand.getEquipmentTypeId())
                .setHospitalCode(monitorEquipmentCommand.getHospitalCode())
                .setEquipmentName(monitorEquipmentCommand.getEquipmentName())
                .setEquipmentBrand(monitorEquipmentCommand.getEquipmentBrand())
                .setClientVisible(monitorEquipmentCommand.getClientVisible())
                .setInstrumentNo(monitorEquipmentCommand.getInstrumentno())
                .setAlwaysAlarm(monitorEquipmentCommand.getAlwaysAlarm())
                .setInstrumentName(monitorinstrumenttypeDTO.getInstrumenttypename())
                .setInstrumentTypeId(String.valueOf(monitorinstrumenttypeDTO.getInstrumenttypeid()))
                .setSn(monitorEquipmentCommand.getSn())
                .setAlwaysAlarm(monitorEquipmentCommand.getAlwaysAlarm())
                .setChannel(monitorEquipmentCommand.getChannel())
                .setWarningTimeList(warningTimeDTOs);
    }

    /**
     * 删除监控设备
     *
     * @param equipmentNo 设备编号
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMonitorEquipment(String equipmentNo) {

        //判断设备是否有探头信息
        Integer integer1 = monitorinstrumentService.findProbeInformationByEno(equipmentNo);
        if (integer1>0) {
            throw new IedsException(MonitorinstrumentEnumCode.FAILED_TO_DELETE.getMessage());
        }

        //通过设备eno查询设备sn信息,用于redis删除
        List<MonitorinstrumentDTO> monitorinstrumentDTO = monitorinstrumentService.selectMonitorByEno(equipmentNo);

        MonitorEquipmentDto monitorEquipmentDto = monitorEquipmentService.selectMonitorEquipmentInfoByNo(equipmentNo);
        //删除探头表中的信息
        monitorinstrumentService.deleteMonitorinstrumentInfo(equipmentNo);
        monitorEquipmentService.deleteMonitorEquipmentInfo(equipmentNo);

        //删除报警时段
        Integer integer = monitorequipmentwarningtimeService.selectWarningtimeByEno(equipmentNo);
        if (integer > 0) {
            monitorequipmentwarningtimeService.deleteInfo(new MonitorequipmentwarningtimeDTO().setEquipmentid(equipmentNo));
        }

        //更新日志信息
        MonitorEquipmentLogCommand convert = BeanConverter.convert(monitorEquipmentDto, MonitorEquipmentLogCommand.class);
        MonitorEquipmentLogInfoCommand build =
                build(Context.getUserId(), monitorEquipmentDto.getEquipmentName(), convert, new MonitorEquipmentCommand(), OperationLogEunm.DEVICE_MANAGEMENT.getCode(), OperationLogEunmDerailEnum.REMOVE.getCode());
        operationlogService.addMonitorEquipmentLogInfo(build);

        for (MonitorinstrumentDTO dto : monitorinstrumentDTO) {
            //删除redis缓存
            if(!ObjectUtils.isEmpty(dto)){
                snDeviceRedisApi.deleteSnDeviceDto(dto.getSn());
                snDeviceRedisApi.deleteCurrentInfo(dto.getHospitalcode(),dto.getEquipmentno());
            }
        }
    }

    /**
     * 查询监控设备类型
     *
     * @param instrumenttypeid 探头类型id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
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

    /**
     * 构建监控仪器对象
     * @param instrumentmonitorDTOS 探头集合
     * @return
     */
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
    @Transactional(rollbackFor = Exception.class)
    public List<MonitorinstrumenttypeVo> getHardwareTypeProbeInformation(String equipmentTypeId) {
        List<MonitorinstrumenttypeVo> mitVo = new ArrayList<>();
        //查出所有的监控设备类型
        List<MonitorinstrumenttypeDTO> monitorinstrumenttypeVoList = monitorinstrumenttypeService.seleclAll();

        //查询出所有的监控配置
        List<InstrumentConfigDTO> instrumentconfigDTOS = instrumentconfigService.selectAllInfo();
        Map<Integer, InstrumentConfigDTO> instrumentconfigMap = instrumentconfigDTOS.stream().collect(Collectors.toMap(InstrumentConfigDTO::getInstrumentconfigid, Function.identity()));

        List<InstrumentmonitorDTO> list = instrumentmonitorService.selectMonitorEquipmentAll();
        Map<Integer, List<InstrumentmonitorDTO>> instrumentmonitorMap = list.stream().collect(Collectors.groupingBy(InstrumentmonitorDTO::getInstrumenttypeid));
        for (MonitorinstrumenttypeDTO monitorinstrumenttypeDTO : monitorinstrumenttypeVoList) {
            boolean flag = checkEquipmentId(equipmentTypeId,monitorinstrumenttypeDTO.getEquipmenttypeid());
            if(flag && instrumentmonitorMap.containsKey(monitorinstrumenttypeDTO.getInstrumenttypeid())){
                List<InstrumentmonitorDTO> instrumentmonitorDTOS = instrumentmonitorMap.get(monitorinstrumenttypeDTO.getInstrumenttypeid());
                List<InstrumentmonitorVo> instrumentmonitorVos = new ArrayList<>();
                instrumentmonitorDTOS.forEach(res -> {
                    Integer instrumentconfigid = res.getInstrumentconfigid();
                    InstrumentConfigDTO instrumentconfig = new InstrumentConfigDTO();
                    if(instrumentconfigMap.containsKey(instrumentconfigid)){
                        instrumentconfig = instrumentconfigMap.get(instrumentconfigid);
                    }
                    InstrumentmonitorVo build1 = buildInstrumentmonitorVo(res,instrumentconfig);
                    instrumentmonitorVos.add(build1);
                });
                MonitorinstrumenttypeVo build = MonitorinstrumenttypeVo.builder()
                        .instrumenttypeid(monitorinstrumenttypeDTO.getInstrumenttypeid())
                        .instrumenttypename(monitorinstrumenttypeDTO.getInstrumenttypename())
                        .instrumentmonitorVos(instrumentmonitorVos)
                        .build();
                mitVo.add(build);
            }
        }
        return mitVo;
    }

    /**
     * 校验设备id
     * @param equipmentTypeId
     * @param equipmenttypeid
     * @return
     */
    private boolean checkEquipmentId(String equipmentTypeId, String equipmenttypeid) {
        if(StringUtils.isEmpty(equipmentTypeId)){
            return true;
        }
        if(equipmentTypeId.equals(equipmenttypeid)){
            return true;
        }
        if(equipmenttypeid.length()>1){
            List<String> list = parseStr(equipmenttypeid);
            if (CollectionUtils.isNotEmpty(list) && list.contains(equipmentTypeId) ) {
                return true;
            }
        }
        return false;
    }

    private List<String> parseStr(String equipmenttypeid) {
        if(equipmenttypeid.contains(",")){
            String[] str = equipmenttypeid.split(",");
            return Arrays.asList(str);
        }
        return null;
    }

    private InstrumentmonitorVo buildInstrumentmonitorVo(InstrumentmonitorDTO res, InstrumentConfigDTO instrumentconfig) {
        return InstrumentmonitorVo.builder()
                .instrumentconfigid(res.getInstrumentconfigid())
                .instrumenttypeid(res.getInstrumenttypeid())
                .lowlimit(res.getLowlimit())
                .instrumentconfigname(instrumentconfig.getInstrumentconfigname())
                .highlimit(res.getHighlimit())
                .saturation(res.getSaturation())
                .channel(StringUtils.isEmpty(res.getChannel())?"":res.getChannel())
                .build();
    }

    /**
     * 将InstrumentparamconfigDTO集合转化为对象的方法
     *
     * @param instrumentparamconfigDTOList 探头信息集合
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
            monitorinstrumenttypeDTO.setInstrumenttypename(instrumentparamconfigDTO.getInstrumentname().replace("探头",""));
            if (map.containsKey(monitorinstrumenttypeDTO)) {
                List<InstrumentmonitorDTO> list1 = map.get(monitorinstrumenttypeDTO);
                InstrumentmonitorDTO convert = BeanConverter.convert(instrumentparamconfigDTO, InstrumentmonitorDTO.class);
                list1.add(convert);
                map.put(monitorinstrumenttypeDTO, list1);
            } else {
                InstrumentmonitorDTO convert = BeanConverter.convert(instrumentparamconfigDTO, InstrumentmonitorDTO.class);
                list.add(convert);
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

    /**
     * 获取所有的监控设备信息
     * @return
     */
    public List<SnDeviceDto> getAllMonitorEquipmentInfo() {
        List<MonitorEquipmentDto> list =  monitorEquipmentService.getAllMonitorEquipmentInfo();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<String> hospitalCodes = list.stream().map(MonitorEquipmentDto::getHospitalCode).collect(Collectors.toList());
        List<MonitorequipmentwarningtimeDTO> warningTimeList = monitorequipmentwarningtimeService.selectWarningtimeByHospitalCode(hospitalCodes);

        Map<String, Map<String, List<MonitorequipmentwarningtimeDTO>>> warningTimeMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(warningTimeList)) {
            warningTimeMap = warningTimeList.stream().collect(Collectors.groupingBy(MonitorequipmentwarningtimeDTO::getHospitalcode, Collectors.groupingBy(MonitorequipmentwarningtimeDTO::getEquipmentid)));
        }
        List<SnDeviceDto> snDeviceDtoList = new ArrayList<>();
        for (MonitorEquipmentDto monitorEquipmentDto : list) {
            SnDeviceDto snDeviceDto = BeanConverter.convert(monitorEquipmentDto, SnDeviceDto.class);
            String hospitalCode = snDeviceDto.getHospitalCode();
            String equipmentNo = snDeviceDto.getEquipmentNo();
            String alwaysAlarm = snDeviceDto.getAlwaysAlarm();
            if(MapUtils.isNotEmpty(warningTimeMap) && "0".equals(alwaysAlarm) && warningTimeMap.containsKey(hospitalCode)){
                Map<String, List<MonitorequipmentwarningtimeDTO>> equipmentTypeIdMap = warningTimeMap.get(hospitalCode);
                if(equipmentTypeIdMap.containsKey(equipmentNo)){
                    List<MonitorequipmentwarningtimeDTO> monitorEquipmentWarningTimeDTOS = equipmentTypeIdMap.get(equipmentNo);
                    List<MonitorEquipmentWarningTimeDto> dtos = BeanConverter.convert(monitorEquipmentWarningTimeDTOS, MonitorEquipmentWarningTimeDto.class);
                    snDeviceDto.setWarningTimeList(dtos);
                }
            }
            snDeviceDtoList.add(snDeviceDto);
        }
        return snDeviceDtoList;
    }

    /**
     * 获取医院ups的设备no集合
     * @param hospitalCode
     * @return
     */
    public List<SnDeviceDto> getEquipmentNoList(String hospitalCode,String equipmentTypeId) {
        List<MonitorEquipmentDto> list = monitorEquipmentService.getEquipmentNoList(hospitalCode,equipmentTypeId);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return BeanConverter.convert(list,SnDeviceDto.class);
    }

    /**
     *
     * @param equipmentNo
     * @return
     */
    public SnDeviceDto selectMonitorEquipmentInfoByEno(String equipmentNo) {
        MonitorEquipmentDto monitorEquipmentDto = monitorEquipmentService.selectMonitorEquipmentInfoByEno(equipmentNo);
        return BeanConverter.convert(monitorEquipmentDto,SnDeviceDto.class);
    }

    /**
     * 获取医院的设备信息
     * @param hospitalCode
     * @return
     */
    public List<SnDeviceDto> getMonitorEquipmentInfoByHCode(String hospitalCode) {
        List<MonitorEquipmentDto> monitorEquipmentDto = monitorEquipmentService.getMonitorEquipmentInfoByHCode(hospitalCode);
        if (CollectionUtils.isEmpty(monitorEquipmentDto)) {
            return null;
        }
        return BeanConverter.convert(monitorEquipmentDto,SnDeviceDto.class);
    }

    /**
     * 判断是否已存在
     * @param sn
     * @return
     */
    public Boolean checkSn(String sn) {
        return monitorEquipmentService.checkSn(sn);
    }
}

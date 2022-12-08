package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.command.labmanagement.model.hospital.InstrumentparamconfigLogCommand;
import com.hc.command.labmanagement.operation.InstrumentParamConfigInfoCommand;
import com.hc.device.ProbeRedisApi;
import com.hc.device.SnDeviceRedisApi;
import com.hc.dto.*;
import com.hc.hospital.HospitalInfoApi;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.InstrumentmonitorDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.*;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class InstrumentparamconfigApplication {

    @Autowired
    private InstrumentparamconfigService instrumentparamconfigService;

    @Autowired
    private MonitorinstrumentService monitorinstrumentService;

    @Autowired
    private InstrumentmonitorService instrumentmonitorService;

    @Autowired
    private OperationlogService operationlogService;

    @Autowired
    private MonitorEquipmentService monitorEquipmentService;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    @Autowired
    private ProbeRedisApi probeRedisApi;

    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;

    /**
     * 通过设备no获取探头参数信息
     *
     * @param equipmentNo 设备id
     * @return 探头信息集合
     */
    public List<InstrumentparamconfigVo> selectInstrumentParamConfigByEqNo(String equipmentNo) {
        List<InstrumentConfigDTO> instrumentConfigList = instrumentparamconfigService.selectInstrumentparamconfigByEqNo(equipmentNo);
        if (CollectionUtils.isNotEmpty(instrumentConfigList)) {
            List<InstrumentparamconfigVo> instrumentParamConfigVos = new ArrayList<>();
            instrumentConfigList.forEach(s -> {
                instrumentParamConfigVos.add(
                        InstrumentparamconfigVo.builder()
                                .instrumentconfigid(s.getInstrumentconfigid())
                                .instrumentconfigname(s.getInstrumentconfigname())
                                .build()
                );
            });
            return instrumentParamConfigVos;
        }
        return null;
    }

    /**
     * 新增探头信息
     *
     * @param instrumentParamConfigCommand 探头信息参数
     */
    @GlobalTransactional
    public void insertInstrumentParamConfig(InstrumentparamconfigCommand instrumentParamConfigCommand) {
        //判断探头的检测类型是否存在
        boolean flag = instrumentmonitorService.selectOne(new InstrumentmonitorDTO().setInstrumentconfigid(instrumentParamConfigCommand.getInstrumentconfigid())
                .setInstrumenttypeid(instrumentParamConfigCommand.getInstrumenttypeid()));
        if(!flag){
            throw new IedsException(LabSystemEnum.EQUIPMENT_PROBE_AND_DETECTION_TYPE_MISMATCH);
        }

        //判断探头检测类型是否存在
        Integer i =  instrumentparamconfigService.selectCount(instrumentParamConfigCommand.getInstrumentNo(),
                instrumentParamConfigCommand.getInstrumentconfigid(),instrumentParamConfigCommand.getInstrumenttypeid());
        if(i>0){
            throw new IedsException(LabSystemEnum.PROBE_INFORMATION_ALREADY_EXISTS);
        }

        String instrumentParamConfigNo = UUID.randomUUID().toString().replaceAll("-", "");
        InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                .setInstrumentparamconfigno(instrumentParamConfigNo)
                .setInstrumentno(instrumentParamConfigCommand.getInstrumentNo())
                .setInstrumentconfigid(instrumentParamConfigCommand.getInstrumentconfigid())
                .setInstrumentname(instrumentParamConfigCommand.getInstrumentname())
                .setLowlimit(instrumentParamConfigCommand.getLowlimit())
                .setHighlimit(instrumentParamConfigCommand.getHighlimit())
                .setInstrumenttypeid(instrumentParamConfigCommand.getInstrumenttypeid())
                .setWarningphone(instrumentParamConfigCommand.getWarningphone())
                .setChannel(instrumentParamConfigCommand.getChannel())
                .setAlarmtime(instrumentParamConfigCommand.getAlarmtime())
                .setFirsttime(new Date())
                .setUnit(StringUtils.isEmpty(instrumentParamConfigCommand.getUnit()) ? "":instrumentParamConfigCommand.getUnit())
                .setStyleMax(StringUtils.isEmpty(instrumentParamConfigCommand.getStyleMax()) ? "":instrumentParamConfigCommand.getStyleMax())
                .setStyleMin(StringUtils.isEmpty(instrumentParamConfigCommand.getStyleMin()) ? "":instrumentParamConfigCommand.getStyleMin())
                .setSaturation(instrumentParamConfigCommand.getSaturation());
        instrumentparamconfigService.insertInstrumentmonitor(instrumentparamconfigDTO);

        //添加日志信息
        InstrumentParamConfigInfoCommand instrumentParamConfigInfoCommand =
                build(Context.getUserId(),
                        new InstrumentparamconfigCommand(),
                        instrumentParamConfigCommand,
                        OperationLogEunm.PROBE_MANAGEMENT.getCode(),
                        OperationLogEunmDerailEnum.ADD.getCode());
        operationlogService.addInstrumentparamconfig(instrumentParamConfigInfoCommand);

        //添加redis缓存信息
        MonitorinstrumentDTO monitorinstrumentDTO = monitorinstrumentService.selectMonitorByIno(instrumentParamConfigCommand.getInstrumentNo());
        addProbeRedisInfo(monitorinstrumentDTO,instrumentParamConfigCommand,instrumentParamConfigNo);
    }

    /**
     * 探头redis缓存
     * @param monitorinstrumentDTO
     * @param instrumentParamConfigCommand
     */
    private void addProbeRedisInfo(MonitorinstrumentDTO monitorinstrumentDTO, InstrumentparamconfigCommand instrumentParamConfigCommand,String instrumentParamConfigNo) {
        InstrumentInfoDto instrumentInfoDto = new InstrumentInfoDto();
        instrumentInfoDto.setInstrumentNo(instrumentParamConfigCommand.getInstrumentNo())
                .setSaturation(instrumentParamConfigCommand.getSaturation())
                .setInstrumentName(instrumentParamConfigCommand.getInstrumentname())
                .setEquipmentNo(monitorinstrumentDTO.getEquipmentno())
                .setInstrumentTypeId(monitorinstrumentDTO.getInstrumenttypeid())
                .setHospitalCode(instrumentParamConfigCommand.getHospitalCode())
                .setSn(instrumentParamConfigCommand.getSn())
                .setEquipmentName(monitorinstrumentDTO.getEquipmentname())
                .setAlarmTime(instrumentParamConfigCommand.getAlarmtime())
                .setInstrumentConfigId(instrumentParamConfigCommand.getInstrumentconfigid())
                .setInstrumentConfigName(instrumentParamConfigCommand.getInstrumentconfigname())
                .setInstrumentParamConfigNO(instrumentParamConfigNo)
                .setLowLimit(instrumentParamConfigCommand.getLowlimit())
                .setHighLimit(instrumentParamConfigCommand.getHighlimit())
                .setWarningPhone(instrumentParamConfigCommand.getWarningphone())
                .setUnit(StringUtils.isEmpty(instrumentParamConfigCommand.getUnit()) ? "" : instrumentParamConfigCommand.getUnit())
                .setStyleMax(StringUtils.isEmpty(instrumentParamConfigCommand.getStyleMax()) ? "":instrumentParamConfigCommand.getStyleMax())
                .setStyleMin(StringUtils.isEmpty(instrumentParamConfigCommand.getStyleMin()) ? "":instrumentParamConfigCommand.getStyleMin())
                .setCalibration(instrumentParamConfigCommand.getCalibration());
        probeRedisApi.addProbeRedisInfo(instrumentInfoDto);
    }

    /**
     * 构建InstrumentParamConfigInfoCommand对象
     * @param userId 用户id
     * @param old 旧信息
     * @param newInfo 新信息
     * @param type 菜单类型
     * @param operationType 操作类型
     * @return
     */
    private InstrumentParamConfigInfoCommand build(String userId, InstrumentparamconfigCommand old, InstrumentparamconfigCommand newInfo, String type, String operationType) {
        InstrumentParamConfigInfoCommand infoCommand = new InstrumentParamConfigInfoCommand();
        //获取用户名称
        UserBackModel userInfo = hospitalInfoApi.findUserInfo(userId).getResult();
        if(null!=userInfo){
            infoCommand.setUsername(userInfo.getUsername());
        }
        //获取医院信息
        String instrumentNo =  old.getInstrumentNo() != null ? old.getInstrumentNo() : newInfo.getInstrumentNo();
        MonitorinstrumentDTO monitorinstrumentDTO = monitorinstrumentService.selectMonitorByIno(instrumentNo);
        HospitalMadel hospitalInfo = hospitalInfoApi.findHospitalInfo(monitorinstrumentDTO.getHospitalcode()).getResult();
        if(!ObjectUtils.isEmpty(hospitalInfo)){
            infoCommand.setHospitalName(hospitalInfo.getHospitalName());
        }
        //获取设备信息
        String equipmentNo = monitorinstrumentDTO.getEquipmentno();
        List<MonitorEquipmentDto> monitorEquipmentDtoList = monitorEquipmentService.selectMonitorEquipmentInfoByNo(equipmentNo);
        MonitorEquipmentDto monitorEquipmentDto = monitorEquipmentDtoList.get(0);
        if(!ObjectUtils.isEmpty(monitorEquipmentDto)){
            infoCommand.setEquipmentName(monitorEquipmentDto.getEquipmentName());
        }
        String equipmentName = monitorinstrumentDTO.getInstrumentname();
        infoCommand.setEquipmentName(equipmentName);
        infoCommand.setType(type);
        infoCommand.setOperationType(operationType);
        infoCommand.setInstrumentName(newInfo.getInstrumentname());
        infoCommand.setInstrumentparamconfigno(newInfo.getInstrumentparamconfigno());
        infoCommand.setInstrumentNo(newInfo.getInstrumentNo());
        InstrumentparamconfigLogCommand logCommand = new InstrumentparamconfigLogCommand()
                .setInstrumentparamconfigno(newInfo.getInstrumentparamconfigno())
                .setSn(newInfo.getSn())
                .setLowlimit(newInfo.getLowlimit())
                .setHighlimit(newInfo.getHighlimit())
                .setCalibration(newInfo.getCalibration())
                .setChannel(newInfo.getChannel())
                .setAlarmtime(newInfo.getAlarmtime())
                .setWarningphone(newInfo.getWarningphone());
        infoCommand.setNewInstrumentparamconfigLogCommand(logCommand);
        InstrumentparamconfigLogCommand convert = BeanConverter.convert(old, InstrumentparamconfigLogCommand.class);
        infoCommand.setOldInstrumentparamconfigLogCommand(convert);
        return infoCommand;
    }

    /**
     * 修改探头信息
     *
     * @param instrumentParamConfigCommand 探头信息参数
     */
    @GlobalTransactional
    public void editInstrumentParamConfig(InstrumentparamconfigCommand instrumentParamConfigCommand) {
        InstrumentparamconfigDTO dto =  instrumentparamconfigService.selectInstrumentparamconfigInfo(instrumentParamConfigCommand.getInstrumentparamconfigno());
        MonitorinstrumentDTO monitorinstrumentDTO = monitorinstrumentService.selectMonitorByIno(instrumentParamConfigCommand.getInstrumentNo());
        String sn = monitorinstrumentDTO.getSn();
        dto.setSn(sn);
        //更新探头信息
        InstrumentparamconfigDTO instrumentparamconfigDTO = buildInstrumentparamconfigDTO(instrumentParamConfigCommand);
        instrumentparamconfigService.updateInfo(instrumentparamconfigDTO);
        //更新设备
        String newWarningPhone = instrumentParamConfigCommand.getWarningphone();
        String oldWarningPhone = dto.getWarningphone();
        String equipmentNo = monitorinstrumentDTO.getEquipmentno();
        MonitorEquipmentDto monitorEquipmentDto = new MonitorEquipmentDto();
        monitorEquipmentDto.setEquipmentNo(equipmentNo);
        //当探头报警开关发生变化时在修改设备的报警开关(有一个探头开启设备开启，所有探头关闭设备关闭)
        //设备报警开关只作为app报警设置接口查看用
        if(!newWarningPhone.equals(oldWarningPhone)){
            build(equipmentNo, monitorEquipmentDto, instrumentparamconfigService);
            //当修改探头报警状态时更新设备数据库
            monitorEquipmentService.updateMonitorEquipment(monitorEquipmentDto);

            //更新设备缓存(当报警状态发生改变时修改)
            SnDeviceDto result1 = snDeviceRedisApi.getSnDeviceDto(sn).getResult();
            if(!ObjectUtils.isEmpty(result1) && ! newWarningPhone.equals(result1.getWarningSwitch())){
                result1.setWarningSwitch(monitorEquipmentDto.getWarningSwitch());
                snDeviceRedisApi.updateSnDeviceDtoSync(result1);
            }
        }
        //添加日志信息
        InstrumentParamConfigInfoCommand instrumentParamConfigInfoCommand =
                build(Context.getUserId(),
                        BeanConverter.convert(dto,InstrumentparamconfigCommand.class),
                        instrumentParamConfigCommand,
                        OperationLogEunm.PROBE_MANAGEMENT.getCode(),
                        OperationLogEunmDerailEnum.EDIT.getCode());
        operationlogService.addInstrumentparamconfig(instrumentParamConfigInfoCommand);

        //更新redis缓存
        addProbeRedisInfo(monitorinstrumentDTO,instrumentParamConfigCommand,instrumentParamConfigCommand.getInstrumentparamconfigno());
    }

    static void build(String equipmentNo, MonitorEquipmentDto monitorEquipmentDto, InstrumentparamconfigService instrumentparamconfigService) {
        List<InstrumentparamconfigDTO> instrumentConfigDTOS = instrumentparamconfigService.getInstrumentParamConfigInfo(equipmentNo);
        long count = instrumentConfigDTOS.stream().filter(res -> SysConstants.IN_ALARM.equals(res.getWarningphone())).count();
        if(count>0){
            monitorEquipmentDto.setWarningSwitch(SysConstants.IN_ALARM);
        }else {
            monitorEquipmentDto.setWarningSwitch(SysConstants.NORMAL);
        }
    }

    private InstrumentparamconfigDTO buildInstrumentparamconfigDTO(InstrumentparamconfigCommand instrumentParamConfigCommand) {
        return new InstrumentparamconfigDTO()
                .setInstrumentparamconfigno(instrumentParamConfigCommand.getInstrumentparamconfigno())
                .setInstrumentno(instrumentParamConfigCommand.getInstrumentNo())
                .setInstrumentconfigid(instrumentParamConfigCommand.getInstrumentconfigid())
                .setInstrumentname(instrumentParamConfigCommand.getInstrumentname())
                .setLowlimit(instrumentParamConfigCommand.getLowlimit())
                .setHighlimit(instrumentParamConfigCommand.getHighlimit())
                .setInstrumenttypeid(instrumentParamConfigCommand.getInstrumenttypeid())
                .setInstrumentconfigname(instrumentParamConfigCommand.getInstrumentconfigname())
                .setWarningphone(instrumentParamConfigCommand.getWarningphone())
                .setChannel(instrumentParamConfigCommand.getChannel())
                .setSaturation(instrumentParamConfigCommand.getSaturation())
                .setCalibration(instrumentParamConfigCommand.getCalibration())
                .setUnit(StringUtils.isEmpty(instrumentParamConfigCommand.getUnit()) ? "":instrumentParamConfigCommand.getUnit())
                .setStyleMax(StringUtils.isEmpty(instrumentParamConfigCommand.getStyleMax()) ? "":instrumentParamConfigCommand.getStyleMax())
                .setStyleMin(StringUtils.isEmpty(instrumentParamConfigCommand.getStyleMin()) ? "":instrumentParamConfigCommand.getStyleMin())
                .setAlarmtime(instrumentParamConfigCommand.getAlarmtime());
    }

    /**
     * 清除探头信息
     *
     * @param instrumentParamConfigNos 探头信息参数
     */
    @GlobalTransactional
    public void removeInstrumentParamConfig(String[] instrumentParamConfigNos) {
        if(!ObjectUtils.isEmpty(instrumentParamConfigNos)){
            List<InstrumentparamconfigDTO> dos = instrumentparamconfigService.selectInstrumentparamconfigAllInfo();
            Map<String, InstrumentparamconfigDTO> collect = dos.stream().collect(Collectors.toMap(InstrumentparamconfigDTO::getInstrumentparamconfigno, Function.identity()));
            instrumentparamconfigService.deleteInfos(instrumentParamConfigNos);

            for (String instrumentParamConfigNo : instrumentParamConfigNos) {
                InstrumentparamconfigDTO instrumentparamconfigDTO = collect.get(instrumentParamConfigNo);
                if (!ObjectUtils.isEmpty(instrumentparamconfigDTO)) {
                    InstrumentParamConfigInfoCommand build = build(Context.getUserId()
                            , BeanConverter.convert(instrumentparamconfigDTO, InstrumentparamconfigCommand.class)
                            , new InstrumentparamconfigCommand()
                            , OperationLogEunm.PROBE_MANAGEMENT.getCode()
                            , OperationLogEunmDerailEnum.REMOVE.getCode());
                    operationlogService.addInstrumentparamconfig(build);
                }
                //清除redis信息
                removeProbeRedisInfo(instrumentparamconfigDTO.getInstrumentno(),instrumentparamconfigDTO.getInstrumentno()+":"+instrumentparamconfigDTO.getInstrumentconfigid());
            }
        }
    }

    /**
     * 删除redis信息
     * @param instrumentno
     * @param str
     */
    private void removeProbeRedisInfo(String instrumentno, String str) {
        MonitorinstrumentDTO monitorinstrumentDTO = monitorinstrumentService.selectMonitorByIno(instrumentno);
        String hospitalcode = monitorinstrumentDTO.getHospitalcode();
        probeRedisApi.removeProbeRedisInfo(LabManageMentServiceEnum.P.getCode() + hospitalcode,str);
    }

    /**
     * 分页获取探头信息
     *
     * @param instrumentParamConfigCommand 探头信息参数
     * @return
     */
    @GlobalTransactional
    public Page<InstrumentparamconfigVo> findInstrumentParamConfig(InstrumentparamconfigCommand instrumentParamConfigCommand) {
        Page<InstrumentparamconfigVo> page = new Page<>(instrumentParamConfigCommand.getPageCurrent(), instrumentParamConfigCommand.getPageSize());
        List<InstrumentparamconfigDTO> instrumentParamConfigList = instrumentparamconfigService.findInstrumentparamconfig(page, instrumentParamConfigCommand);
        List<InstrumentparamconfigVo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(instrumentParamConfigList)) {
            //去除有探头信息没有设备信息的垃圾数据
            List<InstrumentparamconfigDTO> removeList = instrumentParamConfigList.stream().filter(res -> StringUtils.isEmpty(res.getHospitalcode())).collect(Collectors.toList());
            instrumentParamConfigList.removeAll(removeList);
            String lang = Context.getLang();
            boolean flag = "en".equals(lang);
            for (InstrumentparamconfigDTO configDTO : instrumentParamConfigList) {
                InstrumentparamconfigVo build = InstrumentparamconfigVo
                        .builder()
                        .instrumentparamconfigno(configDTO.getInstrumentparamconfigno())
                        .hospitalname(configDTO.getHospitalname())
                        .hospitalCode(configDTO.getHospitalcode())
                        .equipmentName(configDTO.getEquipmentname())
                        .equipmenttypename(flag?configDTO.getEquipmenttypename_us() : configDTO.getEquipmenttypename())
                        .instrumentname(configDTO.getInstrumentname())
                        .instrumentno(configDTO.getInstrumentno())
                        .sn(configDTO.getSn())
                        .firsttime(configDTO.getFirsttime())
                        .channel(configDTO.getChannel())
                        .instrumentconfigid(configDTO.getInstrumentconfigid())
                        .instrumentconfigname(configDTO.getInstrumentconfigname())
                        .instrumenttypeid(configDTO.getInstrumenttypeid())
                        .instrumenttypename(configDTO.getInstrumenttypename())
                        .alarmtime(configDTO.getAlarmtime())
                        .lowlimit(configDTO.getLowlimit())
                        .highlimit(configDTO.getHighlimit())
                        .saturation(configDTO.getSaturation())
                        .warningphone(configDTO.getWarningphone())
                        .calibration(configDTO.getCalibration() == null ? "" : configDTO.getCalibration())
                        .saturation(configDTO.getSaturation())
                        .unit(StringUtils.isEmpty(configDTO.getUnit()) ? "":configDTO.getUnit())
                        .styleMax(StringUtils.isEmpty(configDTO.getStyleMax()) ? "":configDTO.getStyleMax())
                        .styleMin(StringUtils.isEmpty(configDTO.getStyleMin()) ? "":configDTO.getStyleMin())
                        .build();
                list.add(build);
            }
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 查询探头监控信息
     * @return
     */
    public List<InstrumentmonitorDto> selectInstrumentMonitorInfo(String hospitalCode) {
        List<InstrumentmonitorDTO>  dtoList = instrumentmonitorService.selectInstrumentMonitorInfo(hospitalCode);
        if(CollectionUtils.isEmpty(dtoList)) {
            return  null;
        }
        return BeanConverter.convert(dtoList, InstrumentmonitorDto.class);
    }

    /**
     * 更新最新一次的报警时间
     * @param instrumentParamConfigNo 探头检测信息id
     * @param warningTime 报警时间
     */
    public void editWarningTime(String instrumentParamConfigNo, String warningTime){
        instrumentparamconfigService.editWarningTime(instrumentParamConfigNo,warningTime);
    }

    /**
     * 获取设备未添加的设备探头监测类型
     * @param equipmentNo
     * @return
     */
    public List<InstrumentparamconfigVo> getEquipmentUnAddMonitorTypeByNo(String equipmentNo) {
        //获取设备的探头的监测类型
        List<InstrumentConfigDTO> instrumentConfigList = instrumentparamconfigService.selectInstrumentparamconfigByEqNo(equipmentNo);
        if(CollectionUtils.isEmpty(instrumentConfigList)){
            return null;
        }
        //获取设备已添加的探头监测类型
        List<String> instrumentConfigIdList  = instrumentparamconfigService.getEquipmentAddProbeInfo(equipmentNo);
        //当获取设备已添加的探头监测类型不为空时过滤信息
        if(CollectionUtils.isNotEmpty(instrumentConfigIdList)){
            List<InstrumentConfigDTO> removeList = new ArrayList<>();
            instrumentConfigList.forEach(res->{
                if (instrumentConfigIdList.contains(String.valueOf(res.getInstrumentconfigid()))) {
                    removeList.add(res);
                }
            });
            if(CollectionUtils.isNotEmpty(removeList)){
                instrumentConfigList.removeAll(removeList);
            }

        }
        List<InstrumentparamconfigVo> instrumentParamConfigVos = new ArrayList<>();
        instrumentConfigList.forEach(s -> {
            instrumentParamConfigVos.add(
                    InstrumentparamconfigVo.builder()
                            .instrumentconfigid(s.getInstrumentconfigid())
                            .instrumentconfigname(s.getInstrumentconfigname())
                            .unit(StringUtils.isEmpty(s.getUnit()) ? "":s.getUnit())
                            .build()
            );
        });
        return instrumentParamConfigVos;
    }

    /**
     * 同步探头单位
     */
    public void syncProbeUnit() {
        //1.查出所有的探头信息
        List<InstrumentparamconfigDTO> probeList = instrumentparamconfigService.list();
        if(CollectionUtils.isEmpty(probeList)){
            return;
        }
        //2.查出所有的instrumentMonitor表信息
        List<InstrumentmonitorDTO> instrumentmonitorDTOS = instrumentmonitorService.selectMonitorEquipmentAll();
        if(CollectionUtils.isEmpty(instrumentmonitorDTOS)){
            return;
        }
        //通过typeId+configId分组
        Map<String, List<InstrumentmonitorDTO>> tidCidMap = instrumentmonitorDTOS.stream().collect(Collectors.groupingBy(res -> res.getInstrumenttypeid() + "" + res.getInstrumentconfigid()));
        //3.遍历所有的探头信息
        for (InstrumentparamconfigDTO instrumentparamconfigDTO : probeList) {
            Integer instrumentTypeId = instrumentparamconfigDTO.getInstrumenttypeid();
            Integer instrumentConfigId = instrumentparamconfigDTO.getInstrumentconfigid();
            if(!tidCidMap.containsKey(instrumentTypeId.toString()+instrumentConfigId)){
                continue;
            }
            List<InstrumentmonitorDTO> instrumentmonitorDTOList = tidCidMap.get(instrumentTypeId.toString() + instrumentConfigId);
            if(CollectionUtils.isEmpty(instrumentmonitorDTOList)){
                continue;
            }
            InstrumentmonitorDTO instrumentmonitorDTO = instrumentmonitorDTOList.get(0);
            String unit = instrumentmonitorDTO.getUnit();
            String styleMin = instrumentmonitorDTO.getStyleMin();
            String styleMax = instrumentmonitorDTO.getStyleMax();
            instrumentparamconfigDTO.setUnit(StringUtils.isEmpty(unit) ? "":unit);
            instrumentparamconfigDTO.setStyleMin(StringUtils.isEmpty(styleMin) ? "":styleMin);
            instrumentparamconfigDTO.setStyleMax(StringUtils.isEmpty(styleMax) ? "":styleMax);
        }
        long start = System.currentTimeMillis();
        //5.更新探头表
        instrumentparamconfigService.updateBatchData(probeList);
        long time = System.currentTimeMillis() - start;
        System.out.println("用时："+time);
    }
}

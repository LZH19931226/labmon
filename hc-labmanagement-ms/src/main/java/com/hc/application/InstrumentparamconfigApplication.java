package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.command.labmanagement.hospital.InstrumentparamconfigLogCommand;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.command.labmanagement.operation.InstrumentParamConfigInfoCommand;
import com.hc.constants.error.MonitorinstrumentEnumCode;
import com.hc.dto.*;
import com.hc.hospital.HospitalInfoApi;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.*;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    /**
     * 通过设备no获取探头参数信息
     *
     * @param equipmentNo 设备id
     * @return 探头信息集合
     */
    public List<InstrumentparamconfigVo> selectInstrumentParamConfigByEqNo(String equipmentNo) {
        List<InstrumentconfigDTO> instrumentConfigList = instrumentparamconfigService.selectInstrumentparamconfigByEqNo(equipmentNo);
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
    @Transactional(rollbackFor = Exception.class)
    public void insertInstrumentParamConfig(InstrumentparamconfigCommand instrumentParamConfigCommand) {
        //判断探头的检测类型是否存在
        boolean flag = instrumentmonitorService.selectOne(new InstrumentmonitorDTO().setInstrumentconfigid(instrumentParamConfigCommand.getInstrumentconfigid())
                .setInstrumenttypeid(instrumentParamConfigCommand.getInstrumenttypeid()));
        if(!flag){
            throw new IedsException("设备探头与检测类型不匹配");
        }

        //判断探头检测类型是否存在
        Integer i =  instrumentparamconfigService.selectCount(instrumentParamConfigCommand.getInstrumentNo(),
                instrumentParamConfigCommand.getInstrumentconfigid(),instrumentParamConfigCommand.getInstrumenttypeid());
        if(i>0){
            throw new IedsException(MonitorinstrumentEnumCode.PROBE_INFORMATION_ALREADY_EXISTS.getMessage());
        }

        //判断上限与下限逻辑
        int compareTo = instrumentParamConfigCommand.getLowlimit().compareTo(instrumentParamConfigCommand.getHighlimit());
        if(compareTo>0){
            throw new IedsException(MonitorinstrumentEnumCode.THE_LOWER_LIMIT_CANNOT_EXCEED_THE_UPPER_LIMIT.getMessage());
        }
        InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                .setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""))
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
        if(!ObjectUtils.isEmpty(userId)){
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
        String equipmentNo = newInfo.getEquipmentNo();
        MonitorEquipmentDto monitorEquipmentDto = monitorEquipmentService.selectMonitorEquipmentInfoByNo(equipmentNo);
        if(!ObjectUtils.isEmpty(monitorEquipmentDto)){
            infoCommand.setEquipmentName(monitorEquipmentDto.getEquipmentName());
        }
        String equipmentName = monitorinstrumentDTO.getInstrumentname().replaceAll("探头", "");
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
    @Transactional(rollbackFor = Exception.class)
    public void editInstrumentParamConfig(InstrumentparamconfigCommand instrumentParamConfigCommand) {
        //计较上限值和下限值
        int compareTo = instrumentParamConfigCommand.getLowlimit().compareTo(instrumentParamConfigCommand.getHighlimit());
        if(compareTo>=0){
            throw new IedsException(MonitorinstrumentEnumCode.THE_LOWER_LIMIT_CANNOT_EXCEED_THE_UPPER_LIMIT.getMessage());
        }

        InstrumentparamconfigDTO dto =
                instrumentparamconfigService.selectInstrumentparamconfigInfo(instrumentParamConfigCommand.getInstrumentparamconfigno());
        MonitorinstrumentDTO monitorinstrumentDTO
                = monitorinstrumentService.selectMonitorByIno(instrumentParamConfigCommand.getInstrumentNo());
        dto.setSn(monitorinstrumentDTO.getSn());

        InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                .setInstrumentparamconfigno(instrumentParamConfigCommand.getInstrumentparamconfigno())
                .setInstrumentno(instrumentParamConfigCommand.getInstrumentNo())
                .setInstrumentconfigid(instrumentParamConfigCommand.getInstrumentconfigid())
                .setInstrumentname(instrumentParamConfigCommand.getInstrumentname())
                .setLowlimit(instrumentParamConfigCommand.getLowlimit())
                .setHighlimit(instrumentParamConfigCommand.getHighlimit())
                .setInstrumenttypeid(instrumentParamConfigCommand.getInstrumenttypeid())
                .setWarningphone(instrumentParamConfigCommand.getWarningphone())
                .setChannel(instrumentParamConfigCommand.getChannel())
                .setSaturation(instrumentParamConfigCommand.getSaturation())
                .setCalibration(instrumentParamConfigCommand.getCalibration())
                .setAlarmtime(instrumentParamConfigCommand.getAlarmtime());
        instrumentparamconfigService.updateInfo(instrumentparamconfigDTO);

        //添加日志信息
        InstrumentParamConfigInfoCommand instrumentParamConfigInfoCommand =
                build(Context.getUserId(),
                        BeanConverter.convert(dto,InstrumentparamconfigCommand.class),
                        instrumentParamConfigCommand,
                        OperationLogEunm.PROBE_MANAGEMENT.getCode(),
                        OperationLogEunmDerailEnum.EDIT.getCode());
        operationlogService.addInstrumentparamconfig(instrumentParamConfigInfoCommand);
    }

    /**
     * 清除探头信息
     *
     * @param instrumentParamConfigNos 探头信息参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeInstrumentParamConfig(String[] instrumentParamConfigNos) {
        List<InstrumentparamconfigDTO> dtos = instrumentparamconfigService.selectInstrumentparamconfigAllInfo();
        Map<String, InstrumentparamconfigDTO> collect = dtos.stream().collect(Collectors.toMap(InstrumentparamconfigDTO::getInstrumentparamconfigno, Function.identity()));
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
        }
    }

    /**
     * 分页获取探头信息
     *
     * @param instrumentParamConfigCommand 探头信息参数
     * @return
     */
    public Page<InstrumentparamconfigVo> findInstrumentParamConfig(InstrumentparamconfigCommand instrumentParamConfigCommand) {
        Page<InstrumentparamconfigVo> page = new Page<>(instrumentParamConfigCommand.getPageCurrent(), instrumentParamConfigCommand.getPageSize());
        List<InstrumentparamconfigDTO> instrumentParamConfigList = instrumentparamconfigService.findInstrumentparamconfig(page, instrumentParamConfigCommand);
        List<InstrumentparamconfigVo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(instrumentParamConfigList)) {

            for (InstrumentparamconfigDTO configDTO : instrumentParamConfigList) {
                InstrumentparamconfigVo build = InstrumentparamconfigVo
                        .builder()
                        .instrumentparamconfigno(configDTO.getInstrumentparamconfigno())
                        .hospitalname(configDTO.getHospitalname())
                        .hospitalCode(configDTO.getHospitalcode())
                        .equipmentName(configDTO.getEquipmentname())
                        .equipmenttypename(configDTO.getEquipmenttypename())
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
                        .build();
                list.add(build);
            }
        }
        page.setRecords(list);
        return page;
    }
}

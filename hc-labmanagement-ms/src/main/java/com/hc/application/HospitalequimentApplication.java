package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.command.labmanagement.hospital.HospitalEquimentTypeInfoCommand;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.command.labmanagement.operation.HospitalEquipmentOperationLogCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.labmanagent.HospitalInfoApi;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.HospitalequimentService;
import com.hc.service.MonitorequipmentwarningtimeService;
import com.hc.service.OperationlogService;
import com.hc.vo.equimenttype.HospitalequimentVo;
import com.hc.vo.equimenttype.MonitorequipmentwarningtimeVo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class HospitalequimentApplication {

    @Autowired
    private HospitalequimentService hospitalequimentService;

    @Autowired
    private MonitorequipmentwarningtimeService monitorequipmentwarningtimeService;

    @Autowired
    private OperationlogService operationlogService;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    /**
     * 新增医院设备类型
     * @param hospitalEquimentTypeCommand
     */
    public void addHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        hospitalequimentService.addHospitalEquimentType(hospitalEquimentTypeCommand);
        String operationType = OperationLogEunmDerailEnum.ADD.getCode();
        String type = OperationLogEunm.DEVICE_TYPE_MANAGEMENT.getCode();
        operationlogService.addHospitalEquipmentOperationLogCommand(build(Context.getUserId(),new HospitalEquimentTypeCommand(),hospitalEquimentTypeCommand,
                type,"0"));
    }

    private HospitalEquipmentOperationLogCommand build(String userId,HospitalEquimentTypeCommand oldHospitalEquimentTypeCommand, HospitalEquimentTypeCommand newHospitalEquimentTypeCommand, String code, String code1) {
        HospitalEquipmentOperationLogCommand logCommand = new HospitalEquipmentOperationLogCommand();
        logCommand.setOperationType(code1);
        logCommand.setType(code);
        UserBackModel userInfo = hospitalInfoApi.findUserInfo(userId).getResult();
        if(!ObjectUtils.isEmpty(userInfo)){
            logCommand.setUsername(userInfo.getUsername());
        }
        HospitalMadel hospitalInfo = hospitalInfoApi.findHospitalInfo(newHospitalEquimentTypeCommand.getHospitalcode()).getResult();
        if(!ObjectUtils.isEmpty(hospitalInfo)){
            logCommand.setHospitalName(hospitalInfo.getHospitalName());
        }
        logCommand.setHospitalCode(newHospitalEquimentTypeCommand.getHospitalcode());
        HospitalEquimentTypeInfoCommand command = BeanConverter.convert(oldHospitalEquimentTypeCommand, HospitalEquimentTypeInfoCommand.class);
        logCommand.setOldHospitalEquimentTypeCommand(command);
        //设置最新值
        HospitalEquimentTypeInfoCommand convert = BeanConverter.convert(newHospitalEquimentTypeCommand, HospitalEquimentTypeInfoCommand.class);
        logCommand.setNewHospitalEquimentTypeCommand(convert);
        return logCommand;

    }

    /**
     * 修改设备类型
     * @param hospitalEquimentTypeCommand
     */
    public void updateHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        HospitalequimentDTO hospitalequimentDTO =
                hospitalequimentService.selectHospitalEquimentInfoByCodeAndTypeId(hospitalEquimentTypeCommand.getHospitalcode(), hospitalEquimentTypeCommand.getEquipmenttypeid());
        hospitalequimentService.updateHospitalEquimentType(hospitalEquimentTypeCommand);
        operationlogService.addHospitalEquipmentOperationLogCommand(
                build(Context.getUserId()
                ,BeanConverter.convert(hospitalequimentDTO,HospitalEquimentTypeCommand.class)
                ,hospitalEquimentTypeCommand,OperationLogEunm.DEVICE_TYPE_MANAGEMENT.getCode()
                ,OperationLogEunmDerailEnum.EDIT.getCode()));

    }

    /**
     * 查询医院设备类型列表
     * @param hospitalEquimentTypeCommand
     * @return
     */
    public Page<HospitalequimentVo> selectHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        Page<HospitalequimentVo> page = new Page<>(hospitalEquimentTypeCommand.getPageCurrent(),hospitalEquimentTypeCommand.getPageSize());
        List<HospitalequimentVo> hospitalEquipmentVos = new ArrayList<>();
        List<HospitalequimentDTO> hospitalEquipmentList = hospitalequimentService.selectHospitalEquimentType(page,hospitalEquimentTypeCommand);
        if (CollectionUtils.isNotEmpty(hospitalEquipmentList)) {
            List<String> hospitalCodes = hospitalEquipmentList.stream().map(HospitalequimentDTO::getHospitalcode).collect(Collectors.toList());
            List<MonitorequipmentwarningtimeDTO> warningTimes  = monitorequipmentwarningtimeService.selectWarningtimeByHosCode(hospitalCodes);
            Map<String, List<MonitorequipmentwarningtimeDTO>> timesMap = new  HashedMap();
            if (CollectionUtils.isNotEmpty(warningTimes)){
                 timesMap = warningTimes.stream().collect(Collectors.groupingBy(MonitorequipmentwarningtimeDTO::getHospitalcode));
            }
            Map<String, List<MonitorequipmentwarningtimeDTO>> finalTimesMap = timesMap;
            hospitalEquipmentList.forEach(s->{
                String hospitalcode = s.getHospitalcode();
                List<MonitorequipmentwarningtimeVo> workTimeBlock = new ArrayList<>();
                if (!finalTimesMap.isEmpty()){
                    List<MonitorequipmentwarningtimeDTO> monitorequipmentwarningtimeDTOS    = finalTimesMap.get(hospitalcode);
                    if (CollectionUtils.isNotEmpty(monitorequipmentwarningtimeDTOS)){
                        workTimeBlock = new ArrayList<>();
                        List<MonitorequipmentwarningtimeVo> finalWorkTimeBlock = workTimeBlock;
                        monitorequipmentwarningtimeDTOS.forEach(f->{
                            MonitorequipmentwarningtimeVo time = MonitorequipmentwarningtimeVo.builder()
                                    .timeblockid(f.getTimeblockid())
                                    .begintime(f.getBegintime())
                                    .endtime(f.getEndtime())
                                    .build();
                            finalWorkTimeBlock.add(time);
                        });
                    }
                }
                HospitalequimentVo hosEqVo = HospitalequimentVo.builder()
                        .hospitalcode(hospitalcode)
                        .hospitalname(s.getHospitalname())
                        .equipmenttypeid(s.getEquipmenttypeid())
                        .equipmenttypename(s.getEquipmenttypename())
                        .isvisible(s.getIsvisible())
                        .alwayalarm(s.getAlwayalarm())
                        .timeout(s.getTimeout())
                        .timeouttime(s.getTimeouttime())
                        .workTimeBlock(workTimeBlock)
                        .build();
                hospitalEquipmentVos.add(hosEqVo);
            });
        }
        page.setRecords(hospitalEquipmentVos);
        return page;
    }

    /**
     * 删除医院设备类型
     * @param hospitalCode
     * @param equipmenttypeid
     */
    public void deleteHospitalEquimentType(String hospitalCode, String equipmenttypeid) {
        hospitalequimentService.deleteHospitalEquimentType(hospitalCode,equipmenttypeid);
    }
}

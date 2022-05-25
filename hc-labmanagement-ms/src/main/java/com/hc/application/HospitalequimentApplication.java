package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.application.command.WorkTimeBlockCommand;
import com.hc.command.labmanagement.hospital.HospitalEquimentTypeInfoCommand;
import com.hc.command.labmanagement.model.HospitalEquipmentTypeModel;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.command.labmanagement.operation.HospitalEquipmentOperationLogCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.hospital.HospitalEquipmentTypeIdApi;
import com.hc.hospital.HospitalInfoApi;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoDto;
import com.hc.my.common.core.redis.dto.MonitorEquipmentWarningTimeDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.HospitalequimentService;
import com.hc.service.MonitorequipmentwarningtimeService;
import com.hc.service.OperationlogService;
import com.hc.vo.equimenttype.HospitalequimentVo;
import com.hc.vo.equimenttype.MonitorequipmentwarningtimeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private HospitalEquipmentTypeIdApi hospitalEquipmentTypeIdApi;

    /**
     * 新增医院设备类型
     * @param hospitalEquimentTypeCommand 医院设备类型命令类
     */
    public void addHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        hospitalequimentService.addHospitalEquimentType(hospitalEquimentTypeCommand);
        //插入日志
        operationlogService.addHospitalEquipmentOperationLogCommand(build(Context.getUserId(),new HospitalEquimentTypeCommand(),hospitalEquimentTypeCommand,
                OperationLogEunm.DEVICE_TYPE_MANAGEMENT.getCode(),OperationLogEunmDerailEnum.ADD.getCode()));
        //存入redis
        HospitalEquipmentTypeInfoDto hospitalEquipmentTypeInfoModel = buildRedisInfo(hospitalEquimentTypeCommand);
        hospitalEquipmentTypeIdApi.addHospitalEquipmentTypeRedisInfo(hospitalEquipmentTypeInfoModel);
    }

    /**
     * 构建设备类型缓存信息
     * @param hospitalEquimentTypeCommand
     * @return
     */
    private HospitalEquipmentTypeInfoDto buildRedisInfo(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {

        HospitalEquipmentTypeInfoDto result = new HospitalEquipmentTypeInfoDto();

        String hospitalcode = hospitalEquimentTypeCommand.getHospitalcode();
        String equipmenttypeid = hospitalEquimentTypeCommand.getEquipmenttypeid();
        //查询设备信息
        HospitalequimentDTO hospitalequimentDTO = hospitalequimentService.selectHospitalEquimentInfoByCodeAndTypeId(hospitalcode, equipmenttypeid);
        String hospitalname = hospitalequimentDTO.getHospitalname();
        String equipmenttypename = hospitalequimentDTO.getEquipmenttypename();

        result.setHospitalcode(hospitalcode);
        result.setEquipmenttypeid(equipmenttypeid);
        result.setHospitalname(hospitalname);
        result.setEquipmenttypename(equipmenttypename);
        result.setIsvisible(hospitalEquimentTypeCommand.getIsvisible());
        result.setAlwayalarm(hospitalEquimentTypeCommand.getAlwayalarm());

        WorkTimeBlockCommand[] workTimeBlock = hospitalEquimentTypeCommand.getWorkTimeBlock();
        if(workTimeBlock != null && workTimeBlock.length !=0 ){
            List<WorkTimeBlockCommand> workTimeBlockCommands = Arrays.asList(workTimeBlock);
            List<MonitorEquipmentWarningTimeDto> convert = BeanConverter.convert(workTimeBlockCommands, MonitorEquipmentWarningTimeDto.class);
            convert.forEach(res -> {
                res.setEquipmentcategory("type");
                res.setEquipmentid(hospitalEquimentTypeCommand.getEquipmenttypeid());
                res.setHospitalcode(hospitalEquimentTypeCommand.getHospitalcode());
            });

            result.setWarningTimeList(convert);
        }
        return result;
    }

    /**
     * 构建医院设备操作日志
     * @param userId 用户id
     * @param oldHospitalEquipmentTypeCommand 旧的医院设备类型信息
     * @param newHospitalEquipmentTypeCommand 新的医院设备类型信息
     * @param type 医院设备类型
     * @param operationType 操作类型
     * @return 医院设备操作日志类型
     */
    private HospitalEquipmentOperationLogCommand build(String userId,HospitalEquimentTypeCommand oldHospitalEquipmentTypeCommand,
                                                       HospitalEquimentTypeCommand newHospitalEquipmentTypeCommand, String type, String operationType) {

        HospitalEquipmentOperationLogCommand logCommand = new HospitalEquipmentOperationLogCommand();
        logCommand.setOperationType(operationType);
        logCommand.setType(type);
        //获取用户信息
        UserBackModel userInfo = hospitalInfoApi.findUserInfo(userId).getResult();
        if(!ObjectUtils.isEmpty(userInfo)){
            logCommand.setUsername(userInfo.getUsername());
        }
        //获取医院信息
        String hospitalCode = oldHospitalEquipmentTypeCommand.getHospitalcode() != null ? oldHospitalEquipmentTypeCommand.getHospitalcode() : newHospitalEquipmentTypeCommand.getHospitalcode();
        if(!StringUtils.isBlank(hospitalCode)){
            HospitalMadel hospitalInfo = hospitalInfoApi.findHospitalInfo(hospitalCode).getResult();
            if(!ObjectUtils.isEmpty(hospitalInfo)){
                logCommand.setHospitalName(hospitalInfo.getHospitalName());
            }
        }
        logCommand.setHospitalCode(newHospitalEquipmentTypeCommand.getHospitalcode());
        HospitalEquimentTypeInfoCommand command = BeanConverter.convert(oldHospitalEquipmentTypeCommand, HospitalEquimentTypeInfoCommand.class);
        logCommand.setOldHospitalEquimentTypeCommand(command);
        //设置最新值
        HospitalEquimentTypeInfoCommand convert = BeanConverter.convert(newHospitalEquipmentTypeCommand, HospitalEquimentTypeInfoCommand.class);
        logCommand.setNewHospitalEquimentTypeCommand(convert);
        return logCommand;
    }

    /**
     * 修改设备类型
     * @param hospitalEquipmentTypeCommand 医院设备类型命令类
     */
    public void updateHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquipmentTypeCommand) {
        HospitalequimentDTO hospitalequimentDTO =
                hospitalequimentService.selectHospitalEquimentInfoByCodeAndTypeId(hospitalEquipmentTypeCommand.getHospitalcode(), hospitalEquipmentTypeCommand.getEquipmenttypeid());

        hospitalequimentService.updateHospitalEquimentType(hospitalEquipmentTypeCommand);

        operationlogService.addHospitalEquipmentOperationLogCommand(
                build(Context.getUserId()
                ,BeanConverter.convert(hospitalequimentDTO,HospitalEquimentTypeCommand.class)
                ,hospitalEquipmentTypeCommand,OperationLogEunm.DEVICE_TYPE_MANAGEMENT.getCode()
                ,OperationLogEunmDerailEnum.EDIT.getCode()));
        //更新缓存信息
        HospitalEquipmentTypeInfoDto hospitalEquipmentTypeInfoModel = buildRedisInfo(hospitalEquipmentTypeCommand);
        hospitalEquipmentTypeIdApi.addHospitalEquipmentTypeRedisInfo(hospitalEquipmentTypeInfoModel);
    }

    /**
     * 查询医院设备类型列表
     * @param hospitalEquipmentTypeCommand 医院设备类型命令类
     * @return
     */
    public Page<HospitalequimentVo> selectHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquipmentTypeCommand) {
        Page<HospitalequimentVo> page = new Page<>(hospitalEquipmentTypeCommand.getPageCurrent(),hospitalEquipmentTypeCommand.getPageSize());
        List<HospitalequimentVo> hospitalEquipmentVos = new ArrayList<>();
        List<HospitalequimentDTO> hospitalEquipmentList = hospitalequimentService.selectHospitalEquimentType(page,hospitalEquipmentTypeCommand);
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
     * @param hospitalCode 医院id
     * @param equipmenttypeid 设备类型Id
     */
    public void deleteHospitalEquimentType(String hospitalCode, String equipmenttypeid) {
        HospitalequimentDTO hospitalequimentDTO = hospitalequimentService.selectHospitalEquimentInfoByCodeAndTypeId(hospitalCode, equipmenttypeid);

        hospitalequimentService.deleteHospitalEquimentType(hospitalCode,equipmenttypeid);

        //添加日志信息
        HospitalEquimentTypeCommand convert = BeanConverter.convert(hospitalequimentDTO, HospitalEquimentTypeCommand.class);
        HospitalEquipmentOperationLogCommand build = build(Context.getUserId(), convert,
                new HospitalEquimentTypeCommand(), OperationLogEunm.DEVICE_TYPE_MANAGEMENT.getCode(), OperationLogEunmDerailEnum.REMOVE.getCode());
        operationlogService.addHospitalEquipmentOperationLogCommand(build);

        //删除从缓存信息
        hospitalEquipmentTypeIdApi.removeHospitalEquipmentTypeRedisInfo(hospitalCode,equipmenttypeid);
    }

    /**
     * 查询医院设备信息集合
     * @param hospitalCode
     * @return
     */
    public List<HospitalEquipmentTypeModel> findHospitalEquipmentTypeByCode(String hospitalCode) {
        List<HospitalequimentDTO> dtoList =  hospitalequimentService.findHospitalEquipmentTypeByCode(hospitalCode);
        List<HospitalEquipmentTypeModel> list = new ArrayList<>();
        if(!ObjectUtils.isEmpty(dtoList)){
            dtoList.forEach(res->{
                HospitalEquipmentTypeModel model = new HospitalEquipmentTypeModel();
                model.setEquipmentTypeId(res.getEquipmenttypeid())
                      .setEquipmentTypeName(res.getEquipmenttypename());
                list.add(model);
            });
        }
        return list;
    }
}

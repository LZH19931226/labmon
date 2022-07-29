package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.ProbeCommand;
import com.hc.constants.LabMonEnumError;
import com.hc.device.ProbeRedisApi;
import com.hc.dto.HospitalEquipmentDto;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.ProbeCurrentInfoDto;
import com.hc.my.common.core.constant.enums.CurrentProbeInfoEnum;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.command.ProbeRedisCommand;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.service.EquipmentInfoService;
import com.hc.service.HospitalEquipmentService;
import com.hc.service.InstrumentParamConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EquipmentInfoAppApplication {

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private HospitalEquipmentService hospitalEquipmentService;

    @Autowired
    private InstrumentParamConfigService instrumentParamConfigService;

    @Autowired
    private ProbeRedisApi probeRedisApi;

    /**
     * 获取app首页设备数量
     * @param hospitalCode
     * @return
     */
    public List<HospitalEquipmentDto> getEquipmentNum(String hospitalCode) {
        //查出医院的设备类型
        List<HospitalEquipmentDto> hospitalEquipmentDto =  hospitalEquipmentService.selectHospitalEquipmentInfo(hospitalCode);
        if (CollectionUtils.isEmpty(hospitalEquipmentDto)) {
           throw new IedsException(LabMonEnumError.HOSPITAL_IS_NOT_BOUND_EQUIPMENT_TYPE.getMessage());
        }
        //补全数据库数据
        completionData(hospitalCode);
        List<MonitorEquipmentDto> equipmentInfoByHospitalCode = equipmentInfoService.getEquipmentInfoByHospitalCode(hospitalCode);
        Map<String, List<MonitorEquipmentDto>> collect = equipmentInfoByHospitalCode.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmenttypeid));
        List<HospitalEquipmentDto> dtoList = new ArrayList<>();
        for (HospitalEquipmentDto equipmentDto : hospitalEquipmentDto) {
            String equipmentTypeId = equipmentDto.getEquipmentTypeId();
            //通过医院id和设备id查询出
            List<MonitorEquipmentDto> list = collect.get(equipmentTypeId);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            int totalNum = list.size();
            long alarmNum = list.stream().filter(res -> Objects.equals(res.getState(), SysConstants.IN_ALARM)).count();
            long normalNum = list.stream().filter(res -> StringUtils.isBlank(res.getState()) || SysConstants.NORMAL.equals(res.getState())).count();
            equipmentDto.setAlarmNum(String.valueOf(alarmNum));
            equipmentDto.setNormalNum(String.valueOf(normalNum));
            equipmentDto.setTotalNum(String.valueOf(totalNum));
            dtoList.add(equipmentDto);
        }
        return dtoList;
    }

    /**
     * 矫正设备被状态
     * 1.查出所有的设备
     *  2.根据探头状态判断设备状态（只要有探头属于报警状态设备应为报警状态，当所有的探头都是正常状态时设备才正常为正常状态）
     * @param hospitalCode
     */
    private void completionData(String hospitalCode) {
        List<MonitorEquipmentDto> list =  equipmentInfoService.getEquipmentInfoByHospitalCode(hospitalCode);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> eNoList = list.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
        List<InstrumentParamConfigDto> configDtoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(eNoList);
        if (CollectionUtils.isEmpty(configDtoList)) {
            return;
        }
        Map<String, List<InstrumentParamConfigDto>> collect = configDtoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno));
        List<MonitorEquipmentDto> result = new ArrayList<>();
        for (MonitorEquipmentDto monitorEquipmentDto : list) {
            String equipmentno = monitorEquipmentDto.getEquipmentno();
            String state = monitorEquipmentDto.getState();
            if (StringUtils.isBlank(monitorEquipmentDto.getState())) {
                state = "0";
            }
            List<InstrumentParamConfigDto> paramConfigDtos = collect.get(equipmentno);
            if(CollectionUtils.isEmpty(paramConfigDtos)){
                continue;
            }
            switch (state){
                //如果设备状态为正常时，判断探头为报警的数量
                case "0":
                    long count = paramConfigDtos.stream().filter(res -> StringUtils.isBlank(res.getState()) && "1".equals(res.getState())).count();
                    if(count>0){
                        monitorEquipmentDto.setState("1");
                        result.add(monitorEquipmentDto);
                    }
                    break;
                //设备状态为报警时，判断探头报警的数量
                case "1":
                    long count1 = paramConfigDtos.stream().filter(res ->StringUtils.isBlank(res.getState()) && "1".equals(res.getState())).count();
                    if(count1<=0){
                        monitorEquipmentDto.setState("0");
                        result.add(monitorEquipmentDto);
                    }
                    break;
                default:
                    break;
            }
        }
        if (CollectionUtils.isNotEmpty(result)) {
            equipmentInfoService.update(result);
        }
    }

    /**
     *
     * @param probeCommand
     * @return
     */
    public Page<ProbeCurrentInfoDto> getTheCurrentValueOfTheProbe(ProbeCommand probeCommand) {
        String hospitalCode = probeCommand.getHospitalCode();
        String equipmentTypeId = probeCommand.getEquipmentTypeId();
        Page<ProbeCurrentInfoDto> page = new Page<>(probeCommand.getPageCurrent(),probeCommand.getPageSize());
        //分页查询设备信息
        List<MonitorEquipmentDto> list = equipmentInfoService.getEquipmentInfoByPage(page,hospitalCode,equipmentTypeId);
        List<String> enoList = list.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
        ProbeRedisCommand probeRedisCommand = new ProbeRedisCommand();
        probeRedisCommand.setHospitalCode(hospitalCode);
        probeRedisCommand.setENoList(enoList);
        //获取设备对象的探头信息
        List<InstrumentParamConfigDto> instrumentParamConfigByENoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(enoList);
        //先以设备no分组在以instrumentconfid分组
        Map<String, Map<Integer, List<InstrumentParamConfigDto>>> collect1 = null;
        if (CollectionUtils.isNotEmpty(instrumentParamConfigByENoList)) {
            collect1 = instrumentParamConfigByENoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno, Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentconfigid)));
        }
        //批量获取设备对应探头当前值信息
        Map<String, List<ProbeInfoDto>> result = probeRedisApi.getTheCurrentValueOfTheProbeInBatches(probeRedisCommand).getResult();
        List<ProbeCurrentInfoDto> probeCurrentInfoDtos = new ArrayList<>();
        //遍历设备信息
        for (MonitorEquipmentDto monitorEquipmentDto : list) {
            String equipmentname = monitorEquipmentDto.getEquipmentname();
            String equipmentno = monitorEquipmentDto.getEquipmentno();
            String sn = monitorEquipmentDto.getSn();
            List<ProbeInfoDto> probeInfoDtoList = null;
            if (result.containsKey(equipmentno)) {
                probeInfoDtoList = result.get(equipmentno);
            }
            //获取探头信息中最大的时间
            Date maxDate = null;
            if(CollectionUtils.isNotEmpty(probeInfoDtoList)){
                List<Date> collect = probeInfoDtoList.stream().map(ProbeInfoDto::getInputTime).collect(Collectors.toList());
                maxDate = Collections.max(collect);
            }
            ProbeCurrentInfoDto probeInfo = new ProbeCurrentInfoDto();
            probeInfo.setEquipmentName(equipmentname);
            probeInfo.setEquipmentNo(equipmentno);
            probeInfo.setSn(sn);
            if(maxDate!=null){
                probeInfo.setInputTime(maxDate);
            }
            if(CollectionUtils.isNotEmpty(probeInfoDtoList)){
                //构建探头高低值
                buildProbeHighAndLowValue(equipmentno, probeInfoDtoList, collect1);
                probeInfo.setProbeInfoDtoList(probeInfoDtoList);
            }
            probeCurrentInfoDtos.add(probeInfo);
        }
        page.setRecords(probeCurrentInfoDtos);
        return page;
    }

    /**
     * 构建探头高低值
     * @param equipmentno
     * @param probeInfoDtoList
     * @param collect1
     * @return
     */
    private void buildProbeHighAndLowValue(String equipmentno, List<ProbeInfoDto> probeInfoDtoList, Map<String, Map<Integer, List<InstrumentParamConfigDto>>> collect1) {
        if (MapUtils.isEmpty(collect1)) {
            return;
        }
        for (ProbeInfoDto probeInfoDto : probeInfoDtoList) {
            Integer instrumentConfigId = probeInfoDto.getInstrumentConfigId();
            switch (instrumentConfigId){
                case 101:
                case 102:
                case 103:
                    String probeEName = probeInfoDto.getProbeEName();
                    getInstrumentConfigId(probeEName,instrumentConfigId);
                    setTheProbeHeightAndLowValue(equipmentno,instrumentConfigId,collect1,probeInfoDto);
                    break;
                default:
                    setTheProbeHeightAndLowValue(equipmentno,instrumentConfigId,collect1,probeInfoDto);
                    break;
            }
        }

    }

    /**
     * 设置探头高低值
     * @param equipmentno 设备no
     * @param instrumentConfigId 检测类型id
     * @param collect1
     * @return
     */
    private void setTheProbeHeightAndLowValue(String equipmentno, Integer instrumentConfigId, Map<String, Map<Integer, List<InstrumentParamConfigDto>>> collect1,ProbeInfoDto probeInfoDto) {
        if (collect1.containsKey(equipmentno) && collect1.get(equipmentno).containsKey(instrumentConfigId)) {
            List<InstrumentParamConfigDto> list1 = collect1.get(equipmentno).get(instrumentConfigId);
            if(CollectionUtils.isNotEmpty(list1) && !ObjectUtils.isEmpty(list1.get(0))){
                InstrumentParamConfigDto instrumentParamConfigDto = list1.get(0);
                probeInfoDto.setSaturation(instrumentParamConfigDto.getSaturation());
                probeInfoDto.setLowLimit(instrumentParamConfigDto.getLowlimit());
                probeInfoDto.setHighLimit(instrumentParamConfigDto.getHighlimit());
            }
        }
    }

    /**
     * MT310根据ename返回检测的id
     * @param probeEName
     * @return
     */
    private void getInstrumentConfigId(String probeEName,int instrumentConfigId) {
        switch (probeEName){
            //温度
            case "1":
                instrumentConfigId = CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId();
                break;
            //湿度
            case "2":
                instrumentConfigId =  CurrentProbeInfoEnum.CURRENTHUMIDITY.getInstrumentConfigId();
                break;
            //O2浓度
            case "3":
                instrumentConfigId =  CurrentProbeInfoEnum.CURRENTO2.getInstrumentConfigId();
                break;
            //CO2浓度
            case "4":
                instrumentConfigId =  CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getInstrumentConfigId();
                break;
        }
    }
}

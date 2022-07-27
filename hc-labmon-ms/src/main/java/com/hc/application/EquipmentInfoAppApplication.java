package com.hc.application;

import com.hc.dto.HospitalEquipmentDto;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.exception.IedsException;
import com.hc.service.EquipmentInfoService;
import com.hc.service.HospitalEquipmentService;
import com.hc.service.InstrumentParamConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public Map<String,HospitalEquipmentDto> getEquipmentNum(String hospitalCode) {
        //查出医院的设备类型
        List<HospitalEquipmentDto> hospitalEquipmentDto =  hospitalEquipmentService.selectHospitalEquipmentInfo(hospitalCode);
        if (CollectionUtils.isEmpty(hospitalEquipmentDto)) {
           throw new IedsException("该医院没有绑定信息");
        }
        //补全数据库数据
        completionData(hospitalCode);
        List<MonitorEquipmentDto> equipmentInfoByHospitalCode = equipmentInfoService.getEquipmentInfoByHospitalCode(hospitalCode);
        Map<String, List<MonitorEquipmentDto>> collect = equipmentInfoByHospitalCode.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmenttypeid));
        Map<String,HospitalEquipmentDto> map = new HashMap<>();
        for (HospitalEquipmentDto equipmentDto : hospitalEquipmentDto) {
            String equipmentTypeId = equipmentDto.getEquipmentTypeId();
            //通过医院id和设备id查询出
            List<MonitorEquipmentDto> list = collect.get(equipmentTypeId);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            List<MonitorEquipmentDto> equipmentInfoByCodeAndTypeId = equipmentInfoService.getEquipmentInfoByCodeAndTypeId(hospitalCode, equipmentTypeId);
            int totalNum = equipmentInfoByCodeAndTypeId.size();
            long alarmNum = equipmentInfoByCodeAndTypeId.stream().filter(res -> Objects.equals(res.getState(), SysConstants.IN_ALARM)).count();
            long normalNum = equipmentInfoByCodeAndTypeId.stream().filter(res -> StringUtils.isBlank(res.getState()) || SysConstants.NORMAL.equals(res.getState())).count();
            equipmentDto.setAlarmNum(String.valueOf(alarmNum));
            equipmentDto.setNormalNum(String.valueOf(normalNum));
            equipmentDto.setTotalNum(String.valueOf(totalNum));
            map.put(equipmentTypeId,equipmentDto);
        }
        return map;
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
}

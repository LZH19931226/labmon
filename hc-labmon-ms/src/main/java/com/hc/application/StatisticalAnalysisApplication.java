package com.hc.application;

import com.hc.application.response.StatisticalResult;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.dto.HospitalInfoDto;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.service.EquipmentInfoService;
import com.hc.service.HospitalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StatisticalAnalysisApplication {

    @Autowired
    private WarningrecordRepository warningrecordRepository;

    @Autowired
    private HospitalInfoService hospitalInfoService;

    @Autowired
    private EquipmentInfoService equipmentInfoService;
    /**
     * 查询医院设备报警数量
     * @param time
     * @return
     */
    public Map<String,Map<String,Long>> getStatisticalData(String time) {
        //1.查询在当前时间内的报警信息
        List<Warningrecord> wrList =  warningrecordRepository.getWarningInfoByTime(time);
        if(CollectionUtils.isEmpty(wrList)){
            return null;
        }
        List<String> hCodeList = wrList.stream().map(Warningrecord::getHospitalcode).distinct().collect(Collectors.toList());
        List<String> enoList = wrList.stream().map(Warningrecord::getEquipmentno).distinct().collect(Collectors.toList());
        //2.通过code查询所有的医院信息
        List<HospitalInfoDto> hospitalInfoDtoList =  hospitalInfoService.getHospitalInfoByCode(hCodeList);
        if(CollectionUtils.isEmpty(hospitalInfoDtoList)){
            return null;
        }
        Map<String, List<HospitalInfoDto>> hCodeMap = hospitalInfoDtoList.stream().collect(Collectors.groupingBy(HospitalInfoDto::getHospitalCode));
        //3.通过eno查询出所有的设备
        List<MonitorEquipmentDto> monitorEquipmentDtoList = equipmentInfoService.batchGetEquipmentInfo(enoList);
        if(CollectionUtils.isEmpty(monitorEquipmentDtoList)){
            return null;
        }
        Map<String, List<MonitorEquipmentDto>> enoMap = monitorEquipmentDtoList.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmentno));
        Map<String,Map<String,Long>> map = new HashMap<>();
        //4.统计医院设备类型的数量
        for (Warningrecord warningrecord : wrList) {
            String equipmentNo = warningrecord.getEquipmentno();
            Long num = warningrecord.getNum();
            if(enoMap.containsKey(equipmentNo)){
                List<MonitorEquipmentDto> monitorEquipmentDtoList1 = enoMap.get(equipmentNo);
                MonitorEquipmentDto monitorEquipmentDto = monitorEquipmentDtoList1.get(0);
                String hospitalName = getHospitalName(monitorEquipmentDto.getHospitalcode(),hCodeMap);
                String equipmentTypeName = getEquipmentName(monitorEquipmentDto.getEquipmenttypeid());
                if(map.containsKey(hospitalName)){
                    Map<String, Long> stringLongMap = map.get(hospitalName);
                    if(stringLongMap.containsKey(equipmentTypeName)){
                        Long aLong = stringLongMap.get(equipmentTypeName);
                        stringLongMap.put(equipmentTypeName,num+aLong);
                    }else {
                        stringLongMap.put(equipmentTypeName,num);
                        map.put(hospitalName,stringLongMap);
                    }
                }else {
                    Map<String,Long> map1 = new HashMap<>();
                    map1.put(equipmentTypeName,num);
                    map.put(hospitalName,map1);
                }
            }
        }
        return map;
    }

    private String getHospitalName(String hospitalcode,Map<String, List<HospitalInfoDto>> hCodeMap) {
        if(hCodeMap.containsKey(hospitalcode)){
            List<HospitalInfoDto> hospitalInfoDtos = hCodeMap.get(hospitalcode);
            return hospitalInfoDtos.get(0).getHospitalName();
        }
        return "";
    }

    private String getEquipmentName(String typeId) {
        String name ;
        switch (typeId){
            case "1":
                name =  "环境";
                break;
            case "2":
                name =  "培养箱";
                break;
            case "3":
                name =  "液氮罐";
                break;
            case "4":
                name =  "冰箱";
                break;
            case "5":
                name =  "操作台";
                break;
            case "6":
                name =  "市电";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }

}


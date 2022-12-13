package com.hc.application;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.application.response.AlarmNoticeResult;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.dto.HospitalInfoDto;
import com.hc.dto.LabMessengerPublishTaskDto;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.UserRightDto;
import com.hc.my.common.core.message.MailCode;
import com.hc.my.common.core.message.SmsCode;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.ExcelExportUtils;
import com.hc.my.common.core.util.FileUtil;
import com.hc.my.common.core.util.ObjectConvertUtils;
import com.hc.service.EquipmentInfoService;
import com.hc.service.HospitalInfoService;
import com.hc.service.LabMessengerPublishTaskService;
import com.hc.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private LabMessengerPublishTaskService labMessengerPublishTaskService;

    @Autowired
    private UserRightService userRightService;


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

    /**
     * 获取报警通知
     * @param alarmNoticeCommand
     * @return
     */
    public Page<AlarmNoticeResult> getAlarmNotice(AlarmNoticeCommand alarmNoticeCommand) {
        Page<AlarmNoticeResult> page = new Page(alarmNoticeCommand.getPageCurrent(),alarmNoticeCommand.getPageSize());
        List<LabMessengerPublishTaskDto> labMessengerPublishTaskDtoList =  labMessengerPublishTaskService.getAlarmNoticeInfo(page,alarmNoticeCommand);
        if(CollectionUtils.isEmpty(labMessengerPublishTaskDtoList)){
            return null;
        }
        List<UserRightDto> userRightDtoList = new ArrayList<>();
        if(StringUtils.isEmpty(alarmNoticeCommand.getPhoneNum())){
            userRightDtoList = userRightService.getallByHospitalCode(alarmNoticeCommand.getHospitalCode());
        }else {
            userRightDtoList = userRightService. getUserRightInfo(alarmNoticeCommand);
        }
        Map<String, List<UserRightDto>> phoneMap = userRightDtoList.stream().collect(Collectors.groupingBy(UserRightDto::getPhoneNum));
        List<AlarmNoticeResult> list = processData(labMessengerPublishTaskDtoList,phoneMap);
        page.setRecords(list);
        return page;
    }

    private List<AlarmNoticeResult> processData(List<LabMessengerPublishTaskDto> labMessengerPublishTaskDtoList, Map<String, List<UserRightDto>> phoneMap) {
        List<AlarmNoticeResult> list = new ArrayList<>();
        String lang = Context.getLang();
        for (LabMessengerPublishTaskDto labMessengerPublishTaskDto : labMessengerPublishTaskDtoList) {
            AlarmNoticeResult alarmNoticeResult = new AlarmNoticeResult();
            alarmNoticeResult.setDataLoggingTime(labMessengerPublishTaskDto.getPublishTime());
            alarmNoticeResult.setPublishType(labMessengerPublishTaskDto.getPublishType());
            alarmNoticeResult.setPhoneNum(labMessengerPublishTaskDto.getPublishKey());
            Integer status = labMessengerPublishTaskDto.getStatus();
            boolean bool =  status == 2 && "OK".equals(labMessengerPublishTaskDto.getRemark());
            alarmNoticeResult.setState(bool ? "success" : "fail");
            if(bool){
                alarmNoticeResult.setFReason("");
            }else {
                String remark = labMessengerPublishTaskDto.getRemark();
                String publishType = labMessengerPublishTaskDto.getPublishType();
                alarmNoticeResult.setFReason("SMS".equals(publishType) ? SmsCode.SmsCodeParse(remark) : MailCode.MailCodePase(remark));
            }
//            if("zh".equals(lang)){
                alarmNoticeResult.setMailContent(labMessengerPublishTaskDto.getMessageCover()+"出现异常,请尽快查看");
//            }else {
//                alarmNoticeResult.setMailContent(String.format("There is an exception in %s, please check",labMessengerPublishTaskDto.getMessageCover()));
//            }
            if (phoneMap.containsKey(labMessengerPublishTaskDto.getPublishKey())) {
                alarmNoticeResult.setUserName(phoneMap.get(labMessengerPublishTaskDto.getPublishKey()).get(0).getUsername());
            }else {
                alarmNoticeResult.setUserName("");
            }
            list.add(alarmNoticeResult);
        }
        return list;
    }

    public void exportAlarmNotice(AlarmNoticeCommand alarmNoticeCommand, HttpServletResponse response) {
        //1.获取报警通知模板
        List<ExcelExportEntity> beanList = ExcelExportUtils.getAlarmNoticeModel();
        //2.查出数据库信息
        List<LabMessengerPublishTaskDto> alarmNoticeInfo = labMessengerPublishTaskService.getAlarmNoticeInfo(null, alarmNoticeCommand);
        if(CollectionUtils.isEmpty(alarmNoticeInfo)){
            return;
        }
        List<UserRightDto> userRightDtoList = userRightService.getallByHospitalCode(alarmNoticeCommand.getHospitalCode());
        Map<String, List<UserRightDto>> phoneMap = userRightDtoList.stream().collect(Collectors.groupingBy(UserRightDto::getPhoneNum));
        List<AlarmNoticeResult> alarmNoticeResults = processData(alarmNoticeInfo, phoneMap);
        //获取属性map
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (AlarmNoticeResult alarmNoticeResult : alarmNoticeResults) {
            Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(alarmNoticeResult);
            mapList.add(objectToMap);
        }
        System.out.println(mapList);
        FileUtil.exportExcel(ExcelExportUtils.ALARM_NOTICE,beanList,mapList,response);
    }
}


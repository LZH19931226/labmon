package com.hc.application;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AlarmDataCommand;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.application.response.*;
import com.hc.clickhouse.param.AlarmDataParam;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.device.ProbeRedisApi;
import com.hc.dto.HospitalInfoDto;
import com.hc.dto.LabMessengerPublishTaskDto;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.UserRightDto;
import com.hc.my.common.core.constant.enums.DataFieldEnum;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.message.MailCode;
import com.hc.my.common.core.message.SmsCode;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.*;
import com.hc.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static com.hc.my.common.core.util.ObjectConvertUtils.getObjectToMap;

@Component
public class StatisticalAnalysisApplication {

    @Autowired
    private ProbeRedisApi probeRedisApi;

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

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;

    @Autowired
    private ExportLogService exportLogService;

    @Autowired
    private MonitorInstrumentService monitorInstrumentService;


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
        Page<AlarmNoticeResult> page = new Page<>(alarmNoticeCommand.getPageCurrent(),alarmNoticeCommand.getPageSize());
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
        Map<String, List<UserRightDto>> phoneMap = userRightDtoList.stream().filter(res->StringUtils.isNotBlank(res.getPhoneNum())).collect(Collectors.groupingBy(UserRightDto::getPhoneNum));
        List<AlarmNoticeResult> list = processData(labMessengerPublishTaskDtoList,phoneMap);
        page.setRecords(list);
        return page;
    }

    private List<AlarmNoticeResult> processData(List<LabMessengerPublishTaskDto> labMessengerPublishTaskDtoList, Map<String, List<UserRightDto>> phoneMap) {
        List<AlarmNoticeResult> list = new ArrayList<>();
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
        Map<String, List<UserRightDto>> phoneMap = userRightDtoList.stream().filter(res->StringUtils.isNotBlank(res.getPhoneNum())).collect(Collectors.groupingBy(UserRightDto::getPhoneNum));
        List<AlarmNoticeResult> alarmNoticeResults = processData(alarmNoticeInfo, phoneMap);
        //获取属性map
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (AlarmNoticeResult alarmNoticeResult : alarmNoticeResults) {
            Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(alarmNoticeResult);
            mapList.add(objectToMap);
        }
        exportLogService.buildLogInfo(alarmNoticeCommand.getUserId(),ExcelExportUtils.ALARM_DATA_NOTICE, OperationLogEunmDerailEnum.EXPORT.getCode(),OperationLogEunm.ALARM_NOTIFICATION_QUERY.getCode());
        FileUtil.exportExcel(ExcelExportUtils.ALARM_DATA_NOTICE,beanList,mapList,response);
    }

    /**
     * 获取设备数据
     * @param equipmentDataCommand
     */
    public Page getEquipmentData(EquipmentDataCommand equipmentDataCommand) {
        //过滤filter为空的信息
        List<EquipmentDataCommand.Filter> filterList = equipmentDataCommand.getFilterList();
        filterList.removeIf(res->StringUtils.isEmpty(res.getField()) || StringUtils.isEmpty(res.getValue()) || StringUtils.isEmpty(res.getCondition()));

        //获取设备信息
        String equipmentNo = equipmentDataCommand.getEquipmentNo();
        MonitorEquipmentDto equipmentInfoByNo = equipmentInfoService.getEquipmentInfoByNo(equipmentNo);
        String instrumenttypeid = equipmentInfoByNo.getInstrumenttypeid();
        Page page = new Page<>(equipmentDataCommand.getPageCurrent(),equipmentDataCommand.getPageSize());
        switch (instrumenttypeid){
            case "112":
                return getEquipmentMt310DcData(equipmentDataCommand,page);
            default:
                return getEquipmentDefultData(equipmentDataCommand,page);
        }
    }

    private Page getEquipmentDefultData(EquipmentDataCommand equipmentDataCommand,Page page) {
        List<String> fieldList = equipmentDataCommand.getFieldList();
        if(CollectionUtils.isEmpty(fieldList)){
            return page;
        }
        //分页查询
        String startTime = equipmentDataCommand.getStartTime();
        String yearMonth = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(yearMonth);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList =  monitorequipmentlastdataRepository.getEquipmentData(page,dataParam);
        if (CollectionUtils.isEmpty(lastDataList)) {
            return page;
        }
        List<LastDataResult> resultList = new ArrayList<>();
        for (Monitorequipmentlastdata lastData : lastDataList) {
            Map<String, Object> objectToMap = getObjectToMap(lastData);
            ObjectConvertUtils.filterMap(objectToMap,fieldList);
            for (String field : objectToMap.keySet()) {
                if(fieldList.contains(field)){
                    String unit = DataFieldEnum.fromByLastDataField(field).getUnit();
                    String value = (String) objectToMap.get(field);
                    objectToMap.put(field,StringUtils.isEmpty(value) ? "":value+"("+unit+")");
                }
            }
            LastDataResult result = JSON.parseObject(JSON.toJSONString(objectToMap), LastDataResult.class);
            resultList.add(result);
        }
        page.setRecords(resultList);
        return page;
    }

    /**
     * 培养箱310DC
     * @param equipmentDataCommand
     * @return
     */
    public Page getEquipmentMt310DcData(EquipmentDataCommand equipmentDataCommand,Page page){
        //获取设备信息
        String equipmentNo = equipmentDataCommand.getEquipmentNo();
        MonitorEquipmentDto equipmentInfoByNo = equipmentInfoService.getEquipmentInfoByNo(equipmentNo);
        String hospitalCode = equipmentInfoByNo.getHospitalcode();

        List<String> fieldList = equipmentDataCommand.getFieldList();
        //过滤filter为空的信息
        List<EquipmentDataCommand.Filter> filterList = equipmentDataCommand.getFilterList();
        filterList.removeIf(res->StringUtils.isEmpty(res.getField()) || StringUtils.isEmpty(res.getValue()) || StringUtils.isEmpty(res.getCondition()));
        String instrumenttypeid = equipmentInfoByNo.getInstrumenttypeid();
        Map<String,String> map = new HashMap<>();
        if(StringUtils.equals("112",instrumenttypeid)){
            map.put("outerO2",null);
            map.put("outerCO2",null);
            map.put("currenttemperature",null);
            map.put("currenthumidity",null);
            //如果是112为310Dc特殊设备
            //获取设备当前值来确认外置探头的位置
            List<ProbeInfoDto> result = probeRedisApi.getCurrentProbeValueInfo(hospitalCode, equipmentNo).getResult();
            for (ProbeInfoDto probeInfoDto : result) {
                if(probeInfoDto.getInstrumentConfigId()==101){
                    String eName = probeInfoDto.getProbeEName();
                    setMap(eName,map,"1",fieldList);
                    fieldList.add("probe1model");
                    fieldList.add("probe1data");
                }
                if(probeInfoDto.getInstrumentConfigId()==102){
                    String eName = probeInfoDto.getProbeEName();
                    setMap(eName,map,"2",fieldList);
                    fieldList.add("probe2model");
                    fieldList.add("probe2data");
                }
                if(probeInfoDto.getInstrumentConfigId()==103){
                    String eName = probeInfoDto.getProbeEName();
                    setMap(eName,map,"3",fieldList);
                    fieldList.add("probe3model");
                    fieldList.add("probe3data");
                }
            }
        }
        //分页查询
        String startTime = equipmentDataCommand.getStartTime();
        String yearMonth = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(yearMonth);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList =  monitorequipmentlastdataRepository.getEquipmentData(page,dataParam);
        List<LastDataResult> resultList = new ArrayList<>();
        for (Monitorequipmentlastdata lastData : lastDataList) {
            Map<String, Object> objectToMap = getObjectToMap(lastData);
            Map<String,Object> resultMap = new HashMap<>();
            ObjectConvertUtils.filterMap(objectToMap,fieldList);
            boolean flag1 = false;
            boolean flag2 = false;
            for (String field : objectToMap.keySet()) {
                //记录时间
                if(StringUtils.equals(field,"inputdatetime")){
                    String value = (String) objectToMap.get(field);
                    resultMap.put("inputdatetime",value);
                }

                //内置探头
                if(StringUtils.equals(field,"currentcarbondioxide") && fieldList.contains(field)){
                    String unit = DataFieldEnum.fromByLastDataField(field).getUnit();
                    String value = (String) objectToMap.get(field);
                    resultMap.put(field,StringUtils.isEmpty(value) ? "":RegularUtil.checkContainsNumbers(value) ? value+"("+unit+")":"");
                }
                if(StringUtils.equals(field,"currento2") && fieldList.contains(field)){
                    String unit = DataFieldEnum.fromByLastDataField(field).getUnit();
                    String value = (String) objectToMap.get(field);
                    resultMap.put(field,StringUtils.isEmpty(value) ? "":value+"("+unit+")");
                }
                if(StringUtils.equals(field,"currentvoc") && fieldList.contains(field)){
                    String unit = DataFieldEnum.fromByLastDataField(field).getUnit();
                    String value = (String) objectToMap.get(field);
                    resultMap.put(field,StringUtils.isEmpty(value) ? "":value+"("+unit+")");
                }

                //外置Co2探头
                if( map.containsKey("outerCO2") && !flag1){
                    String model = map.get("outerCO2");
                    if(StringUtils.isBlank(model)){
                        resultMap.put("outerCO2","");
                    }else{
                        if(StringUtils.equals(model,"1")){
                            String value = (String) objectToMap.get("probe1data");
                            resultMap.put("outerCO2",StringUtils.isEmpty(value) ? "":value+"("+DataFieldEnum.fromByLastDataField("currentcarbondioxide").getUnit()+")");
                            flag1 = true;
                        }
                        if(StringUtils.equals(model,"2")){
                            String value = (String) objectToMap.get("probe2data");
                            resultMap.put("outerCO2",StringUtils.isEmpty(value) ? "":value+"("+DataFieldEnum.fromByLastDataField("currentcarbondioxide").getUnit()+")");
                            flag1 = true;
                        }
                        if(StringUtils.equals(model,"3")){
                            String value = (String) objectToMap.get("probe3data");
                            resultMap.put("outerCO2",StringUtils.isEmpty(value) ? "":value+"("+DataFieldEnum.fromByLastDataField("currentcarbondioxide").getUnit()+")");
                            flag1 = true;
                        }
                    }
                }
                //外置O2探头
                if( map.containsKey("outerO2") && !flag2){
                    String model = map.get("outerO2");
                    if(StringUtils.isBlank(model)){
                        resultMap.put("outerO2","");
                    }else {
                        if (StringUtils.equals(model, "1")) {
                            String value = (String) objectToMap.get("probe1data");
                            resultMap.put("outerO2", StringUtils.isEmpty(value) ? "" : value + "(" + DataFieldEnum.fromByLastDataField("currento2").getUnit() + ")");
                            flag2 = true;
                        }
                        if (StringUtils.equals(model, "2")) {
                            String value = (String) objectToMap.get("probe2data");
                            resultMap.put("outerO2", StringUtils.isEmpty(value) ? "" : value + "(" + DataFieldEnum.fromByLastDataField("currento2").getUnit() + ")");
                            flag2 = true;
                        }
                        if (StringUtils.equals(model, "3")) {
                            String value = (String) objectToMap.get("probe3data");
                            resultMap.put("outerO2", StringUtils.isEmpty(value) ? "" : value + "(" + DataFieldEnum.fromByLastDataField("currento2").getUnit() + ")");
                            flag2 = true;
                        }
                    }
                }
                //外置温度探头
                if(StringUtils.equals(field,"currenttemperature") && map.containsKey("currenttemperature")){
                    String model = map.get("currenttemperature");
                    if(StringUtils.isBlank(model)){
                        resultMap.put("currenttemperature","");
                    }else {
                        if(StringUtils.equals(model,"1")){
                            String value = (String) objectToMap.get("probe1data");
                            resultMap.put("currenttemperature",StringUtils.isEmpty(value) ? "":value+"("+DataFieldEnum.fromByLastDataField("currenttemperature").getUnit()+")");
                        }
                        if(StringUtils.equals(model,"2")){
                            String value = (String) objectToMap.get("probe2data");
                            resultMap.put("currenttemperature",StringUtils.isEmpty(value) ? "":value+"("+DataFieldEnum.fromByLastDataField("currenttemperature").getUnit()+")");
                        }
                        if(StringUtils.equals(model,"3")){
                            String value = (String) objectToMap.get("probe3data");
                            resultMap.put("currenttemperature",StringUtils.isEmpty(value) ? "":value+"("+DataFieldEnum.fromByLastDataField("currenttemperature").getUnit()+")");
                        }
                    }
                }
                //外置湿度探头
                if(StringUtils.equals(field,"currenthumidity") && map.containsKey("currenthumidity")){
                    String model = map.get("currenthumidity");
                    if(StringUtils.isBlank(model)){
                        resultMap.put("currenthumidity","");
                    }else {
                        if(StringUtils.equals(model,"1")){
                            String value = (String) objectToMap.get("probe1data");
                            resultMap.put("currenthumidity",StringUtils.isEmpty(value) ? "":value+"("+DataFieldEnum.fromByLastDataField("currenthumidity").getUnit()+")");
                        }
                        if(StringUtils.equals(model,"2")){
                            String value = (String) objectToMap.get("probe2data");
                            resultMap.put("currenthumidity",StringUtils.isEmpty(value) ? "":value+"("+DataFieldEnum.fromByLastDataField("currenthumidity").getUnit()+")");
                        }
                        if(StringUtils.equals(model,"3")){
                            String value = (String) objectToMap.get("probe3data");
                            resultMap.put("currenthumidity",StringUtils.isEmpty(value) ? "":value+"("+DataFieldEnum.fromByLastDataField("currenthumidity").getUnit()+")");
                        }
                    }
                }
            }
            LastDataResult result = JSON.parseObject(JSON.toJSONString(resultMap), LastDataResult.class);
            resultList.add(result);
        }
        page.setRecords(resultList);
        return page;
    }

    private void setMap(String eName, Map<String, String> map,String str,List<String> fieldList) {
        switch (eName){
            case "1":
                map.put("currenttemperature",str);
                break;
            case "2":
                map.put("currenthumidity",str);
                break;
            case "3":
                map.put("outerCO2",str);
                fieldList.remove("outerCO2");
                break;
            case "4":
                map.put("outerO2",str);
                fieldList.remove("outerO2");
                break;
        }
    }

    /**
     * 获取报警数量
     * @param equipmentDataCommand
     */
    public SummaryOfAlarmsResult getSummaryOfAlarms(EquipmentDataCommand equipmentDataCommand) {
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam convert = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Warningrecord> wrList = warningrecordRepository.getSummaryOfAlarms(convert);
        Map<String, List<Warningrecord>> wrMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(wrList)){
            wrMap = wrList.stream().collect(Collectors.groupingBy(res->DateUtils.getMMdd(res.getTime())));
        }
        List<String> timeList  = DateUtils.getTimePeriod(equipmentDataCommand.getStartTime(),equipmentDataCommand.getEndTime());
        List<String> numList = new ArrayList<>();
        for (String time : timeList) {
            if(wrMap.containsKey(time)){
                Warningrecord warningrecord = wrMap.get(time).get(0);
                numList.add(String.valueOf(warningrecord.getNum()));
            }else {
                numList.add("0");
            }
        }
        SummaryOfAlarmsResult summaryOfAlarmsResult = new SummaryOfAlarmsResult();
        summaryOfAlarmsResult.setTimeList(timeList);
        summaryOfAlarmsResult.setNumList(numList);
        return summaryOfAlarmsResult;
    }

    /**
     * 导出设备数据
     * @param equipmentDataCommand
     * @param response
     */
    public void exportEquipmentData(EquipmentDataCommand equipmentDataCommand, HttpServletResponse response) {
        List<EquipmentDataCommand.Filter> filterList = equipmentDataCommand.getFilterList();
        if(CollectionUtils.isEmpty(filterList)){
            throw new IedsException("筛选条件不能为空");
        }
        filterList.removeIf(res->StringUtils.isEmpty(res.getField()) || StringUtils.isEmpty(res.getValue()) || StringUtils.isEmpty(res.getCondition()));
        String startTime = equipmentDataCommand.getStartTime();
        String yearMonth = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(yearMonth);
        List<String> fieldList = equipmentDataCommand.getFieldList();
        String instrumentTypeId =  monitorInstrumentService.getInstrumentTypeId(equipmentDataCommand.getEquipmentNo());
        if(SysConstants.EQ_MT310DC.equals(instrumentTypeId)){
            EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
            Mt310DCUtils.get310DCFields(dataParam.getFieldList());
            List<Monitorequipmentlastdata> equipmentData = monitorequipmentlastdataRepository.getEquipmentData(null, dataParam);
            List<Map<String,Object>> resultList = new ArrayList<>();
            for (Monitorequipmentlastdata equipmentDatum : equipmentData) {
                Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(equipmentDatum);
                mt310cFilter(objectToMap,fieldList);
                resultList.add(objectToMap);
            }
            List<ExcelExportEntity> mt310DCEqData = ExcelExportUtils.getMT310DCEqData(Context.IsCh(),fieldList);
            exportLogService.buildLogInfo(Context.getUserId(),ExcelExportUtils.EQUIPMENT_DATA_CUSTOM, OperationLogEunmDerailEnum.EXPORT.getCode(), OperationLogEunm.CUSTOM_QUERY.getCode());
            FileUtil.exportExcel(ExcelExportUtils.EQUIPMENT_DATA_CUSTOM,mt310DCEqData,resultList,response);
            return;
        }
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> equipmentData = monitorequipmentlastdataRepository.getEquipmentData(null, dataParam);
        //设置tittle
        List<ExcelExportEntity> beanList = ExcelExportUtils.getEquipmentData(fieldList);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (Monitorequipmentlastdata equipmentDatum : equipmentData) {
            Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(equipmentDatum);
            ObjectConvertUtils.filterMap(objectToMap,fieldList);
            mapList.add(objectToMap);
        }
        exportLogService.buildLogInfo(Context.getUserId(),ExcelExportUtils.EQUIPMENT_DATA_CUSTOM, OperationLogEunmDerailEnum.EXPORT.getCode(), OperationLogEunm.CUSTOM_QUERY.getCode());
        FileUtil.exportExcel(ExcelExportUtils.EQUIPMENT_DATA_CUSTOM,beanList,mapList,response);
    }

    /**
     * 过滤objMap 将以mt310的格式返回 值带单位
     * map："key":"value(单位)"
     */
    private void mt310cFilter(Map<String, Object> objectToMap,List<String> fieldList) {
        editObjMap(objectToMap,(String)objectToMap.get("probe1model"),(String)objectToMap.get("probe1data"));
        editObjMap(objectToMap,(String)objectToMap.get("probe2model"),(String)objectToMap.get("probe2data"));
        editObjMap(objectToMap,(String)objectToMap.get("probe3model"),(String)objectToMap.get("probe3data"));
        ObjectConvertUtils.filterMap(objectToMap,fieldList);
    }

    /**
     * 过滤objMap 将以mt310的格式返回 值不带单位
     * map："key":"value"
     *
     */
    private void mt310DcFilter(Map<String, Object> objectToMap,List<String> fieldList) {
        editObjectMap(objectToMap,(String)objectToMap.get("probe1model"),(String)objectToMap.get("probe1data"));
        editObjectMap(objectToMap,(String)objectToMap.get("probe2model"),(String)objectToMap.get("probe2data"));
        editObjectMap(objectToMap,(String)objectToMap.get("probe3model"),(String)objectToMap.get("probe3data"));
        ObjectConvertUtils.filterMap(objectToMap,fieldList);
    }

    /**
     * map 中value是有单位的
     * @param objectToMap
     * @param model
     * @param data
     */
    private  void editObjMap(Map<String, Object> objectToMap,String model,String data){
        switch (model){
            case SysConstants.MT310DC_TEMP:
                DataFieldEnum dataFieldEnum1 = DataFieldEnum.fromByLastDataField(SysConstants.MT310DC_DATA_TEMP);
                objectToMap.put(SysConstants.MT310DC_DATA_TEMP,data+"("+dataFieldEnum1.getUnit()+")");
                break;
            case SysConstants.MT310DC_RH:
                DataFieldEnum dataFieldEnum2 = DataFieldEnum.fromByLastDataField(SysConstants.MT310DC_DATA_RH);
                objectToMap.put(SysConstants.MT310DC_DATA_RH,data+"("+dataFieldEnum2.getUnit()+")");
                break;
            case SysConstants.MT310DC_O2:
                DataFieldEnum dataFieldEnum3 = DataFieldEnum.fromByLastDataField(SysConstants.MT310DC_DATA_OUTER_O2);
                objectToMap.put(SysConstants.MT310DC_DATA_OUTER_O2,data+"("+dataFieldEnum3.getUnit()+")");
                break;
            case SysConstants.MT310DC_CO2:
                DataFieldEnum dataFieldEnum4 = DataFieldEnum.fromByLastDataField(SysConstants.MT310DC_DATA_OUTER_CO2);
                objectToMap.put(SysConstants.MT310DC_DATA_OUTER_CO2,data+"("+dataFieldEnum4.getUnit()+")");
                break;
        }
    }

    /**
     * map 中value是无单位的
     * @param objectToMap
     * @param model
     * @param data
     */
    private  void editObjectMap(Map<String, Object> objectToMap,String model,String data){
        switch (model){
            case SysConstants.MT310DC_TEMP:
                objectToMap.put(SysConstants.MT310DC_DATA_TEMP,data);
                break;
            case SysConstants.MT310DC_RH:
                objectToMap.put(SysConstants.MT310DC_DATA_RH,data);
                break;
            case SysConstants.MT310DC_O2:
                objectToMap.put(SysConstants.MT310DC_DATA_OUTER_O2,data);
                break;
            case SysConstants.MT310DC_CO2:
                objectToMap.put(SysConstants.MT310DC_DATA_OUTER_CO2,data);
                break;
        }
    }


    /**
     * 时间点查询
     * @param equipmentDataCommand
     */
    public  List<TimePointCurve>  getThePointInTimeDataCurve(EquipmentDataCommand equipmentDataCommand) {
        //校验筛选条件不能为空
        filterCondition(equipmentDataCommand);
        String field = equipmentDataCommand.getField();
        List<String> timeList = equipmentDataCommand.getTimeList();
        if(CollectionUtils.isEmpty(timeList)){
            return new ArrayList<>();
        }
        String instrumentTypeId = monitorInstrumentService.getInstrumentTypeId(equipmentDataCommand.getEquipmentNo());
        //校验日期格式数据
        List<String> timeLists = DateUtils.filterDate(timeList);
        //排列
        List<Date> dateList = timeLists.stream().filter(res->!StringUtils.isEmpty(res)).map(DateUtils::parseDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        boolean flag = SysConstants.EQ_MT310DC.equals(instrumentTypeId);
        List<Monitorequipmentlastdata> lastDataList = null;
        if(flag){
            dataParam.setField(Mt310DCUtils.get310DCList(dataParam.getField()));
            lastDataList =  monitorequipmentlastdataRepository.getMT310DcLastDataByTime(dataParam);
        }else {
            lastDataList =  monitorequipmentlastdataRepository.getLastDataByTime(dataParam);
        }

        if(CollectionUtils.isEmpty(lastDataList)){
            return new ArrayList<>();
        }
        //根据天分组
        Map<String, List<Monitorequipmentlastdata>> stringListMap = lastDataList.stream().collect(Collectors.groupingBy(res -> DateUtils.paseDateMMdd(res.getInputdatetime())));

        Map<String, List<Monitorequipmentlastdata>> dataMap = sortMapByKey(stringListMap);
        Map<String,TimePointCurve> map = new HashMap<>();
        for (String inputTime : dataMap.keySet()) {
            //获取当天的数据
            List<Monitorequipmentlastdata> dataList = dataMap.get(inputTime);
            //遍历时间点
            for (Date date : dateList) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MINUTE,-30);
                Date start = cal.getTime();
                List<Monitorequipmentlastdata> list = dataList.stream().filter(res -> DateUtils.whetherItIsIn(res.getInputdatetime(), start, date)).collect(Collectors.toList());
                //取字段数据都是最新的对象
                Monitorequipmentlastdata lastObject = listToObject(list);
                Map<String, Object> objectToMap = getObjectToMap(lastObject);
                if(flag){
                    mt310DcFilter(objectToMap,new ArrayList<>(Collections.singletonList(field)));
                }
                String value =  (String)objectToMap.get(field);
                String hHmm = DateUtils.dateReduceHHmm(date);
                if(map.containsKey(hHmm)){
                    TimePointCurve timePointCurve = map.get(hHmm);
                    List<String> xaxis = timePointCurve.getXaxis();
                    List<String> series = timePointCurve.getSeries();
                    xaxis.add(inputTime);
                    series.add(StringUtils.isEmpty(value)?"0":RegularUtil.checkContainsNumbers(value) ? value:"0");
                    map.put(hHmm,timePointCurve);
                }else {
                    TimePointCurve curve = new TimePointCurve();
                    List<String> xaxis = new ArrayList<>();
                    List<String> series = new ArrayList<>();
                    xaxis.add(inputTime);
                    series.add(StringUtils.isEmpty(value)?"0":RegularUtil.checkContainsNumbers(value) ? value:"0");
                    curve.setXaxis(xaxis);
                    curve.setSeries(series);
                    curve.setName(hHmm);
                    map.put(hHmm,curve);
                }
            }
        }
        List<TimePointCurve> list = new ArrayList<>();
        for (String key : map.keySet()) {
            TimePointCurve curve = map.get(key);
            list.add(curve);
        }
        return list.stream().sorted(Comparator.comparing(TimePointCurve::getName)).collect(Collectors.toList());
    }

    public static Map<String, List<Monitorequipmentlastdata>> sortMapByKey(Map<String, List<Monitorequipmentlastdata>> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        TreeMap<String, List<Monitorequipmentlastdata>> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                return obj1.compareTo(obj2);//升序排序
            }
        });
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * list转化为对象
     * @param monitorequipmentlastdataList
     * @return
     */
    private Monitorequipmentlastdata listToObject(List<Monitorequipmentlastdata> monitorequipmentlastdataList) {
        //将list排序以时间递增
        List<Monitorequipmentlastdata> lastDataList =
                monitorequipmentlastdataList.stream().sorted(Comparator.comparing(Monitorequipmentlastdata::getInputdatetime)).collect(Collectors.toList());
        Map<String,Object> map = new HashMap<>();
        //随着数组的遍历该设备的探头监测信息都会被替换成为最新的(也就是离当前时间点最近的有效值)
        for (Monitorequipmentlastdata monitorequipmentlastdata : lastDataList) {
            Map<String, Object> objectToMap = getObjectToMap(monitorequipmentlastdata);
            for (String fieldName : objectToMap.keySet()) {
                if (map.containsKey(fieldName)) {
                    Object object = map.get(fieldName);
                    if (!ObjectUtils.isEmpty(objectToMap.get(fieldName))) {
                        object = objectToMap.get(fieldName);
                    }
                    map.put(fieldName,object);
                }else {
                    map.put(fieldName,objectToMap.get(fieldName));
                }
            }
        }
        return JSON.parseObject(JSON.toJSONString(map),Monitorequipmentlastdata.class);
    }


    /**
     * 过滤map
     * @param objectMap
     */
    private void filterMap(Map<String, Object> objectMap,String field) {
        List<String> list = new ArrayList<>();
        list.add("inputdatetime");
        list.add("remark1");
        list.add(field);
        Iterator<String> iterator = objectMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if(!list.contains(next)){
                iterator.remove();
                objectMap.remove(next);
            }
        }
    }

    /**
     * 时间点查询表格
     * @param equipmentDataCommand
     */
    public  List<Map<String,String>> getThePointInTimeDataTable(EquipmentDataCommand equipmentDataCommand) {
        //1.验证参数
        filterCondition(equipmentDataCommand);
        String field = equipmentDataCommand.getField();
        List<String> timeList = equipmentDataCommand.getTimeList();
        if(CollectionUtils.isEmpty(timeList)){
            return new ArrayList<>();
        }
        String instrumentTypeId  = monitorInstrumentService.getInstrumentTypeId(equipmentDataCommand.getEquipmentNo());
        boolean flag = SysConstants.EQ_MT310DC.equals(instrumentTypeId);
        List<String> timeLists = DateUtils.filterDate(timeList);
        //2.查询数据源
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList;
        if(flag){
            dataParam.setField(Mt310DCUtils.get310DCList(dataParam.getField()));
            lastDataList = monitorequipmentlastdataRepository.getMT310DcLastDataByTime(dataParam);
        }else {
            lastDataList =  monitorequipmentlastdataRepository.getLastDataByTime(dataParam);
        }
        if(CollectionUtils.isEmpty(lastDataList)){
            return  new ArrayList<>();
        }
        //3.加工数据
        //思路：将数据源按照天分组拿到一天的所有数据1，再通过时间点拿到一个时间点前半个小时到这个时间点的数据2，再获取数据2中最靠近时间点的一条数据(有就设置没有就设置为0)
        Map<String, List<Monitorequipmentlastdata>> dateMap = lastDataList.stream().collect(Collectors.groupingBy(res -> DateUtils.paseDate(res.getInputdatetime())));

        List<Map<String,String>> list = new ArrayList<>();
        Calendar cal  = Calendar.getInstance();
        String unit = DataFieldEnum.fromByLastDataField(field).getUnit();
        for (String date : dateMap.keySet()) {
            Map<String,String> map = new HashMap<>();
            map.put("date",date);
            map.put("unit",unit);
            List<Monitorequipmentlastdata> monitorequipmentlastdata = dateMap.get(date);
            for (String timeStr : timeLists) {
                String hHmmTime = DateUtils.getHHmm(timeStr);
                //将00:30 改为 00:00 将23:59改为24:00
                String hhMm = editHhmm(hHmmTime);
                Date rTime = DateUtils.parseDate(timeStr);
                cal.setTime(rTime);
                cal.add(Calendar.MINUTE,-30);
                Date lTime = cal.getTime();
                List<Monitorequipmentlastdata> collect = monitorequipmentlastdata.stream().filter(res -> DateUtils.whetherItIsIn(res.getInputdatetime(),lTime,rTime)).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(collect)){
                    map.put(hhMm,"");
                    continue;
                }
                Monitorequipmentlastdata data = collect.stream().max(Comparator.comparing(Monitorequipmentlastdata::getInputdatetime)).get();
                Map<String, Object> objectToMap = getObjectToMap(data);
                if(flag){
                    mt310DcFilter(objectToMap,new ArrayList<>(Collections.singletonList(field)));
                }
                if(objectToMap.containsKey(field)){
                    String str = (String) objectToMap.get(field);
                    map.put(hhMm,str);
                }else {
                    map.put(hhMm,"");
                }
            }
            list.add(map);
        }
        return list.stream().sorted(Comparator.comparing(o -> o.get("date"))).collect(Collectors.toList());
    }
    public void exportDatePoint(EquipmentDataCommand equipmentDataCommand,HttpServletResponse httpServletResponse) {
        //1.验证参数
        filterCondition(equipmentDataCommand);
        String field = equipmentDataCommand.getField();
        List<String> timeList = equipmentDataCommand.getTimeList();
        if(CollectionUtils.isEmpty(timeList)){
            return;
        }
        String instrumentTypeId  = monitorInstrumentService.getInstrumentTypeId(equipmentDataCommand.getEquipmentNo());
        boolean flag = SysConstants.EQ_MT310DC.equals(instrumentTypeId);
        List<String> timeLists = DateUtils.filterDate(timeList);
        //排列
        List<Date> dateList = timeLists.stream().filter(res->!StringUtils.isEmpty(res)).map(DateUtils::parseDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        //2.查询数据源
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList;
        if(flag){
            dataParam.setField(Mt310DCUtils.get310DCList(dataParam.getField()));
            lastDataList = monitorequipmentlastdataRepository.getMT310DcLastDataByTime(dataParam);
        }else {
            lastDataList =  monitorequipmentlastdataRepository.getLastDataByTime(dataParam);
        }
        if(CollectionUtils.isEmpty(lastDataList)){
            return;
        }
        //3.加工数据
        //思路：将数据源按照天分组拿到一天的所有数据1，再通过时间点拿到一个时间点前半个小时到这个时间点的数据2，再获取数据2中最靠近时间点的一条数据(有就设置没有就设置为0)
        Map<String, List<Monitorequipmentlastdata>> dateMap = lastDataList.stream().collect(Collectors.groupingBy(res -> DateUtils.paseDate(res.getInputdatetime())));

        List<Map<String,Object>> list = new ArrayList<>();
        Calendar cal  = Calendar.getInstance();
        String unit = DataFieldEnum.fromByLastDataField(field).getUnit();
        for (String date : dateMap.keySet()) {
            Map<String,Object> map = new HashMap<>();
            map.put("date",date);
            map.put("unit",unit);
            List<Monitorequipmentlastdata> monitorequipmentlastdata = dateMap.get(date);
            for (String timeStr : timeLists) {
                String hHmmTime = DateUtils.getHHmm(timeStr);
                //将00:30 改为 00:00 将23:59改为24:00
                String hhMm = editHhmm(hHmmTime);
                Date rTime = DateUtils.parseDate(timeStr);
                cal.setTime(rTime);
                cal.add(Calendar.MINUTE,-30);
                Date lTime = cal.getTime();
                List<Monitorequipmentlastdata> collect = monitorequipmentlastdata.stream().filter(res -> DateUtils.whetherItIsIn(res.getInputdatetime(),lTime,rTime)).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(collect)){
                    map.put(hhMm,"");
                    continue;
                }
                Monitorequipmentlastdata data = collect.stream().max(Comparator.comparing(Monitorequipmentlastdata::getInputdatetime)).get();
                Map<String, Object> objectToMap = getObjectToMap(data);
                if(flag){
                    mt310DcFilter(objectToMap,new ArrayList<>(Collections.singletonList(field)));
                }
                if(objectToMap.containsKey(field)){
                    String str = (String) objectToMap.get(field);
                    DataFieldEnum dataFieldEnum = DataFieldEnum.fromByLastDataField(field);
                    map.put(hhMm,str+"("+dataFieldEnum.getUnit()+")");
                }else {
                    map.put(hhMm,"");
                }
            }
            list.add(map);
        }
        List<Map<String, Object>> mapList = list.stream().sorted(Comparator.comparing(o -> (String) o.get("date"))).collect(Collectors.toList());
        //获取tittle
        List<ExcelExportEntity> beanList = ExcelExportUtils.getDatePoint(dateList,flag);

//        exportLogService.buildLogInfo(Context.getUserId(),ExcelExportUtils.EQUIPMENT_DATA_POINT_IN_TIME, OperationLogEunmDerailEnum.EXPORT.getCode(),OperationLogEunm.TIMEOUT_POINT_QUERY.getCode());
        FileUtil.exportExcel(ExcelExportUtils.EQUIPMENT_DATA_POINT_IN_TIME,beanList,mapList,httpServletResponse);

    }

    private String editHhmm(String hHmm) {
        if(hHmm.equals("00:30")){
            return "00:00";
        }
        if(hHmm.equals("23:59")){
            return "24:00";
        }
        return hHmm;
    }

    private void filterCondition(EquipmentDataCommand equipmentDataCommand) {
        List<EquipmentDataCommand.Filter> filterList = equipmentDataCommand.getFilterList();
        if(CollectionUtils.isEmpty(filterList)){
            throw new IedsException("筛选条件不能为空");
        }
        filterList.removeIf(res->StringUtils.isEmpty(res.getField()) || StringUtils.isEmpty(res.getValue()) || StringUtils.isEmpty(res.getCondition()));
    }

    /**
     *
     * @param alarmDataCommand
     * @return
     */
    public Page getAlarmData(AlarmDataCommand alarmDataCommand) {
        setEquipmentNo(alarmDataCommand);
        //分页查询设备报警数量
        Page page = new Page<>(alarmDataCommand.getPageCurrent(),alarmDataCommand.getPageSize());
        AlarmDataParam alarmDataParam = BeanConverter.convert(alarmDataCommand, AlarmDataParam.class);
        List<Warningrecord> list = warningrecordRepository.getAlarmData(page,alarmDataParam);
        if(CollectionUtils.isEmpty(list)){
            return page;
        }
        List<Warningrecord> resultList = transformData(list);
        page.setRecords(resultList);
        return page;
    }

    /**
     * 导出查询报警数据
     * @param alarmDataCommand
     * @param response
     */
    public void exportAlarmData(AlarmDataCommand alarmDataCommand, HttpServletResponse response) {
        setEquipmentNo(alarmDataCommand);
        AlarmDataParam alarmDataParam = BeanConverter.convert(alarmDataCommand, AlarmDataParam.class);
        List<Warningrecord> list = warningrecordRepository.getAlarmData(null,alarmDataParam);
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        //获取标头
        List<ExcelExportEntity> beanList = ExcelExportUtils.getAlarmData(Context.IsCh());

        List<Warningrecord> resultList = transformData(list);
        List<Map<String,Object>> mapList = new ArrayList<>();
        resultList.forEach(res->{
            Map<String, Object> objectToMap = getObjectToMap(res);
            mapList.add(objectToMap);
        });
        exportLogService.buildLogInfo(Context.getUserId(),ExcelExportUtils.ALARM_DATA_SUMMARY,OperationLogEunmDerailEnum.EXPORT.getCode(),OperationLogEunm.ALARM_SUMMARY_QUERY.getCode());
        FileUtil.exportExcel(ExcelExportUtils.ALARM_DATA_SUMMARY,beanList,mapList,response);
    }

    private  List<Warningrecord> transformData(List<Warningrecord> list) {
        List<String> enoList = list.stream().map(Warningrecord::getEquipmentno).collect(Collectors.toList());
        List<MonitorEquipmentDto> monitorEquipmentDtoList = equipmentInfoService.batchGetEquipmentInfo(enoList);
        Map<String, List<MonitorEquipmentDto>> enoMap = monitorEquipmentDtoList.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmentno));
        for (Warningrecord warningrecord : list) {
            String equipmentNo = warningrecord.getEquipmentno();
            if (enoMap.containsKey(equipmentNo)) {
                MonitorEquipmentDto monitorEquipmentDto = enoMap.get(equipmentNo).get(0);
                String equipmentName = monitorEquipmentDto.getEquipmentname();
                String equipmentTypeName = monitorEquipmentDto.getEquipmentTypeName();
                warningrecord.setEquipmentName(equipmentName);
                warningrecord.setEquipmentTypeName(equipmentTypeName);
            }
        }
        return list;
    }

    private void setEquipmentNo(AlarmDataCommand alarmDataCommand) {
        String equipmentTypeId = alarmDataCommand.getEquipmentTypeId();
        String equipmentNo = alarmDataCommand.getEquipmentNo();
        //情况1：
        if (StringUtils.isEmpty(equipmentTypeId) && StringUtils.isEmpty(equipmentNo)) {
            alarmDataCommand.setEquipmentNo("");
        }
        //情况2：
        if(!StringUtils.isEmpty(equipmentTypeId) && StringUtils.isEmpty(equipmentNo)){
            List<String> enoList =  equipmentInfoService.getEnoList(alarmDataCommand.getHospitalCode(),alarmDataCommand.getEquipmentTypeId());
            if(!CollectionUtils.isEmpty(enoList) && enoList.size()>1){
                StringBuilder stringBuilder = new StringBuilder();
                enoList.forEach(res->{
                    stringBuilder.append("'").append(res).append("'").append(",");
                });
                String substring = stringBuilder.substring(0, stringBuilder.length() - 1);
                alarmDataCommand.setEquipmentNo(substring);
            }
            if(enoList.size() == 1){
                String eno = enoList.get(0);
                alarmDataCommand.setEquipmentNo("'"+eno+"'");
            }
        }
        //情况3:
        if(!StringUtils.isEmpty(equipmentTypeId) && !StringUtils.isEmpty(equipmentNo)){
            alarmDataCommand.setEquipmentNo("'"+equipmentNo+"'");
        }
    }

    public AlarmDataCurveResult getAlarmDataCurve(AlarmDataCommand alarmDataCommand) {
        setEquipmentNo(alarmDataCommand);
        //分页查询设备报警数量
        AlarmDataParam alarmDataParam = BeanConverter.convert(alarmDataCommand, AlarmDataParam.class);
        List<Warningrecord> list = warningrecordRepository.getAlarmData(null,alarmDataParam);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<Warningrecord> resultList = transformData(list);
        AlarmDataCurveResult alarmDataCurveResult = new AlarmDataCurveResult();
        List<String> equipmentNameList = new ArrayList<>();
        List<Long> numList = new ArrayList<>();
        for (Warningrecord warningrecord : resultList) {
            equipmentNameList.add(warningrecord.getEquipmentName());
            numList.add(warningrecord.getNum());
        }
        alarmDataCurveResult.setEquipmentNameList(equipmentNameList);
        alarmDataCurveResult.setNumList(numList);
        return alarmDataCurveResult;
    }
}


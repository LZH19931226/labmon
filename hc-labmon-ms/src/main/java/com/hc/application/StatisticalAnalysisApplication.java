package com.hc.application;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.application.response.*;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.command.labmanagement.operation.ExportLogCommand;
import com.hc.dto.*;
import com.hc.labmanagent.OperationlogApi;
import com.hc.my.common.core.constant.enums.DataFieldEnum;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.message.MailCode;
import com.hc.my.common.core.message.SmsCode;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.*;
import com.hc.service.*;
import com.hc.util.EquipmentInfoServiceHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static com.hc.my.common.core.util.ObjectConvertUtils.getObjectToMap;

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

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;

    @Autowired
    private InstrumentParamConfigService instrumentParamConfigService;

    @Autowired
    private OperationlogApi operationlogApi;

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
        buildLogInfo(alarmNoticeCommand.getUsrId(),ExcelExportUtils.ALARM_NOTICE, OperationLogEunmDerailEnum.EXPORT.getCode());
        FileUtil.exportExcel(ExcelExportUtils.ALARM_NOTICE,beanList,mapList,response);
    }

    /**
     * 获取设备数据
     * @param equipmentDataCommand
     */
    public Page getEquipmentData(EquipmentDataCommand equipmentDataCommand) {
        List<EquipmentDataCommand.Filter> filterList = equipmentDataCommand.getFilterList();
        if(CollectionUtils.isEmpty(filterList)){
            throw new IedsException("筛选条件不能为空");
        }
        filterList.removeIf(res->StringUtils.isEmpty(res.getField()) || StringUtils.isEmpty(res.getValue()) || StringUtils.isEmpty(res.getCondition()));
        //分页查询
        Page page = new Page<>(equipmentDataCommand.getPageCurrent(),equipmentDataCommand.getPageSize());
        String startTime = equipmentDataCommand.getStartTime();
        String field = equipmentDataCommand.getField();
        String yearMonth = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(yearMonth);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList =  monitorequipmentlastdataRepository.getEquipmentData(page,dataParam);
        if (CollectionUtils.isEmpty(lastDataList)) {
            return page;
        }
        List<LastDataResult> convert = BeanConverter.convert(lastDataList, LastDataResult.class);
        DataFieldEnum dataFieldEnum = DataFieldEnum.fromByLastDataField(field);
        if(null != dataFieldEnum){
            convert.forEach(res-> {
                res.setUnit(dataFieldEnum.getUnit());
            });
        }
        page.setRecords(convert);
        return page;
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

        List<Long> numList = new ArrayList<>();
        for (String time : timeList) {
            if(wrMap.containsKey(time)){
                Warningrecord warningrecord = wrMap.get(time).get(0);
                numList.add(warningrecord.getNum());
            }else {
                numList.add(0L);
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
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> equipmentData = monitorequipmentlastdataRepository.getEquipmentData(null, dataParam);
        //设置tittle
        String field = equipmentDataCommand.getField();
        DataFieldEnum dataFieldEnum = DataFieldEnum.fromByLastDataField(field);
        String cName = dataFieldEnum.getCName();
        String unit = dataFieldEnum.getUnit();
        List<ExcelExportEntity> beanList = ExcelExportUtils.getEquipmentData(cName + unit, field);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (Monitorequipmentlastdata equipmentDatum : equipmentData) {
            Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(equipmentDatum);
            mapList.add(objectToMap);
        }
        buildLogInfo(Context.getUserId(),ExcelExportUtils.EQUIPMENT_DATA, OperationLogEunmDerailEnum.EXPORT.getCode());
        FileUtil.exportExcel(ExcelExportUtils.EQUIPMENT_DATA,beanList,mapList,response);
    }

    /**
     * 时间点查询
     * @param equipmentDataCommand
     */
    public Map<String,CurveInfoDto>  getThePointInTimeDataCurve(EquipmentDataCommand equipmentDataCommand) {
        List<EquipmentDataCommand.Filter> filterList = equipmentDataCommand.getFilterList();
        if(CollectionUtils.isEmpty(filterList)){
            throw new IedsException("筛选条件不能为空");
        }
        filterList.removeIf(res->StringUtils.isEmpty(res.getField()) || StringUtils.isEmpty(res.getValue()) || StringUtils.isEmpty(res.getCondition()));
        List<String> timeList = equipmentDataCommand.getTimeList();
        if(CollectionUtils.isEmpty(timeList) || timeList.size()>5){
            return null;
        }
        //排列
        List<Date> dateList = timeList.stream().map(DateUtils::parseDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList =  monitorequipmentlastdataRepository.getLastDataByTime(dataParam);
        if(CollectionUtils.isEmpty(lastDataList)){
            return null;
        }
        Map<String, List<InstrumentParamConfigDto>> strListMap = instrumentParamConfigService.getInstrumentParamConfigByENo(equipmentDataCommand.getEquipmentNo());
        //以时间进行分组
        Map<String,CurveInfoDto> map = new HashMap<>();
        for (Date date : dateList) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE,-30);
            Date start = cal.getTime();
            List<Monitorequipmentlastdata> collect = lastDataList.stream().filter(res -> DateUtils.whetherItIsIn(res.getInputdatetime(), start, date)).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(collect)){
                continue;
            }
            List<Monitorequipmentlastdata> monitorEquipmentLastDataList = filterData(collect,equipmentDataCommand.getField());
            if(CollectionUtils.isEmpty(monitorEquipmentLastDataList)){
                continue;
            }
            CurveInfoDto curveFirst = EquipmentInfoServiceHelp.getCurveFirst(monitorEquipmentLastDataList, strListMap, true);
            map.put(DateUtils.dateReduceHHmm(date),curveFirst);
        }
        return map;
    }

    private List<Monitorequipmentlastdata> filterData(List<Monitorequipmentlastdata> lastDataList, String field) {
        //先以时间（MM-dd）分组
        Map<String,List<Monitorequipmentlastdata>>  map = new HashMap<>();
        for (Monitorequipmentlastdata lastData : lastDataList) {
            Date inputdatetime = lastData.getInputdatetime();
            String time = DateUtils.paseDateMMdd(inputdatetime);
            List<Monitorequipmentlastdata> list;
            if (map.containsKey(time)) {
                list = map.get(time);
            }else {
                list = new ArrayList<>();
            }
            list.add(lastData);
            map.put(time,list);
        }
        List<Monitorequipmentlastdata> list = new ArrayList<>();
        for (String time : map.keySet()) {
            Monitorequipmentlastdata monitorequipmentlastdata = listToObject(map.get(time), field);
            list.add(monitorequipmentlastdata);
        }
        return list.stream().sorted(Comparator.comparing(Monitorequipmentlastdata::getInputdatetime)).collect(Collectors.toList());
    }

    /**
     * list转化为对象
     * @param monitorequipmentlastdataList
     * @return
     */
    private Monitorequipmentlastdata listToObject(List<Monitorequipmentlastdata> monitorequipmentlastdataList,String field) {
        //将list排序以时间递增
        List<Monitorequipmentlastdata> lastDataList =
                monitorequipmentlastdataList.stream().sorted(Comparator.comparing(Monitorequipmentlastdata::getInputdatetime)).collect(Collectors.toList());
        Map<String,Object> map = new HashMap<>();
        //随着数组的遍历该设备的探头监测信息都会被替换成为最新的(也就是离当前时间点最近的有效值)
        for (Monitorequipmentlastdata monitorequipmentlastdata : lastDataList) {
            Map<String, Object> objectToMap = getObjectToMap(monitorequipmentlastdata);
            filterMap(objectToMap,field);
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
    public   List<PointInTimeDataTableResult> getThePointInTimeDataTable(EquipmentDataCommand equipmentDataCommand) {
        List<EquipmentDataCommand.Filter> filterList = equipmentDataCommand.getFilterList();
        if(CollectionUtils.isEmpty(filterList)){
            throw new IedsException("筛选条件不能为空");
        }
        filterList.removeIf(res->StringUtils.isEmpty(res.getField()) || StringUtils.isEmpty(res.getValue()) || StringUtils.isEmpty(res.getCondition()));
        List<String> timeList = equipmentDataCommand.getTimeList();
        if(CollectionUtils.isEmpty(timeList) || timeList.size()>5){
            return null;
        }
        //排列
        List<Date> dateList = timeList.stream().map(DateUtils::parseDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList =  monitorequipmentlastdataRepository.getLastDataByTime(dataParam);
        if(CollectionUtils.isEmpty(lastDataList)){
            return null;
        }
        List<Monitorequipmentlastdata> list = new ArrayList<>();
        for (Date date : dateList) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE,-30);
            Date start = cal.getTime();
            lastDataList.forEach(res->res.setRemark1(DateUtils.dateReduceHHmm(date)));
            List<Monitorequipmentlastdata> collect = lastDataList.stream().filter(res -> DateUtils.whetherItIsIn(res.getInputdatetime(), start, date)).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(collect)){
                continue;
            }
            List<Monitorequipmentlastdata> monitorEquipmentLastDataList = filterData(collect,equipmentDataCommand.getField());
            list.addAll(monitorEquipmentLastDataList);
        }
        List<Monitorequipmentlastdata> collect1 = list.stream().sorted(Comparator.comparing(Monitorequipmentlastdata::getInputdatetime)).collect(Collectors.toList());
        Map<String, List<Monitorequipmentlastdata>> collect = collect1.stream().collect(Collectors.groupingBy(res -> DateUtils.paseDate(res.getInputdatetime())));
        List<PointInTimeDataTableResult> results = new ArrayList<>();
        for (String date : collect.keySet()) {
            PointInTimeDataTableResult datePoint = new PointInTimeDataTableResult();
            datePoint.setDate(date);
            List<Monitorequipmentlastdata> monitorEquipmentLastDataList = collect.get(date);
            Map<String, List<Monitorequipmentlastdata>> listMap = monitorEquipmentLastDataList.stream().collect(Collectors.groupingBy(Monitorequipmentlastdata::getRemark1));
            List<TableResult> listResult = new ArrayList<>();
            for (Date time : dateList) {
                TableResult tableResult = new TableResult();
                String hHmm = DateUtils.dateReduceHHmm(time);
                tableResult.setDatePoint(hHmm);
                tableResult.setField(equipmentDataCommand.getField());
                tableResult.setValue("");
                if(listMap.containsKey(hHmm)){
                    Monitorequipmentlastdata monitorequipmentlastdata = listMap.get(hHmm).get(0);
                    Map<String, Object> objectToMap = getObjectToMap(monitorequipmentlastdata);
                    String str =(String) objectToMap.get(equipmentDataCommand.getField());
                    tableResult.setValue(str);
                }
                listResult.add(tableResult);
            }
            datePoint.setList(listResult);
            results.add(datePoint);
        }
        return results.stream().sorted(Comparator.comparing(PointInTimeDataTableResult::getDate)).collect(Collectors.toList());
    }

    /**
     * 导出时间点数据
     * @param equipmentDataCommand
     * @param httpServletResponse
     */
    public void exportDatePoint(EquipmentDataCommand equipmentDataCommand,HttpServletResponse httpServletResponse) {
        List<String> timeList = equipmentDataCommand.getTimeList();
        if(CollectionUtils.isEmpty(timeList) || timeList.size()>5){
            return;
        }
        //排列
        List<Date> dateList = timeList.stream().map(DateUtils::parseDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList =  monitorequipmentlastdataRepository.getLastDataByTime(dataParam);
        if(CollectionUtils.isEmpty(lastDataList)){
            return;
        }
        List<Monitorequipmentlastdata> list = new ArrayList<>();
        for (Date date : dateList) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE,-30);
            Date start = cal.getTime();
            lastDataList.forEach(res->res.setRemark1(DateUtils.dateReduceHHmm(date)));
            List<Monitorequipmentlastdata> collect = lastDataList.stream().filter(res -> DateUtils.whetherItIsIn(res.getInputdatetime(), start, date)).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(collect)){
                continue;
            }
            List<Monitorequipmentlastdata> monitorEquipmentLastDataList = filterData(collect,equipmentDataCommand.getField());
            list.addAll(monitorEquipmentLastDataList);
        }
        List<Monitorequipmentlastdata> collect1 = list.stream().sorted(Comparator.comparing(Monitorequipmentlastdata::getInputdatetime)).collect(Collectors.toList());
        Map<String, List<Monitorequipmentlastdata>> collect = collect1.stream().collect(Collectors.groupingBy(res -> DateUtils.paseDate(res.getInputdatetime())));
       List<Map<String,Object>> mapList = new ArrayList<>();
        for (String date : collect.keySet()) {
            Map<String,Object> map = new HashMap<>();
            map.put("date",date);
            List<Monitorequipmentlastdata> monitorEquipmentLastDataList = collect.get(date);
            Map<String, List<Monitorequipmentlastdata>> listMap = monitorEquipmentLastDataList.stream().collect(Collectors.groupingBy(Monitorequipmentlastdata::getRemark1));
            for (Date time : dateList) {
                String hHmm = DateUtils.dateReduceHHmm(time);
                String value = "";
                if(listMap.containsKey(hHmm)){
                    Monitorequipmentlastdata monitorequipmentlastdata = listMap.get(hHmm).get(0);
                    Map<String, Object> objectToMap = getObjectToMap(monitorequipmentlastdata);
                    value =(String) objectToMap.get(equipmentDataCommand.getField());
                }
                map.put(hHmm,value);
            }
            mapList.add(map);
        }
        mapList.sort(Comparator.comparing(res->res.get("date").toString()));
        boolean flag = Context.IsCh();
        //获取tittle
        List<ExcelExportEntity> beanList = ExcelExportUtils.getDatePoint(dateList,flag);
        //fileName
        String fileName = null;
        if (flag) {
            fileName = "时间点导出结果";
        }else {
            fileName = "date_time_export_result";
        }
        buildLogInfo(Context.getUserId(),fileName, OperationLogEunmDerailEnum.EXPORT.getCode());
        FileUtil.exportExcel(fileName,beanList,mapList,httpServletResponse);
    }

    private void buildLogInfo(String userId, String fileName,String exportCode) {
        ExportLogCommand exportLogCommand = new ExportLogCommand();
        UserRightDto userRightDto =  userRightService.getUserRightInfoByUserId(userId);
        if(null != userRightDto){
            String hospitalCode = userRightDto.getHospitalCode();
            HospitalInfoDto hospitalInfoDto = hospitalInfoService.selectOne(hospitalCode);
            String username = userRightDto.getUsername();
            exportLogCommand.setHospitalCode(hospitalCode);
            exportLogCommand.setUsername(username);
            exportLogCommand.setHospitalName(hospitalInfoDto.getHospitalName());
        }
        exportLogCommand.setOperationType(exportCode);
        exportLogCommand.setMenuName(fileName);
        exportLogCommand.setFunctionName(fileName);
        operationlogApi.addExportLog(exportLogCommand);
    }
}


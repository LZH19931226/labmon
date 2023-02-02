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
        buildLogInfo(alarmNoticeCommand.getUserId(),ExcelExportUtils.ALARM_DATA_NOTICE, OperationLogEunmDerailEnum.EXPORT.getCode());
        FileUtil.exportExcel(ExcelExportUtils.ALARM_DATA_NOTICE,beanList,mapList,response);
    }

    /**
     * 获取设备数据
     * @param equipmentDataCommand
     */
    public Page getEquipmentData(EquipmentDataCommand equipmentDataCommand) {
        Page page = new Page<>(equipmentDataCommand.getPageCurrent(),equipmentDataCommand.getPageSize());
        List<EquipmentDataCommand.Filter> filterList = equipmentDataCommand.getFilterList();
        if(CollectionUtils.isEmpty(filterList)){
            throw new IedsException("筛选条件不能为空");
        }
        List<String> fieldList = equipmentDataCommand.getFieldList();
        if(CollectionUtils.isEmpty(fieldList)){
            return page;
        }
        filterList.removeIf(res->StringUtils.isEmpty(res.getField()) || StringUtils.isEmpty(res.getValue()) || StringUtils.isEmpty(res.getCondition()));
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
        buildLogInfo(Context.getUserId(),ExcelExportUtils.EQUIPMENT_DATA_CUSTOM, OperationLogEunmDerailEnum.EXPORT.getCode());
        FileUtil.exportExcel(ExcelExportUtils.EQUIPMENT_DATA_CUSTOM,beanList,mapList,response);
    }

    /**
     * 时间点查询
     * @param equipmentDataCommand
     */
    public  List<TimePointCurve>  getThePointInTimeDataCurve(EquipmentDataCommand equipmentDataCommand) {
        filterCondition(equipmentDataCommand);
        String field = equipmentDataCommand.getField();
        List<String> timeList = equipmentDataCommand.getTimeList();
        if(CollectionUtils.isEmpty(timeList)){
            return new ArrayList<>();
        }
        //排列
        List<Date> dateList = timeList.stream().filter(res->!StringUtils.isEmpty(res)).map(DateUtils::parseDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList =  monitorequipmentlastdataRepository.getLastDataByTime(dataParam);

        if(CollectionUtils.isEmpty(lastDataList)){
            return new ArrayList<>();
        }

        Map<String, List<Monitorequipmentlastdata>> stringListMap = lastDataList.stream().collect(Collectors.groupingBy(res -> DateUtils.paseDateMMdd(res.getInputdatetime())));

        Map<String, List<Monitorequipmentlastdata>> dataMap = sortMapByKey(stringListMap);
        Map<String,TimePointCurve> map = new HashMap<>();
        for (String inputTime : dataMap.keySet()) {
            List<Monitorequipmentlastdata> dataList = dataMap.get(inputTime);
            //遍历时间点
            for (Date date : dateList) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MINUTE,-30);
                Date start = cal.getTime();
                List<Monitorequipmentlastdata> list = dataList.stream().filter(res -> DateUtils.whetherItIsIn(res.getInputdatetime(), start, date)).collect(Collectors.toList());
                Monitorequipmentlastdata lastObject = listToObject(list);
                Map<String, Object> objectToMap = getObjectToMap(lastObject);
                String value =  (String)objectToMap.get(field);
                String hHmm = DateUtils.dateReduceHHmm(date);
                if(map.containsKey(hHmm)){
                    TimePointCurve timePointCurve = map.get(hHmm);
                    List<String> xaxis = timePointCurve.getXaxis();
                    List<String> series = timePointCurve.getSeries();
                    xaxis.add(inputTime);
                    series.add(StringUtils.isEmpty(value) ? "0":value);
                    map.put(hHmm,timePointCurve);
                }else {
                    TimePointCurve curve = new TimePointCurve();
                    List<String> xaxis = new ArrayList<>();
                    List<String> series = new ArrayList<>();
                    xaxis.add(inputTime);
                    series.add(StringUtils.isEmpty(value) ? "0":value);
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
            public int compare(String obj1, String obj2) {
                return obj1.compareTo(obj2);//升序排序
            }
        });
        sortMap.putAll(map);
        return sortMap;
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
        //2.查询数据源
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList =  monitorequipmentlastdataRepository.getLastDataByTime(dataParam);
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
            for (String timeStr : timeList) {
                String hHmmTime = DateUtils.getHHmm(timeStr);
                Date rTime = DateUtils.parseDate(timeStr);
                cal.setTime(rTime);
                cal.add(Calendar.MINUTE,-30);
                Date lTime = cal.getTime();
                List<Monitorequipmentlastdata> collect = monitorequipmentlastdata.stream().filter(res -> DateUtils.whetherItIsIn(res.getInputdatetime(),lTime,rTime)).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(collect)){
                    map.put(hHmmTime,"");
                    continue;
                }
                Monitorequipmentlastdata data = collect.stream().max(Comparator.comparing(Monitorequipmentlastdata::getInputdatetime)).get();
                Map<String, Object> objectToMap = getObjectToMap(data);
                if(objectToMap.containsKey(field)){
                    String str = (String) objectToMap.get(field);
                    map.put(hHmmTime,str);
                }else {
                    map.put(hHmmTime,"");
                }
            }
            list.add(map);
        }
        return list.stream().sorted(Comparator.comparing(o -> o.get("date"))).collect(Collectors.toList());
    }

    /**
     * 导出时间点数据
     * @param equipmentDataCommand
     * @param httpServletResponse
     */
    public void exportDatePoint(EquipmentDataCommand equipmentDataCommand,HttpServletResponse httpServletResponse) {
        filterCondition(equipmentDataCommand);
        List<String> timeList = equipmentDataCommand.getTimeList();
        if(CollectionUtils.isEmpty(timeList)){
            return;
        }
        //排列
        List<Date> dateList = timeList.stream().filter(res->!StringUtils.isEmpty(res)).map(DateUtils::parseDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
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

        buildLogInfo(Context.getUserId(),ExcelExportUtils.EQUIPMENT_DATA_POINT_IN_TIME, OperationLogEunmDerailEnum.EXPORT.getCode());
        FileUtil.exportExcel(ExcelExportUtils.EQUIPMENT_DATA_POINT_IN_TIME,beanList,mapList,httpServletResponse);
    }

    private void filterCondition(EquipmentDataCommand equipmentDataCommand) {
        List<EquipmentDataCommand.Filter> filterList = equipmentDataCommand.getFilterList();
        if(CollectionUtils.isEmpty(filterList)){
            throw new IedsException("筛选条件不能为空");
        }
        filterList.removeIf(res->StringUtils.isEmpty(res.getField()) || StringUtils.isEmpty(res.getValue()) || StringUtils.isEmpty(res.getCondition()));
    }

    private  void buildLogInfo(String userId, String fileName,String exportCode) {
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
        buildLogInfo(alarmDataCommand.getUserId(),ExcelExportUtils.ALARM_DATA_SUMMARY,OperationLogEunmDerailEnum.EXPORT.getCode());
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


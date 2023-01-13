package com.hc.application;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.application.response.InstrumentTypeNumResult;
import com.hc.application.response.SummaryOfAlarmsResult;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.dto.*;
import com.hc.my.common.core.constant.enums.CurrentProbeInfoEnum;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.*;
import com.hc.repository.HospitalEquipmentRepository;
import com.hc.repository.InstrumentMonitorInfoRepository;
import com.hc.service.EquipmentInfoService;
import com.hc.service.InstrumentParamConfigService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SystemDataApplication {

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;
    @Autowired
    private WarningrecordRepository warningrecordRepository;
    @Autowired
    private HospitalEquipmentRepository  hospitalEquipmentRepository;

    @Autowired
    private InstrumentMonitorInfoRepository instrumentMonitorInfoRepository;

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private InstrumentParamConfigService instrumentParamConfigService;

    //hospitalCode,startTime,equipmentNo,pageSize,pageCurrent
    public Page findPacketLossLog(EquipmentDataCommand equipmentDataCommand) {
        Page<Monitorequipmentlastdata> page = new Page<>(equipmentDataCommand.getPageCurrent(), equipmentDataCommand.getPageSize());
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList = monitorequipmentlastdataRepository.getEquipmentPacketData(page, dataParam);
        if (CollectionUtils.isEmpty(lastDataList)) {
            return null;
        }
        lastDataList.forEach(s->s.setEquipmentName(equipmentDataCommand.getEquipmentName()));
        page.setRecords(lastDataList);
        return page;
    }


    //hospitalCode,startTime,equipmentNo
    public SummaryOfAlarmsResult getPacketLossColumnar(EquipmentDataCommand equipmentDataCommand) {
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList = monitorequipmentlastdataRepository.getPacketLossColumnar(dataParam);
        if (CollectionUtils.isEmpty(lastDataList)) {
            return null;
        }
        SummaryOfAlarmsResult summaryOfAlarmsResult = new SummaryOfAlarmsResult();
        List<String> timeList = lastDataList.stream().map(Monitorequipmentlastdata::getRemark1).collect(Collectors.toList());
        summaryOfAlarmsResult.setTimeList(timeList);
        List<Long> numList = lastDataList.stream().map(s -> Long.parseLong(s.getRemark2())).collect(Collectors.toList());
        summaryOfAlarmsResult.setNumList(numList);
        return summaryOfAlarmsResult;
    }
    //hospitalCode,startTime,equipmentNo
    public void exportPacketLossLog(EquipmentDataCommand equipmentDataCommand, HttpServletResponse response) {
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList = monitorequipmentlastdataRepository.getPacketLoss(dataParam);
        if (CollectionUtils.isEmpty(lastDataList)){
            return;
        }
        boolean isCh = Context.IsCh();
        List<ExcelExportEntity> beanList = ExcelExportUtils.getPacketLossLog(isCh);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (Monitorequipmentlastdata equipmentDatum : lastDataList) {
            if (isCh){
                equipmentDatum.setRemark1("成功接收心跳包数据");
            }else {
                equipmentDatum.setRemark1("Heartbeat packet data was successfully received");
            }
            Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(equipmentDatum);
            mapList.add(objectToMap);
        }
        FileUtil.exportExcel(ExcelExportUtils.EQUIPMENT_DATA,beanList,mapList,response);


    }
    //hospitalCode startTime  endTime
    public List<eqTypeAlarmNumCountDto> eqTypeAlarmNumCount(EquipmentDataCommand equipmentDataCommand) {
        String hospitalCode = equipmentDataCommand.getHospitalCode();
        //获取报警设备
        List<Warningrecord> warningInfos = warningrecordRepository.getWarningEquuipmentInfos(hospitalCode, equipmentDataCommand.getStartTime(), equipmentDataCommand.getEndTime());
        if (CollectionUtils.isEmpty(warningInfos)){
            return null;
        }
        //获取该医院底下设备类型数量
        List<eqTypeAlarmNumCountDto> eqTypeAlarmNumCountDtos  = hospitalEquipmentRepository.findEquipmentByHosCode(hospitalCode);
        if (CollectionUtils.isEmpty(eqTypeAlarmNumCountDtos)){
            return null;
        }

        Map<String, List<eqTypeAlarmNumCountDto>> eqTypeMap = eqTypeAlarmNumCountDtos.stream().collect(Collectors.groupingBy(eqTypeAlarmNumCountDto::getEquipmenttypeid));

        List<eqTypeAlarmNumCountDto>  eqTypeAlarmNumCountDtos1 =new ArrayList<>();
            eqTypeMap.forEach((k,v)->{
                eqTypeAlarmNumCountDto  eqTypeAlarmNumCountDto  = new eqTypeAlarmNumCountDto();
                eqTypeAlarmNumCountDto.setEquipmenttypeid(k);
                eqTypeAlarmNumCountDto.setEquipmenttypename(v.get(0).getEquipmenttypename());
                eqTypeAlarmNumCountDto.setEquipmenttypenameUs(v.get(0).getEquipmenttypenameUs());
                int count = 0;
                List<String> eqNos = v.stream().map(com.hc.dto.eqTypeAlarmNumCountDto::getEquipmentno).collect(Collectors.toList());
                Iterator<Warningrecord> iterator = warningInfos.iterator();
                while (iterator.hasNext()){
                    Warningrecord next = iterator.next();
                    String equipmentno = next.getEquipmentno();
                    if (eqNos.contains(equipmentno)){
                        count++;
                        iterator.remove();
                    }
                }
                eqTypeAlarmNumCountDto.setAlarmCount(count);
                eqTypeAlarmNumCountDtos1.add(eqTypeAlarmNumCountDto);
            });

        return eqTypeAlarmNumCountDtos1;
    }

    public List<Warningrecord> getWarningRecordInfo(String hospitalCode, Integer count) {
        List<Warningrecord> warningRecordList = warningrecordRepository.getWarningInfoByCode(hospitalCode,count);
        if(CollectionUtils.isEmpty(warningRecordList)){
            return null;
        }
        List<String> ipcNoList = warningRecordList.stream().map(Warningrecord::getInstrumentparamconfigno).distinct().collect(Collectors.toList());
        //获取探头信息
        List<InstrumentParamConfigDto> instrumentParamConfigDtoList = instrumentParamConfigService.batchGetProbeInfo(ipcNoList);
        Map<String, List<InstrumentParamConfigDto>> ipcNoMap =
                instrumentParamConfigDtoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentparamconfigno));

        warningRecordList.forEach(res->{
            String instrumentparamconfigno = res.getInstrumentparamconfigno();
            if(ipcNoMap.containsKey(instrumentparamconfigno)){
                InstrumentParamConfigDto instrumentParamConfigDto = ipcNoMap.get(instrumentparamconfigno).get(0);
                Integer instrumentconfigid = instrumentParamConfigDto.getInstrumentconfigid();
                String probeEName = CurrentProbeInfoEnum.from(instrumentconfigid).getProbeEName();
                res.setEName(probeEName);
            }

        });
        return warningRecordList;
    }

    /**
     * 获取设备数量占比
     * @param equipmentDataCommand
     */
    public List<EquipmentTypeNumDto> getEquipmentNumProportion(EquipmentDataCommand equipmentDataCommand) {
        //获取设备类型数量
        List<EquipmentTypeNumDto> equipmentTypeNum = hospitalEquipmentRepository.getEquipmentTypeNum(equipmentDataCommand);
        long sum = equipmentTypeNum.stream().mapToLong(EquipmentTypeNumDto::getEquipmentNum).sum();
        for (EquipmentTypeNumDto equipmentTypeNumDto : equipmentTypeNum) {
            Long num = equipmentTypeNumDto.getEquipmentNum();
            String str = divide(sum, num);
            equipmentTypeNumDto.setPercentage(str);
        }
        return equipmentTypeNum;

    }

    public InstrumentTypeNumResult getInstrumentNum(EquipmentDataCommand equipmentDataCommand) {
        List<InstrumentTypeNumDto> equipmentTypeNumList = instrumentMonitorInfoRepository.getEquipmentTypeNum(equipmentDataCommand);
        if(CollectionUtils.isEmpty(equipmentTypeNumList)){
            return null;
        }
        InstrumentTypeNumResult instrumentTypeNumResult = new InstrumentTypeNumResult();
        long sum = equipmentTypeNumList.stream().mapToLong(InstrumentTypeNumDto::getNum).sum();
        for (InstrumentTypeNumDto instrumentTypeNumDto : equipmentTypeNumList) {
            Long num = instrumentTypeNumDto.getNum();
            String str = divide(sum, num);
            instrumentTypeNumDto.setPercentage(str);
        }
        instrumentTypeNumResult.setTotalNum(sum);
        instrumentTypeNumResult.setInstrumentTypeNumDtoList(equipmentTypeNumList);
        return instrumentTypeNumResult;
    }

     public String divide(Long totalNum,Long num){
         BigDecimal num100 = new BigDecimal(num + "");
         BigDecimal sun100 = new BigDecimal(totalNum + "");
         BigDecimal divide = num100.divide(sun100, 4, RoundingMode.HALF_UP);
         String str = divide.multiply(new BigDecimal(100)).toString();
         return str.substring(0,str.length()-2);
    }


    //hospitalCode startTime  endTime
    public List<eqTypeAlarmNumCountDto> getEqAlarmPeriod(EquipmentDataCommand equipmentDataCommand) {
        String hospitalCode = equipmentDataCommand.getHospitalCode();
        //获取报警设备
        List<Warningrecord> warningInfos = warningrecordRepository.getWarningEquuipmentInfos(hospitalCode, equipmentDataCommand.getStartTime(), equipmentDataCommand.getEndTime());
        if (CollectionUtils.isEmpty(warningInfos)) {
            return null;
        }
        List<eqTypeAlarmNumCountDto> eqTypeAlarmNumCountDtos = new ArrayList<>();
        Date date = DateUtils.initDateByDay();
        for (int i = 1; i <= 12; i++) {
            eqTypeAlarmNumCountDto eqTypeAlarmNumCountDto = new eqTypeAlarmNumCountDto();
            Iterator<Warningrecord> iterator = warningInfos.iterator();
            Date startTime = DateUtils.getAddHour(date, 2 * (i - 1));
            Date endTime ;
            if (i==12){
                endTime =DateUtils.getEndOfDay();
            }else {
                endTime =DateUtils.getAddHour(date, i*2);
            }
            int counnt = 0;
            while (iterator.hasNext()) {
                Warningrecord warningrecord = iterator.next();
                Date inputdatetime = warningrecord.getInputdatetime();
                boolean isNowTime = DateUtils.whetherItIsIn(inputdatetime, startTime, endTime);
                if (isNowTime){
                    counnt++;
                    iterator.remove();
                }
            }
            eqTypeAlarmNumCountDto.setAlarmPeriod(DateUtils.parseDatetime(startTime));
            eqTypeAlarmNumCountDto.setAlarmCount(counnt);
            eqTypeAlarmNumCountDtos.add(eqTypeAlarmNumCountDto);
        }
        return eqTypeAlarmNumCountDtos;
    }


    public List<AlarmEquipmentNumDto> getAlarmDeviceNum(EquipmentDataCommand equipmentDataCommand) {
        EquipmentDataParam convert = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Warningrecord> list =  warningrecordRepository.getAlarmDeviceNum(convert);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<String> eNoList = list.stream().map(Warningrecord::getEquipmentno).collect(Collectors.toList());
        Map<String, List<Warningrecord>> eNoWMap = list.stream().collect(Collectors.groupingBy(Warningrecord::getEquipmentno));
        List<MonitorEquipmentDto> monitorEquipmentList = equipmentInfoService.batchGetEquipmentInfo(eNoList);
        Map<String, List<MonitorEquipmentDto>> eNoMap = monitorEquipmentList.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmentno));

        List<AlarmEquipmentNumDto> equipmentDtoList = new ArrayList<>();
        for (Warningrecord warningrecord : list) {
            AlarmEquipmentNumDto alarmEquipmentNumDto = new AlarmEquipmentNumDto();
            String equipmentNo = warningrecord.getEquipmentno();
            alarmEquipmentNumDto.setEquipmentNo(equipmentNo);
            MonitorEquipmentDto monitorEquipmentDto = eNoMap.get(equipmentNo).get(0);
            Warningrecord warningRecord = eNoWMap.get(equipmentNo).get(0);
            String equipmentName = monitorEquipmentDto.getEquipmentname();
            String sn = monitorEquipmentDto.getSn();
            alarmEquipmentNumDto.setEquipmentName(equipmentName);
            alarmEquipmentNumDto.setSn(sn);
            alarmEquipmentNumDto.setNum(warningRecord.getNum());
            equipmentDtoList.add(alarmEquipmentNumDto);
        }
        return equipmentDtoList;
    }
}

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
import com.hc.my.common.core.constant.enums.DataFieldEnum;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.*;
import com.hc.repository.HospitalEquipmentRepository;
import com.hc.repository.InstrumentMonitorInfoRepository;
import com.hc.service.EquipmentInfoService;
import com.hc.service.ExportLogService;
import com.hc.service.HospitalInfoService;
import com.hc.service.InstrumentParamConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
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
    private HospitalEquipmentRepository hospitalEquipmentRepository;
    @Autowired
    private InstrumentMonitorInfoRepository instrumentMonitorInfoRepository;
    @Autowired
    private EquipmentInfoService equipmentInfoService;
    @Autowired
    private InstrumentParamConfigService instrumentParamConfigService;
    @Autowired
    private ExportLogService exportLogService;
    @Autowired
    private HospitalInfoService hospitalInfoService;

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
        lastDataList.forEach(s->{
            s.setEquipmentName(equipmentDataCommand.getEquipmentName());
            s.setInputdatetime(DateUtils.designatedAreaDateLog(s.getInputdatetime(),Context.getZone()));
        });

        page.setRecords(lastDataList);
        return page;
    }


    //hospitalCode,startTime,equipmentNo
    public SummaryOfAlarmsResult getPacketLossColumnar(EquipmentDataCommand equipmentDataCommand) {
        String startTime = equipmentDataCommand.getStartTime();
        String endTime = equipmentDataCommand.getEndTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList = monitorequipmentlastdataRepository.getPacketLossColumnar(dataParam);
        if (CollectionUtils.isEmpty(lastDataList)) {
            return null;
        }
        List<String> betweenDate = DateUtils.getBetweenDate(startTime, endTime);
        SummaryOfAlarmsResult summaryOfAlarmsResult = new SummaryOfAlarmsResult();
        List<String> timeList = new ArrayList<>();
        List<String> numList = new ArrayList<>();
        for (String yearMonth : betweenDate) {
            int count = 0;
            Iterator<Monitorequipmentlastdata> iterator = lastDataList.iterator();
            while (iterator.hasNext()) {
                Monitorequipmentlastdata next = iterator.next();
                String remark1 = next.getRemark1();
                if (yearMonth.substring(0, 10).equals(remark1.substring(0, 10))) {
                    count++;
                    iterator.remove();
                }
            }
            timeList.add(yearMonth.substring(0, 10));
            //计算丢包率
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(0);
            String result = numberFormat.format((float) (1440 - count) / (float) 1440 * 100);
            numList.add(result);
        }
        summaryOfAlarmsResult.setNumList(numList);
        summaryOfAlarmsResult.setTimeList(timeList);
        return summaryOfAlarmsResult;
    }

    //hospitalCode,startTime,equipmentNo
    public void exportPacketLossLog(EquipmentDataCommand equipmentDataCommand, HttpServletResponse response) {
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam dataParam = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        List<Monitorequipmentlastdata> lastDataList = monitorequipmentlastdataRepository.getPacketLoss(dataParam);
        if (CollectionUtils.isEmpty(lastDataList)) {
            return;
        }
        String lang = equipmentDataCommand.getLang();
        boolean isCh = StringUtils.equals(lang, "zh");
        List<ExcelExportEntity> beanList = ExcelExportUtils.getPacketLossLog(isCh);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Monitorequipmentlastdata equipmentDatum : lastDataList) {
            if (isCh) {
                equipmentDatum.setRemark1("成功接收心跳包数据");
            } else {
                equipmentDatum.setRemark1("Heartbeat packet data was successfully received");
            }
            Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(equipmentDatum);
            mapList.add(objectToMap);
        }
        exportLogService.buildLogInfo(Context.getUserId(), ExcelExportUtils.getSystemDataHeartbeatModel(), OperationLogEunmDerailEnum.EXPORT.getCode(), OperationLogEunm.PACKET_LOSS_QUERY.getCode());
        FileUtil.exportExcel(ExcelExportUtils.getSystemDataHeartbeatModel(), beanList, mapList, response);
    }

    //hospitalCode startTime  endTime
    public List<eqTypeAlarmNumCountDto> eqTypeAlarmNumCount(EquipmentDataCommand equipmentDataCommand) {
        String hospitalCode = equipmentDataCommand.getHospitalCode();
        //获取报警设备
        List<Warningrecord> warningInfos1 = warningrecordRepository.getWarningEquuipmentInfos(hospitalCode, equipmentDataCommand.getStartTime(), equipmentDataCommand.getEndTime());
        //数据筛选只需要非工作时间段得数据
        List<LabHosWarningTimeDto> hospitalWarningTime = hospitalInfoService.getHospitalWarningTime(hospitalCode);
        List<Warningrecord> warningInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hospitalWarningTime)) {
            for (Warningrecord warningInfo : warningInfos1) {
                Date inputdatetime = warningInfo.getInputdatetime();
                for (LabHosWarningTimeDto labHosWarningTimeDto : hospitalWarningTime) {
                    Date beginTime = labHosWarningTimeDto.getBeginTime();
                    Date endTime = labHosWarningTimeDto.getEndTime();
                    if (DateUtils.isEffectiveHhMm(inputdatetime, beginTime, endTime)) {
                        warningInfos.add(warningInfo);
                        break;
                    }
                }

            }
        }
        //获取该医院底下设备类型数量
        List<eqTypeAlarmNumCountDto> eqTypeAlarmNumCountDtos = hospitalEquipmentRepository.findEquipmentByHosCode(hospitalCode);
        if (CollectionUtils.isEmpty(eqTypeAlarmNumCountDtos)) {
            return eqTypeAlarmNumCountDtos;
        }
        Map<String, List<eqTypeAlarmNumCountDto>> eqTypeMap = eqTypeAlarmNumCountDtos.stream().filter(s -> StringUtils.isNotEmpty(s.getEquipmenttypeid())).collect(Collectors.groupingBy(eqTypeAlarmNumCountDto::getEquipmenttypeid));
        List<eqTypeAlarmNumCountDto> eqTypeAlarmNumCountDtos1 = new ArrayList<>();
        eqTypeMap.forEach((k, v) -> {
            eqTypeAlarmNumCountDto eqTypeAlarmNumCountDto = new eqTypeAlarmNumCountDto();
            eqTypeAlarmNumCountDto.setEquipmenttypeid(k);
            eqTypeAlarmNumCountDto.setEquipmenttypename(Context.isChFt()?v.get(0).getEquipmenttypenameFt():v.get(0).getEquipmenttypename());
            eqTypeAlarmNumCountDto.setEquipmenttypenameUs(v.get(0).getEquipmenttypenameUs());
            int count = 0;
            List<String> eqNos = v.stream().map(com.hc.dto.eqTypeAlarmNumCountDto::getEquipmentno).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(warningInfos)) {
                Iterator<Warningrecord> iterator = warningInfos.iterator();
                while (iterator.hasNext()) {
                    Warningrecord next = iterator.next();
                    String equipmentno = next.getEquipmentno();
                    if (eqNos.contains(equipmentno)) {
                        count++;
                        iterator.remove();
                    }
                }
            }
            eqTypeAlarmNumCountDto.setAlarmCount(count);
            eqTypeAlarmNumCountDtos1.add(eqTypeAlarmNumCountDto);
        });

        return eqTypeAlarmNumCountDtos1;
    }

    public List<Warningrecord> getWarningRecordInfo(String hospitalCode, Integer count) {
        Date date = new Date();
        //获取报警设备  查询一周得时间
        List<Warningrecord> warningInfos1 = warningrecordRepository.getWarningEquuipmentInfos(hospitalCode, DateUtils.getPreviousHour(date), DateUtils.paseDatetime(date));
        if (CollectionUtils.isEmpty(warningInfos1)) {
            return null;
        }
        //数据筛选只需要非工作时间段得数据
        List<LabHosWarningTimeDto> hospitalWarningTime = hospitalInfoService.getHospitalWarningTime(hospitalCode);
        List<Warningrecord> warningRecordList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hospitalWarningTime)) {
            for (Warningrecord warningInfo : warningInfos1) {
                Date inputdatetime = warningInfo.getInputdatetime();
                for (LabHosWarningTimeDto labHosWarningTimeDto : hospitalWarningTime) {
                    Date beginTime = labHosWarningTimeDto.getBeginTime();
                    Date endTime = labHosWarningTimeDto.getEndTime();
                    if (DateUtils.isEffectiveHhMm(inputdatetime, beginTime, endTime)) {
                        warningRecordList.add(warningInfo);
                        break;
                    }
                }

            }
        }

        if (CollectionUtils.isEmpty(warningRecordList)){
            return null;
        }
        List<String> ipcNoList = warningRecordList.stream().map(Warningrecord::getInstrumentparamconfigno).distinct().collect(Collectors.toList());
        //获取探头信息
        List<InstrumentParamConfigDto> instrumentParamConfigDtoList = instrumentParamConfigService.batchGetProbeInfo(ipcNoList);
        Map<String, List<InstrumentParamConfigDto>> ipcNoMap =
                instrumentParamConfigDtoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentparamconfigno));

        warningRecordList.forEach(res -> {
            String instrumentparamconfigno = res.getInstrumentparamconfigno();
            if (ipcNoMap.containsKey(instrumentparamconfigno)) {
                InstrumentParamConfigDto instrumentParamConfigDto = ipcNoMap.get(instrumentparamconfigno).get(0);
                Integer instrumentconfigid = instrumentParamConfigDto.getInstrumentconfigid();
                String probeEName = CurrentProbeInfoEnum.from(instrumentconfigid).getProbeEName();
                DataFieldEnum dataFieldEnum = DataFieldEnum.fromByLastDataField(probeEName);
                res.setEName(probeEName);
                if (!Context.IsCh()) {
                    //The temperature of the device name is abnormal  Abnormal data is
                    String eRemark = AlarmInfoUtils.setTypeName(res.getWarningremark(), dataFieldEnum.getEName());
                    res.setWarningremark(eRemark);
                }
            }
        });

        return warningRecordList;

    }

    /**
     * 获取设备数量占比
     *
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
            equipmentTypeNumDto.setEquipmentTypeName(Context.isChFt()?equipmentTypeNumDto.getEquipmentTypeNameFt():equipmentTypeNumDto.getEquipmentTypeName());
        }
        return equipmentTypeNum;

    }

    public InstrumentTypeNumResult getInstrumentNum(EquipmentDataCommand equipmentDataCommand) {
        List<InstrumentTypeNumDto> equipmentTypeNumList = instrumentMonitorInfoRepository.getEquipmentTypeNum(equipmentDataCommand);
        if (CollectionUtils.isEmpty(equipmentTypeNumList)) {
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

    public String divide(Long totalNum, Long num) {
        BigDecimal num100 = new BigDecimal(num + "");
        BigDecimal sun100 = new BigDecimal(totalNum + "");
        BigDecimal divide = num100.divide(sun100, 4, RoundingMode.HALF_UP);
        String str = divide.multiply(new BigDecimal(100)).toString();
        return str.substring(0, str.length() - 2);
    }


    //hospitalCode startTime  endTime
    public List<eqTypeAlarmNumCountDto> getEqAlarmPeriod(EquipmentDataCommand equipmentDataCommand) {
        String hospitalCode = equipmentDataCommand.getHospitalCode();
        //获取报警设备
        List<Warningrecord> warningInfos1 = warningrecordRepository.getWarningEquuipmentInfos(hospitalCode, equipmentDataCommand.getStartTime(), equipmentDataCommand.getEndTime());
        //数据筛选只需要非工作时间段得数据
        List<LabHosWarningTimeDto> hospitalWarningTime = hospitalInfoService.getHospitalWarningTime(hospitalCode);
        List<Warningrecord> warningInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hospitalWarningTime)) {
            for (Warningrecord warningInfo : warningInfos1) {
                Date inputdatetime = warningInfo.getInputdatetime();
                for (LabHosWarningTimeDto labHosWarningTimeDto : hospitalWarningTime) {
                    Date beginTime = labHosWarningTimeDto.getBeginTime();
                    Date endTime = labHosWarningTimeDto.getEndTime();
                    if (DateUtils.isEffectiveHhMm(inputdatetime, beginTime, endTime)) {
                        warningInfos.add(warningInfo);
                        break;
                    }
                }

            }
        }
        List<eqTypeAlarmNumCountDto> eqTypeAlarmNumCountDtos = new ArrayList<>();
        Date date = DateUtils.initDateByDay();
        for (int i = 1; i <= 12; i++) {
            eqTypeAlarmNumCountDto eqTypeAlarmNumCountDto = new eqTypeAlarmNumCountDto();
            Iterator<Warningrecord> iterator = warningInfos.iterator();
            Date startTime = DateUtils.getAddHour(date, 2 * (i - 1));
            Date endTime;
            if (i == 12) {
                endTime = DateUtils.getEndOfDay();
            } else {
                endTime = DateUtils.getAddHour(date, i * 2);
            }
            int count = 0;
            if (CollectionUtils.isNotEmpty(warningInfos)) {
                while (iterator.hasNext()) {
                    Warningrecord warningrecord = iterator.next();
                    Date inputdatetime = warningrecord.getInputdatetime();
                    boolean isNowTime = DateUtils.whetherItIsIn(inputdatetime, startTime, endTime);
                    if (isNowTime) {
                        count++;
                        iterator.remove();
                    }
                }
            }
            eqTypeAlarmNumCountDto.setAlarmPeriod(DateUtils.parseDatetime(startTime));
            eqTypeAlarmNumCountDto.setAlarmCount(count);
            eqTypeAlarmNumCountDtos.add(eqTypeAlarmNumCountDto);
        }
        return eqTypeAlarmNumCountDtos;
    }


    public List<AlarmEquipmentNumDto> getAlarmDeviceNum(EquipmentDataCommand equipmentDataCommand) {
        List<Warningrecord> warningInfos1 = warningrecordRepository.getWarningEquuipmentInfos(equipmentDataCommand.getHospitalCode(), equipmentDataCommand.getStartTime(), equipmentDataCommand.getEndTime());
        if (CollectionUtils.isEmpty(warningInfos1)) {
            return null;
        }
        List<LabHosWarningTimeDto> hospitalWarningTime = hospitalInfoService.getHospitalWarningTime(equipmentDataCommand.getHospitalCode());
        List<Warningrecord> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hospitalWarningTime)) {
            for (Warningrecord warningInfo : warningInfos1) {
                Date inputdatetime = warningInfo.getInputdatetime();
                for (LabHosWarningTimeDto labHosWarningTimeDto : hospitalWarningTime) {
                    Date beginTime = labHosWarningTimeDto.getBeginTime();
                    Date endTime = labHosWarningTimeDto.getEndTime();
                    if (DateUtils.isEffectiveHhMm(inputdatetime, beginTime, endTime)) {
                        list.add(warningInfo);
                        break;
                    }
                }

            }
        }
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<String> eNoList = list.stream().map(Warningrecord::getEquipmentno).collect(Collectors.toList());
        List<MonitorEquipmentDto> monitorEquipmentList = equipmentInfoService.batchGetEquipmentInfo(eNoList);
        Map<String, List<MonitorEquipmentDto>> eNoMap = monitorEquipmentList.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmentno));
        List<AlarmEquipmentNumDto> equipmentDtoList = new ArrayList<>();
        Map<String, List<Warningrecord>> collect = list.stream().collect(Collectors.groupingBy(Warningrecord::getEquipmentno));
        collect.forEach((k,v)->{
            AlarmEquipmentNumDto alarmEquipmentNumDto = new AlarmEquipmentNumDto();
            alarmEquipmentNumDto.setEquipmentNo(k);
            MonitorEquipmentDto monitorEquipmentDto = eNoMap.get(k).get(0);
            String equipmentName = monitorEquipmentDto.getEquipmentname();
            String sn = monitorEquipmentDto.getSn();
            alarmEquipmentNumDto.setEquipmentName(equipmentName);
            alarmEquipmentNumDto.setSn(sn);
            alarmEquipmentNumDto.setNum((long) v.size());
            equipmentDtoList.add(alarmEquipmentNumDto);

        });
        return equipmentDtoList.stream().sorted(Comparator.comparing(AlarmEquipmentNumDto::getNum).reversed()).collect(Collectors.toList());
    }
}

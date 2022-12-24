package com.hc.application;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.application.response.SummaryOfAlarmsResult;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.dto.eqTypeAlarmNumCountDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.*;
import com.hc.repository.HospitalEquipmentRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class SystemDataApplication {

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;
    @Autowired
    private WarningrecordRepository warningrecordRepository;
    @Autowired
    private HospitalEquipmentRepository  hospitalEquipmentRepository;


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
        page.setRecords(lastDataList);
        return page;
    }

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
                for (Warningrecord warningrecord : warningInfos){
                    String equipmentno = warningrecord.getEquipmentno();
                    if (eqNos.contains(equipmentno)){
                        count++;
                    }
                }
                eqTypeAlarmNumCountDto.setAlarmCount(count);
                eqTypeAlarmNumCountDtos1.add(eqTypeAlarmNumCountDto);
            });
        return eqTypeAlarmNumCountDtos1;
    }
}

package com.hc.application;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.application.response.SummaryOfAlarmsResult;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SystemDataApplication {

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;


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
}

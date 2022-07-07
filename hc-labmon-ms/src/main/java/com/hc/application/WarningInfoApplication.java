package com.hc.application;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.WarningInfoCommand;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.dto.CurveInfoDto;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.dto.WarningRecordInfoDto;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.repository.InstrumentParamConfigRepository;
import com.hc.service.WarningRecordInfoService;
import com.hc.util.EquipmentInfoServiceHelp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Component
public class WarningInfoApplication {

    @Autowired
    private WarningrecordRepository warningrecordRepository;

    @Autowired
    private InstrumentParamConfigRepository instrumentParamConfigRepository;

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;

    @Autowired
    private WarningRecordInfoService warningRecordInfoService;
    /**
     * 分页获取报警信息
     * @param warningInfoCommand
     * @return
     */
    public  Page<Warningrecord> getWarningRecord(WarningInfoCommand warningInfoCommand) {
        Page<Warningrecord> page = new Page<>(warningInfoCommand.getPageCurrent(),warningInfoCommand.getPageSize());
        IPage<Warningrecord> warningRecordIPage = warningrecordRepository.getWarningRecord(page);
        List<Warningrecord> records = warningRecordIPage.getRecords();
        page.setRecords(records);
        return page;
    }

    /**
     * 获取报警设备时间段曲线数据
     * @param pkId
     * @return
     */
    public CurveInfoDto getWarningCurveData(String pkId ,String startTime,String endTime ) {
        Warningrecord warningrecord = warningrecordRepository.getOne(Wrappers.lambdaQuery(new Warningrecord()).eq(Warningrecord::getPkid, pkId));
        if(ObjectUtils.isEmpty(warningrecord))
            return null;
        String equipmentNo = warningrecord.getEquipmentno();
        String instrumentParamConfigNo = warningrecord.getInstrumentparamconfigno();
        InstrumentParamConfigDto instrumentParamConfigDto = instrumentParamConfigRepository.getProbeInfo(instrumentParamConfigNo);
        String instrumentConfigName = instrumentParamConfigDto.getInstrumentconfigname();
        //电量无曲线
        if (StringUtils.isNotBlank(instrumentConfigName) && StringUtils.equalsAnyIgnoreCase(instrumentConfigName,"QC","UPS","DOOR","voltage")){
            throw  new IedsException("市电,电量无曲线");
        }
        String configName = changeInstrumentConfigName(instrumentConfigName);
        List<MonitorequipmentlastdataDto> lastDataList = monitorequipmentlastdataRepository.getWarningCurveData(equipmentNo,startTime,endTime,configName);
        List<Monitorequipmentlastdata> monitorEquipmentLastDataList = BeanConverter.convert(lastDataList, Monitorequipmentlastdata.class);
        return EquipmentInfoServiceHelp.getCurveFirst(monitorEquipmentLastDataList, new CurveInfoDto(), false);
    }

    private String changeInstrumentConfigName(String instrumentconfigname) {
        switch (instrumentconfigname) {
            case "CO2":
                return "currentcarbondioxide";
            case "O2":
                return "currento2";
            case "VOC":
                return "currentvoc";
            case "TEMP":
                return "currenttemperature";
            case "RH":
                return "currenthumidity";
            case "PRESS":
                return "currentairflow";
            case "PM2.5":
                return "currentpm25";
            case "PM10":
                return "currentpm10";
            case "DOOR":
                return "currentdoorstate";
            case "甲醛":
                return "currentformaldehyde";
            case "TEMP1":
                return "currenttemperature1";
            case "TEMP2":
                return "currenttemperature2";
            case "TEMP3":
                return "currenttemperature3";
            case "TEMP4":
                return "currenttemperature4";
            case "TEMP5":
                return "currenttemperature5";
            case "TEMP6":
                return "currenttemperature6";
            case "TEMP7":
                return "currenttemperature7";
            case "TEMP8":
                return "currenttemperature8";
            case "TEMP9":
                return "currenttemperature9";
            case "TEMP10":
                return "currenttemperature10";
            case "LEFTTEMP":
                return "currentlefttemperature";
            case "RIGHTTEMP":
                return "currentrigthtemperature";
            case "气流":
                return "currentairflow1";
            case "DIFFTEMP":
                return "currenttemperaturediff";
            case "PM5":
                return "currentpm5";
            case "PM0.5":
                return "currentpm05";
            case "LEFTCOVERTEMP":
                return "currentleftcovertemperature";
            case "LEFTENDTEMP":
                return "currentleftendtemperature";
            case "左气流":
                return "currentleftairflow";
            case "RIGHTCOVERTEMP":
                return "currentrightcovertemperature";
            case "RIGHTENDTEMP":
                return "currentrightendtemperature";
            case "右气流":
                return "currentrightairflow";
            case "N2":
                return "currentn2";
            case "leftCompartmentHumidity":
                return instrumentconfigname;
            case "rightCompartmentHumidity":
                return instrumentconfigname;
            case "voltage":
                return instrumentconfigname;
            default:
                break;
        }
        return null;
    }

    /**
     * 插入报警备注信息
     * @param warningRecordDto
     * @return
     */
    public void saveInsertAlarmRemarks(WarningRecordInfoDto warningRecordDto) {
        Long id = warningRecordDto.getId();
        if (id==null) {
            warningRecordDto.setCreatetime(new Date());
            warningRecordInfoService.save(warningRecordDto);
        }else {
            warningRecordDto.setUpdatetime(new Date());
            warningRecordInfoService.update(warningRecordDto);
        }
    }

    /**
     * 获取报警备注信息
     * @return
     */
    public WarningRecordInfoDto getWarningRecordInfo(String pkId){
        return warningRecordInfoService.selectWarningRecordInfo(pkId);
    }
}

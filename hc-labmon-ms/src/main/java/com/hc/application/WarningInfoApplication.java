package com.hc.application;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.WarningInfoCommand;
import com.hc.clickhouse.param.WarningRecordParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.dto.CurveInfoDto;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.dto.WarningRecordInfoDto;
import com.hc.my.common.core.constant.enums.CurrentProbeInfoEnum;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.repository.InstrumentParamConfigRepository;
import com.hc.service.WarningRecordInfoService;
import com.hc.util.EquipmentInfoServiceHelp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        WarningRecordParam warningRecordParam = new WarningRecordParam()
                .setHospitalCode(warningInfoCommand.getHospitalCode())
                .setEquipmentNo(warningInfoCommand.getEquipmentNo())
                .setStartTime(warningInfoCommand.getStartTime())
                .setEndTime(warningInfoCommand.getEndTime());
        IPage<Warningrecord> warningRecordIPage = warningrecordRepository.getWarningRecord(page,warningRecordParam);
        List<Warningrecord> records = warningRecordIPage.getRecords();
        if(!CollectionUtils.isEmpty(records)){
            //设置EName
            List<String> collect = records.stream().map(Warningrecord::getInstrumentparamconfigno).distinct().collect(Collectors.toList());
            List<InstrumentParamConfigDto> instrumentParamConfigDtoList = instrumentParamConfigRepository.batchGetProbeInfo(collect);
            Map<String, List<InstrumentParamConfigDto>> ipcNoMap =
                    instrumentParamConfigDtoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentparamconfigno));

            //设备备注信息
            List<String> pkIdList = records.stream().map(Warningrecord::getPkid).distinct().collect(Collectors.toList());
            List<WarningRecordInfoDto> warningRecordInfoList =  warningRecordInfoService.selectWarningRecordInfoByPkIdList(pkIdList);
            Map<String, List<WarningRecordInfoDto>> pkIdMap = null;
            if(CollectionUtils.isEmpty(warningRecordInfoList)){
                pkIdMap = warningRecordInfoList.stream().collect(Collectors.groupingBy(WarningRecordInfoDto::getWarningrecordid));
            }
            Map<String, List<WarningRecordInfoDto>> finalPkIdMap = pkIdMap;
            records.forEach(res->{
                String instrumentparamconfigno = res.getInstrumentparamconfigno();
                if(ipcNoMap.containsKey(instrumentparamconfigno)){
                    InstrumentParamConfigDto instrumentParamConfigDto = ipcNoMap.get(instrumentparamconfigno).get(0);
                    Integer instrumentconfigid = instrumentParamConfigDto.getInstrumentconfigid();
                    String probeEName = CurrentProbeInfoEnum.from(instrumentconfigid).getProbeEName();
                    res.setEName(probeEName);
                }
                res.setRemark("");
                if(finalPkIdMap != null && finalPkIdMap.containsKey(res.getPkid())){
                    WarningRecordInfoDto warningRecordInfoDto = finalPkIdMap.get(res.getPkid()).get(0);
                    res.setRemark(StringUtils.isBlank(warningRecordInfoDto.getInfo()) ? "" :warningRecordInfoDto.getInfo());
                }
            });
        }
        page.setRecords(records);
        return page;
    }

    /**
     * 获取报警设备时间段曲线数据
     * @param pkId
     * @return
     */
    public CurveInfoDto getWarningCurveData(String pkId ,String startTime,String endTime ) {
        String ym = DateUtils.getYearMonth(startTime,endTime);
        Warningrecord warningrecord = warningrecordRepository.getOne(Wrappers.lambdaQuery(new Warningrecord()).eq(Warningrecord::getPkid, pkId));
        if(ObjectUtils.isEmpty(warningrecord)) {
            return null;
        }
        String equipmentNo = warningrecord.getEquipmentno();
        String instrumentParamConfigNo = warningrecord.getInstrumentparamconfigno();
        InstrumentParamConfigDto instrumentParamConfigDto = instrumentParamConfigRepository.getProbeInfo(instrumentParamConfigNo);
        String instrumentConfigName = instrumentParamConfigDto.getInstrumentconfigname();
        Integer instrumentconfigid = instrumentParamConfigDto.getInstrumentconfigid();
        //电量无曲线
        if (StringUtils.isNotBlank(instrumentConfigName) && StringUtils.equalsAnyIgnoreCase(instrumentConfigName,"QC","UPS","DOOR","voltage")){
            throw  new IedsException("QC,UPS,DOOR,VOLTAGE NOT CURVE");
        }
        Map<String, List<InstrumentParamConfigDto>> map = instrumentParamConfigRepository.getInstrumentParamConfigByENo(equipmentNo);
        String probeEName = CurrentProbeInfoEnum.from(instrumentconfigid).getProbeEName();
        List<MonitorequipmentlastdataDto> lastDataList = monitorequipmentlastdataRepository.getWarningCurveData(equipmentNo,startTime,endTime,probeEName,ym);
        List<Monitorequipmentlastdata> monitorEquipmentLastDataList = BeanConverter.convert(lastDataList, Monitorequipmentlastdata.class);
        return EquipmentInfoServiceHelp.getCurveFirst(monitorEquipmentLastDataList, map, false);
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

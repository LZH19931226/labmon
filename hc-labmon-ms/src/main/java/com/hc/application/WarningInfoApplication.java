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
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.WarningRecordInfoDto;
import com.hc.my.common.core.constant.enums.CurrentProbeInfoEnum;
import com.hc.my.common.core.constant.enums.DataFieldEnum;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.AlarmInfoUtils;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.my.common.core.util.RegularUtil;
import com.hc.repository.EquipmentInfoRepository;
import com.hc.repository.InstrumentParamConfigRepository;
import com.hc.service.WarningRecordInfoService;
import com.hc.util.EquipmentInfoServiceHelp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    private EquipmentInfoRepository equipmentInfoRepository;


    /**
     * 分页获取报警信息
     * @param warningInfoCommand
     * @return
     */
    public  Page<Warningrecord> getWarningRecord(WarningInfoCommand warningInfoCommand) {
        filterEquipmentNo(warningInfoCommand);
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
            if(!CollectionUtils.isEmpty(warningRecordInfoList)){
                pkIdMap = warningRecordInfoList.stream().collect(Collectors.groupingBy(WarningRecordInfoDto::getWarningrecordid));
            }
            boolean flag = !Context.IsCh();
            for (Warningrecord res : records) {
                String instrumentparamconfigno = res.getInstrumentparamconfigno();
                if(ipcNoMap.containsKey(instrumentparamconfigno)){
                    InstrumentParamConfigDto instrumentParamConfigDto = ipcNoMap.get(instrumentparamconfigno).get(0);
                    Integer instrumentconfigid = instrumentParamConfigDto.getInstrumentconfigid();
                    String probeEName = CurrentProbeInfoEnum.from(instrumentconfigid).getProbeEName();
                    String cName = DataFieldEnum.fromByLastDataField(probeEName).getCName();
                    String eName = DataFieldEnum.fromByLastDataField(probeEName).getEName();
                    if(StringUtils.equalsAnyIgnoreCase(probeEName,"currentdoorstate","currentdoorstate2","currentups")){
                        res.setAlertRules(cName+"异常");
                        if (flag) {
                            res.setAlertRules(eName+" abnormal");
                        }
                    }else {
                        if(StringUtils.isNotBlank(res.getWarningValue()) && RegularUtil.checkContainsNumbers(res.getWarningValue())){
                            int i =  new BigDecimal(res.getWarningValue()).compareTo(new BigDecimal(res.getHighLimit()));
                            if( i > 0 ){
                                res.setAlertRules(cName + "高于设置的上阈值"+res.getHighLimit());
                                if(flag){
                                    res.setAlertRules(eName + "Above the upper threshold"+res.getHighLimit());
                                }
                            }else {
                                res.setAlertRules(cName + "低于设置的下阈值"+res.getLowLimit());
                                if(flag){
                                    res.setAlertRules(eName + "Below the lower threshold"+res.getHighLimit());
                                }
                            }
                        }
                    }
                    res.setEName(probeEName);
                    if (flag) {
                        //The temperature of the device name is abnormal  Abnormal data is
                        String eRemark = AlarmInfoUtils.setTypeName(res.getWarningremark(),eName);
                        res.setWarningremark(eRemark);
                    }
                }
                res.setRemark("");
                if(pkIdMap != null && pkIdMap.containsKey(res.getPkid())){
                    WarningRecordInfoDto warningRecordInfoDto = pkIdMap.get(res.getPkid()).get(0);
                    res.setRemark(StringUtils.isBlank(warningRecordInfoDto.getInfo()) ? "" :warningRecordInfoDto.getInfo());
                }
            }
        }
        page.setRecords(records);
        return page;
    }

    private void filterEquipmentNo(WarningInfoCommand warningInfoCommand) {
        //分三种情况
        String hospitalCode = warningInfoCommand.getHospitalCode();
        String equipmentTypeId = warningInfoCommand.getEquipmentTypeId();
        String equipmentNo = warningInfoCommand.getEquipmentNo();
        if(StringUtils.isBlank(equipmentNo)){
            //1.只输入hospitalCode
            if(StringUtils.isBlank(equipmentTypeId)){
                List<MonitorEquipmentDto> list = equipmentInfoRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentDto())
                        .select(MonitorEquipmentDto::getEquipmentno)
                        .eq(MonitorEquipmentDto::getHospitalcode, hospitalCode));
                if(CollectionUtils.isEmpty(list)){
                    return;
                }
                List<String> enos = list.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
                String eno = strJoin(enos);
                warningInfoCommand.setEquipmentNo(eno);
            }
            //2.只输入equipmentTypeId hospitalCode
            else {
                List<MonitorEquipmentDto> list = equipmentInfoRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentDto())
                        .select(MonitorEquipmentDto::getEquipmentno)
                        .eq(MonitorEquipmentDto::getHospitalcode, hospitalCode)
                        .eq(MonitorEquipmentDto::getEquipmenttypeid,equipmentTypeId));
                if(CollectionUtils.isEmpty(list)){
                    return;
                }
                List<String> enos = list.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
                String eno =strJoin(enos);
                warningInfoCommand.setEquipmentNo(eno);
            }
        }else {
            warningInfoCommand.setEquipmentNo("'"+warningInfoCommand.getEquipmentNo()+"'");
        }
    }

    private String strJoin(List<String> enos) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String eno : enos) {
            stringBuilder.append("'").append(eno).append("'");
            stringBuilder.append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    /**
     * 获取报警设备时间段曲线数据
     * @param pkId
     * @return
     */
    public CurveInfoDto getWarningCurveData(String pkId ,String startTime,String endTime ) {
        String ym = DateUtils.getYearMonth(startTime,endTime);
        Warningrecord warningrecord = warningrecordRepository.getWarningInfo(pkId,ym);
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

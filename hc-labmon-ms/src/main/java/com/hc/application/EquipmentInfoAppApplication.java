package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.CurveCommand;
import com.hc.application.command.ProbeCommand;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.constants.LabMonEnumError;
import com.hc.device.ProbeRedisApi;
import com.hc.dto.*;
import com.hc.my.common.core.constant.enums.CurrentProbeInfoEnum;
import com.hc.my.common.core.constant.enums.ProbeOutlierMt310;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.command.ProbeRedisCommand;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.service.EquipmentInfoService;
import com.hc.service.HospitalEquipmentService;
import com.hc.service.InstrumentParamConfigService;
import com.hc.service.UserRightService;
import com.hc.util.EquipmentInfoServiceHelp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EquipmentInfoAppApplication {

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private HospitalEquipmentService hospitalEquipmentService;

    @Autowired
    private InstrumentParamConfigService instrumentParamConfigService;

    @Autowired
    private ProbeRedisApi probeRedisApi;

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;

    @Autowired
    private UserRightService userRightService;

    @Autowired
    private WarningrecordRepository warningrecordRepository;

    /**
     * 获取app首页设备数量
     * @param hospitalCode
     * @return
     */
    public List<HospitalEquipmentDto> getEquipmentNum(String hospitalCode) {
        //查出医院的设备类型
        List<HospitalEquipmentDto> hospitalEquipmentDto =  hospitalEquipmentService.selectHospitalEquipmentInfo(hospitalCode);
        if (CollectionUtils.isEmpty(hospitalEquipmentDto)) {
           throw new IedsException(LabMonEnumError.HOSPITAL_IS_NOT_BOUND_EQUIPMENT_TYPE.getMessage());
        }
        List<InstrumentParamConfigDto> instrumentParamConfigDtos =  instrumentParamConfigService.getInstrumentParamConfigByCode(hospitalCode);
        if (CollectionUtils.isEmpty(instrumentParamConfigDtos)) {
            return null;
        }
        Map<String, List<InstrumentParamConfigDto>>  eqTypeIdMap = instrumentParamConfigDtos.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmenttypeid));
        List<HospitalEquipmentDto> dtoList = new ArrayList<>();
        for (HospitalEquipmentDto equipmentDto : hospitalEquipmentDto) {
            String equipmentTypeId = equipmentDto.getEquipmentTypeId();
            List<InstrumentParamConfigDto> list = eqTypeIdMap.get(equipmentTypeId);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            Map<String, List<InstrumentParamConfigDto>> map = list.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno));
            long alarmNum = 0;
            long normalNum = 0;
            for (String equipmentNo : map.keySet()) {
                List<InstrumentParamConfigDto> list1 = map.get(equipmentNo);
                long count = list1.stream().filter(res -> StringUtils.equals("1", res.getState())).count();
                if(count>0){
                    alarmNum++;
                }else {
                    normalNum++;
                }
            }
            equipmentDto.setAlarmNum(String.valueOf(alarmNum));
            equipmentDto.setNormalNum(String.valueOf(normalNum));
            equipmentDto.setTotalNum(String.valueOf(alarmNum+normalNum));
            dtoList.add(equipmentDto);
        }
        return dtoList;
    }
    /**
     * 获取探头当前值
     * @param probeCommand 参数对象
     * @return 分页对象
     */
    public Page<ProbeCurrentInfoDto> getTheCurrentValueOfTheProbe(ProbeCommand probeCommand) {
        String hospitalCode = probeCommand.getHospitalCode();
        Page<ProbeCurrentInfoDto> page = new Page<>(probeCommand.getPageCurrent(),probeCommand.getPageSize());
        //分页查询设备信息
        List<MonitorEquipmentDto> list = equipmentInfoService.getEquipmentInfoByPage(page,probeCommand);
        List<String> enoList = list.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
        ProbeRedisCommand probeRedisCommand = new ProbeRedisCommand();
        probeRedisCommand.setHospitalCode(hospitalCode);
        probeRedisCommand.setENoList(enoList);
        //获取设备对象的探头信息
        List<InstrumentParamConfigDto> instrumentParamConfigByENoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(enoList);
        //先以设备no分组在以instrumentconfid分组
        Map<String, Map<Integer, List<InstrumentParamConfigDto>>> collect1 = null;
        if (CollectionUtils.isNotEmpty(instrumentParamConfigByENoList)) {
            collect1 = instrumentParamConfigByENoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno, Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentconfigid)));
        }
        //批量获取设备对应探头当前值信息
        Map<String, List<ProbeInfoDto>> result = probeRedisApi.getTheCurrentValueOfTheProbeInBatches(probeRedisCommand).getResult();
        List<ProbeCurrentInfoDto> probeCurrentInfoDtos = new ArrayList<>();
        //遍历设备信息
        for (MonitorEquipmentDto monitorEquipmentDto : list) {
            String equipmentname = monitorEquipmentDto.getEquipmentname();
            String equipmentno = monitorEquipmentDto.getEquipmentno();
            String sn = monitorEquipmentDto.getSn();
            List<ProbeInfoDto> probeInfoDtoList = null;
            if (result.containsKey(equipmentno)) {
                probeInfoDtoList = result.get(equipmentno);
            }
            ProbeCurrentInfoDto probeInfo = new ProbeCurrentInfoDto();
            probeInfo.setEquipmentName(equipmentname);
            probeInfo.setEquipmentNo(equipmentno);
            probeInfo.setSn(sn);
            Date maxDate = null;
            if(CollectionUtils.isNotEmpty(probeInfoDtoList)){
                //获取探头信息中最大的时间
                List<Date> collect = probeInfoDtoList.stream().map(ProbeInfoDto::getInputTime).collect(Collectors.toList());
                maxDate = Collections.max(collect);
                //构建探头高低值
                buildProbeHighAndLowValue(equipmentno, probeInfoDtoList, collect1);
                probeInfo.setProbeInfoDtoList(probeInfoDtoList);
            }
            if(maxDate!=null){
                probeInfo.setInputTime(maxDate);
            }
            probeCurrentInfoDtos.add(probeInfo);
        }
        page.setRecords(probeCurrentInfoDtos);
        return page;
    }

    /**
     * 构建探头高低值
     * @param equipmentno 设备no
     * @param probeInfoDtoList 探头信息
     * @param collect1 探头参数map
     */
    private void buildProbeHighAndLowValue(String equipmentno, List<ProbeInfoDto> probeInfoDtoList, Map<String, Map<Integer, List<InstrumentParamConfigDto>>> collect1) {
        if (MapUtils.isEmpty(collect1)) {
            return;
        }
        for (ProbeInfoDto probeInfoDto : probeInfoDtoList) {
            Integer instrumentConfigId = probeInfoDto.getInstrumentConfigId();
            switch (instrumentConfigId){
                //MT310DC
                case 101:
                case 102:
                case 103:
                    String probeEName = probeInfoDto.getProbeEName();
                    instrumentConfigId =  getInstrumentConfigId(probeEName,instrumentConfigId);
                    setProbeHeightAndLowValue(equipmentno,instrumentConfigId,collect1,probeInfoDto);
                    break;
                //其他设备
                default:
                    setProbeHeightAndLowValue(equipmentno,instrumentConfigId,collect1,probeInfoDto);
                    break;
            }
        }
    }

    /**
     * 设置探头高低值
     * @param equipmentno 设备no
     * @param instrumentConfigId 检测类型id
     * @param collect 探头参数map
     */
    private void setProbeHeightAndLowValue(String equipmentno, Integer instrumentConfigId, Map<String, Map<Integer, List<InstrumentParamConfigDto>>> collect,ProbeInfoDto probeInfoDto) {
        if (collect.containsKey(equipmentno) && collect.get(equipmentno).containsKey(instrumentConfigId)) {
            List<InstrumentParamConfigDto> list = collect.get(equipmentno).get(instrumentConfigId);
            if(CollectionUtils.isNotEmpty(list) && !ObjectUtils.isEmpty(list.get(0))){
                InstrumentParamConfigDto instrumentParamConfigDto = list.get(0);
                String state = instrumentParamConfigDto.getState();
                probeInfoDto.setSaturation(instrumentParamConfigDto.getSaturation());
                probeInfoDto.setLowLimit(instrumentParamConfigDto.getLowlimit());
                probeInfoDto.setState(state==null?"0":state);
                probeInfoDto.setHighLimit(instrumentParamConfigDto.getHighlimit());
            }
        }
    }

    /**
     * MT310根据ename返回检测的id
     * @param probeEName 探头的英文名称
     * @param instrumentConfigId  探头检测id
     */
    private Integer getInstrumentConfigId(String probeEName,int instrumentConfigId) {
        switch (probeEName){
            //温度
            case "1":
                instrumentConfigId = CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId();
                break;
            //湿度
            case "2":
                instrumentConfigId =  CurrentProbeInfoEnum.CURRENTHUMIDITY.getInstrumentConfigId();

                break;
            //O2浓度
            case "3":
                instrumentConfigId =  CurrentProbeInfoEnum.CURRENTO2.getInstrumentConfigId();
                break;
            //CO2浓度
            case "4":
                instrumentConfigId =  CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getInstrumentConfigId();
                break;
        }
        return instrumentConfigId;
    }

    /**
     * 获取设备运行时间
     * @param equipmentNo
     * @return
     */
    public DateDto getEquipmentRunningTime(String equipmentNo) {
        MonitorEquipmentDto equipmentInfoByNo = equipmentInfoService.getEquipmentInfoByNo(equipmentNo);
        if(ObjectUtils.isEmpty(equipmentInfoByNo)){
            throw new IedsException("未找到设备信息");
        }
        Date createTime = equipmentInfoByNo.getCreateTime();
        if (createTime == null) {
            return new DateDto();
        }
        Date date = new Date();
        return convert(createTime,date);
    }

    /**
     * 获取两个时间相隔的时间
     * @param startDate
     * @param endDate
     * @return
     */
    public DateDto convert(Date startDate, Date endDate) {
        long startTime = startDate.getTime();//获取毫秒数
        long endTime = endDate.getTime();	 //获取毫秒数
        long timeDifference = endTime-startTime;
        long time = (timeDifference/1000);	//计算秒
        DateDto dateDto = new DateDto();
        if(time<60){
            dateDto.setSecond(time);//设置秒
        }else{
            long minute =  time/60;
            if(minute < 60){
                dateDto.setMinute(minute);//设置分
                dateDto.setSecond(time%60);
            }else {
                long hour = minute/60;
                if(hour < 24){
                    dateDto.setHour(hour);
                    dateDto.setMinute(minute%60);
                    dateDto.setSecond(time%60);
                }else {
                    long date = hour/24;
                    if(date < 30){
                        dateDto.setDate(date);
                        dateDto.setHour(hour%24);
                        dateDto.setMinute(minute%60);
                        dateDto.setSecond(time%60);
                    }else {
                        long month = date/30;
                        if(month<12){
                            dateDto.setMonth(month);
                            dateDto.setDate(date%30);
                            dateDto.setHour(hour%24);
                            dateDto.setMinute(minute%60);
                            dateDto.setSecond(time%60);
                        }else {
                            Long year = month/12;
                            dateDto.setYear(year);
                            dateDto.setMonth(month%12);
                            dateDto.setDate(date%30);
                            dateDto.setHour(hour%24);
                            dateDto.setMinute(minute%60);
                            dateDto.setSecond(time%60);
                        }
                    }
                }
            }
        }
        return dateDto;
    }

    /**
     * 获取曲线接口
     * @return
     */
    public CurveInfoDto getCurveFirst(CurveCommand curveCommand) {
        String startTime = curveCommand.getStartTime();
        String endTime = curveCommand.getEndTime();
        String sn = curveCommand.getSn();
        String equipmentNo = curveCommand.getEquipmentNo();
        List<Monitorequipmentlastdata> lastDataModelList  =  monitorequipmentlastdataRepository.getMonitorEquipmentLastDataInfo(startTime,endTime,equipmentNo);
        if(org.apache.commons.collections.CollectionUtils.isEmpty(lastDataModelList)) {
            throw new IedsException(LabMonEnumError.NO_DATA_FOR_CURRENT_TIME.getMessage());
        }
        Map<String,List<InstrumentParamConfigDto>>  map = instrumentParamConfigService.getInstrumentParamConfigByENo(equipmentNo);
        boolean flag = false;
        if(StringUtils.isNotEmpty(sn) && ProbeOutlierMt310.THREE_ONE.getCode().equals(sn.substring(4,6))){
            flag = true;
        }
        return flag ?
                EquipmentInfoServiceHelp.getCurveFirstByMT300DC(lastDataModelList,map,false):
                EquipmentInfoServiceHelp.getCurveFirst(lastDataModelList,map,false);
    }

    /**
     * 获取实施人员信息
     * @param hospitalCode
     */
    public List<UserRightDto> getImplementerInformation(String hospitalCode){
        List<UserRightDto> list = userRightService.getImplementerInformation(hospitalCode);
        list.forEach(res->{
            if(StringUtils.isBlank(res.getReminders())){
                res.setReminders("");
            }
        });
        return list;
    }

    /**
     * 获取设备报警未读数量
     * @param equipmentNo
     * @return
     */
    public  List<Warningrecord> getNumUnreadDeviceAlarms(String equipmentNo) {
        List<Warningrecord> warningRecordInfo = warningrecordRepository.getWarningRecordInfo(equipmentNo);
        return warningRecordInfo.stream().filter(res ->!StringUtils.equals("1", res.getMsgflag())).collect(Collectors.toList());
    }
}

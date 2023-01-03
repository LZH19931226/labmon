package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AlarmSystemCommand;
import com.hc.application.command.CurveCommand;
import com.hc.application.command.ProbeCommand;
import com.hc.application.command.WarningCommand;
import com.hc.application.response.*;
import com.hc.clickhouse.param.CurveParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.device.ProbeRedisApi;
import com.hc.dto.*;
import com.hc.labmanagent.MonitorEquipmentApi;
import com.hc.my.common.core.constant.enums.CurrentProbeInfoEnum;
import com.hc.my.common.core.constant.enums.ProbeOutlier;
import com.hc.my.common.core.constant.enums.ProbeOutlierMt310;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.redis.command.ProbeRedisCommand;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.RegularUtil;
import com.hc.my.common.core.util.date.DateDto;
import com.hc.my.common.core.util.DateUtils;
import com.hc.service.*;
import com.hc.util.CurveUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AppEquipmentInfoApplication {

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

    @Autowired
    private MonitorEquipmentApi monitorEquipmentApi;

    @Autowired
    private MonitorInstrumentService monitorInstrumentService;

    /**
     * 获取app首页设备数量
     * @param hospitalCode
     * @return
     */
    public List<HospitalEquipmentDto> getEquipmentNum(String hospitalCode,String tags) {
        //查出医院的设备类型
        List<HospitalEquipmentDto> hospitalEquipmentDto = null;
        if (StringUtils.equals(tags,"PC")){
            hospitalEquipmentDto=  hospitalEquipmentService.selectHospitalEquipmentInfoByPc(hospitalCode);
        }else {
            hospitalEquipmentDto=  hospitalEquipmentService.selectHospitalEquipmentInfo(hospitalCode);
        }
        if (CollectionUtils.isEmpty(hospitalEquipmentDto)) {
           throw new IedsException(LabSystemEnum.HOSPITAL_IS_NOT_BOUND_EQUIPMENT_TYPE);
        }
        List<InstrumentParamConfigDto> instrumentParamConfigDtos =  instrumentParamConfigService.getInstrumentParamConfigByCode(hospitalCode);
        if (CollectionUtils.isEmpty(instrumentParamConfigDtos)) {
            return null;
        }
        instrumentParamConfigDtos.removeIf(res->res.getEquipmenttypeid()==null);
        Map<String, List<InstrumentParamConfigDto>>  eqTypeIdMap = instrumentParamConfigDtos.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmenttypeid));
        List<HospitalEquipmentDto> dtoList = new ArrayList<>();
        for (HospitalEquipmentDto equipmentDto : hospitalEquipmentDto) {
            String equipmentTypeId = equipmentDto.getEquipmentTypeId();
            List<InstrumentParamConfigDto> list = eqTypeIdMap.get(equipmentTypeId);
            long alarmNum = 0;
            long normalNum = 0;
            if (CollectionUtils.isNotEmpty(list)) {
                Map<String, List<InstrumentParamConfigDto>> map = list.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno));
                for (String equipmentNo : map.keySet()) {
                    List<InstrumentParamConfigDto> list1 = map.get(equipmentNo);
                    long count = list1.stream().filter(res -> StringUtils.equals("1", res.getState())).count();
                    if (count > 0) {
                        alarmNum++;
                    } else {
                        normalNum++;
                    }
                }
            }
            equipmentDto.setAlarmNum(String.valueOf(alarmNum));
            equipmentDto.setNormalNum(String.valueOf(normalNum));
            equipmentDto.setTotalNum(String.valueOf(alarmNum + normalNum));
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
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        //在查出monitorinstrument信息
        List<String> enoList = list.stream().map(MonitorEquipmentDto::getEquipmentno).distinct().collect(Collectors.toList());
        List<MonitorinstrumentDto> monitorInstrumentDTOList =  monitorInstrumentService.selectMonitorInstrumentByEnoList(enoList);
        Map<String, List<MonitorinstrumentDto>> enoAndMiMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(monitorInstrumentDTOList)){
            enoAndMiMap =  monitorInstrumentDTOList.stream().collect(Collectors.groupingBy(MonitorinstrumentDto::getEquipmentno));
        }
        if (CollectionUtils.isEmpty(list)) {
            return page;
        }
        ProbeRedisCommand probeRedisCommand = new ProbeRedisCommand();
        probeRedisCommand.setHospitalCode(hospitalCode);
        probeRedisCommand.setENoList(enoList);
        //获取设备对象的探头信息
        List<InstrumentParamConfigDto> instrumentParamConfigByENoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(enoList);
        //以设备no分组在以instrumentconfid分组
        Map<String, Map<Integer, List<InstrumentParamConfigDto>>> instrumentParamConfigMap = null;
        if (CollectionUtils.isNotEmpty(instrumentParamConfigByENoList)) {
            instrumentParamConfigMap = instrumentParamConfigByENoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno, Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentconfigid)));
        }
        //批量获取设备对应探头当前值信息
        Map<String, List<ProbeInfoDto>> probeInfoMap = probeRedisApi.getTheCurrentValueOfTheProbeInBatches(probeRedisCommand).getResult();
        List<ProbeCurrentInfoDto> probeCurrentInfoDtos = new ArrayList<>();
        //遍历设备信息
        for (MonitorEquipmentDto monitorEquipmentDto : list) {
            String equipmentName = monitorEquipmentDto.getEquipmentname();
            String equipmentNo = monitorEquipmentDto.getEquipmentno();
            String equipmentTypeId = monitorEquipmentDto.getEquipmenttypeid();
            List<ProbeInfoDto> probeInfoDtoList = null;
            if (probeInfoMap.containsKey(equipmentNo)) {
                probeInfoDtoList = probeInfoMap.get(equipmentNo);
                //中英文切换
                zhAndEn(probeInfoDtoList);
            }
            ProbeCurrentInfoDto probeInfo = new ProbeCurrentInfoDto();
            probeInfo.setEquipmentName(equipmentName);
            probeInfo.setEquipmentNo(equipmentNo);
            if(MapUtils.isNotEmpty(enoAndMiMap) && enoAndMiMap.containsKey(equipmentNo)){
                MonitorinstrumentDto monitorinstrumentDto = enoAndMiMap.get(equipmentNo).get(0);
                probeInfo.setSn(monitorinstrumentDto.getSn());
                probeInfo.setInstrumentTypeId(String.valueOf(monitorinstrumentDto.getInstrumenttypeid()));
            }
            probeInfo.setEquipmentTypeId(equipmentTypeId);
            Date maxDate = null;
            if(CollectionUtils.isNotEmpty(probeInfoDtoList)){
                //获取探头信息中最大的时间
                List<Date> collect = probeInfoDtoList.stream().map(ProbeInfoDto::getInputTime).collect(Collectors.toList());
                maxDate = Collections.max(collect);
                //构建探头高低值
                buildProbeHighAndLowValue(equipmentNo, probeInfoDtoList, instrumentParamConfigMap);
                probeInfo.setProbeInfoDtoList(probeInfoDtoList);
                //获取标头信息(用于前端展示)
                List<String> instrumentConfigId = queryTitle(probeInfoDtoList);
                probeInfo.setInstrumentConfigIdList(instrumentConfigId);
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
     * 中英文切换
     * @param probeInfoDtoList
     */
    private void zhAndEn(List<ProbeInfoDto> probeInfoDtoList) {
        String lang = Context.getLang();
        if("en".equals(lang)){
            List<String> nameList = ProbeOutlier.getNameList();
            probeInfoDtoList.forEach(res->{
                String value = res.getValue();
                if (StringUtils.isNotBlank(value) && nameList.contains(value)) {
                    String code = ProbeOutlier.from(res.getValue()).name();
                    res.setValue(code);
                }
            });
        }
    }

    /**
     * 获取探头标头(用于前端展示)
     * @param probeInfoDtoList
     * @return
     */
    private List<String> queryTitle(List<ProbeInfoDto> probeInfoDtoList) {
        return probeInfoDtoList.stream().map(res -> {
            return CurrentProbeInfoEnum.from(res.getInstrumentConfigId()).getProbeEName();
        }).collect(Collectors.toList());
    }

    /***
     * 获取设备UPS信息
     * @param hospitalCode 医院id
     * @param equipmentTypeId 设备id
     * @return
     */
    public List<MonitorUpsInfoDto> getCurrentUps(String hospitalCode, String equipmentTypeId) {
        List<SnDeviceDto> equipmentList = monitorEquipmentApi.getEquipmentNoList(hospitalCode,equipmentTypeId).getResult();
        if(CollectionUtils.isEmpty(equipmentList)){
            return null;
        }
        //获取equipmentNo的集合
        List<String> equipmentNoList = equipmentList.stream().map(SnDeviceDto::getEquipmentNo).collect(Collectors.toList());
        //以equipmentNo分组
        Map<String, List<SnDeviceDto>> equipmentNoMap = equipmentList.stream().collect(Collectors.groupingBy(SnDeviceDto::getEquipmentNo));
        ProbeRedisCommand probeRedisCommand = new ProbeRedisCommand();
        probeRedisCommand.setHospitalCode(hospitalCode);
        probeRedisCommand.setENoList(equipmentNoList);
        Map<String, List<ProbeInfoDto>> probeInfoMap = probeRedisApi.getTheCurrentValueOfTheProbeInBatches(probeRedisCommand).getResult();
        if(MapUtils.isEmpty(probeInfoMap)){
            return null;
        }
        List<MonitorUpsInfoDto> list = new ArrayList<>();
        equipmentNoList.forEach(res->{
            if(equipmentNoMap.containsKey(res) && probeInfoMap.containsKey(res)){
                MonitorUpsInfoDto monitorUpsInfoDto = buildMonitorUpsInfoDto(res, equipmentNoMap, probeInfoMap);
                list.add(monitorUpsInfoDto);
            }
        });
       return list;
    }

    private MonitorUpsInfoDto buildMonitorUpsInfoDto(String eNo, Map<String, List<SnDeviceDto>> equipmentNoMap, Map<String, List<ProbeInfoDto>> probeInfoMap) {
        SnDeviceDto snDeviceDto = equipmentNoMap.get(eNo).get(0);
        MonitorUpsInfoDto monitorUpsInfoDto = new MonitorUpsInfoDto()
                .setEquipmentName(snDeviceDto.getEquipmentName())
                .setEquipmentNo(snDeviceDto.getEquipmentNo())
                .setSn(snDeviceDto.getSn());
        List<ProbeInfoDto> list = probeInfoMap.get(eNo);
        List<ProbeInfoDto> currentUps = list.stream().filter(res -> res.getProbeEName().equals("currentups")).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(currentUps)){
            monitorUpsInfoDto.setCurrentUps(currentUps.get(0).getValue());
        }
        List<ProbeInfoDto> voltage = list.stream().filter(res -> res.getProbeEName().equals("voltage")).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(voltage)){
            monitorUpsInfoDto.setVoltage(voltage.get(0).getValue());
        }

        return monitorUpsInfoDto;
    }


    /**
     * 构建探头高低值
     * @param equipmentno 设备no
     * @param probeInfoDtoList 探头信息
     * @param instrumentParamConfigMap 探头参数map
     */
    private void buildProbeHighAndLowValue(String equipmentno, List<ProbeInfoDto> probeInfoDtoList, Map<String, Map<Integer, List<InstrumentParamConfigDto>>> instrumentParamConfigMap) {
        if (MapUtils.isEmpty(instrumentParamConfigMap)) {
            return;
        }
        List<ProbeInfoDto> remove = new ArrayList<>();
        for (ProbeInfoDto probeInfoDto : probeInfoDtoList) {
            Integer instrumentConfigId = probeInfoDto.getInstrumentConfigId();
            switch (instrumentConfigId){
                //MT310DC
                case 101:
                case 102:
                case 103:
                    String probeEName = probeInfoDto.getProbeEName();
                    instrumentConfigId =  getInstrumentConfigId(probeEName,instrumentConfigId);
                    setProbeHeightAndLowValue(equipmentno,instrumentConfigId,instrumentParamConfigMap,probeInfoDto,remove);
                    break;
                //其他设备
                default:
                    setProbeHeightAndLowValue(equipmentno,instrumentConfigId,instrumentParamConfigMap,probeInfoDto,remove);
                    break;
            }
        }
        if(CollectionUtils.isNotEmpty(remove)){
            probeInfoDtoList.removeAll(remove);
        }
    }

    /**
     * 设置探头高低值
     * @param equipmentno 设备no
     * @param instrumentConfigId 检测类型id
     * @param instrumentParamConfigMap 探头参数map
     */
    private void setProbeHeightAndLowValue(String equipmentno, Integer instrumentConfigId, Map<String, Map<Integer, List<InstrumentParamConfigDto>>> instrumentParamConfigMap,ProbeInfoDto probeInfoDto,List<ProbeInfoDto> removeList) {
        if (!instrumentParamConfigMap.containsKey(equipmentno) || !instrumentParamConfigMap.get(equipmentno).containsKey(instrumentConfigId) ) {
            removeList.add(probeInfoDto);
        }
        List<InstrumentParamConfigDto> list = instrumentParamConfigMap.get(equipmentno).get(instrumentConfigId);
        if(CollectionUtils.isNotEmpty(list)){
            InstrumentParamConfigDto instrumentParamConfigDto = list.get(0);
            String state = instrumentParamConfigDto.getState();
            probeInfoDto.setSaturation(instrumentParamConfigDto.getSaturation());
            probeInfoDto.setLowLimit(instrumentParamConfigDto.getLowLimit());
            probeInfoDto.setState(state==null?"0":state);
            probeInfoDto.setHighLimit(instrumentParamConfigDto.getHighLimit());
            if(StringUtils.isNotBlank(instrumentParamConfigDto.getUnit()) && RegularUtil.checkContainsNumbers(probeInfoDto.getValue())){
                String unit = instrumentParamConfigDto.getUnit();
                probeInfoDto.setUnit(unit);
                probeInfoDto.setValue(probeInfoDto.getValue());
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
            default:
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
            throw new IedsException(LabSystemEnum.EQUIPMENT_INFO_NOT_FOUND);
        }
        Date createTime = equipmentInfoByNo.getCreateTime();
        if (createTime == null) {
            return new DateDto();
        }
        Date date = new Date();
        return DateUtils.convert(createTime,date);
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
        String ym = DateUtils.getYearMonth(startTime,endTime);
        CurveParam curveParam = BeanConverter.convert(curveCommand, CurveParam.class);
        curveParam.setYearMonth(ym);
        List<Monitorequipmentlastdata> lastDataModelList  =  monitorequipmentlastdataRepository.getMonitorEquuipmentLastList(curveParam);

        if(org.apache.commons.collections.CollectionUtils.isEmpty(lastDataModelList)) {
            throw new IedsException(LabSystemEnum.NO_DATA_FOR_CURRENT_TIME);
        }
        Map<String,List<InstrumentParamConfigDto>>  map = instrumentParamConfigService.getInstrumentParamConfigByENo(equipmentNo);
        boolean flag = false;
        if(StringUtils.isNotEmpty(sn) && ProbeOutlierMt310.THREE_ONE.getCode().equals(sn.substring(4,6))){
            flag = true;
        }
        return CurveUtils.getCurveFirst(lastDataModelList,curveCommand.getInstrumentConfigIdList(),map);
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

    public List<WarningRecordInfo> getWarningInfoList(WarningCommand warningCommand){
        List<Warningrecord> warningRecord =  warningrecordRepository.getWarningInfoList(warningCommand.getHospitalCode(),warningCommand.getStartTime());
        if(CollectionUtils.isEmpty(warningRecord)){
            return null;
        }
        List<String> enoList = warningRecord.stream().map(Warningrecord::getEquipmentno).distinct().collect(Collectors.toList());
        List<String> ipcNoList = warningRecord.stream().map(Warningrecord::getInstrumentparamconfigno).distinct().collect(Collectors.toList());
        Map<String, List<Warningrecord>> enoAndWrMap = warningRecord.stream().collect(Collectors.groupingBy(Warningrecord::getEquipmentno));
        //获取设备信息
        List<MonitorEquipmentDto> equipmentDtoList = equipmentInfoService.batchGetEquipmentInfo(enoList);
        Map<String, List<MonitorEquipmentDto>> enoAndEInfoMap = equipmentDtoList.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmentno));
        //获取探头信息
        List<InstrumentParamConfigDto> instrumentParamConfigDtoList = instrumentParamConfigService.batchGetProbeInfo(ipcNoList);
        Map<String, List<InstrumentParamConfigDto>> ipcNoAndProbeMap = instrumentParamConfigDtoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentparamconfigno));
        List<WarningRecordInfo> list = new ArrayList<>();
        for (String eno : enoList) {
            WarningRecordInfo warningRecordInfo = new WarningRecordInfo();
            //设置设备信息
            if(enoAndWrMap.containsKey(eno)){
                Warningrecord warningrecord = enoAndWrMap.get(eno).get(0);
                String instrumentParamConfigNo = warningrecord.getInstrumentparamconfigno();
                //设置探头信息
                if(ipcNoAndProbeMap.containsKey(instrumentParamConfigNo)){
                    InstrumentParamConfigDto instrumentParamConfigDto = ipcNoAndProbeMap.get(instrumentParamConfigNo).get(0);
                    warningRecordInfo.setEName(CurrentProbeInfoEnum.from(instrumentParamConfigDto.getInstrumentconfigid()).getProbeEName());
                }
                warningRecordInfo.setAlwayalarm(warningrecord.getAlwayalarm());
                warningRecordInfo.setAlarmRules(warningrecord.getAlarmTime());
                warningRecordInfo.setHighLimit(warningrecord.getHighLimit());
                warningRecordInfo.setLowLimit(warningrecord.getLowLimit());
                warningRecordInfo.setInputDateTime(warningrecord.getInputdatetime());
                warningRecordInfo.setWarningValue(warningrecord.getWarningValue());

            }
            if(enoAndEInfoMap.containsKey(eno)){
                MonitorEquipmentDto monitorEquipmentDto = enoAndEInfoMap.get(eno).get(0);
                warningRecordInfo.setEquipmentNo(eno);
                warningRecordInfo.setEquipmentName(monitorEquipmentDto.getEquipmentname());
                warningRecordInfo.setEquipmentTypeId(monitorEquipmentDto.getEquipmenttypeid());
                warningRecordInfo.setSn(monitorEquipmentDto.getSn());
            }
            list.add(warningRecordInfo);
        }
        return list;
    }

    /**
     * 获取设备详细信息
     * @param warningCommand
     * @return
     */
    public Page getWarningDetailInfo(WarningCommand warningCommand) {
        String equipmentNo = warningCommand.getEquipmentNo();
        String startTime = warningCommand.getStartTime();
        String endTime = warningCommand.getEndTime();
        Page page = new Page(warningCommand.getPageCurrent(),warningCommand.getPageSize());
        List<Warningrecord> warningRecordList = warningrecordRepository.getWarningRecordDetailInfo(page,equipmentNo, startTime, endTime);
        if (CollectionUtils.isEmpty(warningRecordList)) {
            return null;
        }
        String hospitalCode = warningRecordList.get(0).getHospitalcode();
        //获取医院所有的人员信息
        List<UserRightDto> userRightDtoList = userRightService.getallByHospitalCode(hospitalCode);
        Map<String, List<UserRightDto>> userMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(userRightDtoList)){
            userMap = userRightDtoList.stream().collect(Collectors.groupingBy(UserRightDto::getPhoneNum));
        }
        List<String> collect = warningRecordList.stream().map(Warningrecord::getInstrumentparamconfigno).collect(Collectors.toList());
        List<WarningDetailInfo> detailInfos = BeanConverter.convert(warningRecordList, WarningDetailInfo.class);
        List<InstrumentParamConfigDto> probeInfoList = instrumentParamConfigService.batchGetProbeInfo(collect);
        Map<String, List<InstrumentParamConfigDto>> stringListMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(probeInfoList)){
            stringListMap = probeInfoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentparamconfigno));
        }
        for (WarningDetailInfo detailInfo : detailInfos) {
            //将手机号换成用户名
            String mailCallUser = detailInfo.getMailCallUser();
            if(StringUtils.isNotBlank(mailCallUser)){
               List<String>  list= Arrays.asList(mailCallUser.split("/"));
               String str =  listToStr(list,userMap);
               detailInfo.setMailCallUser(str);
            }
            String phoneCallUser = detailInfo.getPhoneCallUser();
            if(StringUtils.isNotBlank(phoneCallUser)){
                List<String>  list= Arrays.asList(phoneCallUser.split("/"));
                String str =  listToStr(list,userMap);
                detailInfo.setPhoneCallUser(str);
            }
            List<String> list1 = getNoticeList(mailCallUser,phoneCallUser,userMap);
            detailInfo.setInfoNoticeList(list1);

            String instrumentparamconfigno = detailInfo.getInstrumentparamconfigno();
            if(!stringListMap.containsKey(instrumentparamconfigno)){
                continue;
            }
            List<InstrumentParamConfigDto> list = stringListMap.get(instrumentparamconfigno);
            if (CollectionUtils.isNotEmpty(list)) {
                InstrumentParamConfigDto instrumentParamConfigDto = list.get(0);
                Integer instrumentconfigid = instrumentParamConfigDto.getInstrumentconfigid();
                detailInfo.setEName(CurrentProbeInfoEnum.from(instrumentconfigid).getProbeEName());
            }
        }
        page.setRecords(detailInfos);
        return page;
    }

    /**
     * 获取消息通知集合
     * @param mailCallUser
     * @param phoneCallUser
     * @return
     */
    private List<String> getNoticeList(String mailCallUser, String phoneCallUser,Map<String, List<UserRightDto>> userMap) {
        List<String> list = new ArrayList<>();
        //1.都为空时
        if(StringUtils.isBlank(mailCallUser) && StringUtils.isBlank(phoneCallUser)){
            return null;
        }
        //2.都不为空时
        if(!StringUtils.isBlank(mailCallUser) && !StringUtils.isBlank(phoneCallUser)){
            List<String>  mailList= Arrays.asList(mailCallUser.split("/"));
            List<String>  phoneList= Arrays.asList(phoneCallUser.split("/"));
            List<String> sameList = mailList.stream().filter(phoneList::contains).collect(Collectors.toList());
            sameList.forEach(res->{
                list.add(res + SysConstants.PHONE_SMS_NOTIFICATIONS);
            });
            List<String> strings = new ArrayList<>(CollectionUtils.removeAll(mailList, sameList));
            if (strings.size() > 0) {
                mailList.forEach(res -> list.add(res + SysConstants.SMS_NOTIFICATIONS));
            } else {
                phoneList.forEach(res -> list.add(res + SysConstants.PHONE_NOTIFICATIONS));
            }
            return list;
        }
        //3.有一个为空
        if (StringUtils.isBlank(mailCallUser)) {
            List<String>  phoneList= Arrays.asList(phoneCallUser.split("/"));
            phoneList.forEach(res -> list.add(res + SysConstants.PHONE_NOTIFICATIONS));
        }else {
            List<String>  mailList= Arrays.asList(mailCallUser.split("/"));
            mailList.forEach(res -> list.add(res + SysConstants.SMS_NOTIFICATIONS));
        }
        return list;
    }

    private String listToStr(List<String> list, Map<String, List<UserRightDto>> userMap) {
        StringBuilder stringBuilder =new StringBuilder();
        for (String phoneNum : list) {
            if (userMap.containsKey(phoneNum)) {
                String username = userMap.get(phoneNum).get(0).getUsername();
                stringBuilder.append(username).append("/");
            }else {
                stringBuilder.append(phoneNum).append("/");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    public Page getAlarmSystemInfo(ProbeCommand probeCommand) {
        Page page = new Page(probeCommand.getPageCurrent(),probeCommand.getPageSize());
        List<MonitorEquipmentDto> equipmentInfoByPage = equipmentInfoService.getEquipmentInfoByPage(page, probeCommand);
        if (CollectionUtils.isEmpty(equipmentInfoByPage)) {
            return null;
        }
        List<String> collect = equipmentInfoByPage.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
        List<InstrumentParamConfigDto> instrumentParamConfigByENoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(collect);
        Map<String, List<InstrumentParamConfigDto>> eNoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(instrumentParamConfigByENoList)) {
            eNoMap =  instrumentParamConfigByENoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno));
        }
        List<AlarmSystem> list = new ArrayList<>();
        for (MonitorEquipmentDto monitorEquipmentDto : equipmentInfoByPage) {
            AlarmSystem alarmSystem = new AlarmSystem();
            alarmSystem.setEquipmentName(monitorEquipmentDto.getEquipmentname());
            alarmSystem.setSn(monitorEquipmentDto.getSn());
            alarmSystem.setEquipmentNo(monitorEquipmentDto.getEquipmentno());
            alarmSystem.setHospitalCode(monitorEquipmentDto.getHospitalcode());
            if (StringUtils.isBlank(monitorEquipmentDto.getWarningSwitch())) {
                monitorEquipmentDto.setWarningSwitch("1");
            }
            if(eNoMap.containsKey(monitorEquipmentDto.getEquipmentno())){
                List<InstrumentParamConfigDto> paramConfigDtoList = eNoMap.get(monitorEquipmentDto.getEquipmentno());
                long count = paramConfigDtoList.stream().filter(res -> "1".equals(res.getWarningphone())).count();
                if(count>0){
                    alarmSystem.setWarningSwitch("1");
                }else{
                    alarmSystem.setWarningSwitch("0");
                }
                List<ProbeAlarmState> list1 = new ArrayList<>();
                paramConfigDtoList.forEach(res->{
                    ProbeAlarmState probeAlarmState = new ProbeAlarmState();
                    probeAlarmState.setInstrumentParamConfigNo(res.getInstrumentparamconfigno());
                    probeAlarmState.setWarningPhone(res.getWarningphone());
                    probeAlarmState.setInstrumentConfigId(res.getInstrumentconfigid());
                    probeAlarmState.setInstrumentNo(res.getInstrumentno());
                    probeAlarmState.setEName(CurrentProbeInfoEnum.from(res.getInstrumentconfigid()).getProbeEName());
                    probeAlarmState.setLowLimit(res.getLowLimit().toString());
                    probeAlarmState.setHighLimit(res.getHighLimit().toString());
                    list1.add(probeAlarmState);
                });
                if (CollectionUtils.isNotEmpty(list1)) {
                    alarmSystem.setProbeAlarmStateList(list1);
                }
            }
            list.add(alarmSystem);
        }
        page.setRecords(list);
        return page;
    }

    public void synchronizedDeviceAlarmSwitch() {
        List<MonitorEquipmentDto> list = equipmentInfoService.getAll();
        List<InstrumentParamConfigDto> instrumentParamConfigDtoList = instrumentParamConfigService.getAll();
        Map<String, List<InstrumentParamConfigDto>> map =
                instrumentParamConfigDtoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentno));
        for (MonitorEquipmentDto monitorEquipmentDto : list) {
            String instrumentNo = monitorEquipmentDto.getInstrumentNo();
            if(map.containsKey(instrumentNo)){
                List<InstrumentParamConfigDto> paramConfigDtoList = map.get(instrumentNo);
                long count = paramConfigDtoList.stream().filter(res -> SysConstants.IN_ALARM.equals(res.getWarningphone())).count();
                if(count>0){
                    monitorEquipmentDto.setWarningSwitch(SysConstants.IN_ALARM);
                }else {
                    monitorEquipmentDto.setWarningSwitch(SysConstants.NORMAL);
                }
            }
        }
        //更新数据库
        equipmentInfoService.bulkUpdate(list);
    }


    /**
     * 获取报警设置设备的数量
     * @param alarmSystemCommand
     * @return
     */
    public AlarmHand getTheNumberOfAlarmSettingDevices(AlarmSystemCommand alarmSystemCommand) {
        String hospitalCode = alarmSystemCommand.getHospitalCode();
        String equipmentTypeId = alarmSystemCommand.getEquipmentTypeId();
        //通过医院id和设备类型id获取所有的探头信息
        List<InstrumentParamConfigDto> instrumentParamConfigDtoList =
                instrumentParamConfigService.getInstrumentParamConfigByCodeAndTypeId(hospitalCode,equipmentTypeId);
        if(CollectionUtils.isEmpty(instrumentParamConfigDtoList)){
            throw new IedsException(LabSystemEnum.EQUIPMENT_INFO_NOT_FOUND);
        }
        //计算设备报警是否启用：一个设备下只要有一个探头设置了报警视为设备开启报警 全未设置报警视为设备未开启报警
        Map<String, List<InstrumentParamConfigDto>> instrumentNoMap =
                instrumentParamConfigDtoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentno));
        int enableNum = 0;
        int disabledNum = 0;
        for (String instrumentNo : instrumentNoMap.keySet()) {
            List<InstrumentParamConfigDto> instrumentParamConfigDtoList1 = instrumentNoMap.get(instrumentNo);
            long count = instrumentParamConfigDtoList1.stream().filter(res -> "1".equals(res.getWarningphone())).count();
            if(count>0){
                enableNum++;
            }else {
                disabledNum++;
            }
        }
        AlarmHand alarmHand = new AlarmHand();
        alarmHand.setDisabledNum(disabledNum);
        alarmHand.setEnableNum(enableNum);
        return alarmHand;
    }
}

package com.hc.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AlarmSystemCommand;
import com.hc.application.command.CurveCommand;
import com.hc.application.command.ProbeCommand;
import com.hc.application.command.WarningCommand;
import com.hc.application.curvemodel.CurveDataModel;
import com.hc.application.response.*;
import com.hc.clickhouse.param.CurveParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.device.ProbeRedisApi;
import com.hc.dto.*;
import com.hc.labmanagent.MonitorEquipmentApi;
import com.hc.my.common.core.constant.enums.*;
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
import com.hc.repository.HospitalInfoRepository;
import com.hc.repository.InstrumentMonitorInfoRepository;
import com.hc.service.*;
import com.hc.util.CurveUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
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

    @Autowired
    private InstrumentMonitorInfoRepository instrumentMonitorInfoRepository;
    @Autowired
    private HospitalInfoRepository hospitalInfoRepository;

    /**
     * 获取app首页设备数量
     *
     * @param hospitalCode
     * @param tags
     * @return
     */
    public List<HospitalEquipmentDto> getEquipmentNum(String hospitalCode, String tags) {
        ProbeCommand probeCommand = new ProbeCommand();
        probeCommand.setHospitalCode(hospitalCode);
        Page<ProbeCurrentInfoDto> page = new Page<>(1, 10000);
        //获取设备 未禁用的设备
        List<MonitorEquipmentDto> list = equipmentInfoService.getEquipmentInfoByPage(page, probeCommand);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        //在查出monitorinstrument信息
        List<String> enoList = list.stream().map(MonitorEquipmentDto::getEquipmentno).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        ProbeRedisCommand probeRedisCommand = new ProbeRedisCommand();
        probeRedisCommand.setHospitalCode(hospitalCode);
        probeRedisCommand.setENoList(enoList);
        //获取设备对象的探头信息
        List<InstrumentParamConfigDto> instrumentParamConfigByENoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(enoList);
        //以设备no分组在以instrumentconfid分组
        Map<String, Map<String, List<InstrumentParamConfigDto>>> instrumentParamConfigMap = null;
        if (CollectionUtils.isNotEmpty(instrumentParamConfigByENoList)) {
            instrumentParamConfigMap = instrumentParamConfigByENoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno, Collectors.groupingBy(res->res.getInstrumentno()+":"+res.getInstrumentconfigid())));
        }
        //批量获取设备对应探头当前值信息
        Map<String, List<ProbeInfoDto>> probeInfoMap = probeRedisApi.getTheCurrentValueOfTheProbeInBatches(probeRedisCommand).getResult();
        //获取医院超时变红间隔
        HospitalInfoDto hos = hospitalInfoRepository.getOne(Wrappers.lambdaQuery(new HospitalInfoDto()).eq(HospitalInfoDto::getHospitalCode, hospitalCode));
        String timeoutRedDuration = hos.getTimeoutRedDuration();
        if (StringUtils.isEmpty(timeoutRedDuration)){
            timeoutRedDuration="30";
        }
        Map<String, List<MonitorEquipmentDto>> allEq = list.stream().filter(res->StringUtils.isNotBlank(res.getEquipmenttypeid())).collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmenttypeid));

        List<HospitalEquipmentDto> hospitalEquipmentDto = new ArrayList<>();
        if (StringUtils.equals(tags, "PC")) {
            hospitalEquipmentDto = hospitalEquipmentService.selectHospitalEquipmentInfoByPc(hospitalCode);
        } else {
            hospitalEquipmentDto = hospitalEquipmentService.selectHospitalEquipmentInfo(hospitalCode);
        }
        List<HospitalEquipmentDto> reList = new ArrayList<>();
        for (HospitalEquipmentDto value : hospitalEquipmentDto) {
            String eqTypeId = value.getEquipmentTypeId();
            List<MonitorEquipmentDto> eq = allEq.get(eqTypeId);
            HospitalEquipmentDto hospitalEquipmentDto1 = new HospitalEquipmentDto();
            int totalNum = 0;
            int normalCount = 0;
            int abnormalCount = 0;
            int timeoutCount = 0;
            if (CollectionUtils.isNotEmpty(eq)) {
                totalNum = eq.size();
                for (MonitorEquipmentDto monitorEquipmentDto : eq) {
                    String equipmentNo = monitorEquipmentDto.getEquipmentno();
                    List<ProbeInfoDto> probeInfoDtoList = null;
                    if (probeInfoMap.containsKey(equipmentNo)) {
                        probeInfoDtoList = probeInfoMap.get(equipmentNo);
                    }
                    ProbeCurrentInfoDto probeInfo = new ProbeCurrentInfoDto();
                    if (CollectionUtils.isEmpty(probeInfoDtoList)) {
                        probeInfo.setState("1");
                        abnormalCount++;
                    } else {
                        Date maxDate;
                        List<Date> collect = probeInfoDtoList.stream().map(ProbeInfoDto::getInputTime).collect(Collectors.toList());
                        maxDate = Collections.max(collect);
                        if (maxDate != null) {
                            probeInfo.setInputTime(maxDate);
                        }
                        buildProbeInfoDtoListAndInstrumentConfigIdList(probeInfo, equipmentNo, probeInfoDtoList, instrumentParamConfigMap, timeoutRedDuration);
                        switch (probeInfo.getState()) {
                            case "0":
                                normalCount++;
                                probeInfo.setState("0");
                                break;
                            case "1":
                                abnormalCount++;
                                probeInfo.setState("1");
                                break;
                            case "2":
                                timeoutCount++;
                                probeInfo.setState("2");
                                break;
                        }
                    }
                }
            }
            hospitalEquipmentDto1.setEquipmenttypename(Context.IsCh()?value.getEquipmenttypename():value.getEquipmenttypenameUs());
            hospitalEquipmentDto1.setEquipmentTypeId(eqTypeId);
            hospitalEquipmentDto1.setNormalNum(String.valueOf(normalCount));
            hospitalEquipmentDto1.setTotalNum(String.valueOf(totalNum));
            hospitalEquipmentDto1.setTimeoutNum(String.valueOf(timeoutCount));
            hospitalEquipmentDto1.setAlarmNum(String.valueOf(abnormalCount));
            reList.add(hospitalEquipmentDto1);
        }
        return reList;

    }

    /**
     * 获取探头当前值
     *
     * @param probeCommand 参数对象
     * @return 分页对象
     */
    public CurrentProbeInfoResult getTheCurrentValueOfTheProbe(ProbeCommand probeCommand) {
        CurrentProbeInfoResult currentProbeInfoResult = new CurrentProbeInfoResult();
        String hospitalCode = probeCommand.getHospitalCode();

        String state = probeCommand.getState();
        //分页查询设备信息
        List<MonitorEquipmentDto> list = equipmentInfoService.getEquipmentInfo(probeCommand);
        if (CollectionUtils.isEmpty(list)) {
            return new CurrentProbeInfoResult();
        }
        currentProbeInfoResult.setTotalNum(list.size());
        //在查出monitorinstrument信息
        List<String> enoList = list.stream().map(MonitorEquipmentDto::getEquipmentno).distinct().collect(Collectors.toList());
        List<MonitorinstrumentDto> monitorInstrumentDTOList = monitorInstrumentService.selectMonitorInstrumentByEnoList(enoList);
        Map<String, List<MonitorinstrumentDto>> enoAndMiMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(monitorInstrumentDTOList)) {
            enoAndMiMap = monitorInstrumentDTOList.stream().collect(Collectors.groupingBy(MonitorinstrumentDto::getEquipmentno));
        }

        ProbeRedisCommand probeRedisCommand = new ProbeRedisCommand();
        probeRedisCommand.setHospitalCode(hospitalCode);
        probeRedisCommand.setENoList(enoList);
        //获取设备对象的探头信息
        List<InstrumentParamConfigDto> instrumentParamConfigByENoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(enoList);
        //以设备no分组在以instrumentconfid分组
        Map<String, Map<String, List<InstrumentParamConfigDto>>> instrumentParamConfigMap = null;
        if (CollectionUtils.isNotEmpty(instrumentParamConfigByENoList)) {
            instrumentParamConfigMap = instrumentParamConfigByENoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno, Collectors.groupingBy(res->res.getInstrumentno()+":"+res.getInstrumentconfigid())));
        }

        //批量获取设备对应探头当前值信息
        Map<String, List<ProbeInfoDto>> probeInfoMap = probeRedisApi.getTheCurrentValueOfTheProbeInBatches(probeRedisCommand).getResult();

        //获取医院超时报警间隔
        HospitalInfoDto hos = hospitalInfoRepository.getOne(Wrappers.lambdaQuery(new HospitalInfoDto()).eq(HospitalInfoDto::getHospitalCode, hospitalCode));
        String timeoutRedDuration = hos.getTimeoutRedDuration();
        if(StringUtils.isBlank(timeoutRedDuration)){
            timeoutRedDuration="30";
        }

        List<ProbeCurrentInfoDto> probeCurrentInfoDtos = new ArrayList<>();
        /*
         * 1.遍历设备信息赋值
         * */
        int normalCount = 0;
        int abnormalCount = 0;
        int timeoutCount = 0;

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
            if (MapUtils.isNotEmpty(enoAndMiMap) && enoAndMiMap.containsKey(equipmentNo)) {
                MonitorinstrumentDto monitorinstrumentDto = enoAndMiMap.get(equipmentNo).get(0);
                probeInfo.setSn(monitorinstrumentDto.getSn());
                probeInfo.setInstrumentTypeId(String.valueOf(monitorinstrumentDto.getInstrumenttypeid()));
            }
            probeInfo.setEquipmentTypeId(equipmentTypeId);
            if(CollectionUtils.isEmpty(probeInfoDtoList)){
                probeInfo.setState("1");
                abnormalCount++;
            }else {

                buildProbeInfoDtoListAndInstrumentConfigIdList(probeInfo, equipmentNo, probeInfoDtoList, instrumentParamConfigMap, timeoutRedDuration);
                switch (probeInfo.getState()){
                    case "0":
                        normalCount++;
                        probeInfo.setState("0");
                        break;
                    case "1":
                        abnormalCount++;
                        probeInfo.setState("1");
                        break;
                    case "2":
                        timeoutCount++;
                        probeInfo.setState("2");
                        break;
                }
            }
            probeCurrentInfoDtos.add(probeInfo);
        }
        if(StringUtils.isBlank(probeCommand.getState())){
            probeCommand.setState("");
        }
        currentProbeInfoResult.setAbnormalNum(abnormalCount);
        currentProbeInfoResult.setNormalNum( normalCount);
        currentProbeInfoResult.setTimeoutNum(timeoutCount);
        switch (state){
            case "0":
                List<ProbeCurrentInfoDto> normalList = probeCurrentInfoDtos.stream().filter(res -> StringUtils.equals("0", res.getState())).collect(Collectors.toList());
                currentProbeInfoResult.setProbeCurrentInfoDtoList(normalList);
                break;
            case "1":
                List<ProbeCurrentInfoDto> abnormalList = probeCurrentInfoDtos.stream().filter(res -> StringUtils.equals("1", res.getState())).collect(Collectors.toList());
                currentProbeInfoResult.setProbeCurrentInfoDtoList(abnormalList);
                break;
            case "2":
                List<ProbeCurrentInfoDto> timeoutList = probeCurrentInfoDtos.stream().filter(res -> StringUtils.equals("2", res.getState())).collect(Collectors.toList());
                currentProbeInfoResult.setProbeCurrentInfoDtoList(timeoutList);
                break;
            default:
                currentProbeInfoResult.setProbeCurrentInfoDtoList(probeCurrentInfoDtos);
                break;
        }
        return probeSort(currentProbeInfoResult);
    }

    private CurrentProbeInfoResult probeSort(CurrentProbeInfoResult currentProbeInfoResult) {
        //探头排序
        for (ProbeCurrentInfoDto probeCurrentInfoDto : currentProbeInfoResult.getProbeCurrentInfoDtoList()) {
            List<ProbeInfoDto> probeInfoDtoList = probeCurrentInfoDto.getProbeInfoDtoList();
            if(CollectionUtils.isNotEmpty(probeInfoDtoList)){
                List<ProbeInfoDto> collect =
                        probeInfoDtoList.stream().sorted(Comparator.comparing(ProbeInfoDto::getInstrumentConfigId)).collect(Collectors.toList());
                probeCurrentInfoDto.setProbeInfoDtoList(collect);
            }
        }
        //设备排序
//        List<ProbeCurrentInfoDto> collect = currentProbeInfoResult.getProbeCurrentInfoDtoList().stream().sorted(Comparator.comparing(ProbeCurrentInfoDto::getEquipmentName)).collect(Collectors.toList());
//        currentProbeInfoResult.setProbeCurrentInfoDtoList(collect);
        return currentProbeInfoResult;
    }

    private void buildProbeInfoDtoListAndInstrumentConfigIdList(ProbeCurrentInfoDto probeInfo,String equipmentNo, List<ProbeInfoDto> probeInfoDtoList, Map<String, Map<String, List<InstrumentParamConfigDto>>> instrumentParamConfigMap,String timeoutRedDuration) {
        //获取探头信息中最大的时间
        Date maxDate = null;
        List<Date> dates = probeInfoDtoList.stream().map(ProbeInfoDto::getInputTime).collect(Collectors.toList());
        maxDate = Collections.max(dates);
        if (maxDate != null) {
            probeInfo.setInputTime(maxDate);
            boolean b = DateUtils.calculateIntervalTime(maxDate, timeoutRedDuration);
            if(b){
                probeInfo.setState("2");
            }
        }
        //构建探头高低值
        buildProbeHighAndLowValue(equipmentNo, probeInfoDtoList, instrumentParamConfigMap,timeoutRedDuration);
        probeInfo.setProbeInfoDtoList(probeInfoDtoList);
        if(StringUtils.isBlank(probeInfo.getState())){
            long abnormal = probeInfoDtoList.stream().filter(res -> res.getState().equals("1")).count();
            if(abnormal>0){
                probeInfo.setState("1");
            }else {
                probeInfo.setState("0");
            }
        }
        //获取标头信息(用于前端展示),并做过滤
        if(instrumentParamConfigMap.containsKey(equipmentNo)){
            Map<String, List<InstrumentParamConfigDto>> integerListMap = instrumentParamConfigMap.get(equipmentNo);
            List<String> instrumentConfigId  = queryTitle(integerListMap,probeInfo);
            probeInfo.setInstrumentConfigIdList(instrumentConfigId);
            fiterTitle(probeInfo);
        }
    }

    private void fiterTitle(ProbeCurrentInfoDto probeInfo) {
        List<ProbeInfoDto> newProbeInfoDto = new ArrayList<>();
        List<String> instrumentConfigIdList = new ArrayList<>();
        List<String> instrumentConfigId = probeInfo.getInstrumentConfigIdList();
        List<ProbeInfoDto> probeInfoDtoList = probeInfo.getProbeInfoDtoList();
        for (ProbeInfoDto probeInfoDto : probeInfoDtoList) {
            if (instrumentConfigId.contains(probeInfoDto.getProbeEName())) {
                newProbeInfoDto.add(probeInfoDto);
                instrumentConfigIdList.add(probeInfoDto.getProbeEName());
            }
        }
        probeInfo.setProbeInfoDtoList(newProbeInfoDto);
        probeInfo.setInstrumentConfigIdList(instrumentConfigIdList);
    }

    /**
     * 中英文切换
     *
     * @param probeInfoDtoList
     */
    private void zhAndEn(List<ProbeInfoDto> probeInfoDtoList) {
        String lang = Context.getLang();
        if ("en".equals(lang)) {
            List<String> nameList = ProbeOutlier.getNameList();
            probeInfoDtoList.forEach(res -> {
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
     * @return
     */
    private List<String> queryTitle( Map<String, List<InstrumentParamConfigDto>> integerListMap ,ProbeCurrentInfoDto probeCurrentInfoDto) {
        //探头信息
        List<ProbeInfoDto> probeInfoDtoList = probeCurrentInfoDto.getProbeInfoDtoList();
        Map<String, List<ProbeInfoDto>> inoMap = probeInfoDtoList.stream().collect(Collectors.groupingBy(res->res.getInstrumentNo()+":"+res.getInstrumentConfigId()));
        List<String> eNames = new ArrayList<>();
        for (String str : integerListMap.keySet()) {
            int i = str.indexOf(":");
            String instrumentNo = str.substring(0, i);
            String config = str.substring(i+1);
            //当config为35时并且不包含ino:35 但包含ino:7时
            if(StringUtils.equals("35",config) && !inoMap.containsKey(str) && inoMap.containsKey(instrumentNo+":"+"7")){
                String eName = inoMap.get(instrumentNo+":"+"7").get(0).getProbeEName();
                eNames.add(eName);
            }
            if(inoMap.containsKey(str)){
                String eName = inoMap.get(str).get(0).getProbeEName();
                eNames.add(eName);
            }
        }
        return eNames;
    }

    /***
     * 获取设备UPS信息
     * @param hospitalCode 医院id
     * @param equipmentTypeId 设备id
     * @return
     */
    public List<MonitorUpsInfoDto> getCurrentUps(String hospitalCode, String equipmentTypeId) {
        List<SnDeviceDto> equipmentList = monitorEquipmentApi.getEquipmentNoList(hospitalCode, equipmentTypeId).getResult();
        if (CollectionUtils.isEmpty(equipmentList)) {
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
        if (MapUtils.isEmpty(probeInfoMap)) {
            return null;
        }
        HospitalInfoDto hos = hospitalInfoRepository.getOne(Wrappers.lambdaQuery(new HospitalInfoDto()).eq(HospitalInfoDto::getHospitalCode, hospitalCode));
        String timeoutRedDuration = hos.getTimeoutRedDuration();
        if (StringUtils.isEmpty(timeoutRedDuration)){
            timeoutRedDuration="30";
        }
        List<MonitorUpsInfoDto> list = new ArrayList<>();
        String finalTimeoutRedDuration = timeoutRedDuration;
        equipmentNoList.forEach(res -> {
            MonitorUpsInfoDto monitorUpsInfoDto = buildMonitorUpsInfoDto(res, equipmentNoMap, probeInfoMap, finalTimeoutRedDuration);
            list.add(monitorUpsInfoDto);
        });
        return list;
    }

    private MonitorUpsInfoDto buildMonitorUpsInfoDto(String eNo, Map<String, List<SnDeviceDto>> equipmentNoMap, Map<String, List<ProbeInfoDto>> probeInfoMap,String timeoutRedDuration) {
        SnDeviceDto snDeviceDto = equipmentNoMap.get(eNo).get(0);
        MonitorUpsInfoDto monitorUpsInfoDto = new MonitorUpsInfoDto()
                .setEquipmentName(snDeviceDto.getEquipmentName())
                .setEquipmentNo(snDeviceDto.getEquipmentNo())
                .setSn(snDeviceDto.getSn());
        List<ProbeInfoDto> list = probeInfoMap.get(eNo);
        //没有探头新增则代表市电异常
        if (CollectionUtils.isNotEmpty(list)) {
            //判断市电数据是否超时,超时也属于异常
            List<Date> collect = list.stream().map(ProbeInfoDto::getInputTime).collect(Collectors.toList());
            Date maxDate = Collections.max(collect);
            boolean flag = DateUtils.calculateIntervalTime(maxDate, timeoutRedDuration);
            if (flag){
                monitorUpsInfoDto.setCurrentUps("1");
            }else {
                List<ProbeInfoDto> currentUps = list.stream().filter(res -> res.getProbeEName().equals("currentups")).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(currentUps)) {
                    monitorUpsInfoDto.setCurrentUps(currentUps.get(0).getValue());
                }
            }
            List<ProbeInfoDto> voltage = list.stream().filter(res -> res.getProbeEName().equals("voltage")).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(voltage)) {
                monitorUpsInfoDto.setVoltage(voltage.get(0).getValue());
            }
        } else {
            monitorUpsInfoDto.setCurrentUps("1");
        }

        return monitorUpsInfoDto;
    }


    /**
     * 构建探头高低值
     *
     * @param equipmentno              设备no
     * @param probeInfoDtoList         探头信息
     * @param instrumentParamConfigMap 探头参数map
     */
    private void buildProbeHighAndLowValue(String equipmentno, List<ProbeInfoDto> probeInfoDtoList, Map<String, Map<String, List<InstrumentParamConfigDto>>> instrumentParamConfigMap,String timeoutRedDuration) {
        if (MapUtils.isEmpty(instrumentParamConfigMap)) {
            return;
        }
        List<ProbeInfoDto> remove = new ArrayList<>();
        for (ProbeInfoDto probeInfoDto : probeInfoDtoList) {
            Integer instrumentConfigId = probeInfoDto.getInstrumentConfigId();
            String instrumentNo = probeInfoDto.getInstrumentNo();
            switch (instrumentConfigId) {
                case 7:
                    setQcProbe(equipmentno,instrumentNo , instrumentConfigId, instrumentParamConfigMap, probeInfoDto, remove, timeoutRedDuration);
                    break;
                //MT310DC
                case 101:
                case 102:
                case 103:
                    String probeEName = probeInfoDto.getProbeEName();
                    instrumentConfigId = getInstrumentConfigId(probeEName, instrumentConfigId);
                    setProbeHeightAndLowValue(equipmentno, instrumentNo, instrumentConfigId, instrumentParamConfigMap, probeInfoDto, remove, timeoutRedDuration);
                    break;
                //其他设备
                default:
                    setProbeHeightAndLowValue(equipmentno,instrumentNo , instrumentConfigId, instrumentParamConfigMap, probeInfoDto, remove, timeoutRedDuration);
                    break;
            }
        }
        if (CollectionUtils.isNotEmpty(remove)) {
            probeInfoDtoList.removeAll(remove);
        }
    }

    private void setQcProbe(String equipmentno, String instrumentNo, Integer instrumentConfigId, Map<String, Map<String, List<InstrumentParamConfigDto>>> instrumentParamConfigMap, ProbeInfoDto probeInfoDto, List<ProbeInfoDto> remove, String timeoutRedDuration) {
        String str = instrumentNo + ":"+instrumentConfigId;
        String str2 = instrumentNo + ":"+"35";
        Map<String, List<InstrumentParamConfigDto>> stringListMap = instrumentParamConfigMap.get(equipmentno);
        if(!instrumentParamConfigMap.containsKey(equipmentno)){
            remove.add(probeInfoDto);
        }
        if(!stringListMap.containsKey(str) && !stringListMap.containsKey(str2)){
            remove.add(probeInfoDto);
        }
        List<InstrumentParamConfigDto> list = stringListMap.containsKey(str) ? stringListMap.get(str) : stringListMap.get(str2);
        if (CollectionUtils.isNotEmpty(list)) {
            InstrumentParamConfigDto instrumentParamConfigDto = list.get(0);
            String state = instrumentParamConfigDto.getState();
            BigDecimal lowLimit = instrumentParamConfigDto.getLowLimit();
            probeInfoDto.setSaturation(instrumentParamConfigDto.getSaturation());
            probeInfoDto.setLowLimit(instrumentParamConfigDto.getLowLimit());
            probeInfoDto.setState(StringUtils.isBlank(state)?"":state);
            probeInfoDto.setInstrumentNo(instrumentNo);
            if (instrumentConfigId == 11) {
                setState(probeInfoDto, lowLimit, state);
            }
            //设置值和单位
            probeInfoDto.setHighLimit(instrumentParamConfigDto.getHighLimit());
            if (StringUtils.isNotBlank(instrumentParamConfigDto.getUnit()) && RegularUtil.checkContainsNumbers(probeInfoDto.getValue())) {
                String unit = instrumentParamConfigDto.getUnit();
                probeInfoDto.setUnit(unit);
                probeInfoDto.setValue(probeInfoDto.getValue());
            }else{
                probeInfoDto.setUnit("");
            }
            //设置状态
            boolean flag = DateUtils.calculateIntervalTime(probeInfoDto.getInputTime(), timeoutRedDuration);
            if(flag){
                probeInfoDto.setState("2");
            }
        }
    }


    /**
     * 设置探头高低值
     *
     * @param equipmentno              设备no
     * @param instrumentConfigId       检测类型id
     * @param instrumentParamConfigMap 探头参数map
     */
    private void setProbeHeightAndLowValue(String equipmentno,String instrumentNo, Integer instrumentConfigId, Map<String, Map<String, List<InstrumentParamConfigDto>>> instrumentParamConfigMap, ProbeInfoDto probeInfoDto, List<ProbeInfoDto> removeList,String timeoutRedDuration) {
        String str = instrumentNo + ":"+instrumentConfigId;
        if (!instrumentParamConfigMap.containsKey(equipmentno) || !instrumentParamConfigMap.get(equipmentno).containsKey(str)) {
            removeList.add(probeInfoDto);
            return;
        }
        Map<String, List<InstrumentParamConfigDto>> stringListMap = instrumentParamConfigMap.get(equipmentno);
        List<InstrumentParamConfigDto> list = stringListMap.get(str);
        instrumentParamConfigMap.get(equipmentno);
        if (CollectionUtils.isNotEmpty(list)) {
            InstrumentParamConfigDto instrumentParamConfigDto = list.get(0);
            String state = instrumentParamConfigDto.getState();
            BigDecimal lowLimit = instrumentParamConfigDto.getLowLimit();
            probeInfoDto.setSaturation(instrumentParamConfigDto.getSaturation());
            probeInfoDto.setLowLimit(instrumentParamConfigDto.getLowLimit());
            probeInfoDto.setState(StringUtils.isBlank(state)?"":state);
            probeInfoDto.setInstrumentNo(instrumentNo);
            if (instrumentConfigId == 11) {
                setState(probeInfoDto, lowLimit, state);
            }
            //设置值和单位
            probeInfoDto.setHighLimit(instrumentParamConfigDto.getHighLimit());
            if (StringUtils.isNotBlank(instrumentParamConfigDto.getUnit()) && RegularUtil.checkContainsNumbers(probeInfoDto.getValue())) {
                String unit = instrumentParamConfigDto.getUnit();
                probeInfoDto.setUnit(unit);
                probeInfoDto.setValue(probeInfoDto.getValue());
            }else{
                probeInfoDto.setUnit("");
            }
            //设置状态
            boolean flag = DateUtils.calculateIntervalTime(probeInfoDto.getInputTime(), timeoutRedDuration);
            if(flag){
                probeInfoDto.setState("2");
            }
        }
    }

    private void setState(ProbeInfoDto probeInfoDto, BigDecimal lowLimit, String state) {
        if (StringUtils.isBlank(state)) {
            probeInfoDto.setState("0");
        } else {
            int i = lowLimit.intValue();
            Integer integer = Integer.valueOf(probeInfoDto.getValue());
            if (i == integer) {
                probeInfoDto.setState("1");
            } else {
                probeInfoDto.setState("0");
            }
        }

    }

    /**
     * MT310根据ename返回检测的id
     *
     * @param probeEName         探头的英文名称
     * @param instrumentConfigId 探头检测id
     */
    private Integer getInstrumentConfigId(String probeEName, int instrumentConfigId) {
        switch (probeEName) {
            //温度
            case "1":
                instrumentConfigId = CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId();
                break;
            //湿度
            case "2":
                instrumentConfigId = CurrentProbeInfoEnum.CURRENTHUMIDITY.getInstrumentConfigId();
                break;
            //O2浓度
            case "3":
                instrumentConfigId = CurrentProbeInfoEnum.CURRENTO2.getInstrumentConfigId();
                break;
            //CO2浓度
            case "4":
                instrumentConfigId = CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getInstrumentConfigId();
                break;
            default:
                break;
        }
        return instrumentConfigId;
    }

    /**
     * 获取设备运行时间
     *
     * @param equipmentNo
     * @return
     */
    public DateDto getEquipmentRunningTime(String equipmentNo) {
        MonitorEquipmentDto equipmentInfoByNo = equipmentInfoService.getEquipmentInfoByNo(equipmentNo);
        if (ObjectUtils.isEmpty(equipmentInfoByNo)) {
            throw new IedsException(LabSystemEnum.EQUIPMENT_INFO_NOT_FOUND);
        }
        Date createTime = equipmentInfoByNo.getCreateTime();
        if (createTime == null) {
            return new DateDto();
        }
        Date date = new Date();
        return DateUtils.convert(createTime, date);
    }

    /**
     * 获取曲线接口
     *
     * @return
     */
    public List<Map<String, CurveDataModel>> getCurveFirst(CurveCommand curveCommand) {
        List<String> instrumentConfigIdList = curveCommand.getInstrumentConfigIdList();
        if(CollectionUtils.isEmpty(instrumentConfigIdList)){
            return new ArrayList<>();
        }
        String startTime = curveCommand.getStartTime();
        String endTime = curveCommand.getEndTime();
        String sn = curveCommand.getSn();
        String equipmentNo = curveCommand.getEquipmentNo();
        String ym = DateUtils.getYearMonth(startTime, endTime);
        CurveParam curveParam = BeanConverter.convert(curveCommand, CurveParam.class);
        curveParam.setYearMonth(ym);
        List<Monitorequipmentlastdata> lastDataModelList = monitorequipmentlastdataRepository.getMonitorEquuipmentLastList(curveParam);

        if (org.apache.commons.collections.CollectionUtils.isEmpty(lastDataModelList)) {
            throw new IedsException(LabSystemEnum.NO_DATA_FOR_CURRENT_TIME);
        }
        Map<String, List<InstrumentParamConfigDto>> map = instrumentParamConfigService.getInstrumentParamConfigByENo(equipmentNo);
        boolean flag = false;
        if (StringUtils.isNotEmpty(sn) && ProbeOutlierMt310.THREE_ONE.getCode().equals(sn.substring(4, 6))) {
            flag = true;
        }
        return CurveUtils.getCurveFirst(lastDataModelList, curveCommand.getInstrumentConfigIdList(), map);
    }

    /**
     * 获取实施人员信息
     *
     * @param hospitalCode
     */
    public List<UserRightDto> getImplementerInformation(String hospitalCode) {
        List<UserRightDto> list = userRightService.getImplementerInformation(hospitalCode);
        list.forEach(res -> {
            if (StringUtils.isBlank(res.getReminders())) {
                res.setReminders("");
            }
        });
        return list;
    }

    /**
     * 获取设备报警未读数量
     *
     * @param equipmentNo
     * @return
     */
    public List<Warningrecord> getNumUnreadDeviceAlarms(String equipmentNo) {
        List<Warningrecord> warningRecordInfo = warningrecordRepository.getWarningRecordInfo(equipmentNo);
        return warningRecordInfo.stream().filter(res -> !StringUtils.equals("1", res.getMsgflag())).collect(Collectors.toList());
    }

    public List<WarningRecordInfo> getWarningInfoList(WarningCommand warningCommand) {
        List<Warningrecord> warningRecord = warningrecordRepository.getWarningInfoList(warningCommand.getHospitalCode(), warningCommand.getStartTime());
        if (CollectionUtils.isEmpty(warningRecord)) {
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
            if (enoAndWrMap.containsKey(eno)) {
                Warningrecord warningrecord = enoAndWrMap.get(eno).get(0);
                String instrumentParamConfigNo = warningrecord.getInstrumentparamconfigno();
                //设置探头信息
                if (ipcNoAndProbeMap.containsKey(instrumentParamConfigNo)) {
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
            if (enoAndEInfoMap.containsKey(eno)) {
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
     *
     * @param warningCommand
     * @return
     */
    public Page getWarningDetailInfo(WarningCommand warningCommand) {
        String equipmentNo = warningCommand.getEquipmentNo();
        String startTime = warningCommand.getStartTime();
        String endTime = warningCommand.getEndTime();
        Page page = new Page(warningCommand.getPageCurrent(), warningCommand.getPageSize());
        List<Warningrecord> warningRecordList = warningrecordRepository.getWarningRecordDetailInfo(page, equipmentNo, startTime, endTime);
        if (CollectionUtils.isEmpty(warningRecordList)) {
            return null;
        }
        String hospitalCode = warningRecordList.get(0).getHospitalcode();
        //获取医院所有的人员信息
        List<UserRightDto> userRightDtoList = userRightService.getallByHospitalCode(hospitalCode);
        Map<String, List<UserRightDto>> userMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userRightDtoList)) {
            userMap = userRightDtoList.stream().filter(res->StringUtils.isNotBlank(res.getPhoneNum())).collect(Collectors.groupingBy(UserRightDto::getPhoneNum));
        }
        List<String> collect = warningRecordList.stream().map(Warningrecord::getInstrumentparamconfigno).collect(Collectors.toList());
        List<WarningDetailInfo> detailInfos = BeanConverter.convert(warningRecordList, WarningDetailInfo.class);
        List<InstrumentParamConfigDto> probeInfoList = instrumentParamConfigService.batchGetProbeInfo(collect);
        Map<String, List<InstrumentParamConfigDto>> stringListMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(probeInfoList)) {
            stringListMap = probeInfoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentparamconfigno));
        }
        for (WarningDetailInfo detailInfo : detailInfos) {
            //将手机号换成用户名
            String mailCallUser = detailInfo.getMailCallUser();
            if (StringUtils.isNotBlank(mailCallUser)) {
                List<String> list = Arrays.asList(mailCallUser.split("/"));
                String str = listToStr(list, userMap);
                detailInfo.setMailCallUser(str);
            }
            String phoneCallUser = detailInfo.getPhoneCallUser();
            if (StringUtils.isNotBlank(phoneCallUser)) {
                List<String> list = Arrays.asList(phoneCallUser.split("/"));
                String str = listToStr(list, userMap);
                detailInfo.setPhoneCallUser(str);
            }
            List<String> list1 = getNoticeList(mailCallUser, phoneCallUser, userMap);
            detailInfo.setInfoNoticeList(list1);

            String instrumentparamconfigno = detailInfo.getInstrumentparamconfigno();
            if (!stringListMap.containsKey(instrumentparamconfigno)) {
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
     *
     * @param mailCallUser
     * @param phoneCallUser
     * @return
     */
    private List<String> getNoticeList(String mailCallUser, String phoneCallUser, Map<String, List<UserRightDto>> userMap) {
        List<String> list = new ArrayList<>();
        //1.都为空时
        if (StringUtils.isBlank(mailCallUser) && StringUtils.isBlank(phoneCallUser)) {
            return null;
        }
        //2.都不为空时
        if (!StringUtils.isBlank(mailCallUser) && !StringUtils.isBlank(phoneCallUser)) {
            List<String> mailList = Arrays.asList(mailCallUser.split("/"));
            List<String> phoneList = Arrays.asList(phoneCallUser.split("/"));
            List<String> sameList = mailList.stream().filter(phoneList::contains).collect(Collectors.toList());
            sameList.forEach(res -> {
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
            List<String> phoneList = Arrays.asList(phoneCallUser.split("/"));
            phoneList.forEach(res -> list.add(res + SysConstants.PHONE_NOTIFICATIONS));
        } else {
            List<String> mailList = Arrays.asList(mailCallUser.split("/"));
            mailList.forEach(res -> list.add(res + SysConstants.SMS_NOTIFICATIONS));
        }
        return list;
    }

    private String listToStr(List<String> list, Map<String, List<UserRightDto>> userMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String phoneNum : list) {
            if (userMap.containsKey(phoneNum)) {
                String username = userMap.get(phoneNum).get(0).getUsername();
                stringBuilder.append(username).append("/");
            } else {
                stringBuilder.append(phoneNum).append("/");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public Page getAlarmSystemInfo(ProbeCommand probeCommand) {
        Page page = new Page(probeCommand.getPageCurrent(), probeCommand.getPageSize());
        List<MonitorEquipmentDto> equipmentInfoByPage = equipmentInfoService.getEquipmentInfoByPage(page, probeCommand);
        if (CollectionUtils.isEmpty(equipmentInfoByPage)) {
            return null;
        }
        List<String> enoList = equipmentInfoByPage.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
        //查询sn表
        List<MonitorinstrumentDto> monitorinstrumentDtos = monitorInstrumentService.selectMonitorInstrumentByEnoList(enoList);
        Map<String, List<MonitorinstrumentDto>> enoInoMap = monitorinstrumentDtos.stream().collect(Collectors.groupingBy(MonitorinstrumentDto::getEquipmentno));

        List<InstrumentParamConfigDto> instrumentParamConfigByENoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(enoList);
        Map<String, List<InstrumentParamConfigDto>> eNoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(instrumentParamConfigByENoList)) {
            eNoMap = instrumentParamConfigByENoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno));
        }
        List<InstrumentMonitorInfoDto> instrumentMonitorInfoDtoList = instrumentMonitorInfoRepository.list();
        Map<String, List<InstrumentMonitorInfoDto>> idMap =
                instrumentMonitorInfoDtoList.stream().collect(Collectors.groupingBy(res -> "" + res.getInstrumenttypeid() + res.getInstrumentconfigid()));
        List<AlarmSystem> list = new ArrayList<>();
        for (MonitorEquipmentDto next : equipmentInfoByPage) {
            AlarmSystem alarmSystem = new AlarmSystem();
            alarmSystem.setEquipmentName(next.getEquipmentname());
            if(enoInoMap.containsKey(next.getEquipmentno())){
                MonitorinstrumentDto monitorinstrumentDto = enoInoMap.get(next.getEquipmentno()).get(0);
                alarmSystem.setSn(monitorinstrumentDto.getSn());
            }
            alarmSystem.setEquipmentNo(next.getEquipmentno());
            alarmSystem.setHospitalCode(next.getHospitalcode());
            if (StringUtils.isBlank(next.getWarningSwitch())) {
                next.setWarningSwitch("1");
            }
            if (eNoMap.containsKey(next.getEquipmentno())) {
                List<InstrumentParamConfigDto> paramConfigDtoList = eNoMap.get(next.getEquipmentno());
                long count = paramConfigDtoList.stream().filter(res -> "1".equals(res.getWarningphone())).count();
                if (count > 0) {
                    alarmSystem.setWarningSwitch("1");
                } else {
                    alarmSystem.setWarningSwitch("0");
                }
                List<ProbeAlarmState> list1 = new ArrayList<>();
                for (InstrumentParamConfigDto instrumentParamConfigDto : paramConfigDtoList) {
                    ProbeAlarmState probeAlarmState = new ProbeAlarmState();
                    probeAlarmState.setInstrumentParamConfigNo(instrumentParamConfigDto.getInstrumentparamconfigno());
                    probeAlarmState.setWarningPhone(instrumentParamConfigDto.getWarningphone());
                    probeAlarmState.setInstrumentConfigId(instrumentParamConfigDto.getInstrumentconfigid());
                    probeAlarmState.setInstrumentNo(instrumentParamConfigDto.getInstrumentno());
                    probeAlarmState.setEName(CurrentProbeInfoEnum.from(instrumentParamConfigDto.getInstrumentconfigid()).getProbeEName());
                    probeAlarmState.setLowLimit(instrumentParamConfigDto.getLowLimit().toString());
                    probeAlarmState.setHighLimit(instrumentParamConfigDto.getHighLimit().toString());
                    //设置默认的大小值
                    Integer instrumentTypeId = instrumentParamConfigDto.getInstrumenttypeid();
                    Integer instrumentConfigId = instrumentParamConfigDto.getInstrumentconfigid();
                    if (idMap.containsKey("" + instrumentTypeId + instrumentConfigId)) {
                        InstrumentMonitorInfoDto instrumentMonitorInfoDto = idMap.get("" + instrumentTypeId + instrumentConfigId).get(0);
                        probeAlarmState.setMinReferenceValue(instrumentMonitorInfoDto.getLowlimit());
                        probeAlarmState.setMaxReferenceValue(instrumentMonitorInfoDto.getHighlimit());
                    }
                    list1.add(probeAlarmState);
                }
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
            if (map.containsKey(instrumentNo)) {
                List<InstrumentParamConfigDto> paramConfigDtoList = map.get(instrumentNo);
                long count = paramConfigDtoList.stream().filter(res -> SysConstants.IN_ALARM.equals(res.getWarningphone())).count();
                long stateNum = paramConfigDtoList.stream().filter(res -> SysConstants.IN_ALARM.equals(res.getState())).count();
                if (count > 0) {
                    monitorEquipmentDto.setWarningSwitch(SysConstants.IN_ALARM);
                } else {
                    monitorEquipmentDto.setWarningSwitch(SysConstants.NORMAL);
                }
                if (stateNum > 0) {
                    monitorEquipmentDto.setState(SysConstants.IN_ALARM);
                } else {
                    monitorEquipmentDto.setState(SysConstants.NORMAL);
                }
            }
        }
        //更新数据库
        equipmentInfoService.bulkUpdate(list);
    }


    /**
     * 获取报警设置设备的数量
     *
     * @param alarmSystemCommand
     * @return
     */
    public AlarmHand getTheNumberOfAlarmSettingDevices(AlarmSystemCommand alarmSystemCommand) {
        String hospitalCode = alarmSystemCommand.getHospitalCode();
        String equipmentTypeId = alarmSystemCommand.getEquipmentTypeId();
        //查询设备id
        List<String> enoList = equipmentInfoService.getEnoList(hospitalCode, equipmentTypeId);
        if(CollectionUtils.isEmpty(enoList)){
            return null;
        }
        //获取探头sn信息
        List<MonitorinstrumentDto> monitorinstrumentDtos = monitorInstrumentService.selectMonitorInstrumentByEnoList(enoList);
        if(CollectionUtils.isEmpty(monitorinstrumentDtos)){
            return null;
        }
        List<String> iNos = monitorinstrumentDtos.stream().map(MonitorinstrumentDto::getInstrumentno).collect(Collectors.toList());
        Map<String, List<MonitorinstrumentDto>> enoMap = monitorinstrumentDtos.stream().collect(Collectors.groupingBy(MonitorinstrumentDto::getEquipmentno));
        //获取探头信息
        List<InstrumentParamConfigDto> probeInfos = instrumentParamConfigService.getInstrumentParamConfigByINo(iNos);
        if(CollectionUtils.isEmpty(probeInfos)){
            return  null;
        }
        Map<String, List<InstrumentParamConfigDto>> inoMap = probeInfos.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentno));
        //遍历设备信息
        int enableNum = 0;
        int disabledNum = 0;
        for (String eno : enoList) {
            if(enoMap.containsKey(eno)){
                boolean flag =false;
                //有线设备一个eno对应多个ino
                List<String> inoList = enoMap.get(eno).stream().map(MonitorinstrumentDto::getInstrumentno).collect(Collectors.toList());
                for (String ino : inoList) {
                    if(inoMap.containsKey(ino)){
                        List<InstrumentParamConfigDto> instrumentParamConfigDtoList = inoMap.get(ino);
                        long count = instrumentParamConfigDtoList.stream().filter(res -> "1".equals(res.getWarningphone())).count();
                        if(count>0){
                            flag = true;
                        }
                    }
                }
                if(flag){
                    enableNum++;
                }else{
                    disabledNum++;
                }
            }
        }
        AlarmHand alarmHand = new AlarmHand();
        alarmHand.setDisabledNum(disabledNum);
        alarmHand.setEnableNum(enableNum);
        return alarmHand;
    }
}

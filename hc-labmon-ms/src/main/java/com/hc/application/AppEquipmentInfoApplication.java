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
import com.hc.hospital.HospitalEquipmentTypeIdApi;
import com.hc.labmanagent.MonitorEquipmentApi;
import com.hc.my.common.core.constant.enums.*;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.redis.command.ProbeRedisCommand;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoDto;
import com.hc.my.common.core.redis.dto.MonitorEquipmentWarningTimeDto;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.my.common.core.util.Mt310DCUtils;
import com.hc.my.common.core.util.RegularUtil;
import com.hc.my.common.core.util.date.DateDto;
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
import java.math.RoundingMode;
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

    @Autowired
    private HospitalEquipmentTypeIdApi hospitalEquipmentTypeIdApi;

    public List<HospitalEquipmentDto> getEquipmentNum(String hospitalCode, String tags){
        ProbeCommand probeCommand = new ProbeCommand();
        probeCommand.setHospitalCode(hospitalCode);
        Page<ProbeCurrentInfoDto> page = new Page<>(1, 10000);
        //分页查询设备信息 获取设备 未禁用的设备
        List<MonitorEquipmentDto> list = equipmentInfoService.getEquipmentInfoByPage(page,probeCommand);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        //在查出monitorinstrument信息
        List<String> enoList = list.stream().map(MonitorEquipmentDto::getEquipmentno).distinct().collect(Collectors.toList());
        //获取设备对象的探头信息
        ProbeRedisCommand probeRedisCommand = new ProbeRedisCommand();
        probeRedisCommand.setHospitalCode(probeCommand.getHospitalCode());
        probeRedisCommand.setENoList(enoList);
        List<InstrumentParamConfigDto> instrumentParamConfigByENoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(enoList);
        //以设备no分组
        Map<String, List<InstrumentParamConfigDto>> enoAndProbeMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(instrumentParamConfigByENoList)){
            enoAndProbeMap =  instrumentParamConfigByENoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno));
        }
        //批量获取设备对应探头当前值信息
        Map<String, List<ProbeInfoDto>> probeInfoMap = probeRedisApi.getTheCurrentValueOfTheProbeInBatches(probeRedisCommand).getResult();
        //获取医院超时变红间隔
        HospitalInfoDto hos = hospitalInfoRepository.getOne(Wrappers.lambdaQuery(new HospitalInfoDto()).eq(HospitalInfoDto::getHospitalCode, probeCommand.getHospitalCode()));
        String timeoutRedDuration = hos.getTimeoutRedDuration();
        if (StringUtils.isEmpty(timeoutRedDuration)){
            timeoutRedDuration = SysConstants.TIMEOUT_RED_DURATION;
        }

        List<HospitalEquipmentDto> hospitalEquipmentDto = new ArrayList<>();
        if (StringUtils.equals(tags, "PC")) {
            hospitalEquipmentDto = hospitalEquipmentService.selectHospitalEquipmentInfoByPc(hospitalCode);
        } else {
            hospitalEquipmentDto = hospitalEquipmentService.selectHospitalEquipmentInfo(hospitalCode);
        }
        Map<String, List<MonitorEquipmentDto>> allEq = list.stream().filter(res->StringUtils.isNotBlank(res.getEquipmenttypeid())).collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmenttypeid));
        List<HospitalEquipmentDto> reList = new ArrayList<>();
        for (HospitalEquipmentDto value : hospitalEquipmentDto) {
            String eqTypeId = value.getEquipmentTypeId();
            List<MonitorEquipmentDto> eq = allEq.get(eqTypeId);
            HospitalEquipmentDto hospitalEquipmentDto1 = new HospitalEquipmentDto();
            int totalNum = 0;
            int normalCount = 0;
            int abnormalCount = 0;
            int timeoutCount = 0;
            if(CollectionUtils.isNotEmpty(eq)){
               totalNum = eq.size();
               //遍历设备设备
                for (MonitorEquipmentDto monitorEquipmentDto : eq) {
                    String equipmentNo = monitorEquipmentDto.getEquipmentno();
                    ProbeCurrentInfoDto probeInfo = new ProbeCurrentInfoDto();
                    //获取数据库探头信息
                    List<InstrumentParamConfigDto> instrumentParamConfigs = enoAndProbeMap.get(equipmentNo);
                    if(CollectionUtils.isEmpty(instrumentParamConfigs) || null == probeInfoMap.get(equipmentNo)){
                        abnormalCount++;
                    }else {
                        List<ProbeInfoDto> probeInfoList = probeInfoMap.get(equipmentNo);
                        Map<String, List<ProbeInfoDto>> inoConfigMap =
                                probeInfoList.stream().collect(Collectors.groupingBy(res -> res.getInstrumentNo() + ":" + res.getInstrumentConfigId()));
                        List<ProbeInfoDto> probeInfos = new ArrayList<>();
                        Date maxDate ;
                        //遍历数据库探头信息
                        for (InstrumentParamConfigDto configDto : enoAndProbeMap.get(equipmentNo)) {
                            ProbeInfoDto probeInfoDto = new ProbeInfoDto();
                            String instrumentNo = configDto.getInstrumentno();
                            Integer configId = configDto.getInstrumentconfigid();
                            //解决旧数据door2
                            if(StringUtils.equals(SysConstants.CHANNEL_2,configDto.getIChannel()) && configId ==CurrentProbeInfoEnum.CURRENTDOORSTATE.getInstrumentConfigId()){
                                configId = CurrentProbeInfoEnum.CURRENTDOORSTATE2.getInstrumentConfigId();
                            }
                            String inoConfigId =  instrumentNo+":"+configId;
                            if(null == inoConfigMap.get(inoConfigId)){
                                continue;
                            }
                            ProbeInfoDto probeRedis;
                            //解决qc与qcl数据库相同config相同，而缓存不同
                            if(configId != CurrentProbeInfoEnum.CURRENTQC.getInstrumentConfigId()){
                                probeRedis = inoConfigMap.get(inoConfigId).get(0);
                            }else{
                                String inoConfigId2 =  instrumentNo+":"+CurrentProbeInfoEnum.QCL.getInstrumentConfigId();
                                probeRedis = inoConfigMap.containsKey(inoConfigId) ? inoConfigMap.get(inoConfigId).get(0) : inoConfigMap.get(inoConfigId2).get(0);
                            }
                            probeInfoDto.setInputTime(probeRedis.getInputTime());
                            probeInfoDto.setValue(changeString(probeRedis.getValue(),Context.IsCh()));
                            //缓存中没有找到探头状态时默认为正常
                            probeInfoDto.setState(StringUtils.isBlank(probeRedis.getState())?SysConstants.EQ_NORMAL:probeRedis.getState());
                            //当config为11和44时，通过缓存的当前值和最低值比较来计算探头的状态
                            if(configId == CurrentProbeInfoEnum.CURRENTDOORSTATE.getInstrumentConfigId() || configId == CurrentProbeInfoEnum.CURRENTDOORSTATE2.getInstrumentConfigId()){
                                int i = configDto.getLowLimit().intValue();
                                int integer = Integer.parseInt(probeInfoDto.getValue());
                                if (i == integer) {
                                    probeInfoDto.setState(SysConstants.EQ_ABNORMAL);
                                } else {
                                    probeInfoDto.setState(SysConstants.EQ_NORMAL);
                                }
                            }
                            probeInfoDto.setInstrumentConfigId(configId);
                            probeInfos.add(probeInfoDto);
                        }
                        if(CollectionUtils.isNotEmpty(probeInfos)){
                            probeInfo.setProbeInfoDtoList(probeInfos);
                            List<Date> collect = probeInfos.stream().map(ProbeInfoDto::getInputTime).collect(Collectors.toList());
                            //获取最新的时间
                            maxDate = Collections.max(collect);
                            if (maxDate != null) {
                                probeInfo.setInputTime(maxDate);
                                boolean b = DateUtils.calculateIntervalTime(maxDate, timeoutRedDuration);
                                if(b){
                                    timeoutCount++;
                                }
                            }
                            //设备没有超时时，通过探头状态来获取设备状态(有一个探头异常，设备就是异常的，所有探头正常，设备才正常)
                            if(StringUtils.isBlank(probeInfo.getState())){
                                long abnormal = probeInfos.stream().filter(res -> StringUtils.equals(res.getState(),SysConstants.EQ_ABNORMAL)).count();
                                if(abnormal>0){
                                    abnormalCount++;
                                }else {
                                    normalCount++;
                                }
                            }
                        }else {
                            //没有探头信息时判断设备时异常设备
                            abnormalCount++;
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
     * @return
     */
    public CurrentProbeInfoResult getProbeCurrentV(ProbeCommand probeCommand){
        String state = probeCommand.getState();
        CurrentProbeInfoResult currentProbeInfoResult = new CurrentProbeInfoResult();
        //分页查询设备信息 获取设备 未禁用的设备
        List<MonitorEquipmentDto> list = equipmentInfoService.getEquipmentInfo(probeCommand);
        if (CollectionUtils.isEmpty(list)) {
            return currentProbeInfoResult;
        }
        //在查出monitorinstrument信息
        List<String> enoList = list.stream().map(MonitorEquipmentDto::getEquipmentno).distinct().collect(Collectors.toList());
        List<MonitorinstrumentDto> monitorInstrumentDTOList = monitorInstrumentService.selectMonitorInstrumentByEnoList(enoList);
        if (CollectionUtils.isEmpty(monitorInstrumentDTOList)) {
            return currentProbeInfoResult;
        }
        Map<String, List<MonitorinstrumentDto>> enoAndMiMap = monitorInstrumentDTOList.stream().collect(Collectors.groupingBy(MonitorinstrumentDto::getEquipmentno));
        //获取设备对象的探头信息
        ProbeRedisCommand probeRedisCommand = new ProbeRedisCommand();
        probeRedisCommand.setHospitalCode(probeCommand.getHospitalCode());
        probeRedisCommand.setENoList(enoList);
        List<InstrumentParamConfigDto> instrumentParamConfigByENoList = instrumentParamConfigService.getInstrumentParamConfigByENoList(enoList);
        //以设备no分组
        Map<String, List<InstrumentParamConfigDto>> enoAndProbeMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(instrumentParamConfigByENoList)){
            enoAndProbeMap =  instrumentParamConfigByENoList.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getEquipmentno));
        }

        //批量获取设备对应探头当前值信息
        Map<String, List<ProbeInfoDto>> probeInfoMap = probeRedisApi.getTheCurrentValueOfTheProbeInBatches(probeRedisCommand).getResult();

        //获取医院超时变红间隔
        HospitalInfoDto hos = hospitalInfoRepository.getOne(Wrappers.lambdaQuery(new HospitalInfoDto()).eq(HospitalInfoDto::getHospitalCode, probeCommand.getHospitalCode()));
        String timeoutRedDuration = hos.getTimeoutRedDuration();
        if (StringUtils.isEmpty(timeoutRedDuration)){
            timeoutRedDuration = SysConstants.TIMEOUT_RED_DURATION;
        }

        //获取用户信息 判断是否是实施人员
        UserRightDto userRightDto = userRightService.getUserRightInfoByUserId(Context.getUserId());
        boolean isRole = null != userRightDto && (SysConstants.IMPLEMENTERS.equals(userRightDto.getRole()));

        List<ProbeCurrentInfoDto> normalList = new ArrayList<>();
        List<ProbeCurrentInfoDto> abnormalList = new ArrayList<>();
        List<ProbeCurrentInfoDto> timeoutList = new ArrayList<>();
        List<ProbeCurrentInfoDto> totalList = new ArrayList<>();
        //遍历设备信息构建卡片对象
        for (MonitorEquipmentDto monitorEquipmentDto : list) {
            //设置设备信息
            String equipmentNo = monitorEquipmentDto.getEquipmentno();
            ProbeCurrentInfoDto probeInfo = new ProbeCurrentInfoDto();
            if(enoAndMiMap.containsKey(equipmentNo)){
                MonitorinstrumentDto monitorinstrumentDto = enoAndMiMap.get(equipmentNo).get(0);
                probeInfo.setInstrumentTypeId(String.valueOf(monitorinstrumentDto.getInstrumenttypeid()));
                probeInfo.setSn(monitorinstrumentDto.getSn());
            }
            probeInfo.setEquipmentNo(equipmentNo);
            probeInfo.setEquipmentName(monitorEquipmentDto.getEquipmentname());
            probeInfo.setEquipmentTypeId(probeCommand.getEquipmentTypeId());
            //判断是否需要展示产品型号公司
            if(isRole){
                probeInfo.setCompany(monitorEquipmentDto.getCompany());
                probeInfo.setBrand(monitorEquipmentDto.getBrand());
                probeInfo.setModel(monitorEquipmentDto.getModel());
            }
            //获取数据库探头信息
            List<InstrumentParamConfigDto> instrumentParamConfigs = enoAndProbeMap.get(equipmentNo);
            if(CollectionUtils.isEmpty(instrumentParamConfigs) || null == probeInfoMap.get(equipmentNo)){
                probeInfo.setState(SysConstants.EQ_ABNORMAL);
                abnormalList.add(probeInfo);
                totalList.add(probeInfo);
                probeInfo.setInstrumentConfigIdList(new ArrayList<>());
                probeInfo.setProbeInfoDtoList(new ArrayList<>());
            }
            else
            {
                //设置探头信息
                List<ProbeInfoDto> probeInfoList = probeInfoMap.get(equipmentNo);
                //mt310修改configid和eName
                if(StringUtils.equals(probeInfo.getInstrumentTypeId(),SysConstants.EQ_MT310DC)){
                    for (ProbeInfoDto probeInfoDto : probeInfoList) {
                        Integer instrumentConfigId = probeInfoDto.getInstrumentConfigId();
                        String probeEName = probeInfoDto.getProbeEName();
                        instrumentConfigId = getInstrumentConfigId(probeEName, instrumentConfigId);
                        setEName(probeInfoDto,probeEName);
                        probeInfoDto.setInstrumentConfigId(instrumentConfigId);
                    }
                }
                Map<String, List<ProbeInfoDto>> inoConfigMap =
                        probeInfoList.stream().collect(Collectors.groupingBy(res -> res.getInstrumentNo() + ":" + res.getInstrumentConfigId()));
                List<ProbeInfoDto> probeInfos = new ArrayList<>();
                Date maxDate ;
                //遍历数据库探头信息
                for (InstrumentParamConfigDto configDto : enoAndProbeMap.get(equipmentNo)) {
                    ProbeInfoDto probeInfoDto = new ProbeInfoDto();
                    String instrumentNo = configDto.getInstrumentno();
                    Integer configId = configDto.getInstrumentconfigid();
                    //解决旧数据door2
                    if(StringUtils.equals(SysConstants.CHANNEL_2,configDto.getIChannel()) && configId ==CurrentProbeInfoEnum.CURRENTDOORSTATE.getInstrumentConfigId()){
                        configId = CurrentProbeInfoEnum.CURRENTDOORSTATE2.getInstrumentConfigId();
                    }
                    String inoConfigId =  instrumentNo+":"+configId;
                    if(null == inoConfigMap.get(inoConfigId)){
                        continue;
                    }
                    ProbeInfoDto probeRedis;
                    //解决qc与qcl数据库相同config相同，而缓存不同
                    if(configId != CurrentProbeInfoEnum.CURRENTQC.getInstrumentConfigId()){
                        probeRedis = inoConfigMap.get(inoConfigId).get(0);
                    }else{
                        String inoConfigId2 =  instrumentNo+":"+CurrentProbeInfoEnum.QCL.getInstrumentConfigId();
                        probeRedis = inoConfigMap.containsKey(inoConfigId) ? inoConfigMap.get(inoConfigId).get(0) : inoConfigMap.get(inoConfigId2).get(0);
                    }
                    probeInfoDto.setHighLimit(configDto.getHighLimit());
                    probeInfoDto.setLowLimit(configDto.getLowLimit());
                    probeInfoDto.setSaturation(configDto.getSaturation());
                    probeInfoDto.setInstrumentNo(instrumentNo);
                    probeInfoDto.setInstrumentConfigId(configId);
                    //当返回的值不为数字时，unit返回为空
                    if(StringUtils.isNotBlank(configDto.getUnit()) && RegularUtil.checkContainsNumbers(probeRedis.getValue())){
                        probeInfoDto.setUnit(configDto.getUnit());
                    }else {
                        probeInfoDto.setUnit("");
                    }
                    probeInfoDto.setValue(changeString(probeRedis.getValue(),Context.IsCh()));
                    probeInfoDto.setProbeEName(probeRedis.getProbeEName());
                    probeInfoDto.setInputTime(probeRedis.getInputTime());
                    //缓存中没有找到探头状态时默认为正常
                    probeInfoDto.setState(StringUtils.isBlank(probeRedis.getState())?SysConstants.EQ_NORMAL:probeRedis.getState());
                    //当config为11和44时，通过缓存的当前值和最低值比较来计算探头的状态
                    if(configId == CurrentProbeInfoEnum.CURRENTDOORSTATE.getInstrumentConfigId() || configId == CurrentProbeInfoEnum.CURRENTDOORSTATE2.getInstrumentConfigId()){
                        int i = configDto.getLowLimit().intValue();
                        int integer = Integer.parseInt(probeInfoDto.getValue());
                        if (i == integer) {
                            probeInfoDto.setState(SysConstants.EQ_ABNORMAL);
                        } else {
                            probeInfoDto.setState(SysConstants.EQ_NORMAL);
                        }
                    }
                    //设置MT210M单位问题
                    mt210MData(probeInfo,probeInfoDto);
                    probeInfoDto.setInstrumentConfigId(configId);
                    probeInfos.add(probeInfoDto);
                }
                if(CollectionUtils.isNotEmpty(probeInfos)){
                    probeInfo.setProbeInfoDtoList(probeInfos);
                    probeInfo.setInstrumentConfigIdList(probeInfos.stream().map(ProbeInfoDto::getProbeEName).sorted().collect(Collectors.toList()));
                    List<Date> collect = probeInfos.stream().map(ProbeInfoDto::getInputTime).collect(Collectors.toList());
                    //获取最新的时间
                    maxDate = Collections.max(collect);
                    if (maxDate != null) {
                        probeInfo.setInputTime(maxDate);
                        boolean b = DateUtils.calculateIntervalTime(maxDate, timeoutRedDuration);
                        if(b){
                            probeInfo.setState(SysConstants.EQ_TIMEOUT);
                            timeoutList.add(probeInfo);
                            totalList.add(probeInfo);
                        }
                    }
                    //设备没有超时时，通过探头状态来获取设备状态(有一个探头异常，设备就是异常的，所有探头正常，设备才正常)
                    if(StringUtils.isBlank(probeInfo.getState())){
                        long abnormal = probeInfos.stream().filter(res -> StringUtils.equals(res.getState(),SysConstants.EQ_ABNORMAL)).count();
                        if(abnormal>0){
                            probeInfo.setState(SysConstants.EQ_ABNORMAL);
                            abnormalList.add(probeInfo);
                            totalList.add(probeInfo);
                        }else {
                            probeInfo.setState(SysConstants.EQ_NORMAL);
                            normalList.add(probeInfo);
                            totalList.add(probeInfo);
                        }
                    }
                }else {
                    //没有探头信息时判断设备时异常设备
                    probeInfo.setState(SysConstants.EQ_ABNORMAL);
                    abnormalList.add(probeInfo);
                    totalList.add(probeInfo);
                }
            }
        }
        currentProbeInfoResult.setTotalNum(list.size());
        currentProbeInfoResult.setAbnormalNum(abnormalList.size());
        currentProbeInfoResult.setNormalNum(normalList.size());
        currentProbeInfoResult.setTimeoutNum(timeoutList.size());
        switch (state){
            case SysConstants.EQ_NORMAL:
                List<ProbeCurrentInfoDto> normals = normalList.stream().peek(this::sort).collect(Collectors.toList());
                currentProbeInfoResult.setProbeCurrentInfoDtoList(normals);
                break;
            case SysConstants.EQ_ABNORMAL:
                List<ProbeCurrentInfoDto> abnormals = abnormalList.stream().peek(this::sort).collect(Collectors.toList());
                currentProbeInfoResult.setProbeCurrentInfoDtoList(abnormals);
                break;
            case SysConstants.EQ_TIMEOUT:
                List<ProbeCurrentInfoDto> timeouts = timeoutList.stream().peek(this::sort).collect(Collectors.toList());
                currentProbeInfoResult.setProbeCurrentInfoDtoList(timeouts);
                break;
            default:
                List<ProbeCurrentInfoDto> totals = totalList.stream().peek(this::sort).collect(Collectors.toList());
                currentProbeInfoResult.setProbeCurrentInfoDtoList(totals);
                break;
        }
        return currentProbeInfoResult;
    }

    private void mt210MData(ProbeCurrentInfoDto probeInfo,ProbeInfoDto probeInfoDto){
        if(probeInfo.getSn().substring(4, 6).equals(SysConstants.MT210M_SN) &&
                SysConstants.MT210M_UNIT.equals(probeInfoDto.getUnit())&& RegularUtil.checkContainsNumbers(probeInfoDto.getValue())){
            BigDecimal big =  new BigDecimal(probeInfoDto.getValue());
            probeInfoDto.setValue(big.divide(BigDecimal.valueOf(2.54),1, RoundingMode.HALF_UP).toString());
        }
    }

    /**
     * 将不是数字的字符串转化通过中英文改成新的字符串
     * 参数：String，Boolean
     * 返回：string
     */
    private String changeString(String str, Boolean isChinese) {
        String result = "";
        if (StringUtils.isBlank(str) || RegularUtil.checkContainsNumbers(str)) {
            return str;
        }
        if (isChinese) {
            result = str;
        } else {
            result = ProbeOutlier.from(str).name();
        }
        return result;

    }

    private void sort(ProbeCurrentInfoDto res) {
        List<ProbeInfoDto> probeInfoDtoList = res.getProbeInfoDtoList();
        if(CollectionUtils.isNotEmpty(probeInfoDtoList)){
            List<ProbeInfoDto> collect =
                    probeInfoDtoList.stream().sorted(Comparator.comparing(ProbeInfoDto::getInstrumentConfigId)).collect(Collectors.toList());
            res.setProbeInfoDtoList(collect);
        }
    }

    private void setEName(ProbeInfoDto probeInfoDto, String probeEName) {
        if(StringUtils.equalsAny(probeEName,SysConstants.MT310DC_TEMP,SysConstants.MT310DC_RH,SysConstants.MT310DC_O2,SysConstants.MT310DC_CO2)){
            switch (probeEName){
                case "1":
                    probeInfoDto.setProbeEName(CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                    break;
                case "2":
                    probeInfoDto.setProbeEName(CurrentProbeInfoEnum.CURRENTHUMIDITY.getProbeEName());
                    break;
                case "3":
                    probeInfoDto.setProbeEName(CurrentProbeInfoEnum.OUTERO2.getProbeEName());
                    break;
                case "4":
                    probeInfoDto.setProbeEName(CurrentProbeInfoEnum.OUTERCO2.getProbeEName());
                    break;
            }
        }
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
     * MT310，转换外置探头
     *
     * @param probeEName         探头的英文名称
     * @param instrumentConfigId 探头检测id
     */
    private Integer getInstrumentConfigId(String probeEName, int instrumentConfigId) {
        //内置 VOC/O2/CO2  3/2/1
        //外置 温度/湿度/O2/CO2  4/5/43/42
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
                instrumentConfigId = CurrentProbeInfoEnum.OUTERO2.getInstrumentConfigId();
                break;
            //CO2浓度
            case "4":
                instrumentConfigId = CurrentProbeInfoEnum.OUTERCO2.getInstrumentConfigId();
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
        String eqSnAbbreviation = sn.substring(4, 6);
        String equipmentNo = curveCommand.getEquipmentNo();
        String ym = DateUtils.getYearMonth(startTime, endTime);
        CurveParam curveParam = BeanConverter.convert(curveCommand, CurveParam.class);
        if(ProbeOutlierMt310.THREE_ONE.getCode().equals(eqSnAbbreviation)){
            curveCommand.setInstrumentConfigIdList(Mt310DCUtils.get310DCFields(curveParam.getInstrumentConfigIdList()));
        }
        curveParam.setYearMonth(ym);
        List<Monitorequipmentlastdata> lastDataModelList = monitorequipmentlastdataRepository.getMonitorEquuipmentLastList(curveParam);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(lastDataModelList)) {
            throw new IedsException(LabSystemEnum.NO_DATA_FOR_CURRENT_TIME);
        }
        Map<String, List<InstrumentParamConfigDto>> map = instrumentParamConfigService.getInstrumentParamConfigByENo(equipmentNo);
        return CurveUtils.getCurveFirst(lastDataModelList, instrumentConfigIdList, map,eqSnAbbreviation);
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
        String hospitalCode = warningCommand.getHospitalCode();
        List<Warningrecord> warningRecord = warningrecordRepository.getWarningInfoList(hospitalCode, warningCommand.getStartTime());
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
        //获取医院设备类型缓存信息，用于设置异常数据的报警规则
        //已医院id+eqTypeId分组
        List<HospitalEquipmentTypeInfoDto> hosEqTypeList = hospitalEquipmentTypeIdApi.bulkAcquisitionEqType(hospitalCode).getResult();
        Map<String, List<HospitalEquipmentTypeInfoDto>> hosMap = null;
        if(CollectionUtils.isNotEmpty(hosEqTypeList)){
            hosMap =  hosEqTypeList.stream().collect(Collectors.groupingBy(res -> res.getHospitalcode() + res.getEquipmenttypeid()));
        }
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
            //当全天报警为空时，获取缓存的报警信息，当缓存数据出现既不是全天报警，又没有报警时间段时。将全天报警设置为0
            if(StringUtils.isBlank(warningRecordInfo.getAlwayalarm()) && hosMap != null){
                List<HospitalEquipmentTypeInfoDto> hospitalEquipmentTypeInfoDtos = hosMap.get(warningCommand.getHospitalCode() + warningRecordInfo.getEquipmentTypeId());
                if(CollectionUtils.isNotEmpty(hospitalEquipmentTypeInfoDtos) && hospitalEquipmentTypeInfoDtos.get(0) != null){
                    HospitalEquipmentTypeInfoDto result = hospitalEquipmentTypeInfoDtos.get(0);
                    String alwayalarm = result.getAlwayalarm();
                    if(DictEnum.TURN_ON.getCode().equals(alwayalarm)){
                        warningRecordInfo.setAlwayalarm(DictEnum.TURN_ON.getCode());
                    }else {
                        warningRecordInfo.setAlwayalarm(DictEnum.OFF.getCode());
                        List<MonitorEquipmentWarningTimeDto> warningTimeList = result.getWarningTimeList();
                        if(CollectionUtils.isNotEmpty(warningTimeList)){
                            String str = buildHhSsTimeFormart(warningTimeList);
                            warningRecordInfo.setAlarmRules(str);
                        }
                    }
                }else {
                    warningRecordInfo.setAlwayalarm(DictEnum.OFF.getCode());
                }
            }
            list.add(warningRecordInfo);
        }
        return list;
    }

    /**
     * 将日期转换为字符串,拼接对应时间格式
     */
    public String buildHhSsTimeFormart(List<MonitorEquipmentWarningTimeDto> monitorEquipmentWarningTimes){
        if (CollectionUtils.isNotEmpty(monitorEquipmentWarningTimes)){
            StringBuilder timeBuffer = new StringBuilder();
            for (int i = 0; i < monitorEquipmentWarningTimes.size(); i++) {
                MonitorEquipmentWarningTimeDto monitorEquipmentWarningTime = monitorEquipmentWarningTimes.get(i);
                Date endtime = monitorEquipmentWarningTime.getEndtime();
                Date begintime = monitorEquipmentWarningTime.getBegintime();
                if (null!=begintime && null!= endtime){
                    timeBuffer.append(DateUtils.parseDatetime(begintime));
                    timeBuffer.append("~");
                    timeBuffer.append(DateUtils.parseDatetime(endtime));
                    if(i != monitorEquipmentWarningTimes.size()-1){
                        timeBuffer.append(",");
                    }
                }
            }
            return timeBuffer.toString();
        }
        return  null;
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
        //通过设备no查询设备类型id
        String eqTypeId =  equipmentInfoService.getEqTypeIdByEno(equipmentNo);
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

        //获取医院设备类型缓存信息，用于设置异常数据的报警规则
        //已医院id+eqTypeId分组
        List<HospitalEquipmentTypeInfoDto> hosEqTypeList = hospitalEquipmentTypeIdApi.bulkAcquisitionEqType(hospitalCode).getResult();
        Map<String, List<HospitalEquipmentTypeInfoDto>> hosMap = null;
        if(CollectionUtils.isNotEmpty(hosEqTypeList)){
            hosMap =  hosEqTypeList.stream().collect(Collectors.groupingBy(res -> res.getHospitalcode() + res.getEquipmenttypeid()));
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
            if(StringUtils.isEmpty(detailInfo.getAlwayalarm()) && hosMap != null){
                List<HospitalEquipmentTypeInfoDto> hospitalEquipmentTypeInfoDtos = hosMap.get(hospitalCode +eqTypeId);
                if(CollectionUtils.isNotEmpty(hospitalEquipmentTypeInfoDtos) && hospitalEquipmentTypeInfoDtos.get(0) != null){
                    HospitalEquipmentTypeInfoDto result = hospitalEquipmentTypeInfoDtos.get(0);
                    String alwayalarm = result.getAlwayalarm();
                    if(DictEnum.TURN_ON.getCode().equals(alwayalarm)){
                        detailInfo.setAlwayalarm(DictEnum.TURN_ON.getCode());
                    }else {
                        detailInfo.setAlwayalarm(DictEnum.OFF.getCode());
                        List<MonitorEquipmentWarningTimeDto> warningTimeList = result.getWarningTimeList();
                        if(CollectionUtils.isNotEmpty(warningTimeList)){
                            String str = buildHhSsTimeFormart(warningTimeList);
                            detailInfo.setAlarmTime(str);
                        }
                    }
                }else {
                    detailInfo.setAlwayalarm(DictEnum.OFF.getCode());
                }
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

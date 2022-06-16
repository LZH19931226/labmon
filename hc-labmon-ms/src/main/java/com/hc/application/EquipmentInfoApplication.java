package com.hc.application;

import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.command.labmanagement.model.HospitalEquipmentTypeModel;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.constants.LabMonEnumError;
import com.hc.device.SnDeviceRedisApi;
import com.hc.dto.*;
import com.hc.hospital.HospitalInfoApi;
import com.hc.labmanagent.HospitalEquipmentTypeApi;
import com.hc.labmanagent.MonitorEquipmentApi;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.command.EquipmentInfoCommand;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.service.EquipmentInfoService;
import com.hc.service.InstrumentMonitorInfoService;
import com.hc.util.EquipmentInfoServiceHelp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hc
 */
@Component
public class EquipmentInfoApplication {

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private InstrumentMonitorInfoService instrumentMonitorInfoService;

    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    @Autowired
    private HospitalEquipmentTypeApi hospitalEquipmentTypeApi;

    @Autowired
    private MonitorEquipmentApi monitorEquipmentApi;

    /**
     * 查询所有设备当前值信息
     * @param hospitalCode 医院id
     * @param equipmentTypeId 设备类型id
     * @return 监控设备集合
     */
    public List<MonitorEquipmentDto> findEquipmentCurrentData(String hospitalCode, String equipmentTypeId) {

        //获取监控设备的信息
        List<MonitorEquipmentDto> monitorEquipmentDtoList = equipmentInfoService.getEquipmentInfoByCodeAndTypeId(hospitalCode,equipmentTypeId);
        if(CollectionUtils.isEmpty(monitorEquipmentDtoList)){
            throw new IedsException(LabMonEnumError.DEVICE_INFORMATION_NOT_FOUND.getMessage());
        }
        List<String> equipmentNoList = monitorEquipmentDtoList.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
        Map<String, List<MonitorEquipmentDto>> monitorEquipmentMap = monitorEquipmentDtoList.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmentno));

        //获取仪器监控的信息
        List<InstrumentMonitorInfoDto> instrumentMonitorInfoDtoList  = instrumentMonitorInfoService.selectInstrumentMonitorInfoByEqNo(equipmentNoList);
        Map<String, List<InstrumentMonitorInfoDto>> InstrumentMonitorInfoDtoMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(instrumentMonitorInfoDtoList)){
            InstrumentMonitorInfoDtoMap = instrumentMonitorInfoDtoList.stream().collect(Collectors.groupingBy(InstrumentMonitorInfoDto::getEquipmentno));
        }

        //查询lowlimit的值
        List<MonitorinstrumentDto> monitorEquipmentLowLimitList = equipmentInfoService.getLowLimitList(equipmentNoList);
        Map<String, List<MonitorinstrumentDto>> monitorEquipmentLowMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(monitorEquipmentLowLimitList)){
            monitorEquipmentLowMap = monitorEquipmentLowLimitList.stream().collect(Collectors.groupingBy(MonitorinstrumentDto::getEquipmentno));
        }
        EquipmentInfoCommand equipmentInfoCommand = new EquipmentInfoCommand();
        equipmentInfoCommand.setEquipmentNoList(equipmentNoList);
        equipmentInfoCommand.setHospitalCode(hospitalCode);

        //查询设备号当前的信息值
        List<MonitorequipmentlastdataDto> resultList = snDeviceRedisApi.getTheCurrentValueOfTheDeviceInBatches(equipmentInfoCommand).getResult();

        Map<String, List<MonitorequipmentlastdataDto>> resultMap = resultList.stream().collect(Collectors.groupingBy(MonitorequipmentlastdataDto::getEquipmentno));

        for (MonitorEquipmentDto monitorEquipmentDto : monitorEquipmentDtoList) {
            String equipmentNo = monitorEquipmentDto.getEquipmentno();
            List<MonitorEquipmentDto> monitorEquipmentDtos = monitorEquipmentMap.get(equipmentNo);
            if(CollectionUtils.isNotEmpty(monitorEquipmentDtos)){
                monitorEquipmentDto.setSn(monitorEquipmentDtos.get(0).getSn());
            }
            List<MonitorequipmentlastdataDto> monitorEquipmentLastDataDTOs = resultMap.get(equipmentNo);
            if(CollectionUtils.isNotEmpty(monitorEquipmentLastDataDTOs)){
                MonitorequipmentlastdataDto monitorequipmentlastdataDto = monitorEquipmentLastDataDTOs.get(0);
                if(StringUtils.isNotBlank(monitorequipmentlastdataDto.getCurrentdoorstate())){
                    // 查找这个设备下开关量的最低值
                    String lowlimit = monitorEquipmentLowMap.get(equipmentNo).get(0).getLowlimit();
                    monitorEquipmentDto.setLowlimit(lowlimit);
                }
                monitorEquipmentDto.setMonitorequipmentlastdataDto(monitorequipmentlastdataDto);
            }
            List<InstrumentMonitorInfoDto> resultIMList = InstrumentMonitorInfoDtoMap.get(equipmentNo);
            if(CollectionUtils.isNotEmpty(resultIMList)){
                monitorEquipmentDto.setInstrumentMonitorInfoDtoList(resultIMList);
            }
        }
        return monitorEquipmentDtoList;
    }

    /**
     * 获取曲线信息，不包括曲线对比信息
     * @param equipmentNo 设备id
     * @param date 时间
     * @return 曲线信息对象
     */
    public CurveInfoDto getCurveFirst(String equipmentNo, String date) {
        List<Monitorequipmentlastdata> lastDataModelList  =  monitorequipmentlastdataRepository.getMonitorEquipmentLastDataInfo(date,equipmentNo);
        if(CollectionUtils.isEmpty(lastDataModelList)) {
            throw new IedsException(LabMonEnumError.NO_DATA_FOR_CURRENT_TIME.getMessage());
        }
        return EquipmentInfoServiceHelp.getCurveFirst(lastDataModelList, new CurveInfoDto());
    }

    /**
     * 获取医院信息
     * @param hospitalCode
     * @return
     */
    public HospitalMadel getHospitalInfO(String hospitalCode) {
        HospitalMadel hospitalInfo = hospitalInfoApi.findHospitalInfo(hospitalCode).getResult();
        if(ObjectUtils.isEmpty(hospitalInfo)){
            return null;
        }
        String timeoutRedDuration = hospitalInfo.getTimeoutRedDuration();
        if (StringUtils.isEmpty(timeoutRedDuration)) {
            hospitalInfo.setTimeoutRedDuration("60");
        }
        List<HospitalEquipmentTypeModel> hospitalEquipmentTypeModelList = hospitalEquipmentTypeApi.findHospitalEquipmentTypeByCode(hospitalCode).getResult();
        if(CollectionUtils.isNotEmpty(hospitalEquipmentTypeModelList)){
            hospitalInfo.setHospitalEquipmentTypeModelList(hospitalEquipmentTypeModelList);
        }
        return hospitalInfo;
    }

    /**
     * 获取当前市电信息
     *  先获取当前医院所有的设备
     *      再获取所有设备的获取当前值的信息
     *          过滤掉没有ups的数据，取最新集合中的第一条数据
     * @param hospitalCode
     * @return
     */
    public MonitorUpsInfoDto getCurrentUpsInfo(String hospitalCode) {
        List<String> equipmentNoList = monitorEquipmentApi.getEquipmentNoList(hospitalCode).getResult();
        if(CollectionUtils.isEmpty(equipmentNoList)){
          return null;
        }
        EquipmentInfoCommand equipmentInfoCommand = new EquipmentInfoCommand();
        equipmentInfoCommand.setHospitalCode(hospitalCode);
        equipmentInfoCommand.setEquipmentNoList(equipmentNoList);
        List<MonitorequipmentlastdataDto> result = snDeviceRedisApi.getTheCurrentValueOfTheDeviceInBatches(equipmentInfoCommand).getResult();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        List<MonitorequipmentlastdataDto> collect =
                result.stream().filter(res -> StringUtils.isNotEmpty(res.getCurrentups())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return null;
        }
        MonitorequipmentlastdataDto monitorequipmentlastdataDto = collect.get(0);
        String currentUps = monitorequipmentlastdataDto.getCurrentups();
        MonitorUpsInfoDto monitorUpsInfoDto = new MonitorUpsInfoDto();
        monitorUpsInfoDto.setUps(currentUps);
        return monitorUpsInfoDto;
    }
}

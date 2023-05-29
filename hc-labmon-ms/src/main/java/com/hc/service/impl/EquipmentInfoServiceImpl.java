package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.ProbeCommand;
import com.hc.application.response.CurrentProbeInfoResult;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.MonitorinstrumentDto;
import com.hc.dto.ProbeCurrentInfoDto;
import com.hc.my.common.core.redis.dto.EquipmentEnableSetDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.repository.EquipmentInfoRepository;
import com.hc.service.EquipmentInfoService;
import com.hc.service.MonitorInstrumentService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EquipmentInfoServiceImpl implements EquipmentInfoService {

    @Autowired
    private EquipmentInfoRepository equipmentInfoRepository;

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private MonitorInstrumentService monitorInstrumentService;

    /**
     * 查询设备信息
     *
     * @param equipmentNo
     * @return
     */
    @Override
    public MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo) {
        return equipmentInfoRepository.getEquipmentInfoByNo(equipmentNo);
    }

    /**
     * 分页获取设备id
     * @param page
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentInfoByPage(Page page, ProbeCommand probeCommand) {
        return equipmentInfoRepository.getEquipmentInfoByPage(page,probeCommand);
    }

    /**
     * @param equipmentNoList
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> batchGetEquipmentInfo(List<String> equipmentNoList) {
        return equipmentInfoRepository.batchGetEquipmentInfo(equipmentNoList);
    }

    /**
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getAll() {
        return equipmentInfoRepository.getAll();
    }

    /**
     * @param list
     */
    @Override
    public void bulkUpdate(List<MonitorEquipmentDto> list) {
        equipmentInfoRepository.updateBatchById(list);
    }

    @Override
    public List<String> getEnoList(String hospitalCode,String equipmentTypeId) {
        List<MonitorEquipmentDto> list = equipmentInfoRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentDto())
                .select(MonitorEquipmentDto::getEquipmentno)
                .eq(MonitorEquipmentDto::getHospitalcode, hospitalCode)
                        .eq(MonitorEquipmentDto::getClientvisible,"1")
                .eq(MonitorEquipmentDto::getEquipmenttypeid,equipmentTypeId));
        return list.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
    }

    @Override
    public List<MonitorEquipmentDto> getEquipmentInfo(ProbeCommand probeCommand) {
        String sn = probeCommand.getSn();
        String equipmentName = probeCommand.getEquipmentName();
        String address = probeCommand.getAddress();
        if (StringUtils.isEmpty(address)){
            return StringUtils.isBlank(sn) ?
                    equipmentInfoRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentDto())
                            .eq(MonitorEquipmentDto::getHospitalcode,probeCommand.getHospitalCode())
                            .eq(MonitorEquipmentDto::getEquipmenttypeid,probeCommand.getEquipmentTypeId())
                            .eq(MonitorEquipmentDto::getClientvisible,"1")
                            .like(!StringUtils.isBlank(equipmentName),MonitorEquipmentDto::getEquipmentname,equipmentName)
                            .orderByAsc(MonitorEquipmentDto::getSort)
                            .orderByAsc(MonitorEquipmentDto::getEquipmentname)
                          )
                    : equipmentInfoRepository.getEquipmentInfoBySn(probeCommand);
        }else {
            return StringUtils.isBlank(sn) ?
                    equipmentInfoRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentDto())
                            .eq(MonitorEquipmentDto::getHospitalcode,probeCommand.getHospitalCode())
                            .eq(MonitorEquipmentDto::getEquipmenttypeid,probeCommand.getEquipmentTypeId())
                            .eq(MonitorEquipmentDto::getClientvisible,"1")
                            .like(MonitorEquipmentDto::getAddress,address)
                            .like(!StringUtils.isBlank(equipmentName),MonitorEquipmentDto::getEquipmentname,equipmentName)
                            .orderByAsc(MonitorEquipmentDto::getSort)
                            .orderByAsc(MonitorEquipmentDto::getEquipmentname)
                    )
                    : equipmentInfoRepository.getEquipmentInfoBySn(probeCommand);
        }

    }

    @Override
    public String getEqTypeIdByEno(String equipmentNo) {
        return equipmentInfoRepository.getEqTypeIdByEno(equipmentNo);
    }

    @Override
    public EquipmentEnableSetDto getEquipmentEnableSet(ProbeCommand probeCommand) {
        EquipmentEnableSetDto currentProbeInfoResult = new EquipmentEnableSetDto();
        //查询设备信息 获取设备 未禁用的设备
        List<MonitorEquipmentDto> list = equipmentInfoRepository.getEquipmentStateInfo(probeCommand);
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
        List<SnDeviceDto> snDeviceDtoAll  = new ArrayList<>();
        //获取设备对象的探头信息
        for (MonitorEquipmentDto monitorEquipmentDto : list) {
            //设置设备信息
            SnDeviceDto probeInfo = new SnDeviceDto();
            String equipmentNo = monitorEquipmentDto.getEquipmentno();
            if (enoAndMiMap.containsKey(equipmentNo)) {
                MonitorinstrumentDto monitorinstrumentDto = enoAndMiMap.get(equipmentNo).get(0);
                probeInfo.setInstrumentTypeId(String.valueOf(monitorinstrumentDto.getInstrumenttypeid()));
                probeInfo.setSn(monitorinstrumentDto.getSn());
            }
            probeInfo.setEquipmentNo(equipmentNo);
            probeInfo.setEquipmentName(monitorEquipmentDto.getEquipmentname());
            probeInfo.setEquipmentTypeId(probeCommand.getEquipmentTypeId());
            probeInfo.setClientVisible((long) (monitorEquipmentDto.getClientvisible()?1:0));
            snDeviceDtoAll.add(probeInfo);
        }
        List<SnDeviceDto> snDeviceDtoEnable = snDeviceDtoAll.stream().filter(s -> s.getClientVisible() == 1).collect(Collectors.toList());
        List<SnDeviceDto> snDeviceDtoNotEnable = snDeviceDtoAll.stream().filter(s -> s.getClientVisible() == 0).collect(Collectors.toList());
        currentProbeInfoResult.setSnDeviceDtoNotEnable(snDeviceDtoNotEnable);
        currentProbeInfoResult.setSnDeviceDtoAll(snDeviceDtoAll);
        currentProbeInfoResult.setSnDeviceDtoEnable(snDeviceDtoEnable);
        currentProbeInfoResult.setAllCount(snDeviceDtoAll.size());
        currentProbeInfoResult.setOpenCount(CollectionUtils.isEmpty(snDeviceDtoEnable)?0:snDeviceDtoEnable.size());
        currentProbeInfoResult.setCloseCount(CollectionUtils.isEmpty(snDeviceDtoNotEnable)?0:snDeviceDtoNotEnable.size());
        return currentProbeInfoResult;
    }
}

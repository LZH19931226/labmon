package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.repository.EquipmentInfoRepository;
import com.hc.service.EquipmentInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentInfoServiceImpl implements EquipmentInfoService {

    @Autowired
    private EquipmentInfoRepository equipmentInfoRepository;

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
                            .like(!StringUtils.isBlank(equipmentName),MonitorEquipmentDto::getEquipmentname,equipmentName))
                    : equipmentInfoRepository.getEquipmentInfoBySn(probeCommand);
        }else {
            return StringUtils.isBlank(sn) ?
                    equipmentInfoRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentDto())
                            .eq(MonitorEquipmentDto::getHospitalcode,probeCommand.getHospitalCode())
                            .eq(MonitorEquipmentDto::getEquipmenttypeid,probeCommand.getEquipmentTypeId())
                            .eq(MonitorEquipmentDto::getClientvisible,"1")
                            .eq(MonitorEquipmentDto::getEquipmenttypeid,address)
                            .like(!StringUtils.isBlank(equipmentName),MonitorEquipmentDto::getEquipmentname,equipmentName))
                    : equipmentInfoRepository.getEquipmentInfoBySn(probeCommand);
        }

    }
}

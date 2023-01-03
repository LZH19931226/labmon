package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.repository.EquipmentInfoRepository;
import com.hc.service.EquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

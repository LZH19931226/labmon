package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.dto.MonitorinstrumentDto;
import com.hc.infrastructure.dao.EquipmentInfoDao;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.repository.EquipmentInfoRepository;
import com.hc.vo.labmon.model.MonitorEquipmentLastDataModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EquipmentInfoRepositoryImpl extends ServiceImpl<EquipmentInfoDao,MonitorEquipmentDto> implements EquipmentInfoRepository {

    @Autowired
    private EquipmentInfoDao equipmentInfoDao;

    /**
     * 查询设备信息
     *
     * @param equipmentNo
     * @return
     */
    @Override
    public MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo) {
        return equipmentInfoDao.getEquipmentInfoByNo(equipmentNo);
    }


    /**
     * @param page
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentInfoByPage(Page page, ProbeCommand probeCommand) {
        return equipmentInfoDao.getEquipmentInfoByPage(page,probeCommand);
    }

    /**
     * @param equipmentNoList
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> batchGetEquipmentInfo(List<String> equipmentNoList) {
        return equipmentInfoDao.batchGetEquipmentInfo(equipmentNoList);
    }

    /**
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getAll() {
        return equipmentInfoDao.getAll();
    }

}

package com.hc.repository.impl;

import com.hc.dto.MonitorinstrumentDto;
import com.hc.infrastructure.dao.EquipmentInfoDao;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.repository.EquipmentInfoRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EquipmentInfoRepositoryImpl implements EquipmentInfoRepository {

    @Autowired
    private EquipmentInfoDao equipmentInfoDao;

    /**
     * 查询所有设备当前值信息
     *
     * @param hospitalCode    医院id
     * @param equipmentTypeId 设备类型id
     * @return 监控设备集合
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentInfoByCodeAndTypeId(String hospitalCode, String equipmentTypeId) {
        return equipmentInfoDao.getEquipmentInfoByCodeAndTypeId(hospitalCode,equipmentTypeId);
    }

    @Override
    public List<MonitorinstrumentDto> getSns(List<String> equipmentNoList) {
        List<MonitorinstrumentDto> sns = equipmentInfoDao.getSns(equipmentNoList);
        return CollectionUtils.isEmpty(sns)?null:sns;
    }

    @Override
    public String getLowlimit(String equipmentNo) {
        return equipmentInfoDao.getLowlimit(equipmentNo);
    }

    @Override
    public List<MonitorinstrumentDto> getLowLimitList(List<String> equipmentNoList) {
        List<MonitorinstrumentDto> list =  equipmentInfoDao.getLowLimitList(equipmentNoList);
        return CollectionUtils.isEmpty(list)?null:list;
    }
}

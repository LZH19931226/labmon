package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    /**
     * 获取曲线表信息
     *
     * @param date 医院id
     * @param equipmentNo  设备id
     * @param tableName    查询的表名称
     * @return
     */
    @Override
    public List<MonitorEquipmentLastDataModel> getCurveInfo(String date, String equipmentNo, String tableName) {
        return equipmentInfoDao.getCurveInfo(date,equipmentNo,tableName);
    }

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
     * @param hospitalCode
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentInfoByHospitalCode(String hospitalCode) {
        return equipmentInfoDao.selectList(Wrappers.lambdaQuery(new MonitorEquipmentDto()).eq(MonitorEquipmentDto::getHospitalcode,hospitalCode));
    }


}

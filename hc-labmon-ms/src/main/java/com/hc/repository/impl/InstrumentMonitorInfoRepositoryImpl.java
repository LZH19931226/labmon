package com.hc.repository.impl;

import com.hc.application.command.EquipmentDataCommand;
import com.hc.dto.InstrumentMonitorInfoDto;
import com.hc.dto.InstrumentTypeNumDto;
import com.hc.infrastructure.dao.InstrumentMonitorInfoDao;
import com.hc.repository.InstrumentMonitorInfoRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InstrumentMonitorInfoRepositoryImpl implements InstrumentMonitorInfoRepository {

    @Autowired
    private InstrumentMonitorInfoDao instrumentMonitorInfoDao;

    /**
     * 查询仪器监视器信息集合
     * @param equipmentNoList 设备no集合
     * @return 仪器监视器信息集合
     */
    @Override
    public List<InstrumentMonitorInfoDto> selectInstrumentMonitorInfoByEqNo(List<String> equipmentNoList) {
        List<InstrumentMonitorInfoDto> instrumentMonitorInfoDtoList = instrumentMonitorInfoDao.selectInstrumentMonitorInfoByEqNo(equipmentNoList);
        return CollectionUtils.isEmpty(instrumentMonitorInfoDtoList)?null:instrumentMonitorInfoDtoList;
    }

    @Override
    public List<InstrumentTypeNumDto> getEquipmentTypeNum(EquipmentDataCommand equipmentDataCommand) {
        return instrumentMonitorInfoDao.getEquipmentTypeNum(equipmentDataCommand);
    }
}

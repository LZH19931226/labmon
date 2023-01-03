package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.dto.EquipmentTypeNumDto;
import com.hc.dto.HospitalEquipmentDto;
import com.hc.dto.eqTypeAlarmNumCountDto;
import com.hc.infrastructure.dao.HospitalEquipmentDao;
import com.hc.repository.HospitalEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HospitalEquipmentRepositoryImpl extends ServiceImpl<HospitalEquipmentDao, HospitalEquipmentDto> implements HospitalEquipmentRepository {

    @Autowired
    private HospitalEquipmentDao hospitalEquipmentDao;


    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<HospitalEquipmentDto> selectHospitalEquipmentInfo(String hospitalCode) {
        return hospitalEquipmentDao.hospitalEquipmentDao(hospitalCode);
    }

    @Override
    public List<eqTypeAlarmNumCountDto> findEquipmentByHosCode(String hospitalCode) {
        return hospitalEquipmentDao.findEquipmentByHosCode(hospitalCode);
    }

    @Override
    public List<EquipmentTypeNumDto> getEquipmentTypeNum(EquipmentDataCommand equipmentDataCommand) {
        return hospitalEquipmentDao.getEquipmentTypeNum(equipmentDataCommand);
    }

    @Override
    public List<HospitalEquipmentDto> selectHospitalEquipmentInfoByPc(String hospitalCode) {
        return hospitalEquipmentDao.selectHospitalEquipmentInfoByPc(hospitalCode);
    }
}

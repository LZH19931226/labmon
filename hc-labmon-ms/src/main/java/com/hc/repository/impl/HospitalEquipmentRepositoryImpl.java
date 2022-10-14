package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.HospitalEquipmentDto;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.infrastructure.dao.HospitalEquipmentDao;
import com.hc.infrastructure.dao.InstrumentParamConfigDao;
import com.hc.repository.HospitalEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HospitalEquipmentRepositoryImpl extends ServiceImpl<HospitalEquipmentDao, HospitalEquipmentDto> implements HospitalEquipmentRepository {

    @Autowired
    private HospitalEquipmentDao hospitalEquipmentDao;
    @Autowired
    private InstrumentParamConfigDao instrumentParamConfigDao;

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<HospitalEquipmentDto> selectHospitalEquipmentInfo(String hospitalCode) {
        return hospitalEquipmentDao.hospitalEquipmentDao(hospitalCode);
    }

}

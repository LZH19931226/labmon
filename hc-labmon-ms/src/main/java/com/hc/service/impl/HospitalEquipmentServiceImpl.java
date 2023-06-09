package com.hc.service.impl;

import com.hc.dto.HospitalEquipmentDto;
import com.hc.repository.HospitalEquipmentRepository;
import com.hc.service.HospitalEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalEquipmentServiceImpl implements HospitalEquipmentService {

    @Autowired
    private HospitalEquipmentRepository hospitalEquipmentRepository;

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<HospitalEquipmentDto> selectHospitalEquipmentInfo(String hospitalCode) {
        return hospitalEquipmentRepository.selectHospitalEquipmentInfo(hospitalCode);
    }

    @Override
    public List<HospitalEquipmentDto> selectHospitalEquipmentInfoByPc(String hospitalCode) {
        return hospitalEquipmentRepository.selectHospitalEquipmentInfoByPc(hospitalCode);
    }
}

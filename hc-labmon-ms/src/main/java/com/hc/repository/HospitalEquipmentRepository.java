package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.dto.HospitalEquipmentDto;

import java.util.List;

public interface HospitalEquipmentRepository extends IService<HospitalEquipmentDto> {

    List<HospitalEquipmentDto> selectHospitalEquipmentInfo(String hospitalCode);

    List<HospitalEquipmentDto> findHospitalEquipmentTypeByCode(String hospitalCode);
}

package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.dto.EquipmentTypeNumDto;
import com.hc.dto.HospitalEquipmentDto;
import com.hc.dto.eqTypeAlarmNumCountDto;

import java.util.List;

public interface HospitalEquipmentRepository extends IService<HospitalEquipmentDto> {

    List<HospitalEquipmentDto> selectHospitalEquipmentInfo(String hospitalCode);

    List<eqTypeAlarmNumCountDto> findEquipmentByHosCode(String hospitalCode);

    List<EquipmentTypeNumDto> getEquipmentTypeNum(EquipmentDataCommand equipmentDataCommand);

    List<HospitalEquipmentDto> selectHospitalEquipmentInfoByPc(String hospitalCode);
}

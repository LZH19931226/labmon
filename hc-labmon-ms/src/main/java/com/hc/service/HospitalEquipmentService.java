package com.hc.service;

import com.hc.dto.HospitalEquipmentDto;

import java.util.List;

public interface HospitalEquipmentService {
    List<HospitalEquipmentDto> selectHospitalEquipmentInfo(String hospitalCode);

    void batchProbeAlarmState(List<String> probeIds, String warningPhone);
}

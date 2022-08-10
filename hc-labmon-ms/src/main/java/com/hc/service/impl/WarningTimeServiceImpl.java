package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.MonitorEquipmentWarningTimeDTO;
import com.hc.repository.WarningTimeRepository;
import com.hc.service.WarningTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningTimeServiceImpl implements WarningTimeService {

    @Autowired
    private WarningTimeRepository warningTimeRepository;

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<MonitorEquipmentWarningTimeDTO> getWarningInfo(String hospitalCode) {
        return warningTimeRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentWarningTimeDTO())
                .eq(MonitorEquipmentWarningTimeDTO::getHospitalcode,hospitalCode));
    }
}

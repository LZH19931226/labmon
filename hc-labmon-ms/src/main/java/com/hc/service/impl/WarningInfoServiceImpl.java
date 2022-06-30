package com.hc.service.impl;

import com.hc.my.common.core.redis.dto.WarningRecordDto;
import com.hc.repository.WarningInfoRepository;
import com.hc.service.WarningInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningInfoServiceImpl implements WarningInfoService {

    @Autowired
    private WarningInfoRepository warningInfoRepository;

    @Override
    public List<WarningRecordDto> getWarningRecord(String hospitalCode) {
        return warningInfoRepository.getWarningRecord(hospitalCode);
    }
}

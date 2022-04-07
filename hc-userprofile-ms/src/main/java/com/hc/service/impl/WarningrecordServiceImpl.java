package com.hc.service.impl;

import com.hc.dto.WarningrecordDto;
import com.hc.repository.WarningrecordRepository;
import com.hc.service.WarningrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningrecordServiceImpl implements WarningrecordService {

    @Autowired
    private WarningrecordRepository warningrecordRepository;


    @Override
    public List<WarningrecordDto> getWarningRecord(String hospitalcode) {
        return warningrecordRepository.getWarningRecord(hospitalcode);
    }
}

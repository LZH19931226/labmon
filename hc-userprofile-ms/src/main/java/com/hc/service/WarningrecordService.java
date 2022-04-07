package com.hc.service;

import com.hc.dto.WarningrecordDto;

import java.util.List;

public interface WarningrecordService {
    List<WarningrecordDto> getWarningRecord(String hospitalcode);
}

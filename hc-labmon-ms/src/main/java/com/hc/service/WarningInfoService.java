package com.hc.service;

import com.hc.my.common.core.redis.dto.WarningRecordDto;

import java.util.List;

public interface WarningInfoService {

    List<WarningRecordDto> getWarningRecord(String hospitalCode);
}

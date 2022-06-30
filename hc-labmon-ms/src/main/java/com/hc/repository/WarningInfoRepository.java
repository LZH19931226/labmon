package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.my.common.core.redis.dto.WarningRecordDto;

import java.util.List;

public interface WarningInfoRepository extends IService<WarningRecordDto> {

    List<WarningRecordDto> getWarningRecord(String hospitalCode);
}

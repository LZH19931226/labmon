package com.hc.application;

import com.hc.my.common.core.redis.dto.WarningRecordDto;
import com.hc.service.WarningInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarningInfoApplication {

    @Autowired
    private WarningInfoService warningInfoService;

    /**
     *获取报警信息
     * @param hospitalCode
     * @return
     */
    public List<WarningRecordDto> getWarningRecord(String hospitalCode) {
        return  warningInfoService.getWarningRecord(hospitalCode);
    }
}

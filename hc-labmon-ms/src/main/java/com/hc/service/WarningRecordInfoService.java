package com.hc.service;

import com.hc.dto.WarningRecordInfoDto;

import java.util.List;

public interface WarningRecordInfoService {

    WarningRecordInfoDto selectWarningRecordInfo(String pkId);

    void save(WarningRecordInfoDto warningRecordDto);

    void update(WarningRecordInfoDto warningRecordDto);
}

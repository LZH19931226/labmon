package com.hc.service;

import com.hc.dto.LabHosWarningTimeDto;

import java.util.List;

public interface LabHosWarningTimeService {
    void saveObj(List<LabHosWarningTimeDto> labHosWarningTimes);

    void removeObjByCode(String hospitalCode);

    List<LabHosWarningTimeDto> getAll();
}

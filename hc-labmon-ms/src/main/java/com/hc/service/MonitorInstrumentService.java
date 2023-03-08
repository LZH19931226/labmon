package com.hc.service;

import com.hc.dto.MonitorinstrumentDto;

import java.util.List;

public interface MonitorInstrumentService {
    List<MonitorinstrumentDto> selectMonitorInstrumentByEnoList(List<String> enoList);

    String getInstrumentTypeId(String equipmentNo);
}

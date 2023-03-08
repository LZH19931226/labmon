package com.hc.repository;

import com.hc.dto.MonitorinstrumentDto;

import java.util.List;

public interface MonitorInstrumentRepository {
    List<MonitorinstrumentDto> selectMonitorInstrumentByEnoList(List<String> enoList);

    String getInstrumentTypeId(String equipmentNo);
}

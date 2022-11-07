package com.hc.service.impl;

import com.hc.dto.MonitorinstrumentDto;
import com.hc.repository.MonitorInstrumentRepository;
import com.hc.service.MonitorInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorInstrumentServiceImpl implements MonitorInstrumentService {

    @Autowired
    private MonitorInstrumentRepository monitorInstrumentRepository;

    @Override
    public List<MonitorinstrumentDto> selectMonitorInstrumentByEnoList(List<String> enoList) {
        return monitorInstrumentRepository.selectMonitorInstrumentByEnoList(enoList);
    }
}

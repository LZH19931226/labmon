package com.hc.service.impl;

import com.hc.dto.MonitorInstrumentDto;
import com.hc.repository.MonitorInstrumentRepository;
import com.hc.service.MonitorInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 监控仪器服务实施
 * @author hc
 */
@Service
public class MonitorInstrumentServiceImpl implements MonitorInstrumentService {
    @Autowired
    private MonitorInstrumentRepository monitorInstrumentRepository;

    @Override
    public List<MonitorInstrumentDto> selectMonitorInstrumentList() {
        return monitorInstrumentRepository.selectMonitorInstrumentList();
    }
}

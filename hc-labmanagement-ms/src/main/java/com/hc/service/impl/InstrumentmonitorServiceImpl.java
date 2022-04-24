package com.hc.service.impl;

import com.hc.dto.InstrumentmonitorDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.repository.InstrumentmonitorRepository;
import com.hc.service.InstrumentmonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentmonitorServiceImpl implements InstrumentmonitorService {

    @Autowired
    private InstrumentmonitorRepository instrumentmonitorRepository;

    @Override
    public List<MonitorinstrumenttypeDTO> selectMonitorEquipmentType(String instrumenttypeid) {
        return instrumentmonitorRepository.selectMonitorEquipmentType(instrumenttypeid);
    }

    /**
     * 插入监控仪器信息
     *
     * @param instrumentmonitorDTO
     */
    @Override
    public void insertInstrumentmonitorInfo(InstrumentmonitorDTO instrumentmonitorDTO) {
        instrumentmonitorRepository.insertInstrumentmonitorInfo(instrumentmonitorDTO);
    }
}
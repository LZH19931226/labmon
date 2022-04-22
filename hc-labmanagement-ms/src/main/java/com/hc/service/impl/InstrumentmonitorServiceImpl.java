package com.hc.service.impl;

import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.repository.InstrumentmonitorRepository;
import com.hc.service.InstrumentmonitorService;
import com.hc.vo.equimenttype.MonitorinstrumenttypeVo;
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
}
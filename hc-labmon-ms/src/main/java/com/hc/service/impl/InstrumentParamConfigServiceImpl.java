package com.hc.service.impl;

import com.hc.dto.InstrumentParamConfigDto;
import com.hc.repository.InstrumentParamConfigRepository;
import com.hc.service.InstrumentParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InstrumentParamConfigServiceImpl  implements InstrumentParamConfigService {

    @Autowired
    private InstrumentParamConfigRepository instrumentParamConfigRepository;

    @Override
    public Map<String, List<InstrumentParamConfigDto>> getInstrumentParamConfigByENo(String equipmentNo) {
        return instrumentParamConfigRepository.getInstrumentParamConfigByENo(equipmentNo);
    }
}

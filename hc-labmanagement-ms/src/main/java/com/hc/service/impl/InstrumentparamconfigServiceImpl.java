package com.hc.service.impl;

import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.repository.InstrumentparamconfigRepository;
import com.hc.service.InstrumentparamconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstrumentparamconfigServiceImpl implements InstrumentparamconfigService {

    @Autowired
    private InstrumentparamconfigRepository instrumentparamconfigRepository;
    /**
     * 插入探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        instrumentparamconfigRepository.insertInstrumentmonitor(instrumentparamconfigDTO);
    }
}
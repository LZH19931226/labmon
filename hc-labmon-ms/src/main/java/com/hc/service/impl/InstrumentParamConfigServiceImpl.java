package com.hc.service.impl;

import com.hc.repository.InstrumentParamConfigRepository;
import com.hc.service.InstrumentParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstrumentParamConfigServiceImpl implements InstrumentParamConfigService {

    @Autowired
    private InstrumentParamConfigRepository InstrumentParamConfigServiceRepository;

}

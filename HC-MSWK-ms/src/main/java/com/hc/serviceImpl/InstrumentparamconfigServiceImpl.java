package com.hc.serviceImpl;

import com.hc.mapper.InstrumentParamConfigMapper;
import com.hc.po.Instrumentparamconfig;
import com.hc.service.InstrumentparamconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstrumentparamconfigServiceImpl implements InstrumentparamconfigService {

    @Autowired
    private InstrumentParamConfigMapper instrumentParamConfigMapper;

    /**
     * @param instrumentparamconfig
     */
    @Override
    public void updateInfo(Instrumentparamconfig instrumentparamconfig) {
        instrumentParamConfigMapper.updateById(instrumentparamconfig);
    }
}

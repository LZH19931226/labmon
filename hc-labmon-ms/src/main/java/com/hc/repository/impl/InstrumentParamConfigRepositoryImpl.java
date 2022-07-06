package com.hc.repository.impl;

import com.hc.dto.InstrumentParamConfigDto;
import com.hc.infrastructure.dao.InstrumentParamConfigDao;
import com.hc.repository.InstrumentParamConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InstrumentParamConfigRepositoryImpl implements InstrumentParamConfigRepository {

    @Autowired
    private InstrumentParamConfigDao instrumentParamConfigDao;

    @Override
    public InstrumentParamConfigDto getProbeInfo(String instrumentParamConfigNo) {

        return instrumentParamConfigDao.getProbeInfo(instrumentParamConfigNo);
    }
}

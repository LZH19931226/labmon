package com.hc.repository.impl;

import com.hc.dto.MonitorinstrumentDto;
import com.hc.infrastructure.dao.MonitorInstrumentDao;
import com.hc.repository.MonitorInstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MonitorInstrumentRepositoryImpl implements MonitorInstrumentRepository {

    @Autowired
    private MonitorInstrumentDao monitorInstrumentDao;

    @Override
    public List<MonitorinstrumentDto> selectMonitorInstrumentByEnoList(List<String> enoList) {
        return monitorInstrumentDao.selectMonitorInstrumentByEnoList(enoList);
    }
}

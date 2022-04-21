package com.hc.repository.impl;

import com.hc.dto.MonitorInstrumentDto;
import com.hc.infrastructure.dao.MonitorInstrumentDao;
import com.hc.repository.MonitorInstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 监控仪器存储库实现层
 * @author hc
 */
@Repository
public class MonitorInstrumentRepositoryImpl implements MonitorInstrumentRepository {
    @Autowired
    private MonitorInstrumentDao monitorInstrumentDao;

    /**
     * 选择监控仪器列表
     *
     * @return
     */
    @Override
    public List<MonitorInstrumentDto> selectMonitorInstrumentList() {
        return monitorInstrumentDao.selectMonitorInstrumentList();
    }
}

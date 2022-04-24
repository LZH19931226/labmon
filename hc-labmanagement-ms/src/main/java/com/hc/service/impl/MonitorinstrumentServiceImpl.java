package com.hc.service.impl;

import com.hc.dto.MonitorinstrumentDTO;
import com.hc.repository.MonitorinstrumentRepository;
import com.hc.service.MonitorinstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorinstrumentServiceImpl implements MonitorinstrumentService {

    @Autowired
    private MonitorinstrumentRepository monitorinstrumentRepository;

    /**
     * 通过sn和hospitalCode查询sn是否被占用
     *
     * @param monitorinstrumentDTO
     * @return
     */
    @Override
    public Integer selectCount(MonitorinstrumentDTO monitorinstrumentDTO) {
        return monitorinstrumentRepository.selectCount(monitorinstrumentDTO);
    }

    /**
     * 插入监控仪器信息
     *
     * @param monitorinstrumentDTO
     */
    @Override
    public void insertMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO) {
        monitorinstrumentRepository.insertMonitorinstrumentInfo(monitorinstrumentDTO);
    }
}
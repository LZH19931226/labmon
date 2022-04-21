package com.hc.repository;

import com.hc.dto.MonitorInstrumentDto;

import java.util.List;

/**
 * 监控仪器库
 * @author hc
 */
public interface MonitorInstrumentRepository {

    /**
     * 选择监控仪器列表
     * @return
     */
    List<MonitorInstrumentDto> selectMonitorInstrumentList();

}

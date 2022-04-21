package com.hc.service;

import com.hc.dto.MonitorInstrumentDto;

import java.util.List;

/**
 * 监控仪器服务
 * @author hc
 */
public interface MonitorInstrumentService {


    List<MonitorInstrumentDto> selectMonitorInstrumentList();

}

package com.hc.service;

import com.hc.dto.InstrumentMonitorInfoDto;

import java.util.List;

public interface InstrumentMonitorInfoService {

    /**
     *  查询设备监控的信息
     * @param equipmentNoList
     * @return
     */
    List<InstrumentMonitorInfoDto> selectInstrumentMonitorInfoByEqNo(List<String> equipmentNoList);
}

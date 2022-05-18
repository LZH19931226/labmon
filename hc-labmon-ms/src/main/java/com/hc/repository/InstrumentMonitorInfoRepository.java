package com.hc.repository;

import com.hc.dto.InstrumentMonitorInfoDto;

import java.util.List;

public interface InstrumentMonitorInfoRepository {

    /**
     *  查询仪器监视器信息集合
     * @param equipmentNoList 设备no集合
     * @return 仪器监视器信息集合
     */
    List<InstrumentMonitorInfoDto> selectInstrumentMonitorInfoByEqNo(List<String> equipmentNoList);
}

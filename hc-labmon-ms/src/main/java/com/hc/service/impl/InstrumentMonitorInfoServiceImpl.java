package com.hc.service.impl;

import com.hc.dto.InstrumentMonitorInfoDto;
import com.hc.repository.InstrumentMonitorInfoRepository;
import com.hc.service.InstrumentMonitorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentMonitorInfoServiceImpl implements InstrumentMonitorInfoService {

    @Autowired
    private InstrumentMonitorInfoRepository instrumentMonitorInfoRepository;

    /**
     * 查询设备监控的信息
     * @param equipmentNoList
     * @return
     */
    @Override
    public List<InstrumentMonitorInfoDto> selectInstrumentMonitorInfoByEqNo(List<String> equipmentNoList) {
        List<InstrumentMonitorInfoDto> instrumentMonitorInfoDtoList = instrumentMonitorInfoRepository.selectInstrumentMonitorInfoByEqNo(equipmentNoList);
        return instrumentMonitorInfoDtoList;
    }
}

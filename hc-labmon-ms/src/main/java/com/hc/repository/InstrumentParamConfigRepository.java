package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.dto.InstrumentParamConfigDto;

import java.util.List;
import java.util.Map;

public interface InstrumentParamConfigRepository extends IService<InstrumentParamConfigDto> {
    InstrumentParamConfigDto getProbeInfo(String instrumentParamConfigNo);

    Map<String, List<InstrumentParamConfigDto>> getInstrumentParamConfigByENo(String equipmentNo);

    List<InstrumentParamConfigDto> getInstrumentParamConfigByENoList(List<String> eNoList);

    List<InstrumentParamConfigDto> getInstrumentParamConfigByCode(String hospitalCode);

    List<InstrumentParamConfigDto> batchGetProbeInfo(List<String> configParamNo);


    List<InstrumentParamConfigDto> getInstrumentParamConfigInfo(String equipmentNo);

    void batchUpdateProbeAlarmState(String warningPhone, String equipmentNo);
}

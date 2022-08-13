package com.hc.service;

import com.hc.dto.InstrumentParamConfigDto;

import java.util.List;
import java.util.Map;

public interface InstrumentParamConfigService {

    Map<String, List<InstrumentParamConfigDto>> getInstrumentParamConfigByENo(String equipmentNo);

    List<InstrumentParamConfigDto> getInstrumentParamConfigByENoList(List<String> eNoList);


    List<InstrumentParamConfigDto> getInstrumentParamConfigByCode(String hospitalCode);

    List<InstrumentParamConfigDto> batchGetProbeInfo(List<String> configParamNo);


    void updateProbeAlarmState(String instrumentParamConfigNo, String warningPhone);

    List<InstrumentParamConfigDto> getInstrumentParamConfigInfo(String equipmentNo);

    void batchUpdateProbeAlarmState(String warningPhone, List<String> list);

}

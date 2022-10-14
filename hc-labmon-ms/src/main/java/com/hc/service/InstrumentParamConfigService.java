package com.hc.service;

import com.hc.dto.InstrumentParamConfigDto;

import java.util.List;
import java.util.Map;

public interface InstrumentParamConfigService {

    Map<String, List<InstrumentParamConfigDto>> getInstrumentParamConfigByENo(String equipmentNo);

    List<InstrumentParamConfigDto> getInstrumentParamConfigByENoList(List<String> eNoList);


    List<InstrumentParamConfigDto> getInstrumentParamConfigByCode(String hospitalCode);

    List<InstrumentParamConfigDto> batchGetProbeInfo(List<String> configParamNo);

    List<InstrumentParamConfigDto> getAll();

    List<InstrumentParamConfigDto> getInstrumentParamConfigByCodeAndTypeId(String hospitalCode, String equipmentTypeId);
}

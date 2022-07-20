package com.hc.service;

import com.hc.dto.InstrumentParamConfigDto;

import java.util.List;
import java.util.Map;

public interface InstrumentParamConfigService {

    Map<String, List<InstrumentParamConfigDto>> getInstrumentParamConfigByENo(String equipmentNo);
}

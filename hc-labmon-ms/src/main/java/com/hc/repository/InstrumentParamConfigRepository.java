package com.hc.repository;

import com.hc.dto.InstrumentParamConfigDto;

public interface InstrumentParamConfigRepository {
    InstrumentParamConfigDto getProbeInfo(String instrumentParamConfigNo);
}

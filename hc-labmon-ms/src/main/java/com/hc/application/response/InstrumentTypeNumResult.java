package com.hc.application.response;

import com.hc.dto.InstrumentTypeNumDto;
import lombok.Data;

import java.util.List;

@Data
public class InstrumentTypeNumResult {

    private Long totalNum;

    private List<InstrumentTypeNumDto> instrumentTypeNumDtoList;
}

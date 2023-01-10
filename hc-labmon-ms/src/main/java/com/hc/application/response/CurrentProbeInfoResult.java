package com.hc.application.response;

import com.hc.dto.ProbeCurrentInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class CurrentProbeInfoResult {

    private Long normalNum;

    private Long totalNum;

    private Long abnormalNum;

    private Long timeoutNum;

    private List<ProbeCurrentInfoDto> probeCurrentInfoDtoList;
}

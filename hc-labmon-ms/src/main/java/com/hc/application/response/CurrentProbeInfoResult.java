package com.hc.application.response;

import com.hc.dto.ProbeCurrentInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class CurrentProbeInfoResult {

    private Integer normalNum;

    private Integer totalNum;

    private Integer abnormalNum;

    private Integer timeoutNum;

    private List<ProbeCurrentInfoDto> probeCurrentInfoDtoList;
}

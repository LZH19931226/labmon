package com.hc.application.response;

import com.hc.dto.ProbeCurrentInfoDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CurrentProbeInfoResult {

    private Integer normalNum = 0;

    private Integer totalNum = 0;

    private Integer abnormalNum =0;

    private Integer timeoutNum =0;

    private List<ProbeCurrentInfoDto> probeCurrentInfoDtoList = new ArrayList<>();
}

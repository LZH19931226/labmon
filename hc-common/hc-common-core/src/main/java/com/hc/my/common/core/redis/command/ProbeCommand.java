package com.hc.my.common.core.redis.command;

import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class ProbeCommand {

    private String hospitalCode;

    private List<String>  instrumentNo;

    private List<InstrumentInfoDto> instrumentInfoDtoList;
}

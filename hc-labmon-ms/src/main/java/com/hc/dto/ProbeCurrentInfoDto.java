package com.hc.dto;

import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProbeCurrentInfoDto {

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * sn号
     */
    private String sn;

    /**
     * 最新的上传时间
     */
    private Date inputTime;

    /**
     * 探头集合
     */
    private List<ProbeInfoDto> probeInfoDtoList;
}

package com.hc.application.response;

import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MultiprobeTypePointInTimeDto  implements Serializable {

    private Monitorequipmentlastdata monitorequipmentlastdata;

    private String date;

    private String time;

    private List<ProbeInfoDto> probeInfoDtoList;

}

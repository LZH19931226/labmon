package com.hc.clickhouse.param;

import lombok.Data;

@Data
public class AlarmDataParam {

    private String equipmentNo;

    private String startTime;

    private String endTime;

    private Integer pageCurrent;

    private Integer pageSize;
}


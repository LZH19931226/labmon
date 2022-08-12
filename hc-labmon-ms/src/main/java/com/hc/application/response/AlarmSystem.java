package com.hc.application.response;

import lombok.Data;

import java.util.List;

@Data
public class AlarmSystem {
    /**设备名称*/
    private String equipmentName;

    /** sn*/
    private String sn;

    /**设备状态*/
    private String state;

    /** 探头信息 */
    private List<ProbeAlarmState> probeAlarmStateList;
}

package com.hc.application.response;

import lombok.Data;

import java.util.List;

@Data
public class AlarmSystem {

    /**设备名称*/
    private String equipmentName;

    /**设备no */
    private String equipmentNo;

    /** sn*/
    private String sn;

    /**设备状态*/
    private String warningSwitch;

    /** 医院id */
    private String hospitalCode;

    /** 探头信息 */
    private List<ProbeAlarmState> probeAlarmStateList;
}

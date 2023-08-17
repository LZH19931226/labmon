package com.hc.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class eqTypeAlarmNumCountDto implements Serializable {
    /** 设备类型编码 */
    private String equipmenttypeid;
    /** 设备类型名称 */
    private String equipmenttypename;
    /** 报警数量 */
    private int alarmCount;
    /** 设备类型英文名称 */
    private String equipmenttypenameUs;
    /** 设备类型繁体名称 */
    private String equipmenttypenameFt;
    /** 设备编号 */
    private String equipmentno;
    /** 设备报警时段 */
    private String alarmPeriod;

}

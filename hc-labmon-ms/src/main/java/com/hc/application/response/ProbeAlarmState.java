package com.hc.application.response;

import lombok.Data;

@Data
public class ProbeAlarmState {

    /**探头主键
     */
    private String instrumentParamConfigNo;

    /** 报警状态 */
    private String warningPhone;

    /** 英文名称 */
    private String eName;

}

package com.hc.application.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProbeAlarmState {

    /**探头主键
     */
    private String instrumentParamConfigNo;

    /** 报警状态 */
    private String warningPhone;

    /** 英文名称 */
    private String eName;

    /** 检测类型id */
    private Integer instrumentConfigId;

    private String instrumentNo;

    private String lowLimit;

    private String highLimit;

    private BigDecimal minReferenceValue;

    private BigDecimal maxReferenceValue;

}

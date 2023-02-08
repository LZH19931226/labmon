package com.hc.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InstrumentTypeNumDto implements Serializable {

    /** 仪器类型id */
    private String instrumentTypeId;

    /** 仪器类型名称 */
    private String instrumentTypeName;

    /** 数量 */
    private Long num;

    /** 占比 */
    private String percentage;
}

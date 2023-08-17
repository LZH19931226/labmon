package com.hc.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EquipmentTypeNumDto implements Serializable {

    /** 设备类型id */
    private String equipmentTypeId;

    /** 设备类型名称 */
    private String equipmentTypeName;

    /** 设备类型英文名称 */
    private String equipmentTypeNameUs;

    /** 设备类型繁体名称 */
    private String equipmentTypeNameFt;

    /** 设备数量 */
    private Long equipmentNum;

    /** 占比 */
    private String percentage;
}

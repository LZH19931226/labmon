package com.hc.dto;


import com.hc.po.MonitorEquipmentTypePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 *
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MonitorEquipmentTypeDto extends MonitorEquipmentTypePo {

    /** 设备类型id */
    private String equipmentTypeId;

    /** 设备类型名称 */
    private String equipmentTypeName;
}

package com.hc.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorEquipmentCommand {

    /** 医院编码 */
    private String hospitalCode;

    /** 设备类型编码 */
    private String equipmentTypeId;

    /** 设备名称 */
    private String equipmentName;

    /** 分页大小 */
    private Long pageSize;

    /** 当前页 */
    private Long pageCurrent;

    /** 是否可用 */
    private Long clientVisible;
}

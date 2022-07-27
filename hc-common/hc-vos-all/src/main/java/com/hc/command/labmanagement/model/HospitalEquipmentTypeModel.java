package com.hc.command.labmanagement.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 本类用于服务之间的调用
 */
@Data
@Accessors(chain = true)
public class HospitalEquipmentTypeModel {

    /**
     * 设备类型编码
     */
    private String equipmentTypeId;

    /**
     * 设备类型名称
     */
    private String equipmentTypeName;

    /**
     * 医院名称
     */
    private String hospitalName;

    /** 排序*/
    private String orderno;

}

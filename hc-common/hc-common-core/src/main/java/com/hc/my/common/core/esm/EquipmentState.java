package com.hc.my.common.core.esm;

import lombok.Data;

/**
 * 设备状态mq模型
 * @author user
 */
@Data
public class EquipmentState {

    /**
     *
     */
    private String instrumentNo;

    /**
     * 设备id
     */
    private String equipmentNo;

    /**
     * 设备状态 0为正常 1为报警
     */
    private String state;

    /**
     * 设备信息no
     */
    private String instrumentConfigNo;

    /**
     * 监控参数类型编码
     */
    private String instrumentConfigId;

    /**
     * 医院ID
     */
    private String hospitalCode;

    private String sn;

}

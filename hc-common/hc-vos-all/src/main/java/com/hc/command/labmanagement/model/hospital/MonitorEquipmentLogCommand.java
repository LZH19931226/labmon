package com.hc.command.labmanagement.model.hospital;

import lombok.Data;

@Data
public class MonitorEquipmentLogCommand {


    /**
     * 设备名称
     */
    private String equipmentName;


    /**
     * 是否可用
     */
    private Long clientVisible;

    /**
     * 医院编码
     */
    private String hospitalCode;

    /**
     * 地址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;

    /**
     * 报警开关
     */
    private String warningSwitch;

}

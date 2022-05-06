package com.hc.command.labmanagement.hospital;

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



}

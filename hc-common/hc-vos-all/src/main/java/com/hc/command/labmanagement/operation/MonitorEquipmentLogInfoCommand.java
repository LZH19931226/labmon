package com.hc.command.labmanagement.operation;

import com.hc.command.labmanagement.model.hospital.MonitorEquipmentLogCommand;
import lombok.Data;

@Data
public class MonitorEquipmentLogInfoCommand {

    private String username;
    private String hospitalCode;
    private String hospitalName;
    private String equipmentName;
    private String equipmentNo;
    private Long clientVisible;
    private String type;
    private String operationType;

    private MonitorEquipmentLogCommand  newMonitorEquipmentLogCommand;
    private MonitorEquipmentLogCommand  oldMonitorEquipmentLogCommand;

}

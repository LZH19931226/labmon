package com.hc.command.labmanagement.operation;

import com.hc.command.labmanagement.hospital.HospitalEquimentTypeCommand;
import lombok.Data;

@Data
public class HospitalEquipmentOperationLogCommand {
    private String hospitalName;
    private String username;
    private HospitalEquimentTypeCommand hospitalEquimentTypeCommand;
    private String type;
    private String OperationType;
}

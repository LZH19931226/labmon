package com.hc.command.labmanagement.operation;

import com.hc.command.labmanagement.model.hospital.HospitalEquimentTypeInfoCommand;
import lombok.Data;

@Data
public class HospitalEquipmentOperationLogCommand {
    private String hospitalCode;
    private String hospitalName;
    private String equipmentTypeId;
    private String username;
    private HospitalEquimentTypeInfoCommand newHospitalEquimentTypeCommand;
    private HospitalEquimentTypeInfoCommand oldHospitalEquimentTypeCommand;
    private String type;
    private String OperationType;
}

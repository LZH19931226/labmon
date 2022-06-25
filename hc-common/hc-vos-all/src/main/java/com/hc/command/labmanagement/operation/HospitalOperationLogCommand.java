package com.hc.command.labmanagement.operation;

import com.hc.command.labmanagement.model.hospital.HospitalCommand;
import lombok.Data;

@Data
public class HospitalOperationLogCommand {

    private String userId;
    private HospitalCommand newHospitalCommand;
    private HospitalCommand oldHospitalCommand;
    private String type;
    private String operationType;
    private String username;
}

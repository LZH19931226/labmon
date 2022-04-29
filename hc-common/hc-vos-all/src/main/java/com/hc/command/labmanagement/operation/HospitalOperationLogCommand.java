package com.hc.command.labmanagement.operation;

import com.hc.command.labmanagement.hospital.HospitalCommand;
import lombok.Data;

@Data
public class HospitalOperationLogCommand {

    private String hospitalname;
    private String usernames;
    private HospitalCommand oldHospitalCommand;
    private HospitalCommand newHospitalCommand;
    private String type;
    private String operationType;
}

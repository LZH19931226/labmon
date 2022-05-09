package com.hc.command.labmanagement.operation;

import com.hc.command.labmanagement.hospital.InstrumentparamconfigLogCommand;
import lombok.Data;

@Data
public class InstrumentParamConfigInfoCommand {
    private String username;
    private String instrumentName;
    private String equipmentName;
    private String hospitalName;
    private String instrumentNo;
    private String type;
    private String operationType;
    private InstrumentparamconfigLogCommand newInstrumentparamconfigLogCommand;
    private InstrumentparamconfigLogCommand oldInstrumentparamconfigLogCommand;
    private String instrumentparamconfigno;

}

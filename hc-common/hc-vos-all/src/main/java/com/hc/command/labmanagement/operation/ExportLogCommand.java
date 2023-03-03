package com.hc.command.labmanagement.operation;

import lombok.Data;

@Data
public class ExportLogCommand {
    private String menuName;
    private String operationType;
    private String hospitalCode;
    private String username;
    private String functionName;
    private String hospitalName;
    private String platform;
}

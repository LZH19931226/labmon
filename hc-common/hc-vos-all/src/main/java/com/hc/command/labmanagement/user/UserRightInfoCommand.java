package com.hc.command.labmanagement.user;

import lombok.Data;

@Data
public class UserRightInfoCommand {
    private String hospitalName;
    private String username;
    private UserRightLogCommand newUserRight;
    private UserRightLogCommand oldUserRight;
    private String type;
    private String operationType;
}

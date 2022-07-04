package com.hc.application.command;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarningInfoCommand {

    private String  pkid;

    private String hospitalcode;

    private String instrumentparamconfigNO;

    private String warningvalue;

    private Date inputdatetime;

    private String warningremark;

    private Integer  pushstate;

    private Integer msgflag;

    private Integer  isPhone;

    private String equipmentno;

    private Integer pageSize;

    private Integer pageCurrent;
}

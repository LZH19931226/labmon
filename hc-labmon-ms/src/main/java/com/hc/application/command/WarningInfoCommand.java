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

    private String hospitalCode;

    private String startTime;

    private String endTime;

    private String instrumentParamConfigNO;

    private String warningValue;

    private Date inputDatetime;

    private String warningRemark;

    private Integer  pushState;

    private Integer msgFlag;

    private Integer  isPhone;

    private String equipmentNo;

    private Integer pageSize;

    private Integer pageCurrent;

    private String equipmentTypeId;
}

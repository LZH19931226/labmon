package com.hc.my.common.core.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarningRecordDto {

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
}

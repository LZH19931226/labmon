package com.hc.my.common.core.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
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

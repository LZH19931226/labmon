package com.hc.my.common.core.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class InstrumentInfoDto {

    private String equipmentName;

    private String instrumentNo;

    private String instrumentName;

    private String equipmentNo;

    private Integer instrumentTypeId;  //选择几百

    private String hospitalCode;

    private String sn;
    //智能报警限制次数
    private Integer alarmTime;  //默认是3

    private Integer instrumentConfigId;  //监控参数类型  CO2  O2   甲醛 类似于这种

    private String instrumentParamConfigNO;

    private BigDecimal lowLimit;

    private BigDecimal highLimit;

    private String warningPhone;

    private String calibration;

}

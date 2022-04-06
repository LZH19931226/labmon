package com.hc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by 16956 on 2018-08-07.
 */
@Getter
@Setter
@ToString
public class InstrumentInfoModel {
    private String equipmentname;
    private String instrumentno;

    private String instrumentname;

    private String equipmentno;

    private Integer instrumenttypeid;  //选择几百
    private String hospitalcode;
    private String sn;
    //智能报警限制次数
    private Integer alarmtime;  //默认是3

    private Integer instrumentconfigid;  //监控参数类型  CO2  O2   甲醛 类似于这种

    private String instrumentparamconfigNO;

    private BigDecimal lowlimit;

    private BigDecimal highlimit;

    private String warningphone;

    private String calibration;

}

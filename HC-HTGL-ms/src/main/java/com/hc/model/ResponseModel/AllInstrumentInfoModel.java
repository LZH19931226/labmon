package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by 16956 on 2018-08-07.
 */
@ApiModel("所有探头信息模型")
@Getter
@Setter
@ToString
public class AllInstrumentInfoModel {

    private String hospitalcode;

    private String hospitalname;

    private String equipmenttypeid;

    private String equipmenttypename;

    private String equipmentno;

    private String equipmentname;

    private String instrumentparamconfigNO;

    private String instrumentno;

    private String instrumentname;

    private Integer instrumenttypeid;

    private String instrumenttypename;

    private Integer instrumentconfigid;

    private String instrumentconfigname;

    private String warningphone;

    private BigDecimal lowlimit;

    private BigDecimal highlimit;

    private Integer alarmtime;

    private String sn;

    private String channel;

    private String calibration;

    private String firsttime;
}

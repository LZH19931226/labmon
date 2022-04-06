package com.hc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 16956 on 2018-08-06.
 */
@Getter
@Setter
@ToString
@Component
public class InstrumentMonitorInfoModel {
    private Integer instrumenttypeid;

    private Integer instrumentconfigid;
    private String instrumenttypename;
    private String instrumentconfigname;

    private BigDecimal lowlimit;

    private BigDecimal highlimit;
    private Integer alarmtime;
    private String instrumentparamconfigNO;

    private String equipmentname;
    private Date pushtime;
    private String warningphone;
    private String instrumentno;



}

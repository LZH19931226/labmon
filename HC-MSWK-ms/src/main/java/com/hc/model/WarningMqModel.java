package com.hc.model;

import com.hc.po.Monitorinstrument;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by 16956 on 2018-08-20.
 */
@Getter
@Setter
@ToString
public class WarningMqModel {
    private String currrentData;
    private String currentData1;
    private Monitorinstrument monitorinstrument;
    private Date date;
    private Integer instrumentconfigid;
    private String unit;
}

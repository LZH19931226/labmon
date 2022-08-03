package com.hc.my.common.core.domain;

import lombok.Data;

@Data
public class MonitorinstrumentDo {

    private String instrumentno;

    private String instrumentname;

    private String equipmentno;

    private Integer instrumenttypeid;
    private String hospitalcode;
    private String sn;
    //智能报警限制次数
    private Integer alarmtime;
    private String channel;

}

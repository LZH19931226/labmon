package com.hc.my.common.core.domain;

import lombok.Data;

import java.util.Date;

/**
 * 存储服务到报警服务数据传输对象
 */
@Data
public class WarningAlarmDo {
    //当前值
    private String currrentData;
    //针对MT200M 需加二路温度进行判断
    private String currentData1;
    //监控参数模型
    private MonitorinstrumentDo monitorinstrument;
    //当前时间
    private Date date;
    //参数监控类型编号
    private Integer instrumentconfigid;
    //"监控类型名称"
    private String unit;
    private String sn;
    //记录一条数据的生命周期
    private String logId;
}

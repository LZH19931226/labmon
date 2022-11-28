package com.hc.my.common.core.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HarvesterDto {
    private int id;
    //中继器SN
    private String repeatersSn;
    //采集器SN
    private String harvesterSn;
    //时间戳
    private String timeStamp;
    //液位
    private String liquidLevel;
    //温度
    private String temp;
    //电池电压
    private String cellVoltage;
    //信号强度
    private String signalIntensity;
    //当前供电状态
    private String powerSupplyStatus;
    //SD卡
    private String sdCard;
    //当前时间
    private Date creatTime;


}

package com.hc.clickhouse.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName(value = "harvester")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Harvester {
    private int id;
    //中继器SN
    @TableField(value = "repeatersSn")
    private String repeatersSn;
    //采集器SN
    @TableField(value = "harvesterSn")
    private String harvesterSn;
    //时间戳
    @TableField(value = "timeStamp")
    private String timeStamp;
    //液位
    @TableField(value = "liquidLevel")
    private String liquidLevel;
    //温度
    @TableField(value = "temp")
    private String temp;
    //电池电压
    @TableField(value = "cellVoltage")
    private String cellVoltage;
    //信号强度
    @TableField(value = "signalIntensity")
    private String signalIntensity;
    //当前供电状态
    @TableField(value = "powerSupplyStatus")
    private String powerSupplyStatus;
    //SD卡
    @TableField(value = "sdCard")
    private String sdCard;
    //当前时间
    @TableField(value = "creatTime")
    private Date creatTime;


}

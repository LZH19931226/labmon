package com.hc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName(value = "monitorequipmentwarningtime")
@Data
public class MonitorEquipmentWarningTime {
    @TableId(type = IdType.AUTO)
    private Integer timeblockid;
    //设备或者设备类型id
    private String equipmentid;
    //医院id
    private String hospitalcode;
    //警报开始时间
    @JsonFormat(pattern="HH:mm")
    private Date begintime;
    //警报结束时间
    @JsonFormat(pattern="HH:mm")
    private Date endtime;
    //设备类别 TYPE:设备类型 EQ:单个设备
    private String equipmentcategory;
}

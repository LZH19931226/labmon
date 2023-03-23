package com.hc.my.common.core.redis.dto;

import lombok.Data;

@Data
public class EquipmentEnableSetDto {
    //全部设备
    private int allCount;
    //关闭设备
    private int closeCount;
    //开启设备
    private int openCount;
    //设备信息
    private SnDeviceDto snDeviceDto;
}

package com.hc.my.common.core.redis.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EquipmentEnableSetDto implements Serializable {
    //全部设备
    private int allCount;
    //关闭设备
    private int closeCount;
    //开启设备
    private int openCount;
    //设备信息
    private List<SnDeviceDto> snDeviceDtoAll =new ArrayList<>();
    private List<SnDeviceDto> snDeviceDtoEnable = new ArrayList<>();
    private List<SnDeviceDto> snDeviceDtoNotEnable =new ArrayList<>();
}

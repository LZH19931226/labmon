package com.hc.application.response;

import lombok.Data;

@Data
public class EquipmentTypeNum {
    //超时总数
    private int timeOutCount;
    //设备总数
    private int totalCount;
    //正常设备总数
    private int normalCount;
    //异常设备总数
    private int anomalyCount;
}

package com.hc.model;

import lombok.Data;

import java.util.List;

@Data
public class TimeoutMessage {
    /**
     * 有问题设备的次数
     */
    private Integer integer;

    /**
     * 医院id
     */
    private String hospitalCode;

    /**
     * 设备类型id
     */
    private String equipmentTypeId;

    /**
     * 超时设备集合
     */
    private List<TimeoutEquipment> timeoutEquipmentList;
}

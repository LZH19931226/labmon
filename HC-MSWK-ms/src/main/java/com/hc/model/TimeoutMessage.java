package com.hc.model;

import lombok.Data;

import java.util.List;

@Data
public class TimeoutMessage {

    private Integer integer;

    private String hospitalCode;

    private String equipmentTypeId;

    private List<TimeoutEquipment> timeoutEquipmentList;
}

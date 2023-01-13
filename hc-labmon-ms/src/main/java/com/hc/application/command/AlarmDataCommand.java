package com.hc.application.command;


import lombok.Data;

@Data
public class AlarmDataCommand {

    private String equipmentNo;

    private String startTime;

    private String endTime;

    private Integer pageCurrent;

    private Integer pageSize;
}

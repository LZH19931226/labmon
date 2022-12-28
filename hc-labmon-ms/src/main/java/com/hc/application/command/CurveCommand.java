package com.hc.application.command;

import lombok.Data;

import java.util.List;

@Data
public class CurveCommand {

    private String equipmentNo;

    private String startTime;

    private String endTime;

    private String sn;

    private List<String>  instrumentConfigIdList;
}

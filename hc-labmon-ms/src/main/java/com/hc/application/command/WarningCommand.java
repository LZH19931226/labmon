package com.hc.application.command;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class WarningCommand {

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 医院code
     */
    private String hospitalCode;

    /**
     * 设备id
     */
    private String equipmentNo;

}

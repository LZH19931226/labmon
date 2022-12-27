package com.hc.application.command;

import lombok.Data;

@Data
public class ProbeCommand {
    /**
     * 医院id
     */
    private String hospitalCode;

    /**
     * 设备类型id
     */
    private String equipmentTypeId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 状态（0为正常，1为报警中）
     * */
    private String state;

    /**
     * 分页大小
     */
    private Long pageSize;

    /**
     * 当前页
     */
    private Long pageCurrent;

    /** 设备报警开关 */
    private String warningSwitch;
}

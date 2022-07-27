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
     * 分页大小
     */
    private Long pageSize;

    /**
     * 当前页
     */
    private Long pageCurrent;
}

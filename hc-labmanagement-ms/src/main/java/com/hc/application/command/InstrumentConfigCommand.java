package com.hc.application.command;

import lombok.Data;

/**
 * @author user
 */
@Data
public class InstrumentConfigCommand {

    /**
     * 监控参数类型编码
     */
    private Integer instrumentConfigId;
    /**
     * 监控参数类型名称
     */
    private String instrumentConfigName;

    /**
     * 当前分页
     */
    private Integer pageCurrent;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 类型分组
     */
    private String insGroup;
}

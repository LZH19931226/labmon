package com.hc.application.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AlarmSystemCommand {

    /** 分页大小 */
    @NotNull(message = "分页大小不能为空")
    private Integer pageSize;

    /** 当前分页 */
    @NotNull(message = "当前分页值不能为空")
    private Integer pageCurrent;

    /** 医院id */
    @NotBlank(message = "医院不能为空")
    private String hospitalCode;

    /** 设备类型id */
    @NotBlank(message = "设备类型不能为空")
    private String equipmentTypeId;

    /** 查询条件 */
    private String equipmentName;
}

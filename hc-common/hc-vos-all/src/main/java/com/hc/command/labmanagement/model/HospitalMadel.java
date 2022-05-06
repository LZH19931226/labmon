package com.hc.command.labmanagement.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 本类用于服务之间的传递
 */
@Data
public class HospitalMadel {
    /** 医院编号 */
    @ApiModelProperty(value = "医院id")
    private String hospitalCode;

    /** 医院名称 */
    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    /** 是否可用 */
    @ApiModelProperty(value = "是否禁用")
    private String isEnable;

    /** 医院全称 */
    @ApiModelProperty(value = "医院全称")
    private String hospitalFullName;

    /** 全天报警 */
    @ApiModelProperty(value = "全天报警")
    private String alwaysAlarm;

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

}

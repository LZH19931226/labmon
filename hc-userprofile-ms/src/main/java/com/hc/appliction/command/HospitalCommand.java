package com.hc.appliction.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "医院请求信息体")
public class HospitalCommand {

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

    /** 超时设置 */
    @ApiModelProperty(value = "超时设置")
    private String timeout;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    /** 分页大小 */
    @ApiModelProperty(value = "分页大小")
    private Long pageSize;

    /** 当前页 */
    @ApiModelProperty(value = "当前页")
    private Long pageCurrent;

}

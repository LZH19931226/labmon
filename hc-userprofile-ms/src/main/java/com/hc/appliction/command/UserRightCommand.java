package com.hc.appliction.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户权限命令
 * @author hc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRightCommand {

    /** 医院名称 */
    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    /** 用户名称 */
    @ApiModelProperty(value = "用户名称")
    private String username;

    /** 密码 */
    @ApiModelProperty(value = "密码")
    private String pwd;

    /** 手机号 */
    @ApiModelProperty(value = "手机号")
    private String phoneNum;

    /** 是否可用 */
    @ApiModelProperty(value = "是否可用")
    private Long isUse;

    /** 用户类型 */
    @ApiModelProperty(value = "用户类型")
    private String userType;

    /** 超时联系人 */
    @ApiModelProperty(value = "超时联系人")
    private String timeout;

    /** 报警方式 */
    @ApiModelProperty(value = "报警方式")
    private String deviceType;

    /** 超时警告 */
    @ApiModelProperty(value = "超时警告")
    private String timeoutWarning;

    /** 分页大小 */
    @ApiModelProperty(value = "分页大小")
    private Long pageSize;

    /** 当前页 */
    @ApiModelProperty(value = "当前页")
    private Long pageCurrent;
}

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

    /** 用户id */
    private String userid;

    /** 医院名称 */
    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    /** 医院编码 */
    @ApiModelProperty(value = "医院编码")
    private String hospitalCode;

    /** 用户名称 */
    @ApiModelProperty(value = "用户名称")
    private String username;

    /** 用户昵称 */
    @ApiModelProperty(value = "用户名称")
    private String nickname;

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
    @ApiModelProperty(value = "是否设置超时报警0为关闭1为开启")
    private String timeout;

    /** 推送类型 */
    @ApiModelProperty(value = "推送类型")
    private String deviceType;

    /** 超时警告 */
    @ApiModelProperty(value = "超时报警方式(空和0为电话+短信，1为电话，2为短信，3为不报警)")
    private String timeoutWarning;

    /** 报警方式 */
    @ApiModelProperty(value = "报警方式(空和0为电话+短信，1为电话，2为短信，3为不报警)")
    private String reminders;

    /** 分页大小 */
    @ApiModelProperty(value = "分页大小")
    private Long pageSize;

    /** 当前页 */
    @ApiModelProperty(value = "当前页")
    private Long pageCurrent;

    /** 最新一次登录类型 */
    @ApiModelProperty(value = "登录类型")
    private String loginType;

    /** 设备登录状况 是否是第一次登录 0为第一次登录 1为非第一次登录*/
    @ApiModelProperty(value = "设备登录状况")
    private String loginStatus;

    /** 验证码 */
    @ApiModelProperty (value = "验证码")
    private String code;

    @ApiModelProperty(value = "用户权限 1为后台用户 其他为普通用户")
    private String role;

    /** token信息 */
    private String token;

    /**
     * zh 中文 en 英文
     */
    @ApiModelProperty(value = "语种")
    private String lang;
}

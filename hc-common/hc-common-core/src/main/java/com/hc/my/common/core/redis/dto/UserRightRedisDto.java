package com.hc.my.common.core.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRightRedisDto {
    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 医院编号
     */
    private String hospitalCode;

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String pwd;

    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 是否可用
     */
    private Long isUse;

    /**
     * 电话号码
     */
    private String phoneNum;

    /**
     * DeviceToken
     */
    private String deviceToken;

    /**
     * 推送设备类型
     */
    private String deviceType;

    /**
     * 超时联系人设置
     */
    private String timeout;

    /**
     * 是否接受
     */
    private String isReceive;

    /**
     *  接受数量
     */
    private Long receiveCount;

    /**
     * 接收间隔
     */
    private Long receiveInterval;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 报警方式  空为电话和报警 1.为电话 2.短信 3.不报警
     */
    private String reminders;

    /**
     * 超时警告
     */
    private String timeoutWarning;

    /**
     * 用户权限 1为后台用户 其他为普通用户
     */
    private String role;
}

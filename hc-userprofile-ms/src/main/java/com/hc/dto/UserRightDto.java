package com.hc.dto;

import com.hc.po.UserRightPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户传输对象
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserRightDto extends UserRightPo {
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
     * 报警方式(空和0为电话+短信，1为电话，2为短信，3为不报警)
     */
    private String reminders;

    /**
     * 超时报警方式(空和0为电话+短信，1为电话，2为短信，3为不报警)
     */
    private String timeoutWarning;

    /**
     * 用户权限 1为后台用户 其他为普通用户
     */
    private String role;
}

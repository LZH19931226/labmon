package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户信息
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "userright")
public class UserRightPo {

    /**
     * 医院编号
     */
    @TableField(value = "hospitalcode")
    private String hospitalCode;

    /**
     * 用户ID
     */
    @TableId
    @TableField(value = "userid")
    private String userid;

    /**
     * 用户名称
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户密码
     */
    @TableField(value = "pwd")
    private String pwd;

    /**
     * 用户昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 是否可用
     */
    @TableField(value = "isuse")
    private Long isUse;

    /**
     * 电话号码
     */
    @TableField(value = "phonenum")
    private String phoneNum;

    /**
     * DeviceToken
     */
    @TableField(value = "devicetoken")
    private String deviceToken;

    /**
     * 推送设备类型
     */
    @TableField(value = "devicetype")
    private String deviceType;

    /**
     * 超时联系人设置
     */
    @TableField(value = "timeout")
    private String timeout;

    /**
     * 是否接受
     */
    @TableField(value = "isreceive")
    private String isReceive;

    /**
     *  接受数量
     */
    @TableField(value = "reveivecount")
    private Long receiveCount;

    /**
     * 接收间隔
     */
    @TableField(value = "receiveinterval")
    private Long receiveInterval;

    /**
     * 用户类型
     */
    @TableField(value = "usertype")
    private String userType;

    /**
     * 报警方式：""和0为手机加短信 1为电话 2为短信 3不报警
     */
    @TableField(value = "reminders")
    private String reminders;

    /**
     * 超时报警方式： ""和0为手机加短信 1为电话 2为短信 3不报警
     */
    @TableField(value = "timeoutwarning")
    private String timeoutWarning;

    /**
     * 用户权限
     */
    @TableField(value = "role")
    private String role;

    /**
     * 邮箱
     */
    @TableField(value = "mailbox")
    private String mailbox;

    /**
     * 国家信息id
     */
    @TableField(value = "national_id")
    private int nationalId;
}

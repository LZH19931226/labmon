package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "userright")
@Data
public class Userright implements Serializable {
    /**
     * 用户ID
     */
    private String userid;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String pwd;

    /**
     * 是否可用
     */
    private Boolean isuse;

    /**
     * 电话号码
     */
    private String phonenum;

    /**
     * DeviceToken
     */
    private String devicetoken;

    /**
     * 推送设备类型
     */
    private String devicetype;

    private String timeout;

    private Integer receiveinterval;

    private Integer reveivecount;

    private String isreceive;

    private String usertype;


    private String reminders;

    private String timeoutwarning;

    private static final long serialVersionUID = 1L;
}
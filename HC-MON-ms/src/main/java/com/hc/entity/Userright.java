package com.hc.entity;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "userright")
@Entity
@Data
public class Userright implements Serializable {
    /**
     * 用户ID
     */
    @Id
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

    private String usertype;

    private static final long serialVersionUID = 1L;

    @Transient//排除非持久化属性
    private  String reminders;
}
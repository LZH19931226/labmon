package com.hc.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "userright")
@Entity
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

    private static final long serialVersionUID = 1L;

    /**
     * 获取用户ID
     *
     * @return userid - 用户ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置用户ID
     *
     * @param userid 用户ID
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * 获取医院编号
     *
     * @return hospitalcode - 医院编号
     */
    public String getHospitalcode() {
        return hospitalcode;
    }

    /**
     * 设置医院编号
     *
     * @param hospitalcode 医院编号
     */
    public void setHospitalcode(String hospitalcode) {
        this.hospitalcode = hospitalcode;
    }

    /**
     * 获取用户名称
     *
     * @return username - 用户名称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名称
     *
     * @param username 用户名称
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取用户密码
     *
     * @return pwd - 用户密码
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置用户密码
     *
     * @param pwd 用户密码
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 获取是否可用
     *
     * @return isuse - 是否可用
     */
    public Boolean getIsuse() {
        return isuse;
    }

    /**
     * 设置是否可用
     *
     * @param isuse 是否可用
     */
    public void setIsuse(Boolean isuse) {
        this.isuse = isuse;
    }

    /**
     * 获取电话号码
     *
     * @return phonenum - 电话号码
     */
    public String getPhonenum() {
        return phonenum;
    }

    /**
     * 设置电话号码
     *
     * @param phonenum 电话号码
     */
    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    /**
     * 获取DeviceToken
     *
     * @return devicetoken - DeviceToken
     */
    public String getDevicetoken() {
        return devicetoken;
    }

    /**
     * 设置DeviceToken
     *
     * @param devicetoken DeviceToken
     */
    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    /**
     * 获取推送设备类型
     *
     * @return devicetype - 推送设备类型
     */
    public String getDevicetype() {
        return devicetype;
    }

    /**
     * 设置推送设备类型
     *
     * @param devicetype 推送设备类型
     */
    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public enum InnerColumn {
        userid("userid"),
        hospitalcode("hospitalcode"),
        username("username"),
        pwd("pwd"),
        isuse("isuse"),
        phonenum("phonenum"),
        devicetoken("devicetoken"),
        devicetype("devicetype");

        private final String column;

        public String value() {
            return this.column;
        }

        public String getValue() {
            return this.column;
        }

        InnerColumn(String column) {
            this.column = column;
        }

        public String desc() {
            return this.column + " DESC";
        }

        public String asc() {
            return this.column + " ASC";
        }
    }
}
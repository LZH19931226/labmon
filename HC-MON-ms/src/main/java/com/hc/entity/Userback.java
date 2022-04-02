package com.hc.entity;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "userback")
public class Userback implements Serializable {
    /**
     * 用户ID
     */
    @Id
    private String userid;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String pwd;

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

    public enum InnerColumn {
        userid("userid"),
        username("username"),
        pwd("pwd");

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
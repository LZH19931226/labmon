package com.hc.po;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "configname")
public class Configname implements Serializable {
    /**
     * 主键ID
     */
    @Id
    @Column(name = "iD")
    private Integer id;

    /**
     * 名称类型
     */
    private String devicename;

    /**
     * 前缀
     */
    private String prefixname;

    /**
     * 编号
     */
    private Integer countname;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键ID
     *
     * @return iD - 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取名称类型
     *
     * @return devicename - 名称类型
     */
    public String getDevicename() {
        return devicename;
    }

    /**
     * 设置名称类型
     *
     * @param devicename 名称类型
     */
    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    /**
     * 获取前缀
     *
     * @return prefixname - 前缀
     */
    public String getPrefixname() {
        return prefixname;
    }

    /**
     * 设置前缀
     *
     * @param prefixname 前缀
     */
    public void setPrefixname(String prefixname) {
        this.prefixname = prefixname;
    }

    /**
     * 获取编号
     *
     * @return countname - 编号
     */
    public Integer getCountname() {
        return countname;
    }

    /**
     * 设置编号
     *
     * @param countname 编号
     */
    public void setCountname(Integer countname) {
        this.countname = countname;
    }

    public enum InnerColumn {
        id("iD"),
        devicename("devicename"),
        prefixname("prefixname"),
        countname("countname");

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
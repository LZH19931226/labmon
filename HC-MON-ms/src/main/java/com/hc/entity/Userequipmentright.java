package com.hc.po;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "userequipmentright")
public class Userequipmentright implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private Integer id;

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 设备类型项目编号
     */
    private String equipmentitem;

    private static final long serialVersionUID = 1L;

    /**
     * 获取ID
     *
     * @return ID - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

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
     * 获取设备类型项目编号
     *
     * @return equipmentitem - 设备类型项目编号
     */
    public String getEquipmentitem() {
        return equipmentitem;
    }

    /**
     * 设置设备类型项目编号
     *
     * @param equipmentitem 设备类型项目编号
     */
    public void setEquipmentitem(String equipmentitem) {
        this.equipmentitem = equipmentitem;
    }

    public enum InnerColumn {
        id("ID"),
        userid("userid"),
        equipmentitem("equipmentitem");

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
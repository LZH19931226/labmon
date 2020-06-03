package com.hc.entity;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "monitorequipmenttype")
public class Monitorequipmenttype implements Serializable {
    /**
     * 设备类型编码
     */
    @Id
    private String equipmenttypeid;

    /**
     * 设备类型名称
     */
    private String equipmenttypename;

    private static final long serialVersionUID = 1L;

    /**
     * 获取设备类型编码
     *
     * @return equipmenttypeid - 设备类型编码
     */
    public String getEquipmenttypeid() {
        return equipmenttypeid;
    }

    /**
     * 设置设备类型编码
     *
     * @param equipmenttypeid 设备类型编码
     */
    public void setEquipmenttypeid(String equipmenttypeid) {
        this.equipmenttypeid = equipmenttypeid;
    }

    /**
     * 获取设备类型名称
     *
     * @return equipmenttypename - 设备类型名称
     */
    public String getEquipmenttypename() {
        return equipmenttypename;
    }

    /**
     * 设置设备类型名称
     *
     * @param equipmenttypename 设备类型名称
     */
    public void setEquipmenttypename(String equipmenttypename) {
        this.equipmenttypename = equipmenttypename;
    }

    public enum InnerColumn {
        equipmenttypeid("equipmenttypeid"),
        equipmenttypename("equipmenttypename");

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
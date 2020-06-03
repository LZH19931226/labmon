package com.hc.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "monitorequipment")
@Entity
public class Monitorequipment implements Serializable {
    /**
     * 设备编号
     */
    @Id
    private String equipmentno;

    /**
     * 设备类型编码
     */
    private String equipmenttypeid;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 设备名称
     */
    private String equipmentname;

    /**
     * 设备品牌
     */
    private String equipmentbrand;

    /**
     * 是否显示
     */
    private Boolean clientvisible;



    private static final long serialVersionUID = 1L;

    /**
     * 获取设备编号
     *
     * @return equipmentno - 设备编号
     */
    public String getEquipmentno() {
        return equipmentno;
    }

    /**
     * 设置设备编号
     *
     * @param equipmentno 设备编号
     */
    public void setEquipmentno(String equipmentno) {
        this.equipmentno = equipmentno;
    }

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
     * 获取设备名称
     *
     * @return equipmentname - 设备名称
     */
    public String getEquipmentname() {
        return equipmentname;
    }

    /**
     * 设置设备名称
     *
     * @param equipmentname 设备名称
     */
    public void setEquipmentname(String equipmentname) {
        this.equipmentname = equipmentname;
    }

    /**
     * 获取设备品牌
     *
     * @return equipmentbrand - 设备品牌
     */
    public String getEquipmentbrand() {
        return equipmentbrand;
    }

    /**
     * 设置设备品牌
     *
     * @param equipmentbrand 设备品牌
     */
    public void setEquipmentbrand(String equipmentbrand) {
        this.equipmentbrand = equipmentbrand;
    }

    /**
     * 获取是否显示
     *
     * @return clientvisible - 是否显示
     */
    public Boolean getClientvisible() {
        return clientvisible;
    }

    /**
     * 设置是否显示
     *
     * @param clientvisible 是否显示
     */
    public void setClientvisible(Boolean clientvisible) {
        this.clientvisible = clientvisible;
    }

    public enum InnerColumn {
        equipmentno("equipmentno"),
        equipmenttypeid("equipmenttypeid"),
        hospitalcode("hospitalcode"),
        equipmentname("equipmentname"),
        equipmentbrand("equipmentbrand"),
        clientvisible("clientvisible");

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
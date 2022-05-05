package com.hc.po;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "hospitalequiment")
public class Hospitalequiment  implements Serializable {
    /**
     * 设备类型编码
     */
    @Id
    private String equipmenttypeid;

    /**
     * 医院编号
     */
    @Id
    private String hospitalcode;


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
     * 是否显示
     */
    private String isvisible;

    /**
     * 排序
     */
    private Integer orderno;

    private static final long serialVersionUID = 1L;

    /**
     * 获取是否显示
     *
     * @return isvisible - 是否显示
     */
    public String getIsvisible() {
        return isvisible;
    }

    /**
     * 设置是否显示
     *
     * @param isvisible 是否显示
     */
    public void setIsvisible(String isvisible) {
        this.isvisible = isvisible;
    }

    /**
     * 获取排序
     *
     * @return orderno - 排序
     */
    public Integer getOrderno() {
        return orderno;
    }

    /**
     * 设置排序
     *
     * @param orderno 排序
     */
    public void setOrderno(Integer orderno) {
        this.orderno = orderno;
    }

    public enum InnerColumn {
        equipmenttypeid("equipmenttypeid"),
        hospitalcode("hospitalcode"),
        isvisible("isvisible"),
        orderno("orderno");

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
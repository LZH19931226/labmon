package com.hc.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "equipmenttype")
public class Equipmenttype implements Serializable {
    /**
     * 设备类型编号
     */
    @Id
    private String equipmenttypeno;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 设备类型名称
     */
    private String equipmenttypename;

    /**
     * 图片路径
     */
    private String picturefilename;

    /**
     * 是否显示
     */
    private String isvisible;

    /**
     * 排序
     */
    private Integer orderno;

    /**
     * 设备项目编号
     */
    private String equipmentitem;

    private static final long serialVersionUID = 1L;

    /**
     * 获取设备类型编号
     *
     * @return equipmenttypeno - 设备类型编号
     */
    public String getEquipmenttypeno() {
        return equipmenttypeno;
    }

    /**
     * 设置设备类型编号
     *
     * @param equipmenttypeno 设备类型编号
     */
    public void setEquipmenttypeno(String equipmenttypeno) {
        this.equipmenttypeno = equipmenttypeno;
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

    /**
     * 获取图片路径
     *
     * @return picturefilename - 图片路径
     */
    public String getPicturefilename() {
        return picturefilename;
    }

    /**
     * 设置图片路径
     *
     * @param picturefilename 图片路径
     */
    public void setPicturefilename(String picturefilename) {
        this.picturefilename = picturefilename;
    }

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

    /**
     * 获取设备项目编号
     *
     * @return equipmentitem - 设备项目编号
     */
    public String getEquipmentitem() {
        return equipmentitem;
    }

    /**
     * 设置设备项目编号
     *
     * @param equipmentitem 设备项目编号
     */
    public void setEquipmentitem(String equipmentitem) {
        this.equipmentitem = equipmentitem;
    }

    public enum InnerColumn {
        equipmenttypeno("equipmenttypeno"),
        hospitalcode("hospitalcode"),
        equipmenttypename("equipmenttypename"),
        picturefilename("picturefilename"),
        isvisible("isvisible"),
        orderno("orderno"),
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
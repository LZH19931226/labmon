package com.hc.po;


import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
public class HospitalequimentKey implements Serializable {
    /**
     * 设备类型编码
     */

    private String equipmenttypeid;

    /**
     * 医院ID
     */

    private String hospitalcode;

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
     * 获取医院ID
     *
     * @return hospitalid - 医院ID
     */
    public String getHospitalcode() {
        return hospitalcode;
    }

    /**
     * 设置医院ID
     *
     * @param hospitalid 医院ID
     */
    public void setHospitalcode(String hospitalid) {
        this.hospitalcode = hospitalid;
    }


    public HospitalequimentKey() {}

    public HospitalequimentKey(String equipmenttypeid, String hospitalcode) {
        this.equipmenttypeid = equipmenttypeid;
        this.hospitalcode = hospitalcode;
    }




}
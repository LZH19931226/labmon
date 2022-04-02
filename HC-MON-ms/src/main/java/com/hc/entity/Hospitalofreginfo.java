package com.hc.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "hospitalofreginfo")
@Entity
public class Hospitalofreginfo implements Serializable {
    /**
     * 医院编号
     */
    @Id
    private String hospitalcode;

    /**
     * 医院名称
     */
    @Column(name="hospitalname")
    private String hospitalname;

    /**
     * 是否可用
     */
    @Column(name="isenable")
    private String isenable;

    /**
     * 医院全称
     */
    @Column(name="hospitalfullname")
    private String hospitalfullname;

    /**
     * 全天报警
     */
    @Column(name="alwayalarm")
    private String alwayalarm;

    /**
     * 开始时间
     */
    @Column(name="begintime")
    private Date begintime;

    /**
     * 结束时间
     */
    @Column(name="endtime")
    private Date endtime;

    @Column(name="timeout")
    private String timeout;

//    private static final long serialVersionUID = 1L;


    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
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
     * 获取医院名称
     *
     * @return hospitalname - 医院名称
     */
    public String getHospitalname() {
        return hospitalname;
    }

    /**
     * 设置医院名称
     *
     * @param hospitalname 医院名称
     */
    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    /**
     * 获取是否可用
     *
     * @return isenable - 是否可用
     */
    public String getIsenable() {
        return isenable;
    }

    /**
     * 设置是否可用
     *
     * @param isenable 是否可用
     */
    public void setIsenable(String isenable) {
        this.isenable = isenable;
    }

    /**
     * 获取医院全称
     *
     * @return hospitalfullname - 医院全称
     */
    public String getHospitalfullname() {
        return hospitalfullname;
    }

    /**
     * 设置医院全称
     *
     * @param hospitalfullname 医院全称
     */
    public void setHospitalfullname(String hospitalfullname) {
        this.hospitalfullname = hospitalfullname;
    }

    /**
     * 获取全天报警
     *
     * @return alwayalarm - 全天报警
     */
    public String getAlwayalarm() {
        return alwayalarm;
    }

    /**
     * 设置全天报警
     *
     * @param alwayalarm 全天报警
     */
    public void setAlwayalarm(String alwayalarm) {
        this.alwayalarm = alwayalarm;
    }

    /**
     * 获取开始时间
     *
     * @return begintime - 开始时间
     */
    public Date getBegintime() {
        return begintime;
    }

    /**
     * 设置开始时间
     *
     * @param begintime 开始时间
     */
    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    /**
     * 获取结束时间
     *
     * @return endtime - 结束时间
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 设置结束时间
     *
     * @param endtime 结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public enum InnerColumn {
        hospitalcode("hospitalcode"),
        hospitalname("hospitalname"),
        isenable("isenable"),
        hospitalfullname("hospitalfullname"),
        alwayalarm("alwayalarm"),
        begintime("begintime"),
        endtime("endtime"),
        timeout("timeout");

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

    @Override
    public String toString() {
        return "Hospitalofreginfo{" +
                "hospitalcode='" + hospitalcode + '\'' +
                ", hospitalname='" + hospitalname + '\'' +
                ", isenable='" + isenable + '\'' +
                ", hospitalfullname='" + hospitalfullname + '\'' +
                ", alwayalarm='" + alwayalarm + '\'' +
                ", begintime=" + begintime +
                ", endtime=" + endtime +
                ", timeout='" + timeout + '\'' +
                '}';
    }
}
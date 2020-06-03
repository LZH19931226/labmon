package com.hc.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "monitorupsrecord")
public class Monitorupsrecord implements Serializable {
    /**
     * pkid
     */
    @Id
    private Integer pkid;

    /**
     * 探头编号
     */
    @Column(name = "instrumentNo")
    private String instrumentno;

    /**
     * 电源状态
     */
    private String ups;

    /**
     * 记录时间
     */
    private Date inputdatetime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取pkid
     *
     * @return pkid - pkid
     */
    public Integer getPkid() {
        return pkid;
    }

    /**
     * 设置pkid
     *
     * @param pkid pkid
     */
    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    /**
     * 获取探头编号
     *
     * @return instrumentNo - 探头编号
     */
    public String getInstrumentno() {
        return instrumentno;
    }

    /**
     * 设置探头编号
     *
     * @param instrumentno 探头编号
     */
    public void setInstrumentno(String instrumentno) {
        this.instrumentno = instrumentno;
    }

    /**
     * 获取电源状态
     *
     * @return ups - 电源状态
     */
    public String getUps() {
        return ups;
    }

    /**
     * 设置电源状态
     *
     * @param ups 电源状态
     */
    public void setUps(String ups) {
        this.ups = ups;
    }

    /**
     * 获取记录时间
     *
     * @return inputdatetime - 记录时间
     */
    public Date getInputdatetime() {
        return inputdatetime;
    }

    /**
     * 设置记录时间
     *
     * @param inputdatetime 记录时间
     */
    public void setInputdatetime(Date inputdatetime) {
        this.inputdatetime = inputdatetime;
    }

    public enum InnerColumn {
        pkid("pkid"),
        instrumentno("instrumentNo"),
        ups("ups"),
        inputdatetime("inputdatetime");

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
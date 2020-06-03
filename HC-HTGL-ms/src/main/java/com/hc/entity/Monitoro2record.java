package com.hc.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "monitoro2record")
public class Monitoro2record implements Serializable {
    /**
     * pkid
     */
    @Id
    private Integer pkid;

    /**
     * 探头编号
     */
    private String instrumentno;

    /**
     * O2浓度
     */
    private String o2;

    /**
     * 记录时间
     */
    private Date inputfatetime;

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
     * @return instrumentno - 探头编号
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
     * 获取O2浓度
     *
     * @return o2 - O2浓度
     */
    public String getO2() {
        return o2;
    }

    /**
     * 设置O2浓度
     *
     * @param o2 O2浓度
     */
    public void setO2(String o2) {
        this.o2 = o2;
    }

    /**
     * 获取记录时间
     *
     * @return inputfatetime - 记录时间
     */
    public Date getInputfatetime() {
        return inputfatetime;
    }

    /**
     * 设置记录时间
     *
     * @param inputfatetime 记录时间
     */
    public void setInputfatetime(Date inputfatetime) {
        this.inputfatetime = inputfatetime;
    }

    public enum InnerColumn {
        pkid("pkid"),
        instrumentno("instrumentno"),
        o2("o2"),
        inputfatetime("inputfatetime");

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
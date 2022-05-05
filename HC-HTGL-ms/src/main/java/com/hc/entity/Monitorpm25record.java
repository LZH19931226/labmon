package com.hc.po;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "monitorpm25record")
public class Monitorpm25record implements Serializable {
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
     * PM2.5
     */
    private String pm25;

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
     * 获取PM2.5
     *
     * @return pm25 - PM2.5
     */
    public String getPm25() {
        return pm25;
    }

    /**
     * 设置PM2.5
     *
     * @param pm25 PM2.5
     */
    public void setPm25(String pm25) {
        this.pm25 = pm25;
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
        instrumentno("instrumentno"),
        pm25("pm25"),
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
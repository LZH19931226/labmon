package com.hc.po;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "monitordoorstaterecord")
public class Monitordoorstaterecord implements Serializable {
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
     * 开门状态
     */
    private String doorstate;

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
     * 获取开门状态
     *
     * @return doorstate - 开门状态
     */
    public String getDoorstate() {
        return doorstate;
    }

    /**
     * 设置开门状态
     *
     * @param doorstate 开门状态
     */
    public void setDoorstate(String doorstate) {
        this.doorstate = doorstate;
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
        doorstate("doorstate"),
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
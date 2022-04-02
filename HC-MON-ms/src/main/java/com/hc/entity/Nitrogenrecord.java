package com.hc.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "nitrogenrecord")
public class Nitrogenrecord implements Serializable {
    /**
     * RecordID
     */
    @Id
    private Integer recordid;

    /**
     * 记录时间
     */
    private Date recirdtime;

    /**
     * 操作类型
     */
    private Integer optype;

    /**
     * 设备编号
     */
    private String equipmentno;

    /**
     * 操作员1
     */
    private String userid1;

    /**
     * 操作员2
     */
    private String userid2;

    /**
     * 开锁成功
     */
    private Boolean ntlocksuccess;

    private static final long serialVersionUID = 1L;

    /**
     * 获取RecordID
     *
     * @return recordid - RecordID
     */
    public Integer getRecordid() {
        return recordid;
    }

    /**
     * 设置RecordID
     *
     * @param recordid RecordID
     */
    public void setRecordid(Integer recordid) {
        this.recordid = recordid;
    }

    /**
     * 获取记录时间
     *
     * @return recirdtime - 记录时间
     */
    public Date getRecirdtime() {
        return recirdtime;
    }

    /**
     * 设置记录时间
     *
     * @param recirdtime 记录时间
     */
    public void setRecirdtime(Date recirdtime) {
        this.recirdtime = recirdtime;
    }

    /**
     * 获取操作类型
     *
     * @return optype - 操作类型
     */
    public Integer getOptype() {
        return optype;
    }

    /**
     * 设置操作类型
     *
     * @param optype 操作类型
     */
    public void setOptype(Integer optype) {
        this.optype = optype;
    }

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
     * 获取操作员1
     *
     * @return userid1 - 操作员1
     */
    public String getUserid1() {
        return userid1;
    }

    /**
     * 设置操作员1
     *
     * @param userid1 操作员1
     */
    public void setUserid1(String userid1) {
        this.userid1 = userid1;
    }

    /**
     * 获取操作员2
     *
     * @return userid2 - 操作员2
     */
    public String getUserid2() {
        return userid2;
    }

    /**
     * 设置操作员2
     *
     * @param userid2 操作员2
     */
    public void setUserid2(String userid2) {
        this.userid2 = userid2;
    }

    /**
     * 获取开锁成功
     *
     * @return ntlocksuccess - 开锁成功
     */
    public Boolean getNtlocksuccess() {
        return ntlocksuccess;
    }

    /**
     * 设置开锁成功
     *
     * @param ntlocksuccess 开锁成功
     */
    public void setNtlocksuccess(Boolean ntlocksuccess) {
        this.ntlocksuccess = ntlocksuccess;
    }

    public enum InnerColumn {
        recordid("recordid"),
        recirdtime("recirdtime"),
        optype("optype"),
        equipmentno("equipmentno"),
        userid1("userid1"),
        userid2("userid2"),
        ntlocksuccess("ntlocksuccess");

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
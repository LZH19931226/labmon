package com.hc.po;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "instrumentmonitor")
public class Instrumentmonitor implements Serializable {
    /**
     * 监控参数类型编码
     */
    @Id
    private Integer instrumentconfigid;

    /**
     * 探头类型编码
     */
    @Id
    private Integer instrumenttypeid;


    /**
     * 获取监控参数类型编码
     *
     * @return instrumentconfigid - 监控参数类型编码
     */
    public Integer getInstrumentconfigid() {
        return instrumentconfigid;
    }

    /**
     * 设置监控参数类型编码
     *
     * @param instrumentconfigid 监控参数类型编码
     */
    public void setInstrumentconfigid(Integer instrumentconfigid) {
        this.instrumentconfigid = instrumentconfigid;
    }

    /**
     * 获取探头类型编码
     *
     * @return instrumenttypeid - 探头类型编码
     */
    public Integer getInstrumenttypeid() {
        return instrumenttypeid;
    }

    /**
     * 设置探头类型编码
     *
     * @param instrumenttypeid 探头类型编码
     */
    public void setInstrumenttypeid(Integer instrumenttypeid) {
        this.instrumenttypeid = instrumenttypeid;
    }
    /**
     * 最低限值
     */
    private BigDecimal lowlimit;

    /**
     * 最高限值
     */
    private BigDecimal highlimit;

    private static final long serialVersionUID = 1L;

    /**
     * 获取最低限值
     *
     * @return lowlimit - 最低限值
     */
    public BigDecimal getLowlimit() {
        return lowlimit;
    }

    /**
     * 设置最低限值
     *
     * @param lowlimit 最低限值
     */
    public void setLowlimit(BigDecimal lowlimit) {
        this.lowlimit = lowlimit;
    }

    /**
     * 获取最高限值
     *
     * @return highlimit - 最高限值
     */
    public BigDecimal getHighlimit() {
        return highlimit;
    }

    /**
     * 设置最高限值
     *
     * @param highlimit 最高限值
     */
    public void setHighlimit(BigDecimal highlimit) {
        this.highlimit = highlimit;
    }

    public enum InnerColumn {
        instrumentconfigid("instrumentconfigid"),
        instrumenttypeid("instrumenttypeid"),
        lowlimit("lowlimit"),
        highlimit("highlimit");

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
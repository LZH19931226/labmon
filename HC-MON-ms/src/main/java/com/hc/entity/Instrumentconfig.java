package com.hc.po;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "instrumentconfig")
public class Instrumentconfig implements Serializable {
    /**
     * 监控参数类型编码
     */
    @Id
    private Integer instrumentconfigid;

    /**
     * 监控参数类型名称
     */
    private String instrumentconfigname;

    private static final long serialVersionUID = 1L;

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
     * 获取监控参数类型名称
     *
     * @return instrumentconfigname - 监控参数类型名称
     */
    public String getInstrumentconfigname() {
        return instrumentconfigname;
    }

    /**
     * 设置监控参数类型名称
     *
     * @param instrumentconfigname 监控参数类型名称
     */
    public void setInstrumentconfigname(String instrumentconfigname) {
        this.instrumentconfigname = instrumentconfigname;
    }

    public enum InnerColumn {
        instrumentconfigid("instrumentconfigid"),
        instrumentconfigname("instrumentconfigname");

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
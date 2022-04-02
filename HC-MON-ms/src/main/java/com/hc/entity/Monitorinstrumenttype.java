package com.hc.entity;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "monitorinstrumenttype")
public class Monitorinstrumenttype implements Serializable {
    /**
     * 探头类型编码
     */
    @Id
    private Integer instrumenttypeid;

    /**
     * 探头类型名称
     */
    private String instrumenttypename;

    private static final long serialVersionUID = 1L;

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
     * 获取探头类型名称
     *
     * @return instrumenttypename - 探头类型名称
     */
    public String getInstrumenttypename() {
        return instrumenttypename;
    }

    /**
     * 设置探头类型名称
     *
     * @param instrumenttypename 探头类型名称
     */
    public void setInstrumenttypename(String instrumenttypename) {
        this.instrumenttypename = instrumenttypename;
    }

    public enum InnerColumn {
        instrumenttypeid("instrumenttypeid"),
        instrumenttypename("instrumenttypename");

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
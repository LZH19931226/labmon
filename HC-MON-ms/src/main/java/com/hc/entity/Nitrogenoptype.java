package com.hc.po;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "nitrogenoptype")
public class Nitrogenoptype implements Serializable {
    /**
     * 类型编码
     */
    @Id
    private Integer optype;

    /**
     * 类型名称
     */
    private String opname;

    private static final long serialVersionUID = 1L;

    /**
     * 获取类型编码
     *
     * @return optype - 类型编码
     */
    public Integer getOptype() {
        return optype;
    }

    /**
     * 设置类型编码
     *
     * @param optype 类型编码
     */
    public void setOptype(Integer optype) {
        this.optype = optype;
    }

    /**
     * 获取类型名称
     *
     * @return opname - 类型名称
     */
    public String getOpname() {
        return opname;
    }

    /**
     * 设置类型名称
     *
     * @param opname 类型名称
     */
    public void setOpname(String opname) {
        this.opname = opname;
    }

    public enum InnerColumn {
        optype("optype"),
        opname("opname");

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
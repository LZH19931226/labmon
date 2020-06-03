package com.hc.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "hospitalequiment")
@Entity
@Getter
@Setter
public class Hospitalequiment  implements Serializable {
//    /**
//     * 设备类型编码
//     */
//    @Id
//    private String equipmenttypeid;
//
//    /**
//     * 医院编号
//     */
//    @Id
//    private String hospitalcode;
    @EmbeddedId
    private HospitalequimentKey hospitalequimentKey;

    /**
     * 是否显示
     */
    private String isvisible;

    /**
     * 排序
     */
    private Integer orderno;

    private String timeout;

    private Integer timeouttime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取是否显示
     *
     * @return isvisible - 是否显示
     */
    public String getIsvisible() {
        return isvisible;
    }

    /**
     * 设置是否显示
     *
     * @param isvisible 是否显示
     */
    public void setIsvisible(String isvisible) {
        this.isvisible = isvisible;
    }

    /**
     * 获取排序
     *
     * @return orderno - 排序
     */
    public Integer getOrderno() {
        return orderno;
    }

    /**
     * 设置排序
     *
     * @param orderno 排序
     */
    public void setOrderno(Integer orderno) {
        this.orderno = orderno;
    }

    public enum InnerColumn {
        equipmenttypeid("equipmenttypeid"),
        hospitalcode("hospitalcode"),
        isvisible("isvisible"),
        orderno("orderno"),
        timeout("timeout"),
        timeouttime("timeouttime");

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
package com.hc.po;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "lowhighconfig")
public class Lowhighconfig implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "equipmentTypeName")
    private String equipmenttypename;

    private String type;

    @Column(name = "lowLimit")
    private Long lowlimit;

    @Column(name = "highLimit")
    private Long highlimit;

    private static final long serialVersionUID = 1L;

    /**
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return equipmentTypeName
     */
    public String getEquipmenttypename() {
        return equipmenttypename;
    }

    /**
     * @param equipmenttypename
     */
    public void setEquipmenttypename(String equipmenttypename) {
        this.equipmenttypename = equipmenttypename;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return lowLimit
     */
    public Long getLowlimit() {
        return lowlimit;
    }

    /**
     * @param lowlimit
     */
    public void setLowlimit(Long lowlimit) {
        this.lowlimit = lowlimit;
    }

    /**
     * @return highLimit
     */
    public Long getHighlimit() {
        return highlimit;
    }

    /**
     * @param highlimit
     */
    public void setHighlimit(Long highlimit) {
        this.highlimit = highlimit;
    }

    public enum InnerColumn {
        id("ID"),
        equipmenttypename("equipmentTypeName"),
        type("type"),
        lowlimit("lowLimit"),
        highlimit("highLimit");

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
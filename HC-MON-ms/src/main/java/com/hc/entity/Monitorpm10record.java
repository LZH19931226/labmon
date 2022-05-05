package com.hc.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "monitorpm10record")
@Getter
@Setter
@ToString
@Entity
public class Monitorpm10record implements Serializable {
    /**
     * pkid
     */
    @Id
    private String pkid;

    /**
     * 探头编号
     */
    private String equipmentno;

    /**
     * PM10
     */
    private String pm10;

    /**
     * 记录时间
     */
    private Date inputdatetime;

    private static final long serialVersionUID = 1L;


}
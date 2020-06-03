package com.hc.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "monitorcarbondioxiderecord")
@Getter
@Setter
@ToString
@Entity
public class Monitorcarbondioxiderecord implements Serializable {
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
     * CO2浓度
     */
    private String carbondioxide;

    /**
     * 记录时间
     */
    private Date inputdatetime;

    private static final long serialVersionUID = 1L;


}
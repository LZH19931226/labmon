package com.hc.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "monitorupsrecord")
@Getter
@Setter
@ToString
@Entity
public class Monitorupsrecord implements Serializable {
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
     * 电源状态
     */
    private String ups;

    /**
     * 记录时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date inputdatetime;

    private static final long serialVersionUID = 1L;


}
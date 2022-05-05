package com.hc.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "monitorinstrumenttype")
@Entity
@Getter
@Setter
@ToString
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

    private Integer alarmtime;

    private static final long serialVersionUID = 1L;


}
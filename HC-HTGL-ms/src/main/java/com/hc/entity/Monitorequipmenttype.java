package com.hc.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "monitorequipmenttype")
@Getter
@Setter
@ToString
public class Monitorequipmenttype implements Serializable {
    /**
     * 设备类型编码
     */
    @Id
    private String equipmenttypeid;

    /**
     * 设备类型名称
     */
    private String equipmenttypename;

    private static final long serialVersionUID = 1L;


}
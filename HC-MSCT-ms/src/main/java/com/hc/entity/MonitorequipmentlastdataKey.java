package com.hc.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 16956 on 2018-08-08.
 */
@Embeddable
@Getter
@Setter
@ToString
public class MonitorequipmentlastdataKey implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    private String equipmentno;

    /**
     * 记录时间
     */
    private Date inputdatetime;
}

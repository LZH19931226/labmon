package com.hc.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "monitorinstrument")
@Entity
@Getter
@Setter
@ToString
public class Monitorinstrument implements Serializable {
    @Id
    private String instrumentno;

    private String instrumentname;

    private String equipmentno;

    private Integer instrumenttypeid;
    private String hospitalcode;
    private String sn;
    //智能报警限制次数
    private Integer alarmtime;

    private String equipmenttypeid;
    @ApiModelProperty("开关量")
    private String channel;
    /**
     * 全天报警
     *  1=全天报警
     *  0=不全天报警
     */
    @Transient //排除非持久化属性
    private String alwayalarm;

    /**
     * 报警时间段
     */
    @Transient //排除非持久化属性
    List<MonitorEquipmentWarningTime> warningTimeList;

    private static final long serialVersionUID = 1L;


}
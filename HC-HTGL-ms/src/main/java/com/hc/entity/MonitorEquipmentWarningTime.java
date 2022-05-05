package com.hc.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Example;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@Table(name = "monitorequipmentwarningtime")
@Entity
public class MonitorEquipmentWarningTime implements Serializable {
    //主键
    @Id
    @Column(name = "timeblockid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeblockid;
    //设备或者设备类型id
    private String equipmentid;
    //医院id
    private String hospitalcode;
    //警报开始时间
    @JSONField(format="HH:mm")
    private Date begintime;
    //警报结束时间
    @JSONField(format="HH:mm")
    private Date endtime;
    //设备类别 TYPE:设备类型 EQ:单个设备
    private String equipmentcategory;
}

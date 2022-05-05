package com.hc.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by 15350 on 2020/5/27.
 */
@Data
@Entity
@Table(name = "repairinfo")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Repairinfo {
    @Id
    private String logid;

    private String userid;

    private String username;

    private String usertype;

    private String hospitalname;

    private String repairtype;

    private String equipmentname;

    private String equipmentsn;

    private String instrumenttype;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date repairdate;

    private String comment;

    private String equipmenttype;
    @Transient
    private String beginDate;
    @Transient
    private String endDate;




}

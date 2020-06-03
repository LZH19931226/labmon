package com.hc.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by 15350 on 2020/2/9.
 */
@Table(name = "operationrecord")
@Getter
@Setter
@ToString
@Entity
public class Operationrecord {
    @Id
    private String pkid;

    private String hospitalcode;

    private String equipmentname;

    private String updateearlymessage;

    private String updateendmessage;

    private String operationtype;

    private Date operationtime;

    private String account;

    private String operationplatform;

    private String ip;




}

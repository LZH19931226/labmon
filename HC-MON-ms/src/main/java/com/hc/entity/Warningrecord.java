package com.hc.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "warningrecord")
@Getter
@Setter
@ToString
@Entity
public class Warningrecord implements Serializable {
    /**
     * pkid
     */
    @Id
    private String pkid;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 监控参数类型编码
     */
    private String instrumentparamconfigNO;

    /**
     * 报警值
     */
    private String warningvalue;

    /**
     * 报警时间
     */
    private String inputdatetime;

    /**
     * 报警信息
     */
    private String warningremark;

    /**
     * 推送状态
     */
    private String pushstate;

    /**
     * 消息是否已读
     */
    private String msgflag;

    private static final long serialVersionUID = 1L;


    }

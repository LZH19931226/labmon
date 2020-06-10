package com.hc.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author LiuZhiHao
 * @date 2020/6/8 14:20
 * 描述: 报警备注表
 **/
@Table(name = "warningrecordinfo")
@Getter
@Setter
@ToString
@Entity
public class WarningRecordInfo {

    /**
     * pkid
     */
    @Id
    private int id;

    /**
     * 报警备注
     */
    private String info;

    /**
     * 报警主键
     */
    private String warningrecordid;


    /**
     * 创建者
     */
    private String createuser;


    /**
     * 修改者
     */
    private String updateuser;


    /**
     * 创建时间
     */
    private Date createtime;


    /**
     * 修改时间
     */
    private Date updatetime;



}

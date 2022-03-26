package com.hc.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by 15350 on 2020/2/21.
 */
@Table(name = "receiptrecord")
@Data
@Entity
public class Receiptrecord {

    @Id
    private String pkid;

    private String phonenum;

    private String sendtime;

    private String receiptcode;

    private String content;

    private String tolltype;

}

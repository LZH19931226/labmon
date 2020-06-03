package com.hc.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by 15350 on 2020/2/8.
 */
@Table(name = "sendrecord")
@Data
@Entity
public class Sendrecord {

     @Id
     private String pkid;

     private String hospitalcode;

     private String equipmentname;

     private String unit;

     private String phonenum;

     private String sendtype;

     private Date createtime;

}

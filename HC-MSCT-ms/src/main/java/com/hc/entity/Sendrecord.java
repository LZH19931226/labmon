package com.hc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created by 15350 on 2020/2/8.
 */
@TableName(value = "sendrecord")
@Data
public class Sendrecord {

     private String pkid;

     private String hospitalcode;

     private String equipmentname;

     private String unit;

     private String phonenum;

     private String sendtype;

     private Date createtime;

}

package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author LiuZhiHao
 * @date 2020/7/31 14:41
 * 描述:
 **/
@Data
public class UserScheduLing {

    private int usid;

    /**
     * 用户ID
     */
    private String userid;



    /**
     * 医院名称
     */
    private String hospitalcode;


    /**
     * 开始时间
     */
    private Date starttime;


    /**
     * 结束时间
     */
    private Date endtime;


    /**
     * 用户名称
     */
    private String username;


    /**
     * 用户手机
     */
    private String userphone;


    /**
     * 创建时间
     */
    private Date createtime;


    /**
     * 创建者
     */
    private String createuser;


    /**
     * 创建者
     */
    private String reminders;


}

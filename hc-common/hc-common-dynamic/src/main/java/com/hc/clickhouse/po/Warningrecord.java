package com.hc.clickhouse.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "warningrecord")
@Data
public class Warningrecord implements Serializable {
    /**
     *
     */
    private long id;

    /**
     * uuid
     */
    private String pkid;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 监控参数类型编码
     *
     */
    private String instrumentparamconfigno;

    /**
     * 报警值
     */
    private String warningvalue;

    /**
     * 报警时间
     */
    private Date inputdatetime;

    /**
     * 报警信息
     */
    private String warningremark;
    /**
     * 消息是否已读
     */
    private String msgflag;

    private String equipmentno;

    /** 最低值 */
    @TableField(value = "lowLimit")
    private String lowLimit;

    /** 最高值 */
    @TableField(value = "highLimit")
    private String highLimit;

    /** 报警时间段 */
    @TableField(value = "alarmTime")
    private String alarmTime;

    /** 是否全天报警 */
    private String alwayalarm;

    /** 电话通知到得用户 */
    @TableField(value = "phoneCallUser")
    private String phoneCallUser;

    /** 短信通知到得用户 */
    @TableField(value = "mailCallUser")
    private String mailCallUser;

}

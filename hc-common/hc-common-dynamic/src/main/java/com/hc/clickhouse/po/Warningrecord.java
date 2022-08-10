package com.hc.clickhouse.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
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
    private String lowLimit;

    /** 最高值 */
    private String highLimit;

    /** 报警时间段 */
    private String alarmTime;

    /** 是否全天报警 */
    private String alwayalarm;

    /** 电话通知到得用户 */
    private String phoneCallUser;

    /** 短信通知到得用户 */
    private String mailCallUser;

}

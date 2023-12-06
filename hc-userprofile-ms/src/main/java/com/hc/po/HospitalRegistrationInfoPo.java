package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 医院注册信息持久对象
 * @author user
 */
@TableName(value = "hospitalofreginfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HospitalRegistrationInfoPo implements Serializable {

    /** 医院编号 */
    @TableId
    @TableField(value = "hospitalcode")
    private String hospitalCode;

    /** 医院名称 */
    @TableField(value = "hospitalname")
    private String hospitalName;

    /** 是否可用 */
    @TableField(value = "isenable")
    private String isEnable;

    /** 医院简称 */
    @TableField(value = "hospitalfullname")
    private String hospitalFullName;

    /** 全天报警 */
    @TableField(value = "alwayalarm")
    private String alwaysAlarm;

    /** 开始时间 */
    @TableField(value = "begintime")
    private Date beginTime;

    /** 结束时间 */
    @TableField(value = "endtime")
    private Date endTime;

    /** 超时设置 */
    @TableField(value = "timeout")
    private String timeout;

    /** 修改时间 */
    @TableField(value = "update_time")
    private Date updateTime;

    /** 修改人 */
    @TableField(value = "update_by")
    private String updateBy;

    /** 报警时间间隔 */
    @TableField(value = "timeInterval")
    private  String timeInterval;

    /** 超时变红时长 */
    @TableField(value = "timeout_red_duration")
    private  String timeoutRedDuration;

    /**
     * 是否设置因子登录1为设置
     */
    private String factor;

    /**
     * 是否开启声光报警是1否0
     */
    @TableField(value = "sound_light_alarm")
    private String soundLightAlarm;

    /**
     * 语言模板
     */
    @TableField(value = "language_template")
    private String languageTemplate;


    /**
     * 时区
     */
    @TableField(value = "zone")
    private String zone;
}

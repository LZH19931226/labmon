package com.hc.my.common.core.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户存入redis的对象
 */
@Data
@Accessors(chain = true)
public class HospitalInfoDto {
    /** 医院编号 */
    private String hospitalCode;

    /** 医院名称 */
    private String hospitalName;

    /** 是否可用 */
    private String isEnable;

    /** 医院简称 */
    private String hospitalFullName;

    /** 全天报警 */
    private String alwaysAlarm;

    /** 开始时间 */
    private Date beginTime;

    /** 结束时间 */
    private Date endTime;

    /** 超时设置 */
    private String timeout;

    /** 修改时间 */
    private Date updateTime;

    /** 修改人 */
    private String updateBy;

    /**
     * 时间间隔
     */
    private String timeInterval;
}

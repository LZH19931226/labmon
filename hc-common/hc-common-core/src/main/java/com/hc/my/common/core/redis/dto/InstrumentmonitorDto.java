package com.hc.my.common.core.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors
public class InstrumentmonitorDto {
    /**
     * 探头类型名称
     */
    private String instrumenttypename;

    /**
     * 监控参数类型编码
     */
    private Integer instrumentconfigid;

    /**
     * 探头类型编码
     */
    private Integer instrumenttypeid;

    /**
     * 最低限值
     */
    private BigDecimal lowlimit;

    /**
     * 最高限值
     * */
    private BigDecimal highlimit;

    /**
     *
     */
    private BigDecimal saturation;

    /**
     * 监控参数类型名称
     */
    private String instrumentconfigname;



    /**
     * 探头id
     */
    private String instrumentno;

    /**
     * 设备名称
     */
    private String equipmentname;

    /**
     * 医院id
     */
    private String hospitalcode;

    /**
     *智能报警限制次数
     */
    private String alarmtime;

    /**
     * 探头检测信息主键
     */
    private String instrumentparamconfigno;

    /**
     * 推送时间
     */
    private Date pushtime;

    /**
     *报警推送时间
     */
    private Date warningtime;

    /**
     *校准正负值
     */
    private String calibration;

    /**
     *智能报警次数 连续几次才推送报警，条件之一
     */
    private String alwayalarm;

    /**
     * 设备id
     */
    private String equipmentno;

    /**
     * 报警电话
     */
    private String warningphone;
}

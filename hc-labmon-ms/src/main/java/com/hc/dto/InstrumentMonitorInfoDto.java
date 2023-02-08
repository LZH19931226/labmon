package com.hc.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "instrumentmonitor")
public class InstrumentMonitorInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 探头类型编码 */
    private Integer instrumenttypeid;

    /** 监控参数类型编码 */
    private Integer instrumentconfigid;

    /** 探头类型名称 */
    @TableField(exist = false)
    private String instrumenttypename;

    /** 监控参数类型名称 */
    @TableField(exist = false)
    private String instrumentconfigname;

    /** 下限值 */
    private BigDecimal lowlimit;

    /** 上限值 */
    private BigDecimal highlimit;

    /** 智能报警次数 连续几次才推送报警，条件之一 */
    @TableField(exist = false)
    private Integer alarmtime;


    /**  探头监控类型编号 */
    @TableField(exist = false)
    private String instrumentparamconfigNO;

    /** 设备名称 */
    @TableField(exist = false)
    private String equipmentname;

    /** APP推送时间 */
    @TableField(exist = false)
    private Date pushtime;

    /** 是否启用短信、电话、APP报警推送 */
    @TableField(exist = false)
    private String warningphone;

    /**  探头编号 */
    @TableField(exist = false)
    private String instrumentno;

    /** 报警推送时间 */
    @TableField(exist = false)
    private Date warningtime;

    /** 校准正负值 */
    @TableField(exist = false)
    private String calibration;

    /** 饱和值 */
    private BigDecimal saturation;

    /** 设备id */
    @TableField(exist = false)
    private String equipmentno;

    /** 医院id */
    @TableField(exist = false)
    private String hospitalcode;

    /** sn */
    @TableField(exist = false)
    private String sn;
}

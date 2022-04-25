package com.hc.application.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InstrumentparamconfigCommand {

    /**
     * 监控参数编号
     */
    @ApiModelProperty(value = "监控参数编号,修改时候使用")
    private String instrumentparamconfigno;

    /**
     * 设备编号
     */
    @ApiModelProperty(value="设备编号,新增时候使用")
    private String equipmentNo;

    /**
     * 监控参数类型编码
     */
    @ApiModelProperty(value = "监控参数类型编码")
    private Integer instrumentconfigid;
    /**
     * 探头名称
     */
    @ApiModelProperty(value = "探头名称")
    private String instrumentname;
    /**
     * 最低限值
     */
    @ApiModelProperty(value = "最低限值")
    private BigDecimal lowlimit;
    /**
     * 最高限值
     */
    @ApiModelProperty(value = "最高限值")
    private BigDecimal highlimit;
    /**
     * 探头类型编码
     */
    @ApiModelProperty(value = "探头类型编码")
    private Integer instrumenttypeid;
    /**
     * 是否启用电话/短信/App推送报警
     */
    @ApiModelProperty(value = "是否启用电话/短信/App推送报警")
    private String warningphone;

    /**
     *
     */
    @ApiModelProperty(value = "通道")
    private String channel;
    /**
     *
     */
    @ApiModelProperty(value = "报警次数")
    private Integer alarmtime;
    /**
     *
     */
    @ApiModelProperty(value = "校准正负值")
    private String calibration;

    /**
     *
     */
    @ApiModelProperty(value = "饱和值")
    private BigDecimal saturation;




    /** 下面这些用作查询使用
     * 设备类型编码
     */
    @ApiModelProperty(value="设备类型编码")
    private String equipmentTypeId;

    /**
     * 医院编号
     */
    @ApiModelProperty(value="医院编号")
    private String hospitalCode;


    /** sn */
    private String sn;


    /**
     * 分页大小
     */
    private Long pageSize;

    /**
     * 当前页
     */
    private Long pageCurrent;
}

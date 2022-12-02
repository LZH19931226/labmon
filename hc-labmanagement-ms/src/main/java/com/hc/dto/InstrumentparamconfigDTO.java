package com.hc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@ApiModel(value = "instrumentparamconfig")
@Accessors(chain = true)
public class InstrumentparamconfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 监控参数编号
     */
    @ApiModelProperty(value = "监控参数编号")
    private String instrumentparamconfigno;
    /**
     * 探头编号
     */
    @ApiModelProperty(value = "探头编号")
    private String instrumentno;
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
     * 推送消息时间
     */
    @ApiModelProperty(value = "推送消息时间")
    private Date pushtime;
    /**
     * 报警时间
     */
    @ApiModelProperty(value = "报警时间")
    private Date warningtime;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String channel;
    /**
     *报警次数
     */
    @ApiModelProperty(value = "")
    private Integer alarmtime;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String calibration;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private Date firsttime;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private BigDecimal saturation;

    /** 医院编码 */
    private String hospitalcode;

    /** 医院名称 */
    private String hospitalname;

    /** 设备类型名称 */
    private String equipmenttypename;

    /** 设备类型英文名称 */
    private String equipmenttypename_us;

    /** 设备名称 */
    private String equipmentname;

    /** sn */
    private String sn;

    /** 检测类型 */
    private String instrumentconfigname;

    /** 探头类型名称 */
    private String instrumenttypename;

    /** 探头状态 0为正常 1为报警中*/
    private String state;

    /** 单位 */
    private String unit;
}




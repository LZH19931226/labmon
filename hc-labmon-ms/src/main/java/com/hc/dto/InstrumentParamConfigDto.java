package com.hc.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "instrumentparamconfig")
@Data
public class InstrumentParamConfigDto  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 监控参数编号
     */
    @ApiModelProperty(value = "监控参数编号")
    @TableId(value = "instrumentparamconfigNO",type = IdType.INPUT)
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
    @TableField(value = "lowlimit")
    private BigDecimal lowLimit;
    /**
     * 最高限值
     */
    @ApiModelProperty(value = "最高限值")
    @TableField(value = "highlimit")
    private BigDecimal highLimit;
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

    private String channel;

    private Integer alarmtime;

    private String calibration;

    private Date firsttime;

    private BigDecimal saturation;

    /** 医院编码 */
    @TableField(exist = false)
    private String hospitalcode;

    /** 医院名称 */
    @TableField(exist = false)
    private String hospitalname;

    /** 设备类型名称 */
    @TableField(exist = false)
    private String equipmenttypename;

    /** 设备类型id */
    @TableField(exist = false)
    private String equipmenttypeid;

    /** 设备名称 */
    @TableField(exist = false)
    private String equipmentname;

    /** 设备id */
    @TableField(exist = false)
    private String equipmentno;

    /** sn */
    @TableField(exist = false)
    private String sn;

    /** 检测类型名称 */
    @TableField(exist = false)
    private String instrumentconfigname;

    /** 探头类型名称 */
    @TableField(exist = false)
    private String instrumenttypename;

    /** 探头状态 */
    private String state;

    /** 单位 */
    private String unit;
}

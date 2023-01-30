package com.hc.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@ApiModel(value = "instrumentconfig")
public class InstrumentConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 监控参数类型编码
     */
    @ApiModelProperty(value = "监控参数类型编码")
    private Integer instrumentconfigid;
    /**
     * 监控参数类型名称
     */
    @ApiModelProperty(value = "监控参数类型名称")
    private String instrumentconfigname;

    @TableField(exist = false)
    private String unit;

    /**
     * 探头类型分组
     * 同于前端展示
     */
    @TableField(value = "ins_group")
    private String insGroup;

    private BigDecimal lowlimit;

    private BigDecimal highlimit;
}



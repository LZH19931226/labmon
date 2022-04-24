package com.hc.dto;

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
@ApiModel(value = "instrumentmonitor")
public class InstrumentmonitorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 探头类型名称
     */
    @ApiModelProperty(value = "探头类型名称")
    private String instrumenttypename;

    /**
     * 监控参数类型编码
     */
    @ApiModelProperty(value = "监控参数类型编码")
    private Integer instrumentconfigid;

    /**
     * 探头类型编码
     */
    @ApiModelProperty(value = "探头类型编码")
    private Integer instrumenttypeid;

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
     *
     */
    @ApiModelProperty(value = "")
    private BigDecimal saturation;

	/**
	 * 监控参数类型名称
	 */
	@ApiModelProperty(value="监控参数类型名称")
	private String instrumentconfigname;
}



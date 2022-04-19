package com.hc.vo.equimenttype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


import java.math.BigDecimal;


/**
 * 
 * 
 * @author liuzhihao
 * @date 2022-04-18 15:27:01
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ApiModel(value = "instrumentmonitor")
public class InstrumentmonitorVo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 监控参数类型编码
	 */
		@ApiModelProperty(value="监控参数类型编码")
	private Integer instrumentconfigid;
	/**
	 * 探头类型编码
	 */
	@ApiModelProperty(value="探头类型编码")
	private Integer instrumenttypeid;
	/**
	 * 最低限值
	 */
	@ApiModelProperty(value="最低限值")
	private BigDecimal lowlimit;
	/**
	 * 最高限值
	 */
	@ApiModelProperty(value="最高限值")
	private BigDecimal highlimit;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
	private BigDecimal saturation;

}


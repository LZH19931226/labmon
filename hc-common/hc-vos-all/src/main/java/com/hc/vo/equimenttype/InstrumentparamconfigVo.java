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
@ApiModel(value = "instrumentparamconfig")
public class InstrumentparamconfigVo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 监控参数编号
	 */
		@ApiModelProperty(value="监控参数编号")
	private String instrumentparamconfigno;
	/**
	 * 探头编号
	 */
	@ApiModelProperty(value="探头编号")
	private String instrumentno;
	/**
	 * 监控参数类型编码
	 */
	@ApiModelProperty(value="监控参数类型编码")
	private Integer instrumentconfigid;
	/**
	 * 探头名称
	 */
	@ApiModelProperty(value="探头名称")
	private String instrumentname;
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
	 * 探头类型编码
	 */
	@ApiModelProperty(value="探头类型编码")
	private Integer instrumenttypeid;
	/**
	 * 是否启用电话/短信/App推送报警
	 */
	@ApiModelProperty(value="是否启用电话/短信/App推送报警")
	private String warningphone;
	/**
	 * 推送消息时间
	 */
	@ApiModelProperty(value="推送消息时间")
	private Date pushtime;
	/**
	 * 报警时间
	 */
	@ApiModelProperty(value="报警时间")
	private Date warningtime;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
	private String channel;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
	private Integer alarmtime;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
	private String calibration;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
	private Date firsttime;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
	private BigDecimal saturation;

}


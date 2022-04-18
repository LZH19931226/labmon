package com.hc.vo.equimenttype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;




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
@ApiModel(value = "monitorequipmentwarningtime")
public class MonitorequipmentwarningtimeVo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
		@ApiModelProperty(value="主键")
	private Integer timeblockid;
	/**
	 * 设备id或者设备类别id
	 */
	@ApiModelProperty(value="设备id或者设备类别id")
	private String equipmentid;
	/**
	 * 警报起始时间
	 */
	@ApiModelProperty(value="警报起始时间")
	private Date begintime;
	/**
	 * 警报结束时间
	 */
	@ApiModelProperty(value="警报结束时间")
	private Date endtime;
	/**
	 * 设备类别(TYPE:设备类型, EQ:单个设备)
	 */
	@ApiModelProperty(value="设备类别(TYPE:设备类型, EQ:单个设备)")
	private String equipmentcategory;
	/**
	 * 医院编码
	 */
	@ApiModelProperty(value="医院编码")
	private String hospitalcode;

}


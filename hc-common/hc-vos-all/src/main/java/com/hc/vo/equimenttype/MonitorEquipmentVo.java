package com.hc.vo.equimenttype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;


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
@ApiModel(value = "监控设备")
public class MonitorEquipmentVo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 设备编号
	 */
		@ApiModelProperty(value="设备编号")
	private String equipmentNo;
	/**
	 * 设备类型编码
	 */
	@ApiModelProperty(value="设备类型编码")
	private String equipmentTypeId;
	/**
	 * 医院编号
	 */
	@ApiModelProperty(value="医院编号")
	private String hospitalCode;
	/**
	 * 设备名称
	 */
	@ApiModelProperty(value="设备名称")
	private String equipmentName;
	/**
	 * 设备品牌
	 */
	@ApiModelProperty(value="设备品牌")
	private String equipmentBrand;
	/**
	 * 是否显示
	 */
	@ApiModelProperty(value="是否显示")
	private Integer clientVisible;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
	private Integer sort;
	/**
	 * 全天警报 1=开启 0=关闭
	 */
	@ApiModelProperty(value="全天警报 1=开启 0=关闭")
	private String alwaysAlarm;

}


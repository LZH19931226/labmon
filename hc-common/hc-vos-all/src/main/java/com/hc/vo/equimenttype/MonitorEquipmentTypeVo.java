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
@ApiModel(value = "监控设备类型")
public class MonitorEquipmentTypeVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	* 设备类型编码
	*/
	@ApiModelProperty(value="设备类型编码")
	private String equipmentTypeId;

	/**
	* 设备类型名称
	*/
	@ApiModelProperty(value="设备类型名称")
	private String equipmentTypeName;


}


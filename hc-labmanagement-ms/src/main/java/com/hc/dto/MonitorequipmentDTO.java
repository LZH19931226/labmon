package com.hc.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@ApiModel(value = "monitorequipment")
public class MonitorequipmentDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 设备编号
	 */
		@ApiModelProperty(value="设备编号")
        private String equipmentno;
	/**
	 * 设备类型编码
	 */
	@ApiModelProperty(value="设备类型编码")
        private String equipmenttypeid;
	/**
	 * 医院编号
	 */
	@ApiModelProperty(value="医院编号")
        private String hospitalcode;
	/**
	 * 设备名称
	 */
	@ApiModelProperty(value="设备名称")
        private String equipmentname;
	/**
	 * 设备品牌
	 */
	@ApiModelProperty(value="设备品牌")
        private String equipmentbrand;
	/**
	 * 是否显示
	 */
	@ApiModelProperty(value="是否显示")
        private Integer clientvisible;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
        private Integer sort;
	/**
	 * 全天警报 1=开启 0=关闭
	 */
	@ApiModelProperty(value="全天警报 1=开启 0=关闭")
        private String alwayalarm;

}



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
@ApiModel(value = "equipmenttype")
public class EquipmenttypeVo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 设备类型编号
	 */
		@ApiModelProperty(value="设备类型编号")
	private String equipmenttypeno;
	/**
	 * 医院编号
	 */
	@ApiModelProperty(value="医院编号")
	private String hospitalcode;
	/**
	 * 设备类型名称
	 */
	@ApiModelProperty(value="设备类型名称")
	private String equipmenttypename;
	/**
	 * 图片路径
	 */
	@ApiModelProperty(value="图片路径")
	private String picturefilename;
	/**
	 * 是否显示
	 */
	@ApiModelProperty(value="是否显示")
	private String isvisible;
	/**
	 * 排序
	 */
	@ApiModelProperty(value="排序")
	private Integer orderno;
	/**
	 * 设备项目编号
	 */
	@ApiModelProperty(value="设备项目编号")
	private String equipmentitem;

}


package com.hc.vo.equimenttype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


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
public class 	MonitorEquipmentVo  implements Serializable {
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
	private Long clientVisible;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
	private Long sort;
	/**
	 * 全天警报 1=开启 0=关闭
	 */
	@ApiModelProperty(value="全天警报 1=开启 0=关闭")
	private String alwaysAlarm;


	/** 医院名称 */
	private String  hospitalName;

	/** 设备类型名称 */
	private String equipmentTypeName;

	/** sn */
	private String sn;

	/** 监控设备 */
	private String instrumentTypeName;

	/** 报警时段 */
	private List<WarningTimeVo> warningTimeList;

	/** 仪器监控信息 */
	private MonitorinstrumenttypeVo monitorinstrumenttypeDTO;

	/** 探头编号 */
	private String instrumentno;

	/** 仪器类型id */
	private Integer instrumenttypeid;

	/** 是否可以删除 */
	private boolean deleteOrNot;

	/** 通道 */
	private String channel;

	/** 饱和值 */
	private String saturation;
}


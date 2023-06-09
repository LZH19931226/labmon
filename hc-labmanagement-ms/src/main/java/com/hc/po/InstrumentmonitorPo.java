package com.hc.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("instrumentmonitor")
public class InstrumentmonitorPo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	* 监控参数类型编码
	*/
	private Integer instrumentconfigid;

	/**
	* 探头类型编码
	*/
	private Integer instrumenttypeid;

	/**
	* 最低限值
	*/
	private BigDecimal lowlimit;

	/**
	* 最高限值
	*/
	private BigDecimal highlimit;

	/**
	* 饱和值
	*/
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private BigDecimal saturation;

	/** 通道 */
	private String channel;

	/** 单位 */
	private String unit;

	/**
	 * 样式最小值
	 */
	@TableField(value = "style_min")
	private String styleMin;

	/**
	 * 样式最大值
	 */
	@TableField(value = "style_max")
	private String styleMax;
}


package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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
@TableName("monitorequipment")
public class MonitorequipmentPo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 设备编号
	 */
	@TableId(type = IdType.AUTO)
		private String equipmentno;
	/**
	 * 设备类型编码
	 */
		private String equipmenttypeid;
	/**
	 * 医院编号
	 */
		private String hospitalcode;
	/**
	 * 设备名称
	 */
		private String equipmentname;
	/**
	 * 设备品牌
	 */
		private String equipmentbrand;
	/**
	 * 是否显示
	 */
		private Integer clientvisible;
	/**
	 * 
	 */
		private Integer sort;
	/**
	 * 全天警报 1=开启 0=关闭
	 */
		private String alwayalarm;

}


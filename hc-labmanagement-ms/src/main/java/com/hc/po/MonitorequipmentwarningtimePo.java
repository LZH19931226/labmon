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
@TableName("monitorequipmentwarningtime")
public class MonitorequipmentwarningtimePo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
		private Integer timeblockid;
	/**
	 * 设备id或者设备类别id
	 */
		private String equipmentid;
	/**
	 * 警报起始时间
	 */
		private Date begintime;
	/**
	 * 警报结束时间
	 */
		private Date endtime;
	/**
	 * 设备类别(TYPE:设备类型, EQ:单个设备)
	 */
		private String equipmentcategory;
	/**
	 * 医院编码
	 */
		private String hospitalcode;

}


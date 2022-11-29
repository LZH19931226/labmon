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
@TableName("monitorequipmenttype")
public class MonitorequipmenttypePo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 设备类型编码
	 */
	@TableId(type = IdType.AUTO)
		private String equipmenttypeid;
	/**
	 * 设备类型名称
	 */
		private String equipmenttypename;

		private String equipmenttypename_us;

}


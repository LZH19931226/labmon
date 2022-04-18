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
@TableName("monitorinstrumenttype")
public class MonitorinstrumenttypePo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 探头类型编码
	 */
	@TableId(type = IdType.AUTO)
		private Integer instrumenttypeid;
	/**
	 * 探头类型名称
	 */
		private String instrumenttypename;
	/**
	 * 智能报警限制
	 */
		private Integer alarmtime;

}


package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
@TableName("monitorinstrument")
public class MonitorinstrumentPo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
		private String instrumentno;
	/**
	 * 
	 */
		private String instrumentname;
	/**
	 * 
	 */
		private String equipmentno;
	/**
	 * 
	 */
		private Integer instrumenttypeid;
	/**
	 * 
	 */
		private String sn;
	/**
	 * 智能报警限制次数
	 */
		private Integer alarmtime;
	/**
	 * 医院编码
	 */
		private String hospitalcode;
	/**
	 * 
	 */
		private String channel;

}


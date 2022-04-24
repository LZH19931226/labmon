package com.hc.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "monitorinstrument")
public class MonitorinstrumentDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */	@TableId
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



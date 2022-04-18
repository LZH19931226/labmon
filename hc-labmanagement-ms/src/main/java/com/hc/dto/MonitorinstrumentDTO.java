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
@ApiModel(value = "monitorinstrument")
public class MonitorinstrumentDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
		@ApiModelProperty(value="")
        private String instrumentno;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
        private String instrumentname;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
        private String equipmentno;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
        private Integer instrumenttypeid;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
        private String sn;
	/**
	 * 智能报警限制次数
	 */
	@ApiModelProperty(value="智能报警限制次数")
        private Integer alarmtime;
	/**
	 * 医院编码
	 */
	@ApiModelProperty(value="医院编码")
        private String hospitalcode;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
        private String channel;

}



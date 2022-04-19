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
@ApiModel(value = "monitorinstrumenttype")
public class MonitorinstrumenttypeDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 探头类型编码
	 */
		@ApiModelProperty(value="探头类型编码")
        private Integer instrumenttypeid;
	/**
	 * 探头类型名称
	 */
	@ApiModelProperty(value="探头类型名称")
        private String instrumenttypename;
	/**
	 * 智能报警限制
	 */
	@ApiModelProperty(value="智能报警限制")
        private Integer alarmtime;

}



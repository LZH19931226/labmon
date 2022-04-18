package com.hc.vo.equimenttype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;




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
@ApiModel(value = "instrumentconfig")
public class InstrumentconfigVo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 监控参数类型编码
	 */
		@ApiModelProperty(value="监控参数类型编码")
	private Integer instrumentconfigid;
	/**
	 * 监控参数类型名称
	 */
	@ApiModelProperty(value="监控参数类型名称")
	private String instrumentconfigname;

}


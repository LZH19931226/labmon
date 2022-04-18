package com.hc.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统操作日志详细表
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@ApiModel(value = "operationlogdetail")
public class OperationlogdetailDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
		@ApiModelProperty(value="ID")
        private String detailid;
	/**
	 * ID
	 */
	@ApiModelProperty(value="ID")
        private String logid;
	/**
	 * 操作表名称
	 */
	@ApiModelProperty(value="操作表名称")
        private String filedname;
	/**
	 * 操作字段名称
	 */
	@ApiModelProperty(value="操作字段名称")
        private String filedcaption;
	/**
	 * 字段原始值
	 */
	@ApiModelProperty(value="字段原始值")
        private String filedvalueprev;
	/**
	 * 字段修改后的值
	 */
	@ApiModelProperty(value="字段修改后的值")
        private String filedvalue;
	/**
	 * 
	 */
	@ApiModelProperty(value="")
        private String comment;

}



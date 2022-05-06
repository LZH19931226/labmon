package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统操作日志详细表
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("operationlogdetail")
public class OperationlogdetailPo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
		private String detailid;
	/**
	 * ID
	 */
		private String logid;
	/**
	 * 操作表名称
	 */
		private String filedname;
	/**
	 * 操作字段名称
	 */
		private String filedcaption;
	/**
	 * 字段原始值
	 */
		private String filedvalueprev;
	/**
	 * 字段修改后的值
	 */
		private String filedvalue;
	/**
	 * 
	 */
		private String comment;

}


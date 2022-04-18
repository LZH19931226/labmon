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
 * 系统操作日志表
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("operationlog")
public class OperationlogPo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
		private String logid;
	/**
	 * 用户ID
	 */
		private String userid;
	/**
	 * 用户姓名
	 */
		private String username;
	/**
	 * 0   内部用户  1 医院用户
	 */
		private String usertype;
	/**
	 * 
	 */
		private String hospitalname;
	/**
	 * 操作类型 0 新增 1 修改 2 删除
	 */
		private String opeartiontype;
	/**
	 * 操作表
	 */
		private String tablename;
	/**
	 * 操作时间
	 */
		private Date operationtime;
	/**
	 * 0  后台管理 1 APP
	 */
		private String platform;
	/**
	 * IP地址
	 */
		private String operationip;
	/**
	 * 菜单名称
	 */
		private String functionname;
	/**
	 * 操作说明
	 */
		private String loginfo;
	/**
	 * 
	 */
		private String equipmentname;

}


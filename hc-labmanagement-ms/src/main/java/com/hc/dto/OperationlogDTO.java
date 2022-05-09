package com.hc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统操作日志表
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@ApiModel(value = "operationlog")
public class OperationlogDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	* ID
	*/
	@ApiModelProperty(value="ID")
	private String logid;
	/**
	* 用户ID
	*/
	@ApiModelProperty(value="用户ID")
	private String userid;
	/**
	* 用户姓名
	*/
	@ApiModelProperty(value="用户姓名")
	private String username;
	/**
	* 0   内部用户  1 医院用户
	*/
	@ApiModelProperty(value="0   内部用户  1 医院用户")
	private String usertype;
	/**
	*
	*/
	private String hospitalname;
	/**
	* 操作类型 0 新增 1 修改 2 删除
	*/
	@ApiModelProperty(value="操作类型 0 新增 1 修改 2 删除")
	private String opeartiontype;
	/**
	* 操作表
	*/
	@ApiModelProperty(value="操作表")
	private String tablename;
	/**
	* 操作时间
	*/
	@ApiModelProperty(value="操作时间")
	private Date operationtime;
	/**
	* 0  后台管理 1 APP
	*/
	@ApiModelProperty(value="0  后台管理 1 APP")
	private String platform;
	/**
	* IP地址
	*/
	@ApiModelProperty(value="IP地址")
	private String operationip;
	/**
	* 菜单名称
	*/
	@ApiModelProperty(value="菜单名称")
	private String functionname;
	/**
	* 操作说明
	*/
	@ApiModelProperty(value="操作说明")
	private String loginfo;
	/**
	*
	*/
	@ApiModelProperty(value="")
	private String equipmentname;

	/**
	 * 开始时间
	 */
	private Date begintime;

	/**
	 * 结束时间
	 */
	private Date endtime;
}



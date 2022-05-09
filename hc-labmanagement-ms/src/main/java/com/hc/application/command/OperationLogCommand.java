package com.hc.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OperationLogCommand {
    /**
     * ID
     */
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

    /** 分页大小 */
    private  Long pageSize;

    /** 当前分页 */
    private Long pageCurrent;

    /**
     * 开始时间
     */
    private Date begintime;

    /**
     * 结束时间
     */
    private Date endtime;
}

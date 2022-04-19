package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 医院注册信息持久对象
 * @author user
 */
@TableName(value = "hospitalofreginfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HospitalRegistrationInfoPo implements Serializable {

    /** 医院编号 */
    @TableId
    @TableField(value = "hospitalcode")
    private String hospitalCode;

    /** 医院名称 */
    @TableField(value = "hospitalname")
    private String hospitalName;

    /** 是否可用 */
    @TableField(value = "isenable")
    private String isEnable;

    /** 医院简称 */
    @TableField(value = "hospitalfullname")
    private String hospitalFullName;

    /** 全天报警 */
    @TableField(value = "alwayalarm")
    private String alwaysAlarm;

    /** 开始时间 */
    @TableField(value = "begintime")
    private Date beginTime;

    /** 结束时间 */
    @TableField(value = "endtime")
    private Date endTime;

    /** 超时设置 */
    @TableField(value = "timeout")
    private String timeout;

    /** 修改时间 */
    @TableField(value = "update_time")
    private Date updateTime;

    /** 修改人 */
    @TableField(value = "update_by")
    private String updateBy;
}

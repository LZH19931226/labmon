package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 用户排班
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "userscheduling")
public class UserSchedulingPo implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "usid")
    private Long usid;

    /**
     * 用户ID
     */
    @TableField(value = "userid")
    private String userid;

    /**
     * 医院代码
     */
    @TableField(value = "hospitalcode")
    private  String  hospitalCode;

    /**
     * 开始时间
     */
    @TableField(value = "starttime")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "endtime")
    private Date endTime;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户电话
     */
    @TableField(value = "userphone")
    private String userPhone;

    /**
     * 创建时间
     */
    @TableField(value = "createtime")
    private Date createTime;

    /**
     * 创建者
     */
    @TableField(value = "createuser")
    private String createUser;

    /**
     * 报警方式
     */
    @TableField(value = "reminders")
    private String reminders;
}

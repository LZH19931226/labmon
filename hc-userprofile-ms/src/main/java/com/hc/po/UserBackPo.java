package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 *
 * @author hc
 */
@Data
@Accessors(chain = true)
@TableName(value = "userback")
public class UserBackPo {

    private static final long serialVersionUID = 1L;

    /**用户ID*/
    @TableId
    @TableField("userid")
    private String userid;

    /**用户名称*/
    @NotNull
    @TableId("username")
    private String username;

    /**用户密码*/
    @TableField("pwd")
    private String pwd;

}

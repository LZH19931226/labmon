package com.hc.appliction.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author hc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommand {

    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    private String username;

    /** 密码 */
    @ApiModelProperty(value = "密码")
    private String pwd;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private String userid;

    @ApiModelProperty(value = "分页大小")
    private Long pageSize;

    @ApiModelProperty(value = "当前页")
    private Long pageCurrent;

    /**
     * CN表示中文 US 表示英文
     */
    @ApiModelProperty(value = "语种")
    private String lang;
}

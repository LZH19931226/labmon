package com.hc.appliction;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码 */
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String pwd;

    /** 用户id */
    private String userid;
}

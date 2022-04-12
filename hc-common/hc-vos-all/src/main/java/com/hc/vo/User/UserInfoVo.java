package com.hc.vo.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author user
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {

    /**用户id*/
    private String userid;

    /**用户名称*/
    @NotBlank(message = "账号不能为空")
    @ApiModelProperty(value = "账号")
    private String username;

    /**用户密码*/
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String pwd;
}

package com.hc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by 16956 on 2018-07-27.
 */
@Getter
@Setter
@ToString
@ApiModel("用户信息模型")
public class UserModel {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String pwd;
    @ApiModelProperty("永梦token")
    private String devicetoken;
    @ApiModelProperty("登录设备类型")
    private String devicetype;
    @ApiModelProperty("手机号")
    private String phonenum;
    @ApiModelProperty("短信验证码")
    private String code;
    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("用户id")
    private String userid;


}

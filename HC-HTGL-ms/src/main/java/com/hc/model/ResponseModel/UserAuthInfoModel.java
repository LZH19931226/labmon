package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-06.
 */
@Getter
@Setter
@ToString
@ApiModel("用户权限信息展示")
public class UserAuthInfoModel {
    @ApiModelProperty("医院名称")
    private String hospitalname;
    @ApiModelProperty("用户id")
    private String userid;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("环境")
    private String environment;
    @ApiModelProperty("培养箱")
    private String incubator;
    @ApiModelProperty("液氮罐")
    private String nitrogen;
    @ApiModelProperty("冰箱")
    private String refrigerator;
    @ApiModelProperty("操作台")
    private String operationFloor;
    @ApiModelProperty("市电")
    private String UPS;



}

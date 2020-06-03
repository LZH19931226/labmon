package com.hc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("请求分页模糊查询用户信息模型")
@Getter
@Setter
@ToString
public class PageUserModel {
    @ApiModelProperty("医院编码")
    private String hospitalcode;
    @ApiModelProperty("设备类型编码")
    private String equipmenttypeid;
    private String equipmentname;

}
package com.hc.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 15350 on 2020/4/12.
 */
@Data
public class BatchModel {

    @ApiModelProperty("医院编号")
    private String hospitalcode;
    @ApiModelProperty("设备编号   1：市电  2：培养箱 3：液氮罐 4：冰箱 5：操作台")
    private String equipmenttypeid;
    @ApiModelProperty("禁用/启用  0：禁用 1：启用")
    private String warningphone;
    @ApiModelProperty("用户名")
    private String username;
}

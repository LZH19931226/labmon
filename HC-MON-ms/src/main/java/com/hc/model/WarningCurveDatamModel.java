package com.hc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author LiuZhiHao
 * @date 2020/6/10 17:40
 * 描述:
 **/
@Data
@ApiModel("报警探头获取设备编号模型")
public class WarningCurveDatamModel {

    @ApiModelProperty("设备编号")
    private String equipmentno;
    @ApiModelProperty("探头对应的单位")
    private String instrumentconfigname;
}

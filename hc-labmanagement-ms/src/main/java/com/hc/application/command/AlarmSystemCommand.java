package com.hc.application.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AlarmSystemCommand {

    @ApiModelProperty("报警设置(1为开启，0为关闭)")
    private String warningPhone;

    @ApiModelProperty("医院id")
    private  String hospitalCode;

    @ApiModelProperty("探头主键id")
    private String instrumentParamConfigNo;

    @ApiModelProperty("检测信息id")
    private String instrumentConfigId;

    @ApiModelProperty("设备主键id（有sn）")
    private String instrumentNo;

    @ApiModelProperty("设备主键id（无sn）")
    private String equipmentNo;

    @ApiModelProperty("sn")
    private String sn;

    @ApiModelProperty("设备类型id(1.环境 2培养箱 3液氮罐 4冰箱 5操作台 6市电)")
    private String equipmentTypeId;

}

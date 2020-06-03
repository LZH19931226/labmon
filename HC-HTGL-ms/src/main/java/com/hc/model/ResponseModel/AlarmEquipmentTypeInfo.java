package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xxf on 2018/9/28.
 */
@Getter
@Setter
@ToString
public class AlarmEquipmentTypeInfo {

    @ApiModelProperty("设备类型")
    private Integer equipmenttypeid;
    @ApiModelProperty("设备总数")
    private Integer number = 0;
    @ApiModelProperty("数据超时")
    private Integer dataovertime = 0;
    @ApiModelProperty("探头异常")
    private Integer instrumentab = 0;
    @ApiModelProperty("无数据")
    private Integer datanone = 0;
    @ApiModelProperty("市电数据")
    private String type;   // 1 异常   0 正常


    //
}

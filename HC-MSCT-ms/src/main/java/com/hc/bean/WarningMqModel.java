package com.hc.bean;

import com.hc.entity.Monitorinstrument;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by 16956 on 2018-08-20.
 */
@Getter
@Setter
@ToString
public class WarningMqModel {
    @ApiModelProperty("当前值")
    private String currrentData;
    @ApiModelProperty("针对MT200M 需加二路温度进行判断")
    private String currentData1;
    @ApiModelProperty("监控参数模型")
    private Monitorinstrument monitorinstrument;
    @ApiModelProperty("当前时间")
    private Date date;
    @ApiModelProperty("参数监控类型编号")
    private Integer instrumentconfigid;
    @ApiModelProperty("监控类型名称")
    private String unit;
}

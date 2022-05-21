package com.hc.application.curvemodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SeriesDataModel {
    @ApiModelProperty("当前值")
    private List<String> date;
    @ApiModelProperty("设备名称")
    private String name;
}

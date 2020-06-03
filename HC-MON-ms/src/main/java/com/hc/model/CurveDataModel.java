package com.hc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Created by 16956 on 2018-08-02.
 */
@ApiModel("返回曲线数据格式模型")
@Getter
@Setter
@ToString
public class CurveDataModel {

    @ApiModelProperty("时间数据")
    private List<String> xaxis;
    @ApiModelProperty("设备名称模型")
    private List<String> legend;
    @ApiModelProperty("数据参数模型")
    private List<SeriesDataModel> series;
}

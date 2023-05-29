package com.hc.application.curvemodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class CurveDataModel {
    @ApiModelProperty("时间数据")
    private List<String> xaxis;

    @ApiModelProperty("设备名称模型")
    private List<String> legend;
    @ApiModelProperty("数据参数模型")
    private List<SeriesDataModel> series;


    @ApiModelProperty("y轴数据")
    private List<String> seriess;
    @ApiModelProperty("设备名称")
    private String eqName;

    /**
     * 数据集合的最大值
     */
    private String maxNum;
    /**
     * 数据集合的最小值
     */
    private String minNum;

    /**
     * 样式最小值
     */
    private String styleMin;

    /**
     * 样式最大值
     */
    private String styleMax;

    /**
     * 单位
     */
    private String unit;
}

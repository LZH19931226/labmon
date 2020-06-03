package com.hc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by 16956 on 2018-08-02.
 */
@ApiModel("返回曲线series数据模型")
@Getter
@Setter
@ToString
public class SeriesDataModel {

    @ApiModelProperty("当前值")
    private List<String> date;
    @ApiModelProperty("设备名称")
    private String name;
}

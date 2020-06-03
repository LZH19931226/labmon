package com.hc.model;

import com.hc.entity.Monitorequipmentlastdata1;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by 16956 on 2018-08-02.
 */
@Getter
@Setter
@ToString
@ApiModel("查询信息返回数据模板")
public class QueryInfoModel {
    @ApiModelProperty("设备名称")
    private String equipmentname;
    @ApiModelProperty("参数值")
    private List<Monitorequipmentlastdata1> monitorequipmentlastdata;
}

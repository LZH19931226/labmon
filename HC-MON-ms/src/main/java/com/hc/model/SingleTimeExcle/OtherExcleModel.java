package com.hc.model.SingleTimeExcle;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xxf on 2019-02-14.
 */
@ApiModel("其余设备类型导出模型")
@Getter
@Setter
@ToString
public class OtherExcleModel {
    @Excel(name = "设备名称",orderNum = "0")
    private String equipmentname;

    @Excel(name = "记录时间",orderNum = "1")
    private String inputdatetime;

    /**
     * 当前温度
     */
    @Excel(name = "温度",orderNum = "2")
    private String currenttemperature;
}

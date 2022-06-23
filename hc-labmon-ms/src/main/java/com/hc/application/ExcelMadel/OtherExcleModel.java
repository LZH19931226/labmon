package com.hc.application.ExcelMadel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * excel模型 其他
 */
@Data
public class OtherExcleModel {

    @Excel(name = "设备名称",orderNum = "0")
    private String equipmentname;

    @Excel(name = "记录时间",orderNum = "1",format = "yyyy-MM-dd HH:mm")
    private Date inputdatetime;

    /**
     * 当前温度
     */
    @Excel(name = "温度",orderNum = "2")
    private String currenttemperature;
}

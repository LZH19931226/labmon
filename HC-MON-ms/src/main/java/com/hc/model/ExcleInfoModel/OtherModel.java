package com.hc.model.ExcleInfoModel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * Created by 16956 on 2018-08-02.
 */
@ApiModel("其他设备类型导出模型")
@Getter
@Setter
@ToString
@Component
public class OtherModel {
    @Excel(name = "记录时间",orderNum = "0")
    private String inputdatetime;

    /**
     * 当前温度
     */
    @Excel(name = "温度",orderNum = "1")
    private String currenttemperature;

    /**
     * 值班人员
     */
    @Excel(name = "值班人员",orderNum = "2")
    private String userscheduling ;
}

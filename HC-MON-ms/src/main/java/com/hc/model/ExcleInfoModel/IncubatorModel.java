package com.hc.model.ExcleInfoModel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * Created by 16956 on 2018-08-02.
 */
@ApiModel("培养箱导出模型")
@Getter
@Setter
@ToString
@Component
public class IncubatorModel {
    @Excel(name = "记录时间",orderNum = "0")
    private String inputdatetime;

    /**
     * 当前温度
     */
    @Excel(name = "温度",orderNum = "1")
    private String currenttemperature;

    /**
     * 当前二氧化碳
     */
    @Excel(name = "CO2",orderNum = "2")
    private String currentcarbondioxide;

    /**
     * 当前O2
     */
    @Excel(name = "O2",orderNum = "3")
    private String currento2;
    @Excel(name = "湿度",orderNum = "4")
    private String currenthumidity;
    @Excel(name = "气体流量",orderNum = "5")
    private String currentairflow1;
    @Excel(name = "左舱室温度",orderNum = "6")
    private String currentlefttemperature;
    @Excel(name = "右舱室温度",orderNum = "7")
    private String currentrigthtemperature;
    @Excel(name = "一路温度",orderNum = "8")
    private String currenttemperature1;
    @Excel(name = "二路温度",orderNum = "9")
    private String currenttemperature2;
    @Excel(name = "三路温度",orderNum = "10")
    private String currenttemperature3;
    @Excel(name = "四路温度",orderNum = "11")
    private String currenttemperature4;
    @Excel(name = "五路温度",orderNum = "12")
    private String currenttemperature5;
    @Excel(name = "六路温度",orderNum = "13")
    private String currenttemperature6;
    @Excel(name = "七路温度",orderNum = "14")
    private String currenttemperature7;
    @Excel(name = "八路温度",orderNum = "15")
    private String currenttemperature8;
    @Excel(name = "九路温度",orderNum = "16")
    private String currenttemperature9;
    @Excel(name = "十路温度",orderNum = "17")
    private String currenttemperature10;
    @Excel(name = "左盖板温度",orderNum = "18")
    private String currentleftcovertemperature;
    @Excel(name = "左底板温度",orderNum = "19")
    private String currentleftendtemperature;
    @Excel(name = "左气流",orderNum = "20")
    private String currentleftairflow;
    @Excel(name = "右盖板温度",orderNum = "21")
    private String currentrightcovertemperature;
    @Excel(name = "右底板温度",orderNum = "22")
    private String currentrightendtemperature;
    @Excel(name = "右气流",orderNum = "23")
    private String currentrightairflow;
}

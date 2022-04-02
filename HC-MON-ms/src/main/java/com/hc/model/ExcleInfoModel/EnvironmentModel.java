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
@ApiModel("环境导出模型")
@Getter
@Setter
@ToString
@Component
public class EnvironmentModel {

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

    /**
     * 当前气流
     */
    @Excel(name = "压力",orderNum = "4")
    private String currentairflow;


    /**
     * 当前湿度
     */
    @Excel(name = "湿度",orderNum = "5")
    private String currenthumidity;

    /**
     * 当前空气质量
     */
    @Excel(name = "VOC",orderNum = "6")
    private String currentvoc;

    /**
     * 当前甲醛
     */
    @Excel(name = "甲醛",orderNum = "7")
    private String currentformaldehyde;

    /**
     * 当前PM2_5
     */
    @Excel(name = "PM2.5",orderNum = "8")
    private String currentpm25;

    /**
     * 当前PM10
     */
    @Excel(name = "PM10",orderNum = "9")
    private String currentpm10;

    /**
     * 当前PM5
     */
    @Excel(name = "PM5",orderNum = "10")
    private String currentpm5;

    /**
     * 当前PM0.5
     */
    @Excel(name = "PM0.5",orderNum = "11")
    private String currentpm05;

    /**
     * 值班人员
     */
    @Excel(name = "值班人员",orderNum = "12")
    private String userscheduling ;
}

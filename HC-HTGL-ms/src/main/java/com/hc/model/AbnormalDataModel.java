package com.hc.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by 15350 on 2020/5/31.
 */
@Data
@Component
public class AbnormalDataModel {
    @Excel(name = "医院名称", orderNum = "0")
    @ApiModelProperty("医院名称")
    private String hospitalname;
    @Excel(name = "设备名称", orderNum = "1")
    @ApiModelProperty("设备名称")
    private String equipmentname;
    @Excel(name = "监控类型", orderNum = "2")
    @ApiModelProperty("监控类型")
    private String mtName;
    @Excel(name = "SN号", orderNum = "3")
    @ApiModelProperty("SN号")
    private String sn;
    @Excel(name = "氧气探头第一次上传时间", orderNum = "4")
    @ApiModelProperty("氧气探头第一次上传时间")
    private String firsttime;
    @Excel(name = "记录时间", orderNum = "5")
    @ApiModelProperty("记录时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private String inputdatetime;
    @Excel(name = "异常类型", orderNum = "6")
    @ApiModelProperty("异常类型")
    private String abnormaltype;
    @Excel(name = "异常明细", orderNum = "7")
    @ApiModelProperty("异常明细")
    private String abnormaldetails;

}

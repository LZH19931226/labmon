package com.hc.model;

import com.hc.clickhouse.po.Warningrecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-09.
 */
@ApiModel("报警模板")
@Getter
@Setter
@ToString
public class WarningModel {
    @ApiModelProperty("pkid")
    private String pkid;
    @ApiModelProperty("医院编号")
    private String hospitalcode;
    @ApiModelProperty("类型")
    private String unit;
    @ApiModelProperty("值")
    private String value;
    @ApiModelProperty("设备名称")
    private String equipmentname;
    /**
     *探头信息id
     */
    private String instrumentparamconfigNO;

    private String instrumentConfigId;

    private String sn;

    private String logId;

    //存储ck得报警信息
    private Warningrecord warningrecord;
}

package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by xxf on 2018/9/28.
 */
@Getter
@Setter
@ToString
public class CurrentData {

    private String hospitalcode;


    private String equipmentno;

    private String currentData;
    @ApiModelProperty("监控类型")
    private String unit;

    private Date inputdatetime;
}

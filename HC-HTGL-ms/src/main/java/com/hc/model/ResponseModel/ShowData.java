package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by xxf on 2018/10/25.
 */
@Getter
@Setter
@ToString
@ApiModel("展示所有探头监控值")
public class ShowData {

    private String hospitalname;

    private String equipmentname;

    private String data;

    private String unit;

    private Date inputdatetime;
}

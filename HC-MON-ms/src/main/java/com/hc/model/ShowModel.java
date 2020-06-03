package com.hc.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by xxf on 2018/10/25.
 */
@ApiModel("展示当前数据模型")
@Getter
@Setter
@ToString
public class ShowModel {

    private Date inputdatetime;

    private String equipmentno;

    private String data;

    private String unit;

    private String hospitalname;

    private String equipmentname;
 }

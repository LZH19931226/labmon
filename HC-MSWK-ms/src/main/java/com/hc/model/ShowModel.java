package com.hc.model;

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
public class ShowModel {

    private Date inputdatetime;

    private String equipmentno;

    private String data;

    private String unit;
 }

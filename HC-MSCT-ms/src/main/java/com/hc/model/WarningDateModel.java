package com.hc.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by xxf on 2018/9/29.
 */
@Getter
@Setter
@ToString
@ApiModel("报警时间模型")
public class WarningDateModel {

    private Date date;
}

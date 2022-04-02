package com.hc.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by 16956 on 2018-08-05.
 */
@ToString
@Getter
@Setter
@ApiModel("推送状态设置参数模板")
public class PushSetModel {
    @NotNull
    private String instrumentparamconfigNO;
    private String type;




}

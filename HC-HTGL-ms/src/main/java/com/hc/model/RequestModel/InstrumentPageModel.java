package com.hc.model.RequestModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by 16956 on 2018-08-07.
 */
@Getter
@Setter
@ToString
@ApiModel("分页请求探头信息参数模型")
public class InstrumentPageModel {

    private String hospitalcode;

    private String equipmenttypeid;

    private String equipmentno;

    private String fuzzy;
    @NotNull
    private Integer pagesize;
    @NotNull
    private Integer pagenum;
}

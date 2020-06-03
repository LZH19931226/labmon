package com.hc.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-03.
 */
@Getter
@Setter
@ToString
@ApiModel("返回最新一条报警信息记录")
public class NewWarningRecord {
    private String equipmenttypeid;
    private String equipmentname;
    private String instrumentconfigname;
    private String instrumentparamconfigNO;
    private String inputdatetime;
    private String warningremark;
    private String count;



}

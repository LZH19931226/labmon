package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xxf on 2018/9/28.
 */
@ApiModel("所有设备报警大屏展示页面数据模型")
@Getter
@Setter
@ToString
public class AlarmEquipmentInfo {

    private String hospitalcode;

    private String hospitalname;

    private String equipmenttypeid;

    private String equipmentno;

    private String equipmentname;

    private String instrumentconfigname;

}

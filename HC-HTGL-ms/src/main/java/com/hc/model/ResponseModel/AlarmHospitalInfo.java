package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Created by xxf on 2018/9/28.
 */
@Getter
@Setter
@ToString
public class AlarmHospitalInfo {


    private String hospitalcode;
    private String hospitalname;
    private boolean unusual = false;   //是否异常   默认false
    @ApiModelProperty("MT100低电量个数")
    private int mt100;

    private AlarmEquipmentTypeInfo envir;  //环境
    private AlarmEquipmentTypeInfo incubator;  //培养箱
    private AlarmEquipmentTypeInfo nitrogen; //液氮罐
    private AlarmEquipmentTypeInfo icebox; //冰箱
    private AlarmEquipmentTypeInfo floor; //操作台
    private AlarmEquipmentTypeInfo ups; //市电


}

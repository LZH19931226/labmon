package com.hc.model.ResponseModel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xxf on 2018-10-30.
 */
@Getter
@Setter
@ToString
public class AlarmExporeExcleModel {

    private String hospitalcode;
    @Excel(name = "医院名称",orderNum = "0")
    private String hospitalname;
    private boolean unusual = false;   //是否异常   默认false
    @Excel(name = "环境",orderNum = "2")
    private AlarmEquipmentTypeInfo envir;  //环境
    @Excel(name = "培养箱",orderNum = "3")
    private AlarmEquipmentTypeInfo incubator;  //培养箱
    @Excel(name = "液氮罐",orderNum = "4")
    private AlarmEquipmentTypeInfo nitrogen; //液氮罐
    @Excel(name = "冰箱",orderNum = "5")
    private AlarmEquipmentTypeInfo icebox; //冰箱
    @Excel(name = "操作台",orderNum = "6")
    private AlarmEquipmentTypeInfo floor; //操作台
    @Excel(name = "市电",orderNum = "1")
    private String ups; //市电
}

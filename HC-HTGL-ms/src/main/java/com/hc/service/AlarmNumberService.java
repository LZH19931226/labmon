package com.hc.service;

import com.hc.model.ResponseModel.AlarmEquipmentTypeInfo;
import com.hc.model.ResponseModel.ShowData;

import java.util.List;

/**
 * Created by xxf on 2018/10/10.
 */
public interface AlarmNumberService {

    void equipmentType1(String hospitalname,String hospitalcode,String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo);

    void equipmentType2(String hospitalname,String hospitalcode,String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo, List list);

    void equipmentType3(String hospitalname,String hospitalcode,String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo, List list);

    void equipmentType4(String hospitalname,String hospitalcode,String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo, List list);

    void equipmentType5(String hospitalname,String hospitalcode,String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo, List list);

    void equipmentType6(String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo);

    List<ShowData> one (String hospitalname,String equipmentname,String equipmentno);

    List<ShowData> two (String hospitalname,String equipmentname,String equipmentno);

    List<ShowData> three (String hospitalname,String equipmentname,String equipmentno);

    List<ShowData> four (String hospitalname,String equipmentname,String equipmentno);

    List<ShowData> five (String hospitalname,String equipmentname,String equipmentno);

    List<ShowData> six (String hospitalname,String equipmentname,String equipmentno);

}

package com.hc.service;


import com.hc.model.EquipmentInfoModel;
import com.hc.model.InstrumentInfoModel;

/**
 * Created by 15350 on 2020/5/21.
 */
public interface UpdateRecordService {


    /**
     *
     * @param hospitalname
     * @param name
     * @param oldEquipmentInfoModel  原始数据
     * @param nowEquipmentInfoModel  当前数据
     * @param type                  操作平台 1:APP/0:后台管理
     * @param operationType         操作类型 0:新增 /2：删除/1：修改
     */
    void updateEquipmentMonitor(String equipmentname, String hospitalname, String name, EquipmentInfoModel oldEquipmentInfoModel, EquipmentInfoModel nowEquipmentInfoModel, String type, String operationType);

    /**
     *
     * @param hospitalname
     * @param name
     * @param oldInstrumentInfoModel  原始数据
     * @param nowInstrumentInfoModel  当前数据
     * @param type                  操作平台 1:APP/0:后台管理
     * @param operationType         操作类型 0:新增 /2：删除/1：修改
     */
    void updateInstrumentMonitor(String instrumentname,String equipmentname, String hospitalname, String name, InstrumentInfoModel oldInstrumentInfoModel, InstrumentInfoModel nowInstrumentInfoModel, String type, String operationType);
}

package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorinstrument;
import com.hc.entity.Monitorinstrumenttype;
import com.hc.model.RequestModel.EquipmentInfoModel;
import com.hc.model.ResponseModel.MonitorEquipmentInfoModel;
import com.hc.units.ApiResponse;

import java.util.List;

/**
 * Created by 16956 on 2018-08-07.
 */
public interface MonitorEquipmentService {

    /**
     * 添加设备
     */
    ApiResponse<String> addMonitorEquipment(EquipmentInfoModel equipmentInfoModel);

    /**
     * 添加有线设备探头
     */
    ApiResponse<String> addWiredMonitorEquipment(EquipmentInfoModel equipmentInfoModel);

    /**
     * 删除设备
     */
    ApiResponse<String> deleteMonitroEquipment(EquipmentInfoModel equipmentInfoModel);
    /**
     * 修改设备信息
     */
    ApiResponse<String> updateMonitorEquipment(EquipmentInfoModel equipmentInfoModel);
    /**
     * 分页模糊查询所有设备信息
     */
    ApiResponse<Page<MonitorEquipmentInfoModel>> selectEquipmentInfoPage(String fuzzy,String hospitalcode,String equipmenttypeid,Integer pagesize,Integer pagenum);

    /**
     * 根据设备类型编号、医院编号查询设备信息
     */
    ApiResponse<List<Monitorequipment>> selectEquipmentByCode(String hospitalcode,String equipmenttypeid);
    /**
     * 展示当前医院所有  已注册医院 MT几百 而未跟设备关联  MT几百信息
     */
    ApiResponse<List<Monitorinstrumenttype>> showInstrumentType(String hospitalcode);

    /**
     * 展示当前医院 未绑定监控设备类型  对应 SN信息
     */
    ApiResponse<List<Monitorinstrument>> showInstrumentInfo(String hospitalcode,Integer instrumenttypeid,String channel);

    /**
     *
     */

}

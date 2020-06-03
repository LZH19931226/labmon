package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.entity.Monitorequipmenttype;
import com.hc.model.RequestModel.EquipmentTypeInfoModel;
import com.hc.model.ResponseModel.HospitalEquipmentTypeInfoModel;
import com.hc.units.ApiResponse;

import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
public interface EquipmentTypeService {

    /**
     * 添加设备类型   同时可以添加设备
     */
    ApiResponse<String> addEquipmentType(EquipmentTypeInfoModel equipmentTypeInfoModel);

    /**
     * 删除当前设备类型
     */
    ApiResponse<String> deleteEquipmentType(EquipmentTypeInfoModel equipmentTypeInfoModel);

    /**
     * 修改当前设备类型
     */
    ApiResponse<String> updateEquipmentType(EquipmentTypeInfoModel equipmentTypeInfoModel);

    /**
     * 获取当前医院所有设备类型
     */
    ApiResponse<List<Monitorequipmenttype>> selectHospitalEquipment(String hospitalcode);

    /**
     * 获取所有设备类型
     */
    ApiResponse<List<Monitorequipmenttype>> selectEquipmentType();

    /**
     * 分页模糊查询所有医院设备类型信息
     * @param fuzzy
     * @param hospitalcode
     * @param pagesize
     * @param pagenum
     * @return
     */
    ApiResponse<Page<HospitalEquipmentTypeInfoModel>> showAllHospitalEquipmentTypePage(String fuzzy,String hospitalcode,Integer pagesize,Integer pagenum);
}

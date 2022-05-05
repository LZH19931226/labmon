package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.po.Monitorinstrument;
import com.hc.po.Repairinfo;
import com.hc.units.ApiResponse;

import java.util.List;

/**
 * Created by 15350 on 2020/5/27.
 */
public interface RepairinfoService {

    /**
     * 根据设备编号查询S
     * N
     */
    ApiResponse<List<Monitorinstrument>> getSnByEquipmentNo(String equipmentno);


    /**
     * 新增
     */
    ApiResponse<Repairinfo> addRepairinfo(Repairinfo repairinfo);

    /**
     * 修改
     */
    ApiResponse<String> updateRepairinfo(Repairinfo repairinfo);

    /**
     * 删除
     */
    ApiResponse<String> deleteRepairinfo(Repairinfo repairinfo);

    /**
     * 分页查询
     */
    ApiResponse<Page<Repairinfo>> selectPageInfo(int pagesize,int pagenum,String beginDate,String endDate,String hospitaname,String equipmenttype,String equipmentname,String repairtype);
}

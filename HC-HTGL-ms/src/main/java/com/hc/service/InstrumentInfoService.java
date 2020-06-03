package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.entity.Monitorinstrumenttype;
import com.hc.model.RequestModel.InstrumentInfoModel;
import com.hc.model.RequestModel.InstrumentPageModel;
import com.hc.model.ResponseModel.AllInstrumentInfoModel;
import com.hc.units.ApiResponse;

import java.util.List;

/**
 * Created by 16956 on 2018-08-07.
 */
public interface InstrumentInfoService {



    /**
     * 添加探头  有线探头
     */
    ApiResponse<String> addInstrument(InstrumentInfoModel instrumentInfoModel);
    /**
     * 添加探头监控类型
     */
    ApiResponse<String> addInstrumentParam(InstrumentInfoModel instrumentInfoModel);

    /**
     * 删除探头
     */
    ApiResponse<String> deleteInstrument(InstrumentInfoModel instrumentInfoModel);

    /**
     * 修改探头   修改名称 ： 修改对应的设备  : 修改sn号  可修改sn号  妈的如何修改sn号   改你奶奶的腿
     */
    ApiResponse<String> updateInstrument(InstrumentInfoModel instrumentInfoModel);


    /**
     *   分页模糊查询所有探头类型
     */
    ApiResponse<Page<AllInstrumentInfoModel>> selectInstrumentPage(InstrumentPageModel instrumentPageModel);

    /**
     * 显示所有监控设备类型
     */
    ApiResponse<List<Monitorinstrumenttype>> showInstrumentType();
}

package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.entity.Hospitalofreginfo;
import com.hc.units.ApiResponse;

import java.util.List;

/**
 * Created by 16956 on 2018-08-05.
 */
public interface HospitalService {

    /**
     * 添加医院信息
     */
    ApiResponse<Hospitalofreginfo> addHosptalInfo(Hospitalofreginfo hospitalofreginfo);

    /**
     * 修改医院信息
     */
    ApiResponse<Hospitalofreginfo> updateHospital(Hospitalofreginfo hospitalofreginfo);

    /**
     * 删除当前医院信息
     */
    ApiResponse<String> deleteHospital(Hospitalofreginfo hospitalofreginfo);

    /**
     * 分页模糊查询医院信息
     */
    ApiResponse<Page<Hospitalofreginfo>> getHospitalInfoPage(String fuzzy,Integer pagesize,Integer pagenum);

    /**
     * 展示所有医院信息
     */
    ApiResponse<List<Hospitalofreginfo>> getHospitalInfo();

}

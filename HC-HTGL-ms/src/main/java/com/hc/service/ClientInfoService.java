package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.entity.Userright;
import com.hc.model.ResponseModel.ClientInfoModel;
import com.hc.units.ApiResponse;

/**
 * Created by 16956 on 2018-08-06.
 */
public interface ClientInfoService {

    /**
     * 添加用户
     */
    ApiResponse<Userright> addUser(Userright userright);

    /**
     * 删除用户
     */
    ApiResponse<String> deleteUser(Userright userright);

    /**
     * 修改用户
     */
    ApiResponse<Userright> updateUser(Userright userright);

    /**
     * 分页模糊查询展示所有用户信息
     * 新增setterWarningUsername 用作判断.在mon服务调用
     * 当前admin只能查看和修改当前admin的信息
     */
    ApiResponse<Page<ClientInfoModel>> selectUserInfoPage(String hospitalcode,String fuzzy,Integer pagesize,Integer pagenum,String setterWarningUsername);
}

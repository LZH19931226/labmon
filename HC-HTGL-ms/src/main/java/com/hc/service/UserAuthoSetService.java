package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.model.RequestModel.UserauthorInfoModel;
import com.hc.model.ResponseModel.UserAuthInfoModel;
import com.hc.units.ApiResponse;

/**
 * Created by 16956 on 2018-08-06.
 */
public interface UserAuthoSetService {

    /**
     * 展示所有人员权限设置
     */
    ApiResponse<Page<UserAuthInfoModel>> showUserAuth(Integer pagesize,Integer pagenum);

    /**
     * 修改人员权限
     */
    ApiResponse<String> updateAuthor(UserauthorInfoModel userauthorInfoModel);
}

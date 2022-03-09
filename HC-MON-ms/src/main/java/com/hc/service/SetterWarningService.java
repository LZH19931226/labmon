package com.hc.service;

import com.hc.entity.Userright;
import com.hc.model.ClientInfoModel;
import com.hc.utils.ApiResponse;

import java.util.LinkedHashMap;

public interface SetterWarningService {
    /**
     * 获取警报的用户列表
     * @param fuzzy
     * @param pageSize
     * @param pageNumber
     * @param hospitalcode
     * @param token
     * @return
     */
    public ApiResponse<ClientInfoModel> getWarningUsers(String fuzzy, Integer pageSize, Integer pageNumber, String hospitalcode, String token) throws Exception;

    /**
     * admin用户新增警报用户
     * @param userright
     * @param token
     * @return
     */
    ApiResponse<Userright> addWarningUser(Userright userright,String token) throws Exception;

    /**
     * admin更新警报用户
     * @param userright
     * @param token
     * @return
     */
    ApiResponse<Userright> updatedWarningUser(Userright userright,String token) throws Exception;
}

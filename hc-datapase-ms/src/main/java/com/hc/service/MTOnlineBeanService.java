package com.hc.service;

import com.hc.my.common.core.redis.dto.ParamaterModel;

import java.util.List;

public interface MTOnlineBeanService {



    /*
       * 解析硬件上传数据
     */
    List<ParamaterModel> paseData(String data);

}

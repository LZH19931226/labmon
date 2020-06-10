package com.hc.service;


import com.hc.entity.WarningRecordInfo;
import com.hc.utils.ApiResponse;

/**
 * @author LiuZhiHao
 * @date 2020/6/8 14:45
 * 描述:
 **/
public interface WarningRecordInfoService {

    ApiResponse<String> instwarningrecordinfo(WarningRecordInfo warningrecordinfo);

}

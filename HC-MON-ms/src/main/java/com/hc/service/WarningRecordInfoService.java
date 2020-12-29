package com.hc.service;


import com.hc.entity.WarningRecordInfo;
import com.hc.model.CurveInfoModel;
import com.hc.utils.ApiResponse;

/**
 * @author LiuZhiHao
 * @date 2020/6/8 14:45
 * 描述:
 **/
public interface WarningRecordInfoService {

    ApiResponse<WarningRecordInfo> instwarningrecordinfo(WarningRecordInfo warningrecordinfo);

    ApiResponse<CurveInfoModel> getWarningCurveData(String warningRecordId, String startTime, String endTime);

    ApiResponse<String> zfbwarningRuleSend(String warningRecordSortId);
}

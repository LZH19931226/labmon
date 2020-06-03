package com.hc.service;

import com.hc.model.ResponseModel.AlarmData;
import com.hc.model.ResponseModel.AlarmHospitalInfo;
import com.hc.model.ResponseModel.ShowData;
import com.hc.units.ApiResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xxf on 2018/9/28.
 */
public interface AlarmService {
    /**
     * 展示所有医院异常信息
     * @return
     */
    ApiResponse<List<AlarmHospitalInfo>> showAllHospitalAbInfo();

    ApiResponse<List<AlarmData>> showHospitalAlarmStatistics(String hospitalcode, String equipmenttypeid, String type);

    /**
     * 展示所有设备探头信息
     */
    ApiResponse<List<ShowData>> showAllData();

    /**
     * 所有医院异常信息 导出excle
     */
    ApiResponse<String> exportExcle(HttpServletResponse response);

    /**
     * 异常明细导出
     */
    ApiResponse<String> exportExcles(HttpServletResponse response);

}

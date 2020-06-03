package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.entity.Warningrecord;
import com.hc.model.NewWarningRecord;
import com.hc.model.PushSetModel;
import com.hc.model.ShowData;
import com.hc.utils.ApiResponse;

import java.util.List;

/**
 * Created by 16956 on 2018-08-01.
 */
public interface WarningInfoService {

    /**
     * 获取报警信息
     */
    ApiResponse<List<Warningrecord>> getWarningRecord(String hospitalcode);

    /**
     * 获取当前医院所有设备监控类型最新一条报警信息
     */
    ApiResponse<Page<NewWarningRecord>> getNewWarnRecord(String hospitalcode,Integer pagesize,Integer pagenum);

    /**
     * 获取当前探头监控类型历史报警（一个探头有多个监控参数：co2  o2  甲醛  这些几把玩意）
     */
    ApiResponse<Page<Warningrecord>> getInstrumentTypeHistoryWarn(String instrumentparamconfigNO,Integer pagesize,Integer pagenum);

    /**
     * 当前探头监控类型报警已读
     */
    ApiResponse<String> isRead(String instrumentparamconfigNO);

    /**
     * 删除当前监控探头类型报警信息
     * @param instrumentparamconfigNO
     * @return
     */
    ApiResponse<String> deleteWarningInfo(PushSetModel pushSetModel);

    /**
     * 大屏轮询报警信息
     */
    ApiResponse<List<ShowData>> showData();


}

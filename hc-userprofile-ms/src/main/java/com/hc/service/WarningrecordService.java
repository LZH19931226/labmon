package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.WarningrecordDto;
import com.hc.vo.waring.WarningrecordVo;

import java.util.List;

/**
 * 警告记录服务
 * @author hc
 */
public interface WarningrecordService {
    /**
     * 警告记录服务
     * @param hospitalcode 医院编码
     * @return
     */
    List<WarningrecordDto> getWarningRecord(String hospitalcode);

    /**
     * 获得新的警告记录
     * @param page 分页对象
     * @param hospitalcode 医院编码
     * @return
     */
    List<WarningrecordDto> getNewWarnRecord(Page<WarningrecordVo> page, String hospitalcode);

    /**
     * 获取报警记录月数
     * @param collect
     * @return
     */
    List<WarningrecordDto> getWarNingRecordMonthCount(List<String> collect);

    List<WarningrecordDto> getWarNingRecordInfoMonthCount(List<String> collect);

    List<WarningrecordDto> getInstrumentTypeHistoryWarnAll(String instrumentparamconfigNO, Page page);

    List<WarningrecordDto> getInstrumentTypeHistoryWarn(String instrumentparamconfigNO, Page page);

    void updateMsgFlag(String instrumentparamconfigNO);

    int updatePushState(String instrumentparamconfigNO, String s);
}

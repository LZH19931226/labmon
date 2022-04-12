package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.dto.WarningrecordDto;
import com.hc.po.WarningrecordPo;

import java.util.List;

public interface WarningrecordRepository extends IService<WarningrecordPo> {
    List<WarningrecordDto> getWarningRecord(String hospitalcode);

    List<WarningrecordDto> getNewWarnRecord(Page page, String hospitalcode);

    List<WarningrecordDto> getWarNingRecordMonthCount(List<String> collect);

    List<WarningrecordDto> getWarNingRecordInfoMonthCount(List<String> collect);

    List<WarningrecordDto> getInstrumentTypeHistoryWarnAll(String instrumentparamconfigNO, Page page);

    List<WarningrecordDto> getInstrumentTypeHistoryWarn(String instrumentparamconfigNO, Page page);

    void updateMsgFlag(String instrumentparamconfigNO);

    int updatePushState(String instrumentparamconfigNO, String s);
}

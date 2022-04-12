package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.WarningrecordDto;
import com.hc.repository.WarningrecordRepository;
import com.hc.service.WarningrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningrecordServiceImpl implements WarningrecordService {

    @Autowired
    private WarningrecordRepository warningrecordRepository;


    @Override
    public List<WarningrecordDto> getWarningRecord(String hospitalcode) {
        return warningrecordRepository.getWarningRecord(hospitalcode);
    }

    @Override
    public List<WarningrecordDto> getWarNingRecordMonthCount(List<String> collect) {
        return warningrecordRepository.getWarNingRecordMonthCount(collect);
    }

    @Override
    public List<WarningrecordDto> getWarNingRecordInfoMonthCount(List<String> collect) {
        return warningrecordRepository.getWarNingRecordInfoMonthCount(collect);
    }

    @Override
    public List<WarningrecordDto> getNewWarnRecord(Page page, String hospitalcode) {
        return warningrecordRepository.getNewWarnRecord(page,hospitalcode);
    }

    @Override
    public List<WarningrecordDto> getInstrumentTypeHistoryWarnAll(String instrumentparamconfigNO, Page page) {
        return warningrecordRepository.getInstrumentTypeHistoryWarnAll(instrumentparamconfigNO,page);
    }

    @Override
    public List<WarningrecordDto> getInstrumentTypeHistoryWarn(String instrumentparamconfigNO, Page page) {
        return warningrecordRepository.getInstrumentTypeHistoryWarn(instrumentparamconfigNO,page);
    }

    @Override
    public void updateMsgFlag(String instrumentparamconfigNO) {
        warningrecordRepository.updateMsgFlag(instrumentparamconfigNO);
    }

    @Override
    public int updatePushState(String instrumentparamconfigNO, String s) {
        return warningrecordRepository.updatePushState(instrumentparamconfigNO,s);

    }
}

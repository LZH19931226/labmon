package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.WarningrecordDto;
import com.hc.infrastructure.dao.WarningrecordDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.po.WarningrecordPo;
import com.hc.repository.WarningrecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarningrecordRepositoryImpl extends ServiceImpl<WarningrecordDao,WarningrecordPo> implements WarningrecordRepository {

    @Autowired
    private WarningrecordDao warningrecordDao;

    @Override
    public List<WarningrecordDto> getWarningRecord(String hospitalcode) {
        List<WarningrecordPo> warningrecordPos = warningrecordDao.selectList(Wrappers.lambdaQuery(new WarningrecordPo())
                .eq(WarningrecordPo::getHospitalcode, hospitalcode)
                .orderByDesc(WarningrecordPo::getInputdatetime)
                .last("limit 20"));
        return BeanConverter.convert(warningrecordPos,WarningrecordDto.class);
    }

    @Override
    public List<WarningrecordDto> getNewWarnRecord(Page page, String hospitalcode) {
        List<WarningrecordPo>  newWarnRecordList =warningrecordDao.getNewWarnRecord(page,hospitalcode);
        return BeanConverter.convert(newWarnRecordList,WarningrecordDto.class);
    }

    @Override
    public List<WarningrecordDto> getWarNingRecordMonthCount(List<String> collect) {
        DateUtils dateUtils = new DateUtils();
        List<WarningrecordPo> warningrecordPos  = warningrecordDao.getWarNingRecordMonthCount(
                collect,dateUtils.getCurrentDateTimeBeforeOneMonth(),dateUtils.getNowDate());
        return BeanConverter.convert(warningrecordPos,WarningrecordDto.class);
    }

    @Override
    public List<WarningrecordDto> getWarNingRecordInfoMonthCount(List<String> collect) {
        DateUtils dateUtils = new DateUtils();
        List<WarningrecordPo> warningrecordPos = warningrecordDao.getWarNingRecordInfoMonthCount(
                collect,dateUtils.getCurrentDateTimeBeforeOneMonth(),dateUtils.getNowDate());
        return BeanConverter.convert(warningrecordPos,WarningrecordDto.class);
    }

    @Override
    public List<WarningrecordDto> getInstrumentTypeHistoryWarnAll(String instrumentparamconfigNO, Page page) {
        List<WarningrecordPo> warningrecordPos = warningrecordDao.getInstrumentTypeHistoryWarnAll(instrumentparamconfigNO,page);
        return BeanConverter.convert(warningrecordPos,WarningrecordDto.class);
    }

    @Override
    public List<WarningrecordDto> getInstrumentTypeHistoryWarn(String instrumentparamconfigNO, Page page) {
        List<WarningrecordPo> warningrecordPos = warningrecordDao.getInstrumentTypeHistoryWarn(instrumentparamconfigNO,page);
        return BeanConverter.convert(warningrecordPos,WarningrecordDto.class);
    }

    @Override
    public void updateMsgFlag(String instrumentparamconfigNO) {
        warningrecordDao.updateMsgFlag(instrumentparamconfigNO);
    }

    @Override
    public int updatePushState(String instrumentparamconfigNO, String s) {
        return warningrecordDao.updatePushState(instrumentparamconfigNO,s);
    }
}

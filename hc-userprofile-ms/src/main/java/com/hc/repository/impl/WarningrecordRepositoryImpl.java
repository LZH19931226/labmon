package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.WarningrecordDto;
import com.hc.infrastructure.dao.WarningrecordDao;
import com.hc.my.common.core.util.BeanConverter;
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
}

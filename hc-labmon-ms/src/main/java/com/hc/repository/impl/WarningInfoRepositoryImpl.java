package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.infrastructure.dao.WarningInfoDao;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import com.hc.repository.WarningInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarningInfoRepositoryImpl extends ServiceImpl<WarningInfoDao, WarningRecordDto> implements WarningInfoRepository {

    @Autowired
    private WarningInfoDao warningInfoDao;

    @Override
    public List<WarningRecordDto> getWarningRecord(String hospitalCode) {
        return warningInfoDao.getWarningRecord(hospitalCode);
    }
}

package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.WarningRecordInfoDto;
import com.hc.infrastructure.dao.WarningRecordInfoDao;
import com.hc.repository.WarningRecordInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WarningRecordInfoRepositoryImpl extends ServiceImpl<WarningRecordInfoDao, WarningRecordInfoDto> implements WarningRecordInfoRepository {

    @Autowired
    private WarningRecordInfoDao warningRecordInfoDao;

}

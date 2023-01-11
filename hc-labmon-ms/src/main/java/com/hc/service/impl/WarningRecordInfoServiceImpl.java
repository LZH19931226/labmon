package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.WarningRecordInfoDto;
import com.hc.repository.WarningRecordInfoRepository;
import com.hc.service.WarningRecordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningRecordInfoServiceImpl implements WarningRecordInfoService {

    @Autowired
    private WarningRecordInfoRepository warningRecordInfoRepository;

    @Override
    public WarningRecordInfoDto selectWarningRecordInfo(String pkId) {
        return warningRecordInfoRepository.getOne(Wrappers.lambdaQuery(new WarningRecordInfoDto()).eq(WarningRecordInfoDto::getWarningrecordid,pkId));
    }

    @Override
    public void save(WarningRecordInfoDto warningRecordDto) {
        warningRecordInfoRepository.save(warningRecordDto);
    }

    @Override
    public void update(WarningRecordInfoDto warningRecordDto) {
        warningRecordInfoRepository.updateById(warningRecordDto);
    }

    @Override
    public List<WarningRecordInfoDto> selectWarningRecordInfoByPkIdList(List<String> pkIdList) {
        return warningRecordInfoRepository.list(Wrappers.lambdaQuery(new WarningRecordInfoDto()).in(WarningRecordInfoDto::getWarningrecordid,pkIdList));
    }
}

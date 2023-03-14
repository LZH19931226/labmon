package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.HospitalInfoDto;
import com.hc.dto.LabHosWarningTimeDto;
import com.hc.infrastructure.dao.LabHosWarningTimeDao;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.repository.HospitalInfoRepository;
import com.hc.service.HospitalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalInfoServiceImpl  implements HospitalInfoService {

    @Autowired
    private HospitalInfoRepository hospitalInfoRepository;
    @Autowired
    private LabHosWarningTimeDao labHosWarningTimeDao;

    @Override
    public HospitalInfoDto selectOne(String hospitalCode) {
        return hospitalInfoRepository.getOne(Wrappers.lambdaQuery(new HospitalInfoDto()).eq(HospitalInfoDto::getHospitalCode,hospitalCode));
    }

    @Override
    public List<HospitalInfoDto> getHospitalInfoByCode(List<String> hospitalCodeList) {
        return hospitalInfoRepository.list(Wrappers.lambdaQuery(new HospitalInfoDto()).in(HospitalInfoDto::getHospitalCode,hospitalCodeList));
    }

    @Override
    public List<LabHosWarningTimeDto> getHospitalWarningTime(String hospitalCode) {
        HospitalInfoDto hospitalInfoDto = hospitalInfoRepository.getOne(Wrappers.lambdaQuery(new HospitalInfoDto()).eq(HospitalInfoDto::getHospitalCode, hospitalCode));
        String alwaysAlarm = hospitalInfoDto.getAlwaysAlarm();
        if (DictEnum.TURN_ON.getCode().equals(alwaysAlarm)) {
            return null;
        }
        return labHosWarningTimeDao.selectList(Wrappers.lambdaQuery(new LabHosWarningTimeDto()).eq(LabHosWarningTimeDto::getHospitalCode, hospitalCode));
    }
}

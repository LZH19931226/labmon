package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.HospitalInfoDto;
import com.hc.repository.HospitalInfoRepository;
import com.hc.service.HospitalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalInfoServiceImpl  implements HospitalInfoService {

    @Autowired
    private HospitalInfoRepository hospitalInfoRepository;

    @Override
    public HospitalInfoDto selectOne(String hospitalCode) {
        return hospitalInfoRepository.getOne(Wrappers.lambdaQuery(new HospitalInfoDto()).eq(HospitalInfoDto::getHospitalCode,hospitalCode));
    }

    @Override
    public List<HospitalInfoDto> getHospitalInfoByCode(List<String> hospitalCodeList) {
        return hospitalInfoRepository.list(Wrappers.lambdaQuery(new HospitalInfoDto()).in(HospitalInfoDto::getHospitalCode,hospitalCodeList));
    }
}

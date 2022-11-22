package com.hc.service;

import com.hc.dto.HospitalInfoDto;

import java.util.List;

public interface HospitalInfoService {
    HospitalInfoDto selectOne(String hospitalCode);

    List<HospitalInfoDto> getHospitalInfoByCode(List<String> hospitalCode);
}

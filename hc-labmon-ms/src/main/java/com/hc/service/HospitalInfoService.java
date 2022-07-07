package com.hc.service;

import com.hc.dto.HospitalInfoDto;

public interface HospitalInfoService {
    HospitalInfoDto selectOne(String hospitalCode);
}

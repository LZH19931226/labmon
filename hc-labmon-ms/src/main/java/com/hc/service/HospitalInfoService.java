package com.hc.service;

import com.hc.dto.HospitalInfoDto;
import com.hc.dto.LabHosWarningTimeDto;

import java.util.List;

public interface HospitalInfoService {
    HospitalInfoDto selectOne(String hospitalCode);

    List<HospitalInfoDto> getHospitalInfoByCode(List<String> hospitalCode);

    //通过医院code获取医院统计得报警时段,医院为全天报警则不返回
    List<LabHosWarningTimeDto>  getHospitalWarningTime(String hospitalCode);
}

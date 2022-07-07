package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.HospitalInfoDto;
import com.hc.infrastructure.dao.HospitalInfoDao;
import com.hc.repository.HospitalInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HospitalInfoRepositoryImpl extends ServiceImpl<HospitalInfoDao, HospitalInfoDto> implements HospitalInfoRepository {

    @Autowired
    private HospitalInfoDao hospitalInfoDao;


}

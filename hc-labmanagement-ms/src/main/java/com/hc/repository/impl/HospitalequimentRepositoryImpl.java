package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.HospitalequimentRepository;
import com.hc.infrastructure.dao.HospitalequimentDao;
import com.hc.po.HospitalequimentPo;


@Repository
public class HospitalequimentRepositoryImpl extends ServiceImpl<HospitalequimentDao,HospitalequimentPo> implements HospitalequimentRepository  {


}
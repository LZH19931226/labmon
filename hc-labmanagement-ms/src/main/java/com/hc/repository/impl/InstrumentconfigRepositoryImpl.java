package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.InstrumentconfigRepository;
import com.hc.infrastructure.dao.InstrumentconfigDao;
import com.hc.po.InstrumentconfigPo;


@Repository
public class InstrumentconfigRepositoryImpl extends ServiceImpl<InstrumentconfigDao,InstrumentconfigPo> implements InstrumentconfigRepository  {


}
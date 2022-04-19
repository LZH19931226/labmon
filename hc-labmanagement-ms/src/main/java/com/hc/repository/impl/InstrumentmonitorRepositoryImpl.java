package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.InstrumentmonitorRepository;
import com.hc.infrastructure.dao.InstrumentmonitorDao;
import com.hc.po.InstrumentmonitorPo;


@Repository
public class InstrumentmonitorRepositoryImpl extends ServiceImpl<InstrumentmonitorDao,InstrumentmonitorPo> implements InstrumentmonitorRepository  {


}
package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.InstrumentparamconfigRepository;
import com.hc.infrastructure.dao.InstrumentparamconfigDao;
import com.hc.po.InstrumentparamconfigPo;


@Repository
public class InstrumentparamconfigRepositoryImpl extends ServiceImpl<InstrumentparamconfigDao,InstrumentparamconfigPo> implements InstrumentparamconfigRepository  {


}
package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.MonitorinstrumentRepository;
import com.hc.infrastructure.dao.MonitorinstrumentDao;
import com.hc.po.MonitorinstrumentPo;


@Repository
public class MonitorinstrumentRepositoryImpl extends ServiceImpl<MonitorinstrumentDao,MonitorinstrumentPo> implements MonitorinstrumentRepository  {


}
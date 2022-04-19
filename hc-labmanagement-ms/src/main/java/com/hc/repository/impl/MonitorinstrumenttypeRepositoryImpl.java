package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.MonitorinstrumenttypeRepository;
import com.hc.infrastructure.dao.MonitorinstrumenttypeDao;
import com.hc.po.MonitorinstrumenttypePo;


@Repository
public class MonitorinstrumenttypeRepositoryImpl extends ServiceImpl<MonitorinstrumenttypeDao,MonitorinstrumenttypePo> implements MonitorinstrumenttypeRepository  {


}
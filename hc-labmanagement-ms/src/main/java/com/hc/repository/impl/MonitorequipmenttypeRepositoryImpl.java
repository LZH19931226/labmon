package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.MonitorequipmenttypeRepository;
import com.hc.infrastructure.dao.MonitorequipmenttypeDao;
import com.hc.po.MonitorequipmenttypePo;


@Repository
public class MonitorequipmenttypeRepositoryImpl extends ServiceImpl<MonitorequipmenttypeDao,MonitorequipmenttypePo> implements MonitorequipmenttypeRepository  {


}
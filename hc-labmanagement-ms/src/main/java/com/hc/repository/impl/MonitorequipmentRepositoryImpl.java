package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.MonitorequipmentRepository;
import com.hc.infrastructure.dao.MonitorequipmentDao;
import com.hc.po.MonitorequipmentPo;


@Repository
public class MonitorequipmentRepositoryImpl extends ServiceImpl<MonitorequipmentDao,MonitorequipmentPo> implements MonitorequipmentRepository  {


}
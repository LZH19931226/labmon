package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.OperationlogRepository;
import com.hc.infrastructure.dao.OperationlogDao;
import com.hc.po.OperationlogPo;


@Repository
public class OperationlogRepositoryImpl extends ServiceImpl<OperationlogDao,OperationlogPo> implements OperationlogRepository  {


}
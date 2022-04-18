package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.OperationlogdetailRepository;
import com.hc.infrastructure.dao.OperationlogdetailDao;
import com.hc.po.OperationlogdetailPo;


@Repository
public class OperationlogdetailRepositoryImpl extends ServiceImpl<OperationlogdetailDao,OperationlogdetailPo> implements OperationlogdetailRepository  {


}
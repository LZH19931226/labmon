package com.hc.infrastructure.dao;

import com.hc.po.Operationlogdetail;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Created by 15350 on 2020/5/20.
 */
@Transactional
public interface OperationlogdetailDao extends JpaRepository<Operationlogdetail,String> {
}

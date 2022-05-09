package com.hc.infrastructure.dao;

import com.hc.po.Operationlog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 15350 on 2020/5/20.
 */

public interface OperationlogDao extends JpaRepository<Operationlog,String> {
}

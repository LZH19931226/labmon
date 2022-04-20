package com.hc.infrastructure.dao;

import com.hc.entity.Operationrecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 15350 on 2020/2/9.
 */
public interface OperationrecordDao extends JpaRepository<Operationrecord,String> {
}

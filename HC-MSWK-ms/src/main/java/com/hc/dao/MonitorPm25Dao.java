package com.hc.dao;

import com.hc.entity.Monitorpm25record;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 16956 on 2018-08-08.
 */
public interface MonitorPm25Dao extends JpaRepository<Monitorpm25record,String> {
}

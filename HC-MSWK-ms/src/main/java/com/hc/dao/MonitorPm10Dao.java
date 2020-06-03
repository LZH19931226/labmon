package com.hc.dao;

import com.hc.entity.Monitorpm10record;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 16956 on 2018-08-08.
 */
public interface MonitorPm10Dao extends JpaRepository<Monitorpm10record,String> {
}

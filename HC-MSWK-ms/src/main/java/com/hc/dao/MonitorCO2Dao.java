package com.hc.dao;

import com.hc.entity.Monitorcarbondioxiderecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 16956 on 2018-08-08.
 */
public interface MonitorCO2Dao extends JpaRepository<Monitorcarbondioxiderecord,String> {
}

package com.hc.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hc.entity.Monitorhumidityrecord;

/**
 * Created by 16956 on 2018-08-08.
 */
public interface MonitorRhDao extends JpaRepository<Monitorhumidityrecord,String> {
}

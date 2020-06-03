package com.hc.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hc.entity.Monitorequipmentlastdata;
import com.hc.entity.MonitorequipmentlastdataKey;

/**
 * Created by 16956 on 2018-08-09.
 */
public interface MonitorequipmentlastdataDao extends JpaRepository<Monitorequipmentlastdata,String> {

}

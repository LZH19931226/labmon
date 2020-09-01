package com.hc.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hc.entity.Monitorequipmentlastdata;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-09.
 */
//@Transactional
public interface MonitorequipmentlastdataDao extends JpaRepository<Monitorequipmentlastdata,String> {
//
//    @Modifying
//    @Query(value = "alter table monitorequipmentlastdata rename  to :tableName ",nativeQuery = true)
//    void altetTableMonitorequipmentlastdata(@Param("tableName")String tableName);
//
//
//
//    @Modifying
//    @Query(value = "create table monitorequipmentlastdata like  :tableName ",nativeQuery = true)
//    void createTableMonitorequipmentlastdata(@Param("tableName")String tableName);
}

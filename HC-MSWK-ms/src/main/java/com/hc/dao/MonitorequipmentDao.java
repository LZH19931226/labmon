package com.hc.dao;

import com.hc.entity.Monitorequipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by 15350 on 2019/10/9.
 */
@Transactional
public interface MonitorequipmentDao extends JpaRepository<Monitorequipment,String> {

    @Modifying
    @Query("update Monitorequipment a set a.clientvisible = 1 where a.equipmentno =:equipmentno")
    Integer updateMonitorequipmentIsAble(@Param("equipmentno") String pkid);

    @Modifying
    @Query("update Monitorequipment a set a.clientvisible = 0 where a.equipmentno =:equipmentno")
    Integer updateMonitorequipmentAble(@Param("equipmentno") String pkid);

}

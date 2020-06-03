package com.hc.dao;

import com.hc.entity.Monitorequipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 15350 on 2020/5/25.
 */
@Component
public interface MonitorequipmentDao extends JpaRepository<Monitorequipment,String> {


    @Query("select  a  from Monitorequipment a  where a.hospitalcode = '2cc0db66222042389cca37ba4ac5f281' ")
    List<Monitorequipment> getMonitorequipmentByHospitalcode();
}

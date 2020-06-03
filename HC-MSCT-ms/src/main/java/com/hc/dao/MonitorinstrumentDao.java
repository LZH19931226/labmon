package com.hc.dao;

import com.hc.entity.Monitorinstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by xxf on 2018/10/14.
 */
public interface MonitorinstrumentDao extends JpaRepository<Monitorinstrument,String> {

    @Query("select  a from Monitorinstrument a where a.hospitalcode=:hospitalcode and a.instrumenttypeid = 9 ")
    Monitorinstrument getMonitorinstrumentByCode(@Param("hospitalcode") String hospitalcode);
}
